@echo off

REM
REM first download and place the new JAR file in the \h2\bin directory
REM https://mvnrepository.com/artifact/com.h2database/h2/2.2.220
REM

copy /Y ..\db_files\imdb.mv.db old_imdb.mv.db

echo Exporting database...

"%JAVA_HOME%/bin/java" ^
  -cp ../bin/h2-1.4.200.jar org.h2.tools.Script ^
  -url jdbc:h2:./old_imdb ^
  -user sa ^
  -password MrfQ7w6RAC_9oUV ^
  -script export.sql 

echo Importing data...

"%JAVA_HOME%/bin/java" ^
  -cp ../bin/h2-2.2.220.jar org.h2.tools.RunScript ^
  -url jdbc:h2:./new_imdb ^
  -user sa ^
  -password MrfQ7w6RAC_9oUV ^
  -script export.sql 

echo CHECK EXPORT SQL SCRIPT BEFORE CONTINUING...
pause

del ..\db_files\imdb.*.db
copy /Y new_imdb.mv.db ..\db_files\imdb.mv.db

move ..\bin\h2-1.4.200.jar ..\bin\h2-1.4.200.jar_old

echo FINISHED. Now start the new H2DB using the main script.
