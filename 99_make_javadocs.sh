#! /bin/bash

# The javadocs have already been built:
exit 0

#  This assumes you have already built the 
#  target/TitleWebDemo.jar file:

$JAVA_HOME/bin/javadoc \
  -d javadocs \
  -classpath target/TitleWebDemo.jar \
  -stylesheetfile javadoc_stylesheet.css \
  -encoding UTF-8 \
  -charset UTF-8 \
  -docencoding UTF-8 \
  -overview src/main/java/overview.html \
  -sourcepath src/main/java \
  -subpackages org.northcoder.titlewebdemo

