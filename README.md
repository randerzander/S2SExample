The intent of this repository is to provide an example Java project that connects to and sends a FlowFile (record) to an [Apache NiFi](http://nifi.apache.org/) instance over its [Site to Site protocol](http://nifi.apache.org/docs/nifi-docs/html/user-guide.html#site-to-site).

Build:
```
mvn clean package
```

In $NIFI_HOME/conf/nifi.properties, make sure you have S2S enabled:
```
# Site to Site properties
nifi.remote.input.host=localhost
nifi.remote.input.secure=false
nifi.remote.input.socket.port=1026
nifi.remote.input.http.enabled=true
nifi.remote.input.http.transaction.ttl=30 sec
```

Your NiFi flow should contain an input port named "source1" connected to a stopped downstream processor. An example flow.xml.gz is available [here](flow.xml.gz).

Run the Java client:
```
java -jar target/S2SExample-0.0.1-SNAPSHOT.jar
```

In your NiFi Web UI you should see:

![S2SExample](/screenshots/S2SExample.png?raw=true)

Right click on the queue and "List queue"..

![S2SExample](/screenshots/S2SExample-list-queue.png?raw=true)

You'll be taken to the Provenance view where you can see the details of the record your Java client produced:

![S2SExample](/screenshots/S2SExample-provenance.png?raw=true)
