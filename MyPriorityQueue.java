//priority queue

import java.util.ArrayList; //array list used for minHeap

public class MyPriorityQueue<T extends Comparable<T>> {

   private ArrayList<T> minHeap;
   
   public void swapNodes(int i, int j) { //helper method, swaps nodes at given indexes
      T node = minHeap.get(i);
      minHeap.set(i, minHeap.get(j));
      minHeap.set(j, node);
   }
   
   public void bubbleUp(T node) { //helper method
      boolean flag = true;
      while (flag) {   
         int parent = (minHeap.indexOf(node) - 1)/2; 
         if (node.compareTo(minHeap.get(parent)) < 0) { //after adding to priority queue, move up until it's parent has less weight than the new item
            swapNodes(minHeap.indexOf(node), parent);
         }
         else { //when node's parents are less that it, break loop
            flag = false;
         }  
      }
   }
   
   public void sinkDown(T node) { //poll helper method
      int child1 = 1;
      int child2 = 2;
      int child;

      boolean flag = true;
      while (flag) { //while root's children have less weight, swap with lowest child
         int check = minHeap.size()-1;
         if (child2 > check && child1 > check) { //if both children exceed minheap size
            break;
         }
         else if (child1 <= check && child2 > check) { //if one child exceeds minheap size
            child = child1;
         }
         else { //both children exist in minheap
            if (minHeap.get(child1).compareTo(minHeap.get(child2)) < 0) { //set node "child" to node with lowest weight
               child = child1; 
             }
            else {
               child = child2;
            }
         }
         if (node.compareTo(minHeap.get(child)) > 0) { //compare root node to childwith smallest weight
            swapNodes(minHeap.indexOf(node), child);
            child1 = (minHeap.indexOf(node) * 2) + 1; //get index of new children after swapping
            child2 = (minHeap.indexOf(node) * 2) + 2;
         }
         else { //break loop after root's children have more weight than it
             flag = false;
         }
          
      }
      
   }
   
   
   public void offer(T item) { //add new node to minheap

      if (minHeap == null) { //if null create new minheap
         minHeap = new ArrayList<T>();
         minHeap.add(item);
      }
      else { //else add and bubble up new node
         minHeap.add(item);
         bubbleUp(item);
      }
   }
 
   public T poll() { //return and remove top node
      T ret = minHeap.get(0); //get return node
      if (minHeap.size() == 1) {
         T node = minHeap.get(0);
         minHeap.remove(0);
         return node;
      }
      swapNodes(0, minHeap.size() -1); //swap first and last node for safe removal
      
      minHeap.remove(minHeap.size() -1); //remove former root
      
      sinkDown(minHeap.get(0)); //sinkdown new root
      
      return ret; //former root
   }
   
   
   public boolean isEmpty() { //return true when minheap has no nodes
      boolean ret = false;
      if (minHeap.size() == 0) {
         ret = true;
      }
      return ret;   
   }
   
   
   public int size() { //return minheap size
      return minHeap.size();
   }
  
}
