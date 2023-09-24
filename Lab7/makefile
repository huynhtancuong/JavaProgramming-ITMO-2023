.PHONY: server client target

out_server := server.jar 
out_client := client.jar
out_sourceslist := sourceslist.txt
env_var_name := HEHE
env_var_value := spacemarine_data.xml
server_ip := localhost
server_port := 25566
server_entry := server.App
client_entry := client.App
manifest := MANIFEST.MF
db_password := 8D8q7Srz7k8YylYO


# _path_libs is a variable that store path to all jar file in folder lib
# the separator between paths is SPACE
_path_libs += $(shell find lib -name "*.jar") 
# path to resource 
_path_libs := $(_path_libs)src/main/resources/

# path_libs is a variable that store path to all jar file in folder lib
# the separator between paths is COLON
space := $(subst ,, )
path_libs := $(subst $(space),:,$(_path_libs))

sourceslist.%:
	find server -name *.java >> $(out_sourceslist)
	find common -name *.java >> $(out_sourceslist)
	find client -name *.java >> $(out_sourceslist)
target: $(out_sourceslist)
	mkdir -p target
	javac -cp $(path_libs) -d target @$(out_sourceslist)
env-var:
	export $(env_var_name)=$(shell pwd)/$(env_var_value)

server: target
	echo "Main-class: $(server_entry)" > $(manifest)
	echo "Class-Path: target/ $(_path_libs)" >> $(manifest)
	jar -cvfm $(out_server) $(manifest) target/common target/server $(_path_libs)

run-server: $(out_server)
	java -jar $(out_server)	$(server_port) $(server_ip) $(db_password)

run-client: $(out_client) 
	java -jar $(out_client)	$(server_ip) $(server_port)

client: target
	echo "Main-class: $(client_entry)" > $(manifest)
	echo "Class-Path: target/ $(_path_libs)" >> $(manifest)
	jar -cvfm $(out_client) $(manifest) target/client target/common $(_path_libs)
clean:
	# -f flag here to make sure rm won't complain if file doesn't exist
	rm -rf target
	rm -f $(out_server)
	rm -f $(out_client)
	rm -f $(out_sourceslist)
	rm -f $(manifest)
test: 
	@echo $(shell find lib -name "*.jar")	







