CC=javac
TARGET=target

all: client_jar server_jar

directory:
	@mkdir -p $(TARGET)/Clavardage/
	@mkdir -p $(TARGET)/PresenceManagementServer/

client_class: directory
	@$(CC) -d $(TARGET)/Clavardage/ Clavardage/src/*.java

server_class: directory
	@$(CC) -d $(TARGET)/PresenceManagementServer/ PresenceManagementServer/src/*.java

client_jar: client_class
	@cd $(TARGET)/Clavardage/ && jar cfe clavardage.jar Main *.class && rm -f *.class

server_jar: server_class
	@cd $(TARGET)/PresenceManagementServer/ && jar cfe presencemanagementserver.jar Main *.class && rm -f *.class

clean: 
	@rm -rf $(TARGET)
