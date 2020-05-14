# ELK instructions (Linux + Docker)

In this project ELK is used to collect, process and visualize logs from different services.

## Elasticsearch

Each node in elastic cluster is given it's own volume using docker-compose. If too much space is taken by the documents the elastic indices will become read-only and all write operations will be suspended. 
To clear your volumes (use this in development):
- Stop the containers and delete them
- Run a `docker volume prune`, this will delete the unused volumes and their data
- Be aware that all your indices will be lost

## Logstash configuration

### Filebeat approach

Logstash is configured using `logstash-pipeline.conf` file. The name of the file can be anything as long as it is copied on a proper destination `/usr/share/logstash/pipeline/`.

- In this example one beats input is used, which means that LS receives data from *Filebeat*.
- Logstash has to have an open port for receiving input.

- [Grok](https://www.elastic.co/guide/en/logstash/current/plugins-filters-grok.html) is used for parsing and filtering the messages. 

Two different regexp patterns are provided to Grok to work with:
 - Pattern for reading custom JAVA logs
 - Pattern for reading NGINX access logs

Patterns can be tested using [Grok Debbuger](http://grokdebug.herokuapp.com/).
- Messages are then indexed differently based on type and sent to elasticsearch output.

### Syslog approach

If you are developing on linux you can use `syslog` as a logging driver for your docker container.
This means that all the logs from that container will be written to `/var/log/syslog` file.

- Logstash has to have an open port for receiving input
- In case of using docker-compose logging driver can be specified in logging section of a service:
```
logging:
    driver: syslog
    options:
       tag: "any_tag"
```
- Edit rsyslog default configuration `/etc/rsyslog.d/50-default.conf` to send messages to the address Logstash is running on `*.* @@LSAddress:LSPort`. Two @@ specify TCP connection, and one @ specifies UDP connection.






