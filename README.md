# bpms_dynamic_wrapper

A dynamic wrapper for the BPMS REST API which loads data model dynamically by scanning BPMS REST repository.

Installation instructions:

1. Build the project:
$ mvn clean package

2. Deploy target/bpms_dynamic_wrapper.war on EAP 6 (tested with EAP 6.1.1)

3. (optional) set system variables on EAP 6 to override default settings from BPMSService:
$ ./bin/standalone.sh -Djboss.socket.binding.port-offset=100 -DBPMS_USERNAME=<business-central-user> -DBPMS_PASSWORD=<password> -DBPMS_GAV=test:test:1.0 -DBPMS_URL=http://localhost:8080/business-central

4. Use curl to start/abort a process instance:

$ curl -X POST --data @person.json -H 'Content-type: Application/json' http://localhost:8180/bpms_dynamic_wrapper/rest/process/start > processnew.json

$ curl -X POST --data @processnew.json -H 'Content-type: Application/json' http://localhost:8180/bpms_dynamic_wrapper/rest/process/abort
