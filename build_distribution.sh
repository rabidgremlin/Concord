#!/bin/bash

echo "Building Concord distribution..."

# Build UI
cd concord-ui
#echo "Install UI dependencies..."
#npm install || exit 1

#echo "Building UI..."
#npm run build || exit 1
#rm -rfv ../concord-server/server/src/main/resources/ui/*
#cp -rf build ../concord-server/server/src/main/resources/ui

# Build server
cd ../concord-server/server
chmod +x gradlew
echo "Building Server...."
./gradlew build || exit 1

echo "Done. See concord-server/server/build/distributions"