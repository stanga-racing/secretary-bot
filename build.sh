#!/bin/bash

docker build \
  --rm \
  -t stanga-secretary-bot \
  -f src/docker/Dockerfile \
  .
