@ECHO OFF
javac -d classes -cp jar\* src/cloud/*.java
ECHO Compilation success
cd classes
java -cp ..\jar\*;. cloud.Driver
PAUSE