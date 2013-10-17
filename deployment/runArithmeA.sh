#!/bin/bash
if [ "$JAVA_HOME" == "" ]; then
echo ArithmeA-Bot needs Java: http://java.com/download
  echo Please install Java and try again
  exit
fi
cd ${0%/*}
echo ArithmeA-Bot is starting up. Please wait...
echo
java -jar ArithmeA-Bot-1.0-SNAPSHOT-jar-with-dependencies.jar
