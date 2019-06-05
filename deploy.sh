#!/bin/bash

docker tag stanga-secretary-bot:latest registry.heroku.com/stanga-secretary-bot/worker
docker push registry.heroku.com/stanga-secretary-bot/worker
heroku container:release -a stanga-secretary-bot worker

