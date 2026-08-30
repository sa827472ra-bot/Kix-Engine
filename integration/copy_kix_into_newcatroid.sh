#!/usr/bin/env bash
# Copy Kix Engine production sources into a local NewCatroid checkout.
# Usage:
#   ./integration/copy_kix_into_newcatroid.sh /path/to/NewCatroid
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
TARGET="${1:?Usage: $0 /path/to/NewCatroid}"

DEST_JAVA="$TARGET/catroid/src/main/java/org/catrobat/catroid/kix"
DEST_RES="$TARGET/catroid/src/main/res/values"

mkdir -p "$DEST_JAVA" "$DEST_RES"

echo "Copying Kotlin sources..."
rsync -a --delete \
  --exclude 'content' \
  "$ROOT/src/main/kotlin/org/catrobat/catroid/kix/" \
  "$DEST_JAVA/"

echo "Copying colors..."
cp "$ROOT/res/values/kix_colors.xml" "$DEST_RES/kix_colors.xml"

echo "Done."
echo "Next: edit CategoryBricksFactory, XstreamSerializer, stage tick — see integration/NEWCATROID.md"
