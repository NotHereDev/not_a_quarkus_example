import path from 'path';
import webpack from 'webpack';
import "webpack-dev-server";

const CLIENT_OUTPUT_PATH = 'src/main/resources/META-INF/resources/static';
const SERVER_OUTPUT_PATH = 'src/main/resources/public';
const ASSETS_PATH = 'assets';
const JS_ASSETS_PATH = `${ASSETS_PATH}/javascripts`;
const CSS_ASSETS_PATH = `${ASSETS_PATH}/stylesheets`;

function jsConfig(
    mode: webpack.Configuration["mode"] = "development",
    side: "client" | "server" = "client"
): webpack.Configuration {
    return {
        mode: mode,
        entry: `./${JS_ASSETS_PATH}/${side}.ts`,
        target: side === "client" ? "web" : "node",
        output: {
            filename: `${side}.js`,
            path: path.resolve(__dirname, side === "client" ? CLIENT_OUTPUT_PATH : SERVER_OUTPUT_PATH),
        },
        watch: side === "server" && mode === "development" || undefined,
        devServer: side === "client" ? {
            //liveReload: false, => see how to do hot reload for solid, not working at all
            hot: true,
            open: false,
            watchFiles: [`./${JS_ASSETS_PATH}/**/*.{tsx,jsx,ts,js}`],
            historyApiFallback: true,
            allowedHosts: "all",
            devMiddleware: {
                writeToDisk: (path) => path.includes("client.js"),
            },
            port: 8081,
            proxy: {
                "/": {
                    target: {
                        host: "localhost",
                        protocol: 'http:',
                        port: 8080,
                    },
                },
                ignorePath: true,
                changeOrigin: true,
                secure: false,
            },
        } : undefined,
        devtool: mode === "development" ? "inline-source-map" : undefined,
        module: {
            rules: [
                {
                    test: /\.tsx?$/,
                    loader: 'babel-loader',
                    exclude: /node_modules/,
                    options: {
                        presets: [
                            "@babel/preset-typescript",
                            ["solid", { "generate": side === "client" ? "dom" : "ssr", "hydratable": true }]
                        ],
                        env: side === "client" ? {
                            development: {
                                plugins: ["solid-refresh/babel"],
                            },
                        } : undefined,
                    }
                },
            ],
        },
        plugins: side === "client" ? [
            new webpack.HotModuleReplacementPlugin(),
        ] : undefined,
    }
}

const env = (process.env.NODE_ENV || "development") as webpack.Configuration["mode"];
const side = (process.env.SIDE || "client") as "client" | "server";
console.log(`Building ${side} in ${env} mode`);

const config: webpack.Configuration = jsConfig(env, side)

export default config;