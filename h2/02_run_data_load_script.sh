#! /bin/bash

# the database has already been populated, so...
exit 0

$JAVA_HOME/bin/java \
  -cp bin/h2-1.4.200.jar org.h2.tools.RunScript \
  -url jdbc:h2:tcp://localhost:9092/imdb \
  -user sa \
  -password MrfQ7w6RAC_9oUV \
  -showResults \
  -script create_imdb_tables_and_load_data.sql

