# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Quick Start Commands

Essential commands for daily development:
```bash
# Development
pnpm dev:user           # Run user app (http://localhost:5173)
pnpm dev:admin          # Run admin app (http://localhost:5174)

# Build & Quality
pnpm build              # Build all packages
pnpm build:user         # Build user app only
pnpm build:admin        # Build admin app only
pnpm lint               # Lint all packages
pnpm type-check         # TypeScript type checking
pnpm format             # Format code with Prettier
pnpm format:check       # Check code formatting

# Development utilities
pnpm clean              # Clean build artifacts
pnpm dev:openapi        # Fetch OpenAPI spec and generate types
```

## Project Architecture

This is a **monorepo frontend** for a point-based roulette system with separate user and admin interfaces.

### Workspace Structure
```
ui/
├── packages/
│   ├── user-app/      # Customer-facing roulette application
│   ├── admin-app/     # Admin dashboard for management
│   └── shared/        # Shared types and API models
├── openapi.yaml       # API specification
└── orval.config.ts    # API client generation config
```

### Tech Stack
- **Framework**: React 19.2 + TypeScript 5.9
- **Build Tool**: Vite 7.x
- **Styling**: TailwindCSS
- **State Management**: TanStack React Query (user app)
- **Package Manager**: pnpm (workspaces)
- **Code Generation**: Orval (API clients from OpenAPI)

### Application Separation
- **user-app**: Customer interface for roulette participation, point management, order history
- **admin-app**: Administrative interface for budget management, order oversight, dashboard analytics
- **shared**: Common API models and types generated from OpenAPI spec

### API Integration
- API clients are auto-generated using Orval from `openapi.yaml`
- Shared models in `packages/shared/src/api-models/`
- Individual API clients in each app's `src/api/` directory
- Base axios configuration in each app's `axiosInstance.ts`

### Development Patterns
- **Aliases**: `@/` for app src, `@shared/` for shared package
- **Port allocation**: User app (5173), Admin app (5174)  
- **Code splitting**: React vendor chunks separated from other dependencies
- **Type safety**: Strict TypeScript with `noEmit` checking

### Mobile Integration
The project includes a Flutter mobile app at `/mobile/` that integrates with the web interfaces via:
- WebView embedding
- JavaScript channels for native communication
- PostMessage API for cross-platform messaging