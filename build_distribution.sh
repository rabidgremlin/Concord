#!/bin/bash

# Build UI
cd concord-ui
npm install || exit 1
npm run build || exit 1
rm -rfv ../concord-server/server/src/main/resources/ui/*
cp -rf build ../concord-server/server/src/main/resources/ui

# Build server
cd ../concord-server/server
chmod +x gradlew
./gradlew build || exit 1

project_version=$(gradle properties -q | grep "version:" | awk '{print $2}' | tr -d '[:space:]')

# Package distribution, configuration into zip
zip ../../distribution-$project_version.zip build/libs/concord-server-$project_version-all.jar src/main/yml/server.yml || exit 1