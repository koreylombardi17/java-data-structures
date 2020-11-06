import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class BST {
    // Node class used to build the Binary Search Tree
    class Node {
        // int data type
        int value;
        Node left, right, parent;

        // Node Constructor
        public Node(int value) {
            this.value = value;
            this.right = this.left = this.parent = null;
        }
    }

    // Instantiation of root
    Node root;

    // Constructor
    public BST() {
        this.root = null;
    }

    // Function to insert a value into the BST
    public Node insert(Node root, int value) {
        // Tree is empty, insert here to create root
        if (root == null) {
            root = new Node(value);
            return root;
        }

        // Go left
        if (value < root.value) {
            // Null left child case
            if (root.left == null) {
                root.left = insert(root.left, value);
                root.left.parent = root;
            }
            // Left child is not null case
            else {
                root.left = insert(root.left, value);
            }
        }
        // Go right
        else if (value >= root.value) {
            // Null right child case
            if (root.right == null) {
                root.right = insert(root.right, value);
                root.right.parent = root;
            }
            // Right child is not null case
            else {
                root.right = insert(root.right, value);
            }
        }
        return root;
    }

    // Function to search for a value
    public Node search(Node root, int value) {
        // Null case
        if (root == null) {
            return null;
        }

        // Go left
        if (value < root.value) {
            return search(root.left, value);
        }
        // Go right
        else if (value > root.value) {
            return search(root.right, value);
        }
        // Found the value
        else if (root.value == value) {
            return root;
        }
        return null;
    }

    // Function to delete a value
    public Node delete(Node root, int value) {
        // Base case, root is null
        if (root == null) {
            System.out.println(value + ": NOT found - NOT deleted");
            return null;
        }

        // Go left
        if (value < root.value) {
            root.left = delete(root.left, value);
        }
        // Go right
        else if (value > root.value) {
            root.right = delete(root.right, value);
        }
        else {
            // No children case
            if (root.right == null && root.left == null) {
                root = null;
            }
            // Only right child case
            else if (root.right == null && root.left != null) {
                root = root.left;
            }
            // Only left child case
            else if (root.left == null && root.right != null) {
                root = root.right;
            }
            // Two children case
            else {
                Node ios = ios(root, value);
                root.value = ios.value;
                root.right = delete(root.right, ios.value);
            }
        }
        return root;
    }

    // Function to print the BST in order
    public void print(Node root) {
        // Check for null case
        if (root != null) {

            // Check if left node is null
            if (root.left != null) {
                print(root.left);
            }

            // Print the data
            System.out.print(root.value + " ");

            // Check if right node is null
            if (root.right != null) {
                print(root.right);
            }
        }
    }

    // Returns the next in order successor
    public Node ios(Node root, int value) {
        // Check to make sure the node exist and create a variable for it
        Node temp = search(root, value);

        if (temp != null) {
            // Case 1, right node != NULL and left node of right node == NULL
            if (temp.right != null && temp.right.left == null) {
                return temp.right;
            }

            // Case2, right node != NULL and left node of right node != NULL
            if (temp.right != null && temp.right.left != null) {
                temp = temp.right;

                // While node's left child does not equal null
                while (temp.left != null) {
                    temp = temp.left;
                }
                return temp;
            }

            // Case 3, left leaf node
            if (temp.left == null && temp.right == null && temp.value == value) {
                return temp.parent;
            }

            // Case 4, right leaf node
            if (temp.left == null && temp.right == null && temp.value == value) {
                // While node's parent is less than node
                while (temp.parent.value < temp.value) {
                    temp = temp.parent;
                }
                return temp.parent;
            }

            // Case 5, right node is NULL
            if (temp.right == null) {
                // While node's parent is less than node
                while (temp.parent.value < temp.value && temp.parent != null) {
                    temp = temp.parent;
                }
                return temp;
            }
        }
        return null;
    }

    // Returns roots number of children
    public int countChildren(Node root) {
        // Null case
        if (root == null) {
            return 0;
        }
        // Count children recursively for left side and right side
        else{
            return 1 + countChildren(root.left) + countChildren(root.right);
        }
    }

    // Returns roots depth
    public int getDepth(Node root) {
        // Null case
        if (root == null) {
            return 0;
        }
        // Calculate depth recursively for left side and right side
        // return the highest of the two
        else {
            return 1 + Math.max(getDepth(root.left), getDepth(root.right));
        }
    }

    // Complexity indicator for professor to maintain data analytics
    public void complexityIndicator() {
        System.err.println("ko320078;1.0;10.0");
    }

    // main method
    public static void main(String[] args) {

        // Create a Binary Search Tree
        BST tree = new BST();

        // Create an ArrayList top store the data read from the file
        List<String> entries = new ArrayList<>();

        // BufferedReader used to read the input file
        BufferedReader br = null;
        try {
            // Get command from the user at command line
            br = new BufferedReader(new FileReader(args[0]));
            String text = br.readLine();
            // Loop until BufferedReader reaches null
            while (text != null) {
                entries.add(text);
                text = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            // Close down the BufferedReader
            try {
                if (br != null)
                    br.close();
            } catch (IOException ioe) {
                // Error message
                System.out.println("BufferedReader failed to close");
            }
        }

        System.out.println(args[0] + " contains:");
        for (String string : entries) {
            System.out.println(string);
        }

        for (String temp : entries) {
            // Used to split the String in two substrings for insert, search and delete functions
            String[] tempArr;
            int intValue;

            // For inserts, split command, parse data, and insert value
            if (temp.charAt(0) == 'i') {
                tempArr = temp.split(" ", 2);
                intValue = Integer.parseInt(tempArr[1]);
                tree.root = tree.insert(tree.root, intValue);
            }
            // For deletes, split command, parse data, and delete value
            else if (temp.charAt(0) == 'd') {
                tempArr = temp.split(" ", 2);
                intValue = Integer.parseInt(tempArr[1]);
                Node tempNode = tree.search(tree.root, intValue);

                if (tempNode == null) {
                    System.out.println(intValue + ": NOT found - NOT deleted");
                }
                else {
                    tree.root = tree.delete(tree.root, intValue);
                }
            }
            // For searchs, split command, parse data, and verify if found the value
            else if (temp.charAt(0) == 's') {
                tempArr = temp.split(" ", 2);
                intValue = Integer.parseInt(tempArr[1]);
                Node tempNode = tree.search(tree.root, intValue);

                if (tempNode == null) {
                    System.out.println(intValue + ": NOT found");
                }
                else {
                    System.out.println(intValue + ": found");
                }
            }
            // Print the tree
            else if (temp.charAt(0) == 'p') {
                tree.print(tree.root);
                System.out.println();
            }
            // Exit the program
            else if (temp.charAt(0) == 'q') {
                System.out.println("left children:          " + tree.countChildren(tree.root.left));
                System.out.println("left depth:             " + tree.getDepth(tree.root.left));
                System.out.println("right children:         " + tree.countChildren(tree.root.right));
                System.out.println("right depth:            " + tree.getDepth(tree.root.right));
                tree.complexityIndicator();
                System.exit(0);
            }
        }

    }
}