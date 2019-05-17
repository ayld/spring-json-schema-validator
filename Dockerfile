FROM openjdk:11-jre-slim
USER root
RUN mkdir -p /opt/service/resource
RUN chmod 777 -R /opt/service/resource
ADD resource-0.0.1-SNAPSHOT.jar /opt/service/resource
ADD run.sh /opt/service/resource
RUN chmod 777 -R /opt/service/resource
EXPOSE 8082
ENTRYPOINT ["./opt/service/resource/run.sh"]