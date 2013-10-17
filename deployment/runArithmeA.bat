@echo off
IF "%JAVA_HOME%" == "" (
  ECHO ArithmeA-Bot needs Java: http://java.com/download
  ECHO Please install Java and try again
  PAUSE
  EXIT
)
CHDIR %~dp0
ECHO ArithmeA-Bot is starting up. Please wait...
ECHO.
JAVA -jar ArithmeA-Bot-1.0-SNAPSHOT-jar-with-dependencies.jar
PAUSE
