#!/bin/sh

echo "Usage: run.sh ( commands ... )  ./run2.sh &>/dev/null &"
/usr/java/jre1.8.0_65/bin/java -Xmx384m -jar fcweb-project-1.0.0.jar

echo "exit"

exit 




/usr/java/jre1.8.0_65/bin/java -Xmx384m -jar spring-boot-send-email-1.0.jar