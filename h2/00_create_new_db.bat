@echo off

REM when creating the DB:
REM
REM
REM url is ---->    jdbc:h2:/your/path/to/TitleWebDemo/h2/db_files/imdb    <----
REM
REM
REM when using the DB after creation:
REM url is: jdbc:h2:tcp://localhost:9092/imdb
REM assuming you started the server with '-baseDir ./db_files'
REM
REM user: sa
REM pass: MrfQ7w6RAC_9oUV

REM databse has already been created, so you shouldn't need this:
REM "%JAVA_HOME%/bin/java" -cp bin/h2-1.4.200.jar org.h2.tools.Shell

