# jubula-maven-plugin
Run [Jubula Functional Tests](http://www.eclipse.org/jubula/) in a Maven build. Beware: This project is under heavy development and may be broken from time to time.

# Build

    mvn clean install

# Usage

Add the following snippet inside build -> plugins:

```xml
<plugin>
    <groupId>org.mule.tooling</groupId>
    <artifactId>jubula-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <executions>
        <execution>
            <phase>integration-tests</phase>
            <goals>
                <goal>test</goal>
            </goals>
            <configuration>
                <product>org.mule.tooling:my-product:3.2.1</product>
            </configuration>
        </execution>
    </executions>
</plugin>
```

# Authors
Alejo Abdala (alejo dot abdala at mulesoft dot org)
Alberto Pose (@thepose)

# License
Copyright 2012 MuleSoft, Inc.

Licensed under the Common Public Attribution License (CPAL), Version 1.0.
    
### Happy hacking!
