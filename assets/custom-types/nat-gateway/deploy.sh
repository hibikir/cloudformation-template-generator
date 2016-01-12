#!/bin/bash
#mkdir -p target
#cd target
#npm install aws-sdk
#zip -r nat_gateway.zip nat_gateway.js  node_modules/
#cd ..

account_id=$(aws iam get-user | jq -r .User.Arn |  perl -pe 's/arn:aws:iam::(\d+):.*/$1/')

if aws iam get-role --role-name lambda-execution-cf-nat-gateway >/dev/null 2>&1 ; then
    aws iam delete-role-policy \
        --role-name lambda-execution-cf-nat-gateway \
        --policy-name lambda-execution-cf-nat-gateway

    aws iam delete-role --role-name lambda-execution-cf-nat-gateway
fi

if aws lambda get-function --function-name cf-nat-gateway >/dev/null 2>&1 ; then
    aws lambda delete-function --function-name cf-nat-gateway
fi

aws iam create-role --role-name lambda-execution-cf-nat-gateway \
 --assume-role-policy-document file://trust-policy.json

aws iam put-role-policy --role-name lambda-execution-cf-nat-gateway \
 --policy-name lambda-execution-cf-nat-gateway \
 --policy-document file://policy.json

# This seems to take a little time to settle down and work. Because...amazon.
while ! aws lambda create-function --function-name cf-nat-gateway \
    --runtime nodejs \
    --role arn:aws:iam::${account_id}:role/lambda-execution-cf-nat-gateway \
    --handler nat_gateway.handler \
    --timeout 300 \
    --zip-file fileb://target/nat_gateway.zip ; do

    echo "The 'The role defined for the function cannot be assumed by Lambda' error you may have just seen is time based.  Sleeping 1 and trying again."
    echo "If you saw a different error or this doesn't resolve itself after a few tries, hit ctrl-c"
    sleep 1
done

