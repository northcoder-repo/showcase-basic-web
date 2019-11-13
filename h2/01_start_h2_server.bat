@echo off

REM url is:      jdbc:h2:tcp://localhost:9092/imdb
REM user is:     sa
REM pass is:     MrfQ7w6RAC_9oUV

REM start the server:
"%JAVA_HOME%/bin/java" ^
  -cp bin/* org.h2.tools.Server ^
  -baseDir ./db_files

pause
