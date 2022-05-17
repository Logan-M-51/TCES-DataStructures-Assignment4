//main

public class Main {


   public static void main(String[] args) {

      long stime = System.nanoTime();
      Encoder temp = new Encoder(); 
      long etime = System.nanoTime();
      double time = (etime - stime) / 1000000.0;
      
      System.out.println(temp.toString());
      System.out.println("Operation Time: " + time + " miliseconds");
   }
   
   
}