#!/bin/sh
 
set -x

VERSION_ID="${BUILD_ID}"
IMAGE_NAME="IRBL:${BUILD_ID}"
IMAGE_ADDR="101.132.148.43/${IMAGE_NAME}"
 
docker build -f Dockerfile --build-arg jar_name=target/IRBL-0.0.1-SNAPSHOT.jar -t ${IMAGE_NAME}:${VERSION_ID} .
 
docker tag  ${IMAGE_NAME}:${VERSION_ID}  ${IMAGE_ADDR}:${VERSION_ID}
docker login --username=admin --password=Harbor12345@irbl 101.132.148.43
docker push ${IMAGE_ADDR}:${VERSION_ID}
