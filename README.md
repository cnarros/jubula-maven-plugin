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
						<goals>
							<goal>prepare</goal>
							<goal>test</goal>
						</goals>
					</execution>
				</executions>
				<configuration>
					<rcpTargetDirectory>${basedir}/target/MyRCP</rcpTargetDirectory>
                    <executableFileName>MyRCP.exe</executableFileName>
					<jubulaPlugins>
						<dependency>
							<groupId>org.mule.tooling</groupId>
							<artifactId>jubula-accessibility-plugin</artifactId>
							<version>1.0-SNAPSHOT</version>
						</dependency>
					</jubulaPlugins>
					<autId>${jubula.autid}</autId>
					<rcpWorkingDir>${basedir}/target/MyRCP</rcpWorkingDir>
					<projectName>MyRCPApplication</projectName>
					<projectVersion>1.0</projectVersion>
					<databaseUrl>jdbc:mysql://${db.url}/jubula</databaseUrl>
					<databaseUser>${db.username}</databaseUser>
					<databasePassword>${db.password}</databasePassword>
					<testJob>${job}</testJob>
				</configuration>
			</plugin>
```

# Authors
Alejo Abdala (alejo dot abdala at mulesoft dot com)

Nicolas Avila (nicolas dot avila at mulesoft dot com)

Miguel Oliva (miguel dot oliva at mulesoft dot com)

Damian Pelaez (damian dot pelaez at mulesoft dot com)

Alberto Pose (@thepose)

# License
Copyright 2012 MuleSoft, Inc.

Licensed under the Common Public Attribution License (CPAL), Version 1.0.
    
### Happy hacking!
