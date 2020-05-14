FROM docker.elastic.co/beats/filebeat:7.6.2
COPY ./filebeat.yml /usr/share/filebeat/filebeat.yml
USER root
RUN chown root /usr/share/filebeat/filebeat.yml
RUN chmod 444 /usr/share/filebeat/filebeat.yml