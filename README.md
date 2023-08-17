Requirements:
•

### COMMANDS

In project directory:

**BUILD**:
`$ ./gradlew build`

**RUN**:
`$ ./gradlew bootRun`

**TEST**:
`$ ./gradlew test`

### INTERFACE

This is microservice with REST interface. There are two operations defined: assigning sample to rack and getting
all assignments.

**POST** /assignments - assigning sample to rack

**GET** /assignments - getting all racks with samples

Interface details are available in Swagger UI: http://localhost:8080/swagger-ui/index.html

### GENERAL CONCEPT

**SampleSorter** service is designed to sort samples to racks with specific rules. The samples assignment to racks
is persisted in repository as sorting machine is agnostic and has no capability to do this.
If any of the policies are not met in a given rack, the possibility in the next one is checked
till sample is assigned to rack or error returned. Created assignment is persisted and requested to sorter machine
client.

### USE CASE EXAMPLE

Let's try to add sample:
```shell
curl --location 'http://localhost:8080/assignments' \
--header 'Content-Type: application/json' \
--data '{
    "sampleId": "16b5c362-3ad4-11ee-be56-0242ac120003",
    "patient": {
        "age": 31,
        "cityDistrict": "Downtown",
        "company": "Tesla",
        "visionDefect": "cataract"
    }
}'
```

The response is 201 - created and sample id with assigned rack id is returned:
```json
{
  "sampleId": "16b5c362-3ad4-11ee-be56-0242ac120003",
  "rackId": 1
}
```

There is no possible to add another sample with the same id so repeated call move sample to next rack:
```json
{
  "sampleId": "16b5c362-3ad4-11ee-be56-0242ac120003",
  "rackId": 2
}
```

Let's change sample id slightly;
```shell
curl --location 'http://localhost:8080/assignments' \
--header 'Content-Type: application/json' \
--data '{
    "sampleId": "16b5c362-3ad4-11ee-be56-0242ac120004",
    "patient": {
        "age": 31,
        "cityDistrict": "Downtown",
        "company": "Tesla",
        "visionDefect": "cataract"
    }
}'
```

The response is 500 - internal server error and `BusinessException` is logged with message: 
_Cannot assign sample to any rack_ as we have broken polices - patients with the same age or city district or company 
or vision defect:
```json
{
    "timestamp": "2023-08-17T08:45:43.493+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "path": "/assignments"
}
```
We have two racks configured so no more option to add sample to another rack. Check if it is true and call
```shell
curl --location 'http://localhost:8080/assignments'
```
It gives us expected result:
```json
{
    "racks": [
        {
            "rackId": 2,
            "samples": [
                {
                    "sampleId": "16b5c362-3ad4-11ee-be56-0242ac120003",
                    "patient": {
                        "age": 31,
                        "company": "Tesla",
                        "cityDistrict": "Downtown",
                        "visionDefect": "cataract"
                    }
                }
            ]
        },
        {
            "rackId": 1,
            "samples": [
                {
                    "sampleId": "16b5c362-3ad4-11ee-be56-0242ac120003",
                    "patient": {
                        "age": 31,
                        "company": "Tesla",
                        "cityDistrict": "Downtown",
                        "visionDefect": "cataract"
                    }
                }
            ]
        }
    ]
}
```
Finally, let's add another sample with no duplicates to be accepted:

```shell
curl --location 'http://localhost:8080/assignments' \
--header 'Content-Type: application/json' \
--data '{
    "sampleId": "16b5c362-3ad4-11ee-be56-0242ac120002",
    "patient": {
        "age": 32,
        "cityDistrict": "Meldox",
        "company": "Sonix",
        "visionDefect": "none"
    }
}'
```

and check all assignments again:
```shell
curl --location 'http://localhost:8080/assignments'
```
That's it, new sample is assigned to rack 1: 
```json
{
    "racks": [
        {
            "rackId": 1,
            "samples": [
                {
                    "sampleId": "16b5c362-3ad4-11ee-be56-0242ac120002",
                    "patient": {
                        "age": 32,
                        "company": "Sonix",
                        "cityDistrict": "Meldox",
                        "visionDefect": "none"
                    }
                },
                {
                    "sampleId": "16b5c362-3ad4-11ee-be56-0242ac120003",
                    "patient": {
                        "age": 31,
                        "company": "Tesla",
                        "cityDistrict": "Downtown",
                        "visionDefect": "cataract"
                    }
                }
            ]
        },
        {
            "rackId": 2,
            "samples": [
                {
                    "sampleId": "16b5c362-3ad4-11ee-be56-0242ac120003",
                    "patient": {
                        "age": 31,
                        "company": "Tesla",
                        "cityDistrict": "Downtown",
                        "visionDefect": "cataract"
                    }
                }
            ]
        }
    ]
}
```





