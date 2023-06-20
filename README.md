# Realtime Transaction Challenge

## Description
Simple backend service implemented using specifications from 
```
service.yml
```

## Running the app
In root execute
```
docker compose up
```

This will bring up two docker containers. One is a server that will serve the `.war` from the root of the project and the other is a standard mongodb container.

## Interacting with service
After the docker containers are up all endpoints are under
```
localhost:8080/service-1.0/
```

I have also provided a Postman collection for easy interaction with the service, `realtime_transaction_challenge.postman_collection`

You can also curl it directly
```
curl --location --request PUT 'localhost:8080/service-1.0/load/50e70c62-e480-49fc-bc1b-e991ac672173' \
--header 'Content-Type: application/json' \
--data '{
    "messageId": "55210c62-e480-asdf-bc1b-e991ac67FSAC4",
    "userId": "8786e2f9-d472-46a8-958f-d659880e723d",
    "transactionAmount": {
        "amount": "1",
        "currency": "USD",
        "debitOrCredit": "DEBIT"
    }
}'
```

## Comments on the provided specifications
* `AuthorizationRequest` and `LoadRequst` are identical. I would have it as one class to make things more streamlined. I made them as two seperate classes to adhere to the specs.
* Same idea and decision done for `LoadResponse` and `AuthorizationResponse` even though `AuthorizationResponse` has an extra field in the specs. I would argue `LoadResponse` needs a `responseCode` field as well.


## Assumptions
* `messageId` is unique and will never repeat on subsequent requests. If a request has a `messageId` that was already processed, it will be declined.
* Used only two decimal places as I am expecting only normal currency for this challenge
* Debit means addition of funds, credit means substraction of funds

## Deployment/infrastructure
My expectation is that this service will be deployed on a pod in a K8s cluster and will have no public exposure as it will be called only by other internal pods. Thus security on this will be minimal as the K8s cluster security will be managed by another component.

The pod is absolutely stateless and will only have environment specific properties when deployed. The state will be stored in a database which will exist elsewhere with more rigor around it.

Scaling to match increased demand can be easily achieved by deploying more copies of the same pod in the cluster. Redirection of requests will be done by the cluster load balancer as needed.

Rolling updates will ensure that there is no downtime when deploying a newer version of the service to the cluster.

## Monitoring/logging
The pod resource consumption and health can be easily monitored through a variety of tools. Scaling threshold can set during K8s set up.

The service has basic logging that outputs to a log file. That file can be easily picked up and consumed by various tools such as https://github.com/grafana/loki and displayed in any desireable way. The logs will also contain errors during app runtime. Those errors, along with pod errors, can be used to trigger alerts and notify appropriate parties. 

## Next development steps
Here are some next things I would look at when continue to develop this app
* Implement mongodb aggregate pipeline. This will reduce the number of data layer accesses per request to 1 thus most likely speeding up the app.
* Add integration tests. Unit tests in the current set up don't test as much because a lot of the operations are performed in mongo. Integration tests with a local mongo instance will fully test the application.
* Consolidate the data models after agreement/sign-off from stake holders
* Add scheduled health checks that will hit the `/ping` endpoint to check regularly if the service is available and ready to process requests