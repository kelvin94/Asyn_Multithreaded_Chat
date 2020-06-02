create-network:
	docker network create javaapp
stop:
	docker container stop chatserver; docker container stop chatclient; docker container stop chatclient2; docker container stop chatclient3; docker container rm chatserver; docker container rm chatclient; docker container rm chatclient2; docker container rm chatclient3;
run-client:
	docker run --net javaapp --name chatclient -it chatclient:latest;
run-client2:
	docker run --net javaapp --name chatclient2 -it chatclient:latest;
run-client3:
	docker run --net javaapp --name chatclient3 -it chatclient:latest;
run-server:
	docker run --net javaapp --name chatserver -p 3003:30005 -it chatserver:latest;
build:
	docker build -t chatclient -f ./Dockerfile.client .; docker build -t chatserver -f ./Dockerfile.server .
