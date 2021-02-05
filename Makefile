CC=javac
TARGET=target

all: jar

directory:
	@mkdir -p $(TARGET)/Clavardage/
	@mkdir -p $(TARGET)/PresenceManagementServer/

class: directory
	@$(CC) -d $(TARGET)/Clavardage/ Clavardage/src/*.java
	@$(CC) -d $(TARGET)/PresenceManagementServer/ PresenceManagementServer/src/*.java

jar: class
	@cd $(TARGET)/Clavardage/ && jar cfe clavardage.jar Main *.class && rm -f *.class
	@cd $(TARGET)/PresenceManagementServer/ && jar cfe presencemanagementserver.jar Main *.class && rm -f *.class

clean: 
	@rm -rf $(TARGET)
