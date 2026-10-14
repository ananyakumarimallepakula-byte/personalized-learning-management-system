// ===================================================================
// Zero-Dependency Frontend Static Web Server (Node.js)
// ===================================================================

const http = require('http');
const fs = require('fs');
const path = require('path');
const url = require('url');

const PORT = 3000;
const ROOT_DIR = path.resolve(__dirname);

const MIME_TYPES = {
    '.html': 'text/html; charset=utf-8',
    '.css': 'text/css; charset=utf-8',
    '.js': 'application/javascript; charset=utf-8',
    '.json': 'application/json',
    '.png': 'image/png',
    '.jpg': 'image/jpeg',
    '.jpeg': 'image/jpeg',
    '.gif': 'image/gif',
    '.svg': 'image/svg+xml',
    '.ico': 'image/x-icon',
    '.woff': 'font/woff',
    '.woff2': 'font/woff2',
    '.ttf': 'font/ttf'
};

const server = http.createServer((req, res) => {
    const parsedUrl = url.parse(req.url);
    let pathname = decodeURIComponent(parsedUrl.pathname);

    if (pathname === '/') {
        pathname = '/index.html';
    }

    let filePath = path.join(ROOT_DIR, pathname);

    // Prevent directory traversal attacks
    if (!filePath.startsWith(ROOT_DIR)) {
        res.writeHead(403, { 'Content-Type': 'text/plain' });
        return res.end('403 Forbidden');
    }

    fs.stat(filePath, (err, stats) => {
        if (err || !stats.isFile()) {
            // Check if it's a directory with an index.html
            const indexPath = path.join(filePath, 'index.html');
            if (fs.existsSync(indexPath)) {
                filePath = indexPath;
            } else {
                res.writeHead(404, { 'Content-Type': 'text/plain' });
                return res.end(`404 Not Found: ${pathname}`);
            }
        }

        const ext = path.extname(filePath).toLowerCase();
        const contentType = MIME_TYPES[ext] || 'application/octet-stream';

        fs.readFile(filePath, (readErr, content) => {
            if (readErr) {
                res.writeHead(500, { 'Content-Type': 'text/plain' });
                return res.end('500 Server Error');
            }

            res.writeHead(200, {
                'Content-Type': contentType,
                'Access-Control-Allow-Origin': '*'
            });
            res.end(content);
        });
    });
});

server.listen(PORT, () => {
    console.log('====================================================');
    console.log('  LearnPredict Frontend Server Running!');
    console.log(`  Access URL: http://localhost:${PORT}`);
    console.log('  Backend API expected at: http://localhost:8080/api');
    console.log('====================================================');
});
