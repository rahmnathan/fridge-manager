<h1>Fridge Manager</h1>

[![Build Status](https://jenkins.nathanrahm.com/buildStatus/icon?job=fridge-manager)](https://jenkins.nathanrahm.com/job/fridge-manager/)

This project provides a REST API that facilitates fridge management.

<h3>Documentation</h3>
REST API documentation is provided via Swagger at the service's root URL. This page also provides the ability to run test 
transactions against the service.

***The 'get fridge list' documentation/test request in the Swagger docs are currently invalid due to the use of the Pageable helper object.

<h3>Pipeline</h3>
Jenkins pipeline script is found in the jenkins.groovy file. This pipeline file is providing the above build status.

<h3>Compile - Package - Unit Test</h3>
This is a Maven project which supports all of the standard goals. This command will compile, package, and test the project.

```
mvn clean package
```

<h3>Test Coverage</h3>
Test coverage data is available in the target/site/jacoco directory after tests are run. Test coverage can be viewed 
graphically by opening the following file in a browser: target/site/jacoco/index.html.

<h3>Service Tests</h3>
There's a JMeter script located in the src/test/jmeter directory. This script will authenticate and test all endpoints 
of the service.

<h3>Docker</h3>
A Docker image can be created by running the following command.

```
mvn clean package dockerfile:build
```

<h3>Docker Compose</h3>
After building the image, you can run it with the provided docker-compose.yml file.

```
docker-compose up
```

<h3>Kubernetes - Helm</h3>
A Helm chart is included in this project. The processed Helm chart will be available in the target/classes/fridge-manager 
directory after installation.

```
mvn clean install
```

<h3>Metrics</h3>
Metrics are available at the /actuator/metrics endpoint of the service. You can dig down into each metric, for example: 
/actuator/metrics/system.cpu.usage

<h3>ToDo</h3>

* Fix 'get fridge list' Swagger documentation.

* Extend Swagger docs to include more detailed descriptions and error message examples.

* Extend unit test coverage.

* Add auditing.