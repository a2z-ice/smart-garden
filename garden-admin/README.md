# Docker for rabbitmq

docker run -d --name rabbit_stomp rabbitmq:management-alpine

docker exec -it rabbit_stomp sh

Run following in when you are in container by above command

rabbitmq-plugins enable --offline rabbitmq_mqtt rabbitmq_web_mqtt_examples rabbitmq_stomp


docker stop rabbit_stomp

docker commit rabbit_stomp rabbitmq-container-some-plugin

docker run -d -p 15672:15672 -p 5672:5672 -p 5671:5671 -p 15670:15670 -p 61613:61613 -p 1883:1883 --name rabbitmq-container-some-plugin rabbitmq-container-some-plugin


# From Dockerfile
FROM rabbitmq:management-alpine
USER root

RUN rabbitmq-plugins enable --offline rabbitmq_mqtt rabbitmq_web_mqtt_examples rabbitmq_stomp
CMD [ "echo","done" ]

docker run -d -p 15672:15672 -p 5672:5672 -p 5671:5671 -p 15670:15670 -p 61613:61613 -p 1883:1883 --name rabbitmq rabbitmq:management-alpine
