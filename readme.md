### Installation:

1. Right Click Project
2. Open Module Settings
3. Libraries > Add > Add from Maven:
    * `com.nimbusds:nimbus-jose-jwt:4.23`
    * `org.jboss.resteasy:resteasy-multipart-provider:2.2.3.GA`
    * `commons-io:commons-io:2.8.0`
4. Open `pom.xml` file, right click the content, Maven > Reload Project
4. Rename Artifact to `dae_project.war`