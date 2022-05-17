//MyHashTable
import java.util.*;


public class MyHashTable<Key, Value> {
   private int capacity;
   private int size;
   private int probes;
   private ArrayList<Node> buckets;
   private int[] histogram;
   
   private class Node {
      public Key key;
      public Value value;
      
      public String toString() {
         return (this.key + " : " + this.value);
      }
   }
   
   public MyHashTable() {
      histogram = new int[400]; 
      for (int j = 0; j < histogram.length; j++) {
         histogram[j] = 0;      
      }
      capacity = 32767;
      buckets  = new ArrayList<Node>(capacity);
      probes = 0;
      for (int i = 0; i < capacity; i++) {
         buckets.add(new Node());
      }
   }
   
   public int getCap() {
      return this.capacity;
   } 
   private int Hash(Key hashkey) {
      int hash = hashkey.hashCode();
      return Math.abs((hash % (capacity-1)));
   }
   
   public Value Get(Key getKey) {
      int bucket = Hash(getKey);
      Node BNode = buckets.get(bucket);
      String getStr = getKey.toString();
      while (BNode != null) { 
         String temp = BNode.key.toString();
         if (temp.compareTo(getStr) == 0) {
            return BNode.value;
         }
           bucket = bucket + 1 % capacity;
           if (bucket >= capacity) {
              String newS = "";
           }
           BNode = buckets.get(bucket);
           //probes++;
      }
      return null;
      
   }
   
   public void put(Key newKey, Value newVal) {
      int probeCn = 0;
      Node newNode = new Node();
      newNode.key = newKey;
      newNode.value = newVal;
      int temp = Math.abs(Hash(newKey));
      Node check = buckets.get(temp);
      while (true) {   
         if (check.key == null) {       
            check.key = newKey;
            check.value = newVal;
            size++;
            histogram[probeCn] = histogram[probeCn] + 1;
            break;
         }
         else if (check.key.equals(newKey)) {
            check.value = newVal;
            break;
         }
         else {
            temp = Math.abs((temp + 1) % (capacity-1));
            check = buckets.get(temp);
            probeCn++;
            probes++;
         }
      }
      
      
   }
   
   public void stats() {
      System.out.println("Number of Entries: " + this.size);
      System.out.println("Number of Buckets: " + this.capacity);
      System.out.println("Number of Probes: " + this.probes + "\n");
      System.out.println("Histogram: ");
      System.out.println(Arrays.toString(histogram));
      
      
   }
   
   public String toString() {
      StringBuilder str = new StringBuilder();
      Node temp = new Node();
      for (int i = 0; i < capacity; i++) {
         temp = buckets.get(i);
         str.append("Key: " + temp.key);
         if (temp.value != null) {
            str.append(" Value: " + temp.value);
         }
         else {
            str.append(" Value: NULL");
         }
      }
      return (str.toString());
   }
}