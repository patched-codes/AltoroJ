// Test script for markdown functionality after ReDoS fix
const fs = require('fs');
const vm = require('vm');

// Load the marked library
const markedCode = fs.readFileSync('./WebContent/swagger/lib/marked.js', 'utf8');

// Create a sandbox environment
const sandbox = {
    console: console,
    Error: Error,
    global: {},
};

// Run marked.js in the sandbox
vm.runInNewContext(markedCode, sandbox);

// Test markdown samples
const samples = [
    {
        name: 'Basic Markdown',
        input: '# Heading\n\n**Bold text** and *italic*\n\n* List item 1\n* List item 2\n\n```js\nconst code = true;\n```',
        expectedElements: ['<h1>', '<strong>', '<em>', '<ul>', '<li>', '<code>']
    },
    {
        name: 'Links and Images',
        input: '[Link](https://example.com)\n\n![Image](https://example.com/image.jpg)',
        expectedElements: ['<a href="https://example.com">', '<img src="https://example.com/image.jpg"']
    },
    {
        name: 'Tables',
        input: '| Header 1 | Header 2 |\n|----------|----------|\n| Cell 1   | Cell 2   |',
        expectedElements: ['<table>', '<th>', '<td>']
    },
    {
        name: 'Nested Elements',
        input: '> Blockquote with **bold** and *italic*\n\n* List with [link](http://example.com)',
        expectedElements: ['<blockquote>', '<strong>', '<em>', '<ul>', '<a href']
    }
];

console.log('Testing markdown functionality with various patterns...\n');

let allTestsPassed = true;
let testResults = [];

for (const sample of samples) {
    console.log(`Testing: ${sample.name}`);
    console.log('Input:', sample.input);
    
    try {
        const start = process.hrtime();
        const result = sandbox.marked(sample.input);
        const end = process.hrtime(start);
        const timeMs = (end[0] * 1000 + end[1] / 1000000).toFixed(2);
        
        // Check if all expected elements are present
        const missingElements = sample.expectedElements.filter(el => !result.includes(el));
        
        if (missingElements.length === 0) {
            console.log(`✓ PASS (${timeMs}ms)`);
            testResults.push({ name: sample.name, status: 'PASS', time: timeMs });
        } else {
            console.log('✗ FAIL - Missing elements:', missingElements.join(', '));
            testResults.push({ name: sample.name, status: 'FAIL', missing: missingElements });
            allTestsPassed = false;
        }
    } catch (e) {
        console.log('✗ FAIL - Error:', e.message);
        testResults.push({ name: sample.name, status: 'ERROR', error: e.message });
        allTestsPassed = false;
    }
    console.log('\nOutput preview (first 150 chars):');
    try {
        console.log(sandbox.marked(sample.input).substring(0, 150), '...\n');
    } catch (e) {
        console.log('Error generating output\n');
    }
    console.log('-'.repeat(80), '\n');
}

// Test potentially malicious patterns
console.log('Testing protection against malicious patterns...\n');

const maliciousPatterns = [
    '(a+)+$',
    '([a-z]+)*$',
    '(a*)*b',
    '(x+x+)+y'
];

for (const pattern of maliciousPatterns) {
    console.log(`Testing malicious pattern: ${pattern}`);
    try {
        // Try to use the pattern in a way that would trigger the regex engine
        const result = sandbox.marked(`\`\`\`\n${pattern}\n\`\`\``);
        console.log('✗ FAIL - Malicious pattern was not caught');
        allTestsPassed = false;
    } catch (e) {
        if (e.message.includes('unsafe') || e.message.includes('too long')) {
            console.log('✓ PASS - Malicious pattern was correctly blocked');
        } else {
            console.log('✗ FAIL - Unexpected error:', e.message);
            allTestsPassed = false;
        }
    }
    console.log();
}

// Print summary
console.log('Test Summary:');
console.log('-'.repeat(40));
testResults.forEach(result => {
    console.log(`${result.name}: ${result.status}${result.time ? ` (${result.time}ms)` : ''}`);
});
console.log('-'.repeat(40));

if (allTestsPassed) {
    console.log('✓ All tests passed successfully!');
    process.exit(0);
} else {
    console.log('✗ Some tests failed!');
    process.exit(1);
}