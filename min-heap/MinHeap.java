import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Binary min heap data structure used as a priority queue
public class MinHeap {

    // Used to represent values 0-99
    public static final int MAX = 100;

    // Create the heap using an ArrayList
    public static List<Integer> heap = new ArrayList<>();
    public static int value = 0;

    public static void main(String[] args) {

        // Add 10 random values(0-99)
        for(int i = 0; i < 10; i++) {
            addToHeap();
        }

        // 10 random heap updates
        for (int i = 0; i < 10; i++) {
            // Generate a 1 or 0
            if (((int)(Math.random() * 10) & 1) == 0) {
                addToHeap();
            }else{
                removeFromHeap();
            }
        }
        printHeap();
    }

    public static void addToHeap() {
        // Random number to add to heap(0-99)
        value = (int)(Math.random() * MAX);
        heap.add(value);
        percolateUp(heap.size() - 1);
        System.out.println("Inserting " + value + " into heap...");
    }

    public static void removeFromHeap() {
        // Check if heap is empty
        if(frontOfHeap() != -1){
        heap.set(0, heap.get(heap.size() - 1));
        percolateDown(0);
        System.out.println("Removing " + value + " from heap...");
        } else {
            throw new IllegalArgumentException("Heap is empty");
        }
    }

    public static int frontOfHeap() {
        // Empty Heap
        if(heap.size() == 0){
            return -1;
        }
        // Pop the first element in heap
        return heap.get(0);
    }

    public static void percolateUp(int index) {
        // Initialize parent
        int parent = parent(index);

        // Check if at the root
        if(index == 0) {
            return;
        }

        // Check if parent has a greater value
        if(heap.get(parent) > heap.get(index)) {
            // Swap values
            Collections.swap(heap, index, parent);
            // Recursively percolate up
            percolateUp(parent);
        }
    }

    public static void percolateDown(int index) {
        // Initialize children
        int leftChild = leftChild(index);
        int rightChild = rightChild(index);

        // Check for left child
        if(leftChild > heap.size() - 1){
            return;
        }

        // Check for left child and see if value is greater than right's
        if(rightChild < heap.size() - 1
                && heap.get(leftChild) > heap.get(rightChild)){
            
            // If right child is less than value, do a swap
            if (heap.get(rightChild) < heap.get(index)) {
                Collections.swap(heap, rightChild, index);
                // Recursively percolate down
                percolateDown(rightChild(index));
            }
        }else if(heap.get(leftChild) < heap.get(index)) {
            // Left child is less than value, do a swap
            Collections.swap(heap, leftChild, index);
            // Recursively percolate down
            percolateDown(leftChild(index));
        }
    }

    public static void printHeap() {
        for(Integer value : heap){
            System.out.print(value + " ");
        }
    }

    public static int parent(int index){
        return index >> 1;
    }

    public static int leftChild(int index) {
        return (index << 1) + 1;
    }

    public static int rightChild(int index) {
        return (index << 1) + 2;
    }
}
