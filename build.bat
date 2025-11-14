@echo off
set JAVA_HOME=D:\Software\Scoop\apps\openjdk25\current
set PATH=%JAVA_HOME%\bin;%PATH%
echo Using Java version:
java -version
echo.
echo Building project...
mvn clean package