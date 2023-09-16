
Start by building the docker container

    sbt docker:publishLocal

Then run

    docker-compose up -d

Then create record:

    curl -X POST -d '{"phone": "123","fio": "Arlen"}' http://localhost:9000/api

Get record:

    curl http://localhost:9000/api/123


