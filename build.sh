#!/bin/bash

# Build docker image
docker build -t campaign-flink --build-arg FLINK_HOME=/opt/flink --no-cache .