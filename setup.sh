#!/bin/bash

mkdir -p target/Clavardage/
mkdir -p target/PresenceManagementServer/

javac -d target/Clavardage/ Clavardage/src/*.java
javac -d target/PresenceManagementServer/ PresenceManagementServer/src/*.java


cd target/Clavardage/
jar cfe clavardage.jar Main *.class
rm *.class

cd ../PresenceManagementServer
jar cfe presencemanagementserver.jar Main *.class
rm *.class

cd ../..
