// Targeted regression guard, not a replacement for a full secret scanner.
const fs = require('node:fs');
const path = require('node:path');
const root = path.resolve(__dirname, '..');
const failures = [];
function visit(dir) {
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const file = path.join(dir, entry.name);
    if (entry.isDirectory()) { visit(file); continue; }
    if (!/\.(java|html|properties|xml)$/.test(entry.name)) continue;
    const content = fs.readFileSync(file, 'utf8');
    const rules = [
      /KakaoAK\s+[a-zA-Z0-9]{20,}/,
      /appkey=[a-fA-F0-9]{32}/,
      /-----BEGIN (?:RSA |EC |OPENSSH )?PRIVATE KEY-----/,
      /\$\{vo2\.pw\}/,
      /log\.[a-z]+\([^\n]*(?:getPw\(\)|requestUrl|strUrl)/
    ];
    if (rules.some(rule => rule.test(content))) failures.push(path.relative(root, file));
    if (entry.name.endsWith('.properties')) {
      for (const line of content.split(/\r?\n/)) {
        const match = line.match(/^\s*(?:spring\.datasource\.password|public\.api\.serviceKey|kakao\.maps\.javascript-key)\s*=\s*(.*)$/);
        if (match && !/^\$\{[A-Z_]+(?::)?\}$/.test(match[1])) failures.push(path.relative(root, file));
      }
    }
  }
}
visit(path.join(root, 'src/main'));
if (failures.length) {
  // Report paths only: never echo matched credential values.
  console.error('Credential guard failed: ' + [...new Set(failures)].join(', '));
  process.exit(1);
}
console.log('Credential regression guard passed (source/config/templates).');
