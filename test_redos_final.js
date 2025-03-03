// Test script for ReDoS vulnerability fix in marked.js
const fs = require('fs');
const vm = require('vm');

// Load marked.js
const markedCode = fs.readFileSync('./WebContent/swagger/lib/marked.js', 'utf8');

// Create sandbox
const sandbox = {
    console: console,
    require: require,
    module: { exports: {} }
};

// Run marked.js in sandbox
vm.runInNewContext(markedCode, sandbox);
const marked = sandbox.module.exports;

console.log('Starting tests...\n');

// Helper function to test patterns
function testPattern(name, pattern, shouldPass = true) {
    console.log(`Test: ${name}`);
    console.log(`Pattern: ${pattern}`);
    
    try {
        const result = sandbox.replace(pattern, 'g')();
        if (shouldPass) {
            console.log('✓ PASS - Pattern accepted as expected\n');
            return true;
        } else {
            console.log('✗ FAIL - Dangerous pattern was accepted\n');
            return false;
        }
    } catch (e) {
        if (shouldPass) {
            console.log(`✗ FAIL - Safe pattern was rejected: ${e.message}\n`);
            return false;
        } else {
            console.log(`✓ PASS - Pattern correctly rejected: ${e.message}\n`);
            return true;
        }
    }
}

// Test markdown functionality
function testMarkdown(name, input, expectedTags) {
    console.log(`Testing markdown: ${name}`);
    try {
        const result = marked(input);
        const missingTags = expectedTags.filter(tag => !result.includes(tag));
        
        if (missingTags.length === 0) {
            console.log('✓ PASS - All expected elements present\n');
            return true;
        } else {
            console.log(`✗ FAIL - Missing elements: ${missingTags.join(', ')}\n`);
            return false;
        }
    } catch (e) {
        console.log(`✗ FAIL - Error processing markdown: ${e.message}\n`);
        return false;
    }
}

let allTestsPassed = true;

// 1. Test known vulnerable patterns (should fail)
console.log('Testing vulnerable patterns:\n');
const vulnerablePatterns = [
    ['ReDoS - (a+)+', '(a+)+$'],
    ['ReDoS - Nested quantifiers', '(a*)*b'],
    ['ReDoS - Complex nesting', '([a-z]+)*$'],
    ['ReDoS - Backreference', '(a+)\\1+'],
    ['Very long pattern', 'a'.repeat(2001)]
];

for (const [name, pattern] of vulnerablePatterns) {
    allTestsPassed &= testPattern(name, pattern, false);
}

// 2. Test safe markdown patterns (should pass)
console.log('Testing safe patterns:\n');
const safePatterns = [
    ['Bold syntax', '\\*\\*[^\\*]+\\*\\*'],
    ['List item', '^ *[-*+] +[^\\n]+'],
    ['Header', '^#{1,6} .+'],
    ['Link', '\\[([^\\]]+)\\]\\(([^\\)]+)\\)'],
    ['Code block', '```[\\s\\S]*?```']
];

for (const [name, pattern] of safePatterns) {
    allTestsPassed &= testPattern(name, pattern, true);
}

// 3. Test actual markdown rendering
console.log('Testing markdown rendering:\n');
const markdownTests = [
    {
        name: 'Basic formatting',
        input: '# Title\n**bold** and *italic*',
        expected: ['<h1>', '<strong>', '<em>']
    },
    {
        name: 'Lists and links',
        input: '* Item 1\n* Item 2\n[link](http://example.com)',
        expected: ['<ul>', '<li>', '<a href']
    },
    {
        name: 'Code blocks',
        input: '```javascript\nconst x = 1;\n```',
        expected: ['<pre>', '<code>']
    }
];

for (const test of markdownTests) {
    allTestsPassed &= testMarkdown(test.name, test.input, test.expected);
}

// Final results
console.log('Test Summary:');
if (allTestsPassed) {
    console.log('✓ All tests passed - ReDoS protection is working and markdown functionality is preserved!');
    process.exit(0);
} else {
    console.log('✗ Some tests failed - Please check the logs above.');
    process.exit(1);
}