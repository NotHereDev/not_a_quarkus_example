import path from 'path';
import webpack from 'webpack';

const CLIENT_OUTPUT_PATH = 'src/main/resources/META-INF/resources/static';
const SERVER_OUTPUT_PATH = 'src/main/resources/public';

const config: webpack.Configuration[] = [
    {
        mode: 'development',
        entry: './js-src/client.ts',
        output: {
            filename: 'client.js',
            path: path.resolve(__dirname, CLIENT_OUTPUT_PATH),
        },

        resolve: {
            extensions: ['.tsx', '.ts', '.js'],
        },

        //devtool: 'inline-source-map',

        module: {
            rules: [
                {
                    test: /\.tsx?$/,
                    loader: 'babel-loader',
                    exclude: /node_modules/,
                    options: {
                        presets: [
                            "@babel/preset-typescript",
                            ["solid", { "generate": "dom", "hydratable": true }]
                        ]
                    }
                },
            ],
        },
    },
    {
        mode: 'development',
        entry: './js-src/server.ts',
        target: 'node',

        output: {
            filename: 'server.js',
            path: path.resolve(__dirname, SERVER_OUTPUT_PATH),
        },
        resolve: {
            extensions: ['.tsx', '.ts', '.js'],
        },

        devtool: 'inline-source-map',

        module: {
            rules: [
                {
                    test: /\.tsx?$/,
                    loader: 'babel-loader',
                    exclude: /node_modules/,
                    options: {
                        presets: [
                            "@babel/preset-typescript",
                            ["solid", { "generate": "ssr", "hydratable": true }]
                        ]
                    }

                },
            ],
        },
    },
];

export default config;