@ECHO OFF
javac -d classes src/*.java
ECHO Compilation success
cd classes
java Loader
PAUSE