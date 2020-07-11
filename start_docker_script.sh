#!/bin/bash
DOCKER_BUILDKIT=1 docker build -t pollservice:1.0 .
docker run -p 8080:8080 -it pollservice:1.0 /bin/bash