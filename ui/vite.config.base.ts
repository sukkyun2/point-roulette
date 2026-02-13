import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path';

export const createBaseConfig = (dirname: string, port: number) =>
  defineConfig({
    plugins: [react()],
    resolve: {
      alias: {
        '@': path.resolve(dirname, './src'),
        '@shared': path.resolve(dirname, '../shared/src'),
      },
    },
    server: {
      port,
      host: true,
    },
    preview: {
      port,
    },
    build: {
      target: 'esnext',
      rollupOptions: {
        output: {
          manualChunks: (id) => {
            // Vendor chunk for core React dependencies
            if (id.includes('node_modules')) {
              if (id.includes('react') || id.includes('react-dom')) {
                return 'react-vendor';
              }
              // Other npm packages
              return 'vendor';
            }
          },
        },
      },
    },
  });