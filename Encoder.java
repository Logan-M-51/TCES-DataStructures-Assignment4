//encoder

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.PriorityQueue;
//imports for utilising built in map methods
//and file read write methods



public class Encoder {

   private ArrayList<Character> array; //holds all possible characters within input file
   private String inputFileName;
   private String outputFileName;
   private String codesFileName;
   private String text; //String holding input file in entirety
   private MyHashTable<String, Integer> frequencies; //counts number of times each character appears in text
   private MyHashTable<String, String> codes; // keeps track of all assigned codes to characters
   private HuffmanNode huffmanTree; //Holds completed huffman tree
   private HashMap<String, Integer> wordMap;
   private ArrayList<String> wordArr;
   private byte[] encodedText; //byte array holding the codes of text

   public Encoder() {
      inputFileName = "./WarAndPeace.txt";
      outputFileName = "./output.txt";
      codesFileName = "./codes.txt";
      array = new ArrayList<Character>();
      wordArr = new ArrayList<String>();
      wordMap = new HashMap<String, Integer>();
      //Initilaize maps and array
      frequencies = new MyHashTable<String, Integer>(); 
      codes = new MyHashTable<String, String>();
      String temp = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'";
      for (int i = 0; i < temp.length(); i++) {
         array.add(temp.charAt(i));
      }  
                      
      //read input
      text = "";
      try {
         readInputFile();
      }
      catch (IOException e) {
         System.out.println("IOException");
      }  
      
          
      countFrequency();//count all character frequencies
      buildTree();//build huffman tree according to character weight
      
      //long st = System.nanoTime();
      assignCodes(huffmanTree, "");//assigns codes based on huffman tree location
      //long et = System.nanoTime();
      //double time = (et-st)/1000000.0;
      //System.out.println(time + "codes");
      
      
      
      //created encoded message
      encode();
      try { //writing output
         writeOutputFile();
         writeCodesFile();
      }
      catch (IOException e) {
         System.out.println("IOException");
      } 
      frequencies.stats();
      System.out.println("\n");
   }   
   
   
   
   private class HuffmanNode implements Comparable<HuffmanNode> {
      
      public String code; //binray code holding huffma tree location
      public int weight;  //frequency of character appearnce
      public String s; //string
      public HuffmanNode left, right; //child nodes
   
      public void HuffmanNode(HuffmanNode left, HuffmanNode right) {
         this.left = left; //left child
         this.right = right; //right child
      }
      
      public String toString() { //write out for node
         return ("char; " + this.s + " weight: " + this.weight + " left: " + this.left + " right " + this.right); 
      }
      
      @Override public int compareTo(HuffmanNode data) {   //returns >0 when this has greater weight 
         int ret = 0;
         if (this != null && data != null) {  
            ret = this.weight - data.weight;
        }
        return ret;
      }
   
   }
   
   
   
   private void readInputFile() throws IOException{ //read inputFile
      char temp;
      int tempInt;
      StringBuilder str = new StringBuilder();
      long startTime = System.nanoTime();
      try {
         FileReader file = new FileReader(inputFileName);
         BufferedReader br = new BufferedReader(file);
         tempInt = br.read();
         while (tempInt != -1) {
            temp = (char) tempInt;
            str.append(temp); 
            tempInt = br.read();
         }
         text = str.toString();
      }
      catch (FileNotFoundException e) {
         System.out.println("File not found");
      
      }
   }


   private void countFrequency() {  
      //long st = System.nanoTime();
      StringBuilder sep = new StringBuilder();
      StringBuilder str = new StringBuilder();
      boolean bool = false;
      for (int i = 0; i < text.length(); i++) { //iterate over every character in text
         char temp = (char) text.charAt(i);
         if ( !(array.contains(temp)) ) {
            sep.append(temp);
            if (bool) {
               if (wordMap.containsKey(str.toString())) {
                  frequencies.put(str.toString(), frequencies.Get(str.toString()) + 1); // update character frequency
               }
               else {
                  frequencies.put(str.toString(), 1); // update character frequency
                  wordMap.put(str.toString(), 0);
                  wordArr.add(str.toString());
               } 
            }  
            if (wordMap.containsKey(sep.toString())) {
               frequencies.put(sep.toString(), frequencies.Get(sep.toString()) + 1); // update character frequency
            }
            else {
               frequencies.put(sep.toString(), 1); // update character frequency
               wordMap.put(sep.toString(), 0);
               wordArr.add(sep.toString());
            }
            str = new StringBuilder();
            bool = false;
            sep = new StringBuilder();
         }
         else {
            str.append(temp);
            bool = true;
         }
      }
      //long et = System.nanoTime();
      //double time = (et-st)/1000000.0;
      //System.out.println(time + "count");
   }
   
   
   
   private void buildTree() {
     // long st = System.nanoTime();
      ////////CHANGE BACK TO YOUR IMPLEMENTATION
      MyPriorityQueue q = new MyPriorityQueue(); //build minheap
      for (int i = 0; i < wordArr.size(); i++) { //iterate over all word in word arr
         HuffmanNode node = new HuffmanNode(); //create node  
         node.s = wordArr.get(i); //set node weight from frequeny count
         node.weight = frequencies.Get(node.s); //set node character
         q.offer(node); //add node to minheap
      }
      //long et = System.nanoTime();
     // double time = (et-st)/1000000.0;
      //System.out.println(time + "Q");
      
      //long st2 = System.nanoTime();
      boolean flag = true; //flag used for while loop
      int check = 0;
      while (flag) {
         
         HuffmanNode leftTemp = (HuffmanNode) q.poll(); //pull root
         HuffmanNode rightTemp = (HuffmanNode) q.poll(); //pull root
         
         HuffmanNode temp = new HuffmanNode(); //create new parent node
         temp.HuffmanNode(leftTemp, rightTemp); //set new parent's children
         temp.weight  = leftTemp.weight + rightTemp.weight; //combine children weight
         
         if (q.isEmpty()) { //if all nodes have been added to huffmantree, break the while
            flag = false;
            huffmanTree = temp;
         }
         
         q.offer(temp); //add finalized huffman tree back to minheap as the root
      }
      //long et2 = System.nanoTime();
      //double time2 = (et2-st2)/1000000.0;
      //System.out.println(time2 + "tree");
   }
   
   private boolean isLeaf(HuffmanNode node) { //return true when huffman node has no children
      return (node.left == null && node.right == null);
   }
   
   private void assignCodes(HuffmanNode root, String code) {  

      if (isLeaf(root)) { //check if leaf
         codes.put(root.s, code);
      }
      else {
         if (root.s != null) {
          codes.put(root.s, code);
         }
         if (root.left != null) {
            String codeCont0 = code + "0"; //add 0 to code for travelling left down the tree
            assignCodes(root.left, codeCont0); //recursive call
         
         }
         
         if (root.right != null) {
            String codeCont1 = code + "1"; //add 1 to code for travelling right down the tree
            assignCodes(root.right, codeCont1); //recursive call
         }
      
      }
      
  } 
  
    
   private void encode() {
      int encoder = 0;
      boolean bool = false;
      StringBuilder binString1 = new StringBuilder();
      StringBuilder str = new StringBuilder();
      StringBuilder sep = new StringBuilder();
      char tempChr;
      for (int i = 0; i < text.length(); i++) {
         tempChr = text.charAt(i);
         if (array.contains(tempChr)) {
            str.append(tempChr);
            bool = true;
         }
         else {
            sep.append(tempChr);
            if (bool) {
               binString1.append(codes.Get(str.toString())); //create a string that holds text encoded to its assigned codes
            }
            binString1.append(codes.Get(sep.toString()));
            str = new StringBuilder();
            sep = new StringBuilder();
            bool = false;
         }
      }
      
      StringBuilder temp = new StringBuilder();
      String binString2 = binString1.toString();
      encodedText = new byte[(binString2.length()/8)+1]; //set the byte array to be 1/8 the length of the binString
      for (int j = 0; j < binString2.length(); j++) { //for every 8 bytes, spilt the string and add to the byte array
         temp.append(binString2.charAt(j));
         if ((j+1)%8 == 0) {
            encodedText[encoder] = (byte) Integer.parseInt(temp.toString(), 2);
            temp = new StringBuilder();
            encoder++;
         }
      }
   }
   
   
   
   private void writeOutputFile() throws IOException{
      OutputStream os = new FileOutputStream(outputFileName); //write encoded text to file
      os.write(encodedText);
      os.close();
   }  
   
   
   private void writeCodesFile() throws IOException{ //write out codes map
      File outFile = new File(codesFileName);
      if (outFile.createNewFile()) {
         FileWriter wr = new FileWriter(codesFileName);
         wr.write(codes.toString());
         wr.close();
      }
      else {
         FileWriter wr = new FileWriter(codesFileName);
         for (int i = 0; i < wordArr.size(); i++) {
            String temp = codes.Get(wordArr.get(i));
            wr.write("Word: " + wordArr.get(i) + " Code: " + temp + "\n");
         }
         
         wr.close();
      }
   }  
   
   
   public String toString() { //
      File file1 = new File(outputFileName);
      long bytes0 = text.getBytes().length / 1024; //convert to kilobytes
      long bytes1 = file1.length() / 1024; //convert to kilobytes
      //write program output to screen
      String ret = "Uncompressed file size: " + (bytes0 + 1) + " Kilobytes "+"\n" +"Compressed file size: " + (bytes1 + 1) + " Kilobytes" + "\n"
      + "Compression Ratio: " + (int)((((double)bytes1)/((double)bytes0))*100) + "% \n";
      return ret;
   }
   
}  
