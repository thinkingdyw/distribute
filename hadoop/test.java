package org.apache.oozie.test;
 
import java.io.*;
import java.util.Properties;
 
public class MyTest {
 
   ////////////////////////////////
   // Do whatever you want in here
   ////////////////////////////////
   public static void main (String[] args){
      String fileName = args[0];
      try{
         File file = new File(System.getProperty("oozie.action.output.properties"));
         Properties props = new Properties();
         props.setProperty("PASS_ME", "123456");
 
         OutputStream os = new FileOutputStream(file);
         props.store(os, "");
         os.close();
         System.out.println(file.getAbsolutePath());
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
}
