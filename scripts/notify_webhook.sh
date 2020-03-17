#!/bin/bash

[[ -n "$NOTIFY_WEBHOOK_URL" ]] && curl -X POST -d {} "$NOTIFY_WEBHOOK_URL"
