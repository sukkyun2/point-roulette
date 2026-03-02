#!/bin/bash
file=$(cat - | jq -r '.tool_input.file_path // empty')

if echo "$file" | grep -q '/api/.*\.kt$'; then
  echo '🔧 Formatting Kotlin...'
  cd "$(dirname "$0")/../.." && cd api && ./gradlew ktlintFormat > /dev/null 2>&1 || echo '⚠️  API format failed'
  echo '✅ Kotlin Done'
fi

if echo "$file" | grep -q '/ui/'; then
  echo '🎨 Formatting UI...'
  cd "$(dirname "$0")/../.." && cd ui && pnpm format > /dev/null 2>&1 || echo '⚠️  UI format failed'
  echo '✅ UI Done'
fi