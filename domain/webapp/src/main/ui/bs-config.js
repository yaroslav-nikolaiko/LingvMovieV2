var proxyMiddleware = require('http-proxy-middleware');
var fallbackMiddleware = require('connect-history-api-fallback');

module.exports = {
    files: ['index.html', 'compiled/**/*', 'app/**/*'],
    server: {
        baseDir: ".",
        middleware: {
            1: proxyMiddleware('/api', {
                target: 'http://localhost:8080',
                pathRewrite: {
                    '^/api' : '',     // rewrite path
                },
                changeOrigin: true   // for vhosted sites, changes host header to match to target's host
            }),

            2: fallbackMiddleware({
                index: '/index.html', verbose: true
            })
        }
    }
};