@ECHO OFF


IF NOT EXIST target\ (md target\Clavardage target\PresenceManagementServer)

javac -d target\Clavardage\ Clavardage\src\*.java
javac -d target\PresenceManagementServer\ PresenceManagementServer\src\*.java

cd target\Clavardage\
jar cfe clavardage.jar Main *.class
del *.class

cd ..\PresenceManagementServer
jar cfe presencemanagementserver.jar Main *.class
del *.class