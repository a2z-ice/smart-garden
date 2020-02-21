# Docker log rotating:my-app:
<pre><code>
image: my-app:latest
    logging:
        driver: "json-file"
        options:
            max-file: 5
            max-size: 10m
</pre></code>            
# Docker for rabbitmq

docker run -d --name rabbit_stomp rabbitmq:management-alpine

docker exec -it rabbit_stomp sh

Run following in when you are in container by above command

rabbitmq-plugins enable --offline rabbitmq_mqtt rabbitmq_web_mqtt_examples rabbitmq_stomp


docker stop rabbit_stomp

docker commit rabbit_stomp rabbitmq-container-some-plugin

docker run -d -p 15672:15672 -p 5672:5672 -p 5671:5671 -p 15670:15670 -p 61613:61613 -p 1883:1883 --name rabbitmq-container-some-plugin rabbitmq-container-some-plugin


# From Dockerfile
<pre><code>
FROM rabbitmq:management-alpine
USER root

RUN rabbitmq-plugins enable --offline rabbitmq_mqtt rabbitmq_web_mqtt_examples rabbitmq_stomp

CMD ["rabbitmq-server"]

EXPOSE 15672 1883 61613 15670 5671

</code></pre>

docker build -t smart-g-amqp -f Dockerfile .

docker run -d -p 15672:15672 -p 5672:5672 -p 5671:5671 -p 15670:15670 -p 61613:61613 -p 1883:1883 --name rabbitmq smart-g-amqp

#Run docker with name
docker run -v /var/run/docker.sock:/var/run/docker.sock --name jenkins-docker -p 8080:8080 jenkins-docker
