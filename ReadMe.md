<h1>Fridge Manager</h1>

[![Build Status](https://jenkins.nathanrahm.com/buildStatus/icon?job=fridge-manager)](https://jenkins.nathanrahm.com/job/fridge-manager/)

<h3>Pipeline</h3>
Jenkins pipeline script is found in jenkins.groovy file. This pipeline file is providing the above build status.

<h3>Compile - Package - Test</h3>
This is a Maven project which supports all of the standard goals. This command will compile, package, and test the project.

```
mvn clean package
```

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
A Helm chart is included in this project. The processed Helm chart will be available in the ./target/classes/fridge-manager directory after installation.

```
mvn clean install
```

<h3>ToDo</h3>
 * Extend unit test coverage.
