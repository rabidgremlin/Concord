#!/bin/bash

# Build UI
cd concord-ui
npm run build
cp -rf build ../concord-server/server/src/main/resources/ui

# Build server
set https_proxy=https://proxy2.isis.airnz.co.nz:5865
cd ../concord-server/server
chmod +x gradlew
./gradlew build

project_version=$(gradle properties -q | grep "version:" | awk '{print $2}' | tr -d '[:space:]')

# Package distribution, configuration into zip
zip ../../distribution-$project_version.zip build/libs/concord-server-$project_version-all.jar src/main/yml/server.yml