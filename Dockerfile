FROM openjdk:8
ADD build/libs/springboot-docker-mysql-*.jar springboot-docker-mysql.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "springboot-docker-mysql.jar"]