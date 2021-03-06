FROM maven:3.8.2-jdk-11
	WORKDIR /webservice_crm
	COPY . .
	RUN mvn clean install -DskipTests
	CMD mvn spring-boot:run