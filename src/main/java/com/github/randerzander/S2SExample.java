package com.github.randerzander;

import java.io.File;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.nifi.remote.Transaction;
import org.apache.nifi.remote.TransactionCompletion;
import org.apache.nifi.remote.TransferDirection;
import org.apache.nifi.remote.client.SiteToSiteClient;
import org.apache.nifi.remote.protocol.SiteToSiteTransportProtocol;

import org.apache.nifi.remote.protocol.DataPacket;

public class S2SExample {
  public static void main(String[] args){
    SiteToSiteClient.Builder builder = new SiteToSiteClient.Builder();
		builder.url("http://localhost:8080/nifi");
		builder.portName("source1");
    builder.transportProtocol(SiteToSiteTransportProtocol.HTTP);
		builder.nodePenalizationPeriod(60, TimeUnit.SECONDS);
		SiteToSiteClient client = builder.build();

    String content = "Hello, world";
    HashMap<String,String> attributes = new HashMap<String,String>();
    attributes.put("whoami", "java client");
    try{
      Transaction transaction = client.createTransaction(TransferDirection.SEND);
      transaction.send(new SimplePacket(content, attributes));
      transaction.confirm();
      TransactionCompletion completion =  transaction.complete();
      System.out.println("Bytes transferred: " + completion.getBytesTransferred());
    }catch(Exception e){
      e.printStackTrace(System.out);
    }
  }

  private static class SimplePacket implements DataPacket{
    private final InputStream input;
    private final long size;
    private final Map<String,String> attributes;
    
    public SimplePacket(final String data, final Map<String,String> attributes) throws IOException{
      if(data!=null){
        this.size = data.length();
        this.input= new ByteArrayInputStream(data.getBytes());
      }else{
        this.size=0;
        this.input = new ByteArrayInputStream(new byte[0]);
      }
      
      if(attributes==null)
        this.attributes = new HashMap<String,String>();
      else
        this.attributes=attributes;
    }

    public Map<String, String> getAttributes() {
      return this.attributes;
    }

    public InputStream getData() {
      return this.input;
    }

    public long getSize() {
      return this.size;
    }
  }
}
