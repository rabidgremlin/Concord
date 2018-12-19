#!/bin/bash

echo "Building Concord distribution..."

# Build UI
cd concord-ui
echo "Install UI dependencies..."
npm install || exit 1

echo "Building UI..."
npm run build || exit 1
rm -rfv ../concord-server/server/src/main/resources/ui/*
cp -rf build/* ../concord-server/server/src/main/resources/ui

# Build server
echo "Building Server...."
cd ../concord-server/server
chmod +x gradlew
./gradlew  --no-daemon --refresh-dependencies clean build || exit 1

echo "Done. See concord-server/server/build/distributions"