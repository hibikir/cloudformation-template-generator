// Source http://www.spacevatican.org/2015/12/20/cloudformation-nat-gateway/
// https://gist.github.com/fcheung/baec53381350a4b11037

var aws = require('aws-sdk');
var ec2 = new aws.EC2();

exports.handler = function(event, context) {
    if (event.ResourceType === 'Custom::NatGateway') {
        handleGateway(event, context);
    } else if (event.ResourceType === 'Custom::NatGatewayRoute') {
        handleRoute(event, context);
    } else {
        console.log("Unknown resource type: " + event.ResourceType);
        response.send(event, context, {
            Error: "unknown resource type: " + event.ResourceType
        }, response.FAILED);
    }
};

var handleRoute = function(event, context) {
    var destinationCidrBlock = event.ResourceProperties.DestinationCidrBlock;
    var routeTableId = event.ResourceProperties.RouteTableId;
    var responseData = {};
    if (!destinationCidrBlock) {
        responseData = {
            Error: "missing parameter DestinationCidrBlock "
        };
        console.log(responseData.Error);
        response.send(event, context, response.FAILED, responseData);
        return;
    }
    else {
        if (!routeTableId) {
            responseData = {
                Error: "missing parameter RouteTableId "
            };
            console.log(responseData.Error);
            response.send(event, context, response.FAILED, responseData);
            return;
        }
    }

    if (event.RequestType === 'Delete') {
        deleteRoute(event, context);
    } else if (event.RequestType === 'Create') {
        createRoute(event, context);
    } else if (event.RequestType === 'Update') {
        if (event.ResourceProperties.DestinationCIDRBlock === event.OldResourceProperties.DestinationCIDRBlock &&
            event.ResourceProperties.RouteTableId === event.OldResourceProperties.RouteTableId) {
            replaceRoute(event, context);
        } else {
            createRoute(event, context);
        }
    } else {
        console.log("Unknown request type " + event.RequestType);
        response.send(event, context, {
            Error: "unknown request type: " + event.RequestType
        }, response.FAILED);
    }
};

var deleteRoute = function(event, context) {
    var responseData = {};
    var destinationCidrBlock = event.ResourceProperties.DestinationCidrBlock;
    var routeTableId = event.ResourceProperties.RouteTableId;

    if(event.PhysicalResourceId.match(/^gateway-route-/)){

        ec2.deleteRoute({
            RouteTableId: routeTableId,
            DestinationCidrBlock: destinationCidrBlock
        }, function(err, data) {
            if (err) {
                responseData = {
                    Error: "delete route failed " + err
                };
                console.log(responseData.Error);
                response.send(event, context, response.FAILED, responseData);

            } else {
                response.send(event, context, response.SUCCESS, {}, physicalId(event.ResourceProperties));
            }
        });
    }else{
        console.log("unexpected physical id for route " + event.PhysicalResourceId + " - ignoring");
        response.send(event, context, response.SUCCESS, {});
    }
};


var createRoute = function(event, context) {
    var responseData = {};
    var destinationCidrBlock = event.ResourceProperties.DestinationCidrBlock;
    var routeTableId = event.ResourceProperties.RouteTableId;
    var natGatewayId = event.ResourceProperties.NatGatewayId;

    if (natGatewayId) {
        ec2.createRoute({
            RouteTableId: routeTableId,
            DestinationCidrBlock: destinationCidrBlock,
            NatGatewayId: natGatewayId
        }, function(err, data) {
            if (err) {
                responseData = {
                    Error: "create route failed " + err
                };
                console.log(responseData.Error);
                response.send(event, context, response.FAILED, responseData);

            } else {
                response.send(event, context, response.SUCCESS, {}, physicalId(event.ResourceProperties));
            }
        });
    } else {
        responseData = {
            Error: "missing parameter natGatewayId "
        };
        console.log(responseData.Error);
        response.send(event, context, response.FAILED, responseData);
        return;
    }
};

var replaceRoute = function(event, context) {
    var responseData = {};
    var destinationCidrBlock = event.ResourceProperties.DestinationCidrBlock;
    var routeTableId = event.ResourceProperties.RouteTableId;
    var natGatewayId = event.ResourceProperties.NatGatewayId;

    if (natGatewayId) {
        ec2.replaceRoute({
            RouteTableId: routeTableId,
            DestinationCidrBlock: destinationCidrBlock,
            NatGatewayId: natGatewayId
        }, function(err, data) {
            if (err) {
                responseData = {
                    Error: "create route failed " + err
                };
                console.log(responseData.Error);
                response.send(event, context, response.FAILED, responseData);

            } else {
                response.send(event, context, response.SUCCESS, {}, physicalId(event.ResourceProperties));
            }
        });
    } else {
        responseData = {
            Error: "missing parameter natGatewayId "
        };
        console.log(responseData.Error);
        response.send(event, context, response.FAILED, responseData);
        return;
    }
};


var physicalId = function(properties) {
    return 'gateway-route-' + properties.RouteTableId + '-' + properties.DestinationCIDRBlock;
};


var handleGateway = function(event, context) {
    if (event.RequestType === 'Delete') {
        deleteGateway(event, context);
    } else if (event.RequestType === 'Update' || event.RequestType === 'Create') {
        createGateway(event, context);
    } else {
        response.send(event, context, {
            Error: "unknown type: " + event.RequestType
        }, response.FAILED);
    }
};

var createGateway = function(event, context) {
    var responseData = {};
    var subnetId = event.ResourceProperties.SubnetId;
    var allocationId = event.ResourceProperties.AllocationId;
    var waitHandle = event.ResourceProperties.WaitHandle;

    if (subnetId && allocationId) {
        ec2.createNatGateway({
            AllocationId: allocationId,
            SubnetId: subnetId
        }, function(err, data) {
            if (err) {
                responseData = {
                    Error: "create gateway failed " + err
                };
                console.log(responseData.Error);
                response.send(event, context, response.FAILED, responseData);
            } else {
                responseData = {
                }
                response.send(event, context, response.SUCCESS, responseData, data.NatGateway.NatGatewayId, true);

                waitForGatewayStateChange(data.NatGateway.NatGatewayId, ['available', 'failed'], function(state){
                    if(waitHandle){
                        signalData = {
                            "Status": state == 'available' ? 'SUCCESS' : 'FAILURE',
                            "UniqueId": data.NatGateway.NatGatewayId,
                            "Data": "Gateway has state " + state,
                            "Reason": ""
                        }
                        sendSignal(waitHandle, context, signalData);
                    }else{
                        if(state != 'available'){
                            console.log("gateway state is not available");
                        }
                        context.done();
                    }
                });
            }
        })
    } else {
        if (!subnetId) {
            responseData = {
                Error: 'subnet id not specified'
            };
            console.log(responseData.Error);
            response.send(event, context, response.FAILED, responseData);
        } else {
            responseData = {
                Error: 'allocationId not specified'
            };
            console.log(responseData.Error);
            response.send(event, context, response.FAILED, responseData);
        }
    }
};

var waitForGatewayStateChange = function (id, states, onComplete){
    ec2.describeNatGateways({NatGatewayIds: [id], Filter: [{Name: "state", Values: states}]}, function(err, data){
        if(err){
            console.log("could not describeNatGateways " + err);
            onComplete('failed');
        }else{
            if(data.NatGateways.length > 0){
                onComplete(data.NatGateways[0].State)
            }else{
                console.log("gateway not ready; waiting");
                setTimeout(function(){ waitForGatewayStateChange(id, states, onComplete);}, 15000);
            }
        }
    });
};

var deleteGateway = function(event, context) {
    var responseData = {};
    if (event.PhysicalResourceId && event.PhysicalResourceId.match(/^nat-/)) {
        ec2.deleteNatGateway({
            NatGatewayId: event.PhysicalResourceId
        }, function(err, data) {
            if (err) {
                responseData = {
                    Error: "delete gateway failed " + err
                };
                console.log(responseData.Error);
                response.send(event, context, response.FAILED, responseData, event.PhysicalResourceId);
            } else {
                waitForGatewayStateChange(event.PhysicalResourceId, ['deleted'], function(state){
                    response.send(event, context, response.SUCCESS, {}, event.PhysicalResourceId);
                });
            }
        })
    } else {
        console.log("No valid physical resource id passed to destroy - ignoring " + event.PhysicalResourceId);
        response.send(event, context, response.SUCCESS, responseData, event.PhysicalResourceId);
    }
}


var sendSignal = function(handle, context, data){
    var body = JSON.stringify(data);
    var https = require("https");
    var url = require("url");
    console.log("signal body:\n", body);

    var parsedUrl = url.parse(handle);
    var options = {
        hostname: parsedUrl.hostname,
        port: 443,
        path: parsedUrl.path,
        method: "PUT",
        headers: {
            "content-type": "",
            "content-length": body.length
        }
    };

    var request = https.request(options, function(response) {
        console.log("Status code: " + response.statusCode);
        console.log("Status message: " + response.statusMessage);
        context.done();
    });

    request.on("error", function(error) {
        console.log("sendSignal(..) failed executing https.request(..): " + error);
        context.done();
    });

    request.write(body);
    request.end();
};
/* The below section is adapted from the cfn-response module, as published at:

 http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-lambda-function-code.html

 */

/* Copyright 2015 Amazon Web Services, Inc. or its affiliates. All Rights Reserved.
 This file is licensed to you under the AWS Customer Agreement (the "License").
 You may not use this file except in compliance with the License.
 A copy of the License is located at http://aws.amazon.com/agreement/.
 This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, express or implied.
 See the License for the specific language governing permissions and limitations under the License. */
var response = {}

response.SUCCESS = "SUCCESS";
response.FAILED = "FAILED";

response.send = function(event, context, responseStatus, responseData, physicalResourceId, continueFuncton) {

    var responseBody = JSON.stringify({
        Status: responseStatus,
        Reason: "See the details in CloudWatch Log Stream: " + context.logStreamName,
        PhysicalResourceId: physicalResourceId || context.logStreamName,
        StackId: event.StackId,
        RequestId: event.RequestId,
        LogicalResourceId: event.LogicalResourceId,
        Data: responseData
    });

    console.log("Response body:\n", responseBody);

    var https = require("https");
    var url = require("url");

    var parsedUrl = url.parse(event.ResponseURL);
    var options = {
        hostname: parsedUrl.hostname,
        port: 443,
        path: parsedUrl.path,
        method: "PUT",
        headers: {
            "content-type": "",
            "content-length": responseBody.length
        }
    };

    var request = https.request(options, function(response) {
        console.log("Status code: " + response.statusCode);
        console.log("Status message: " + response.statusMessage);
        if(!continueFuncton){
            context.done();
        }
    });

    request.on("error", function(error) {
        console.log("send(..) failed executing https.request(..): " + error);
        context.done();
    });

    request.write(responseBody);
    request.end();
}
