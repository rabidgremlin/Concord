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

# Package distribution, configuration into zip
echo "Building distribution zip...."
project_version=$(./gradlew properties -q | grep "version:" | awk '{print $2}' | tr -d '[:space:]')
zip ../../concord-$project_version.zip build/libs/concord-server-$project_version-all.jar src/main/yml/server.yml || exit 1

echo "Done."