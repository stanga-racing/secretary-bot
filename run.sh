#!/bin/bash

docker run \
  --rm \
  -i \
  -t \
  -e STANGA_MASTER_EXCEL_API_KEY=$STANGA_MASTER_EXCEL_API_KEY \
  -e STANGA_MASTER_EXCEL_SPREADSHEET_ID=$STANGA_MASTER_EXCEL_SPREADSHEET_ID \
  -e STANGA_GENERAL_WEBHOOK_URL=$STANGA_GENERAL_WEBHOOK_URL \
  stanga-secretary-bot

