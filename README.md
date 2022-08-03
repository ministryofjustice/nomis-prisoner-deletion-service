# nomis-prisoner-deletion-service

[![CircleCI](https://circleci.com/gh/ministryofjustice/probation-offender-events/tree/main.svg?style=svg)](https://circleci.com/gh/ministryofjustice/nomis-prisoner-deletion-service)
[![Docker](https://quay.io/repository/hmpps/probation-offender-events/status)](https://quay.io/repository/hmpps/nomis-prisoner-deletion-service/status)
[![API docs](https://img.shields.io/badge/API_docs_(needs_VPN)-view-85EA2D.svg?logo=swagger)](https://nomis-delete.aks-dev-1.studio-hosting.service.justice.gov.uk/swagger-ui.html)

Nomis prisoner deletion service is currently being used to retrieve data from the NOMIS database for assessing
an offender's eligibility of deletion under data compliance law.

### Running locally with Data Compliance Queues

A suite of services can be started with docker-compose that will also attach Nomis prisoner deletion service to
a couple of queues used to communicate deletions and deletion referrals.

```bash
docker-compose up
```
Alternatively, to run a local version of the service (along with the dependencies), run the following:

```bash
bash run_local.sh
```

This will start up the required dependencies, build & run the application locally.

#### To publish to the data compliance request queue:
(Warning, if configured, this will prompt nomis-prisoner-deletion-service to delete the Offender provided)

The `referralId` should match an existing referral in the `OFFENDER_DELETION_REFERRAL` table
in the Data Compliance database.

The offenders 
```bash

aws --endpoint-url=http://localhost:4566 sqs send-message \
    --queue-url http://localstack:4566/queue/data_compliance_request_queue \
    --message-body '{"offenderIdDisplay":"A1234AA","referralId":123}' \
    --message-attributes "eventType={StringValue=DATA_COMPLIANCE_OFFENDER-DELETION-GRANTED,DataType=String}"
```

#### To read off the data compliance response queue:
```bash
aws --endpoint-url=http://localhost:4566 sqs receive-message \
    --queue-url http://localhost:4566/queue/data_compliance_response_queue
```