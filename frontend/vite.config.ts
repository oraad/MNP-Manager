/// <reference types="vitest" />
/// <reference types="vite/client" />

import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import automapperTransformerPlugin from '@automapper/classes/transformer-plugin'
import typescript from '@rollup/plugin-typescript'
import * as path from 'path'

const autoMapperPluginOptions = {}

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        typescript({
            transformers: {
                before: [
                    {
                        type: 'program',
                        factory: (program) =>
                            automapperTransformerPlugin(
                                program, autoMapperPluginOptions
                            ).before,
                    }
                ]
            }
        }),
        react(),
    ],
    build: {
        sourcemap: true
    },
    test: {
        globals: true,
        include: ['src/**/*.{test,spec}.{ts,tsx}'],
        environment: 'jsdom',
        setupFiles: 'src/test/setup.ts',
        coverage: {
            reporter: ['lcovonly'],
        },
    },
    resolve: {
        alias: {
            '@/tests': path.resolve(__dirname, './tests'),
            '@/mocks': path.resolve(__dirname, './__mocks__'),
            '@': path.resolve(__dirname, './src'),
        },
    },
})
