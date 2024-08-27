#!/bin/bash

docker compose -f ./src/main/resources/docker-compose.yml up -d

# -- > Create DynamoDb Table
echo Creating  DynamoDb \'Users\' table ...
aws dynamodb create-table --endpoint-url http://127.0.0.1:4566 --cli-input-json \
 '{
    "TableName": "Users",
    "AttributeDefinitions": [
      {
        "AttributeName": "id",
        "AttributeType": "S"
      }
    ],
    "KeySchema": [
      {
        "AttributeName": "id",
        "KeyType": "HASH"
      }
    ],
    "BillingMode": "PAY_PER_REQUEST"
  }'

# --> List DynamoDb Tables
echo Listing tables ...
aws dynamodb list-tables --endpoint-url http://127.0.0.1:4566

aws dynamodb scan --table-name Users --endpoint-url http://127.0.0.1:4566

