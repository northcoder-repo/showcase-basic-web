#! /bin/bash

# url is:      jdbc:h2:tcp://localhost:9092/imdb
# user is:     sa
# pass is:     MrfQ7w6RAC_9oUV

# start the h2 server:
$JAVA_HOME/bin/java \
  -cp bin/* org.h2.tools.Server \
  -baseDir ./db_files

