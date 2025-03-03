// Script to verify the ReDoS vulnerability fix
const fs = require('fs');
const vm = require('vm');

// Load the marked library
const markedCode = fs.readFileSync('./WebContent/swagger/lib/marked.js', 'utf8');

// Create sandbox environment
const sandbox = {
    console: console,
    setTimeout: setTimeout,
    clearTimeout: clearTimeout,
    Error: Error
};

// Run marked.js in sandbox
vm.runInNewContext(markedCode, sandbox);

// Test function to simulate the original vulnerability
function testReDoSPattern() {
    console.log('Testing ReDoS vulnerability fix...\n');
    
    const dangerousPatterns = [
        {
            pattern: '(a+)+$',
            input: 'a'.repeat(25) + 'b',
            description: 'Original ReDoS pattern'
        },
        {
            pattern: '([a-z]+)*$',
            input: 'abcdef'.repeat(10) + '0',
            description: 'Complex nested pattern'
        }
    ];

    for (const test of dangerousPatterns) {
        console.log(`Testing: ${test.description}`);
        console.log(`Pattern: ${test.pattern}`);
        console.log(`Input: ${test.input.substring(0, 30)}...`);
        
        try {
            console.time('Execution time');
            sandbox.replace(test.pattern, 'g')();
            console.timeEnd('Execution time');
            console.log('✗ FAIL - Dangerous pattern was not blocked!\n');
        } catch (e) {
            console.timeEnd('Execution time');
            if (e.message.includes('unsafe')) {
                console.log('✓ PASS - Dangerous pattern was correctly blocked');
                console.log(`Error message: ${e.message}\n`);
            } else {
                console.log('? UNEXPECTED - Pattern was blocked for a different reason');
                console.log(`Error message: ${e.message}\n`);
            }
        }
    }
}

// Test function to verify markdown still works
function testMarkdownFunctionality() {
    console.log('Testing markdown functionality...\n');
    
    const markdownTests = [
        {
            name: 'Basic formatting',
            input: '# Header\n**Bold** and *italic*\n',
            expectedInOutput: ['<h1>Header</h1>', '<strong>Bold</strong>', '<em>italic</em>']
        },
        {
            name: 'Lists and links',
            input: '* Item 1\n* Item 2\n[Link](http://example.com)',
            expectedInOutput: ['<ul>', '<li>Item', '<a href="http://example.com">']
        }
    ];

    for (const test of markdownTests) {
        console.log(`Testing: ${test.name}`);
        try {
            const output = sandbox.marked(test.input);
            const missingElements = test.expectedInOutput.filter(el => !output.includes(el));
            
            if (missingElements.length === 0) {
                console.log('✓ PASS - All expected elements present in output\n');
            } else {
                console.log('✗ FAIL - Missing elements:', missingElements.join(', '), '\n');
            }
        } catch (e) {
            console.log('✗ FAIL - Error processing markdown:', e.message, '\n');
        }
    }
}

console.log('Starting verification of ReDoS fix...\n');
console.log('1. Vulnerability Protection Test');
console.log('--------------------------------');
testReDoSPattern();

console.log('2. Markdown Functionality Test');
console.log('------------------------------');
testMarkdownFunctionality();