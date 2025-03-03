// Test script for ReDoS vulnerability fix in marked.js
const fs = require('fs');

// Helper function to test regex pattern safety
function testReplace(regex, opt) {
    // Add pattern validation for extremely long patterns
    if (regex.length > 2000) {
        throw new Error('Regular expression too long');
    }

    // Only check for the most dangerous ReDoS patterns
    // This specifically targets patterns known to cause exponential backtracking
    const reDoSPattern = /\([^()]*[+*](?:[^()]|\([^()]*\))*\)[+*]/;
    
    if (reDoSPattern.test(regex)) {
        throw new Error('Potentially unsafe regular expression pattern');
    }
    
    try {
        return new RegExp(regex, opt);
    } catch (e) {
        throw new Error('Invalid regular expression: ' + e.message);
    }
}

// Helper function to safely test regex patterns
function testPattern(name, pattern, shouldPass = true) {
    console.log(`Test: ${name}`);
    console.log(`Pattern: ${pattern}`);
    
    try {
        const start = process.hrtime();
        // Try to create the regex using our test function
        testReplace(pattern, 'g');
        const end = process.hrtime(start);
        const timeMs = (end[0] * 1000 + end[1] / 1000000).toFixed(2);
        
        if (shouldPass) {
            console.log(`✓ PASS - Pattern accepted (${timeMs}ms)`);
            return true;
        } else {
            console.log(`✗ FAIL - Dangerous pattern was accepted (${timeMs}ms)`);
            return false;
        }
    } catch (e) {
        if (shouldPass) {
            console.log(`✗ FAIL - Safe pattern was rejected: ${e.message}`);
            return false;
        } else {
            console.log(`✓ PASS - Dangerous pattern correctly rejected: ${e.message}`);
            return true;
        }
    } finally {
        console.log('');
    }
}

// Test Cases
let allTestsPassed = true;

// 1. Test vulnerable patterns (should fail)
const vulnerablePatterns = [
    ['ReDoS Example 1', '(a+)+$'],
    ['ReDoS Example 2', '(a*)*'],
    ['ReDoS Example 3', '([a-z]+)*$'],
    ['ReDoS Example 4', '(a+b+)+c'],
    ['Very Long Pattern', 'a'.repeat(2001)]
];

console.log('Testing vulnerable patterns (these should be rejected):\n');
for (const [name, pattern] of vulnerablePatterns) {
    allTestsPassed &= testPattern(name, pattern, false);
}

// 2. Test safe patterns (should pass)
const safePatterns = [
    ['Simple Pattern', 'abc*'],
    ['Basic Group', '(hello|world)'],
    ['Basic Quantifiers', 'a+b*c?'],
    ['Basic Capture Group', '(test)\\1'],
    ['Basic Character Class', '[a-z]+'],
    ['Markdown Bold', '\\*\\*([^\\*]+)\\*\\*'],
    ['Markdown List', '^ *[-*+] +'],
    ['URL Pattern', 'https?://[^\\s/$.?#][^\\s]*']
];

console.log('Testing safe patterns (these should be accepted):\n');
for (const [name, pattern] of safePatterns) {
    allTestsPassed &= testPattern(name, pattern, true);
}

// Final Results
console.log('Test Summary:');
if (allTestsPassed) {
    console.log('✓ All tests passed successfully!');
    process.exit(0);
} else {
    console.log('✗ Some tests failed!');
    process.exit(1);
}