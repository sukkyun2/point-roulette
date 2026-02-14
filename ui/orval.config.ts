import { defineConfig } from "orval";

const STALENESS_5_MINUTES = 5 * 60 * 1000;

export default defineConfig({
  user: {
    input: {
      target: "./openapi.yaml",
    },
    output: {
      mode: "tags-split",
      target: "./packages/user-app/src/api",
      schemas: "./packages/user-app/src/api/models",
      client: "react-query",
      httpClient: "axios",
      prettier: true,
      fileExtension: ".ts",
      override: {
        mutator: {
          path: "./packages/user-app/src/api/axiosInstance.tsx",
          name: "axiosInstance",
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
