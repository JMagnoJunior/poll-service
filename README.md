## Poll Service

Endpoint to list and search polls in a mongodb database.

## How to run
The dockerfile was built using experimental features. Please, use the script to  `start_docker_script.sh` to execute do 
docker-compose in a easy way or execute the docker compose command this way:

    >  COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILDKIT=1 docker-compose up


## Additional info

This project will load all data for test on the startup time using the PopulateInitialDataConfig configuration.

There is no reason to have volume for this simple database, that is why there is no volume specified in the
docker-compose file.

## Endpoints

###### GET /polls

    // get all
    curl --location --request GET 'localhost:8080/polls
    
    // get by date
    curl --location --request GET 'localhost:8080/polls?fromDate=2017-01-27'
   
    // get by initiator
    curl --location --request GET 'localhost:8080/polls?createdBy=John Doe'


###### POST /polls/search

ps: This is a simplified search method. Sort by title is given by default. 
The payload is : { "page": number, "size": number, "title": string }

    curl --location --request POST 'localhost:8080/polls/search' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "page": 0,
        "size": 5,
        "title": "the"
    }'

