import { defineConfig } from 'orval';

const STALENESS_5_MINUTES = 5 * 60 * 1000;

export default defineConfig({
  user: {
    input: {
      target: './openapi.yaml',
      override: {
        transformer: (spec) => {
          const filteredPaths: typeof spec.paths = {};

          Object.keys(spec.paths).forEach((path) => {
            if (!path.startsWith('/api/admin/')) {
              filteredPaths[path] = spec.paths[path];
            }
          });

          return {
            ...spec,
            paths: filteredPaths,
          };
        },
      },
    },
    output: {
      mode: 'tags-split',
      target: './packages/user-app/src/api',
      schemas: './packages/shared/src/api-models',
      client: 'react-query',
      httpClient: 'axios',
      prettier: true,
      fileExtension: '.ts',
      override: {
        mutator: {
          path: './packages/user-app/src/api/axiosInstance.ts',
          name: 'axiosInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
          options: {
            staleTime: STALENESS_5_MINUTES,
            refetchOnWindowFocus: false,
          },
        },
      },
    },
  },
  admin: {
    input: {
      target: './openapi.yaml',
      override: {
        transformer: (spec) => {
          const filteredPaths: typeof spec.paths = {};

          Object.keys(spec.paths).forEach((path) => {
            if (path.startsWith('/api/admin/')) {
              filteredPaths[path] = spec.paths[path];
            }
          });

          return {
            ...spec,
            paths: filteredPaths,
          };
        },
      },
    },
    output: {
      mode: 'tags-split',
      target: './packages/admin-app/src/api',
      schemas: './packages/shared/src/api-models',
      client: 'react-query',
      httpClient: 'axios',
      prettier: true,
      fileExtension: '.ts',
      override: {
        mutator: {
          path: './packages/admin-app/src/api/axiosInstance.ts',
          name: 'axiosInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
          options: {
            staleTime: STALENESS_5_MINUTES,
            refetchOnWindowFocus: false,
          },
        },
      },
    },
  },
});
