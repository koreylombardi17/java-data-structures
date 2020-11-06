import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class SkipList {

    // Node class used to build the skiplist
    class Node {
        // int data type
        int value;
        Node left, right, up, down;

        // Node Constructor
        public Node(int value) {
            this.value = value;
            this.right = this.left = this.up = this.down = null;
        }
    }

    // Head and tail nodes, height tracker, and randomizer	
    Node head, tail;
    int height;
    Random r;

    // Constructor without a seed
    public SkipList() {
        // Connect all the neccessary pointers
        this.head = new Node(0);
        this.tail = new Node(700001);
        this.head.right = this.tail;
        this.tail.left = this.head;
        this.height = 0;
        this.r = new Random(42);
    }

    // Constructor with a seeded value
    public SkipList(int seed) {
        // Connect all neccessary pointers
        this.head = new Node(0);
        this.tail = new Node(700001);
        this.head.right = this.tail;
        this.tail.left = this.head;
        this.height = 0;
        this.r = new Random(seed);
    }

    // Method used when top level needs to be added to skiplist
    public void addTopLevel() {
        // Connect all neccessary pointers
        Node tempHead = new Node(0);
        Node tempTail = new Node(700001);
        tempHead.down = this.head;
        tempTail.down = this.tail;
        tempHead.right = tempTail;
        tempTail.left = tempHead;
        this.head.up = tempHead;
        this.tail.up = tempTail;
        this.head = tempHead;
        this.tail = tempTail;
    }

    // Method used when the node gets promoted a level
    public void promote(Node node, int levels) {
        // Make temporary nodes that can be modifed
        Node leftNode = node.left;
        Node rightNode = node.right;
        Node downNode = node;

        // Loop through all possible levels
        for (int i = 1; i <= levels; i++) {
            // Create new node
            Node newNode = new Node(node.value);

            // Loop while left node and up node aren't null
            while (leftNode.up == null && leftNode.left != null) {
                leftNode = leftNode.left;
            }

            // Loop while right node and up node aren't null
            while (rightNode.up == null && rightNode.right != null) {
                rightNode = rightNode.right;
            }

            // Loop while up is not null
            while (downNode.up != null) {
                downNode = downNode.up;
            }

            // Go up one node
            leftNode = leftNode.up;
            rightNode = rightNode.up;

            // Connect neccessary pointers
            newNode.left = leftNode;
            newNode.right = rightNode;
            newNode.down = downNode;
            downNode.up = newNode;
            leftNode.right = newNode;
            rightNode.left = newNode;

            // Add neccessary levels
            if (this.height == i) {
                addTopLevel();
                this.height++;
            }
        }
    }

    // Method used to insert a value
    public Node insert(int value) {
        // Search for the node first and make temp nodes
        Node tempNode = search(value);
        Node tempHead = this.head;
        Node tempTail = this.tail;
        Node newNode;
        int counter = 0;

        // Return if null
        if (tempNode != null) {
            return null;
        }

        // Loop while down node for head doesn't equal null
        while (tempHead.down != null) {
            tempHead = tempHead.down;
        }

        // Loop while down node for tail doesn't equal null
        while (tempTail.down != null) {
            tempTail = tempTail.down;
        }

        // Shift pointer
        tempNode = tempHead.right;

        // Case when value is both highest and lowest rank
        if (tempNode.value == 700001) {
            // Create new node, connect pointers and add level
            newNode = new Node(value);
            newNode.right = this.tail;
            this.tail.left = newNode;
            newNode.left = this.head;
            this.head.right = newNode;
            this.height = 1;
            addTopLevel();

            // Flip coin to see if needs promoted a level
            while (true) {
                if (flipCoin() == 1) {
                    counter++;
                } else {
                    break;
                }
            }

            // Only one level so promote
            if (counter > 0) {
                promote(newNode, counter);
            }
            return newNode;
        }
        // Case when value is lowest rank
        else if (tempNode.value > value) {
            // Connect neccessary pointers
            newNode = new Node(value);
            newNode.right = tempNode;
            tempHead.right = newNode;
            newNode.left = tempHead;
            tempNode.left = newNode;

            // Flip coin for promotion
            while (true) {
                if (flipCoin() == 1) {
                    counter++;
                } else {
                    break;
                }
            }

            // Only one level so promote
            if (counter > 0) {
                promote(newNode, counter);
            }
            return newNode;
        } else {
            while (tempNode.right.value != 700001 && tempNode.right.value < value) {
                tempNode = tempNode.right;
            }

            // Case when value isnt highest or lowest rank
            // Connect neccessary pointers
            if (tempNode.right.value > value && tempNode.right.value != 700001) {
                newNode = new Node(value);
                newNode.right = tempNode.right;
                newNode.left = tempNode;
                tempNode.right.left = newNode;
                tempNode.right = newNode;

                // Flip coin for promotion
                while (true) {
                    if (flipCoin() == 1) {
                        counter++;
                    } else {
                        break;
                    }
                }

                // Only one level so promote
                if (counter > 0) {
                    promote(newNode, counter);
                }
                return newNode;
            }
            // Case when value is highest rank
            else if (tempNode.right.value > value && tempNode.right.value == 700001) {
                // Connect neccessary pointers
                newNode = new Node(value);
                newNode.right = tempTail;
                newNode.left = tempTail.left;
                tempTail.left.right = newNode;
                tempTail.left = newNode;

                // Flip coin for promotion
                while (true) {
                    if (flipCoin() == 1) {
                        counter++;
                    } else {
                        break;
                    }
                }

                // Only one level so promote
                if (counter > 0) {
                    promote(newNode, counter);
                }
                return newNode;
            }
        }
        // If code reaches this point return null
        return null;
    }

    // Method used to delete a value
    public Node delete(int value) {
        // Search for node
        Node temp = search(value);

        // Return if null
        if (temp == null)
            return null;

        // Loop while temp isn't null
        while (temp != null) {
            // If on the bottom level
            if (temp.down == null) {
                temp.left.right = temp.right;
                temp.right.left = temp.left;
                break;
            }
            // Connected to -infinity and infinity
            else if (temp.left.value == 0 && temp.right.value == 700001) {
                temp = temp.down;
                temp.up = temp.up.right = temp.up.left = null;
                this.head = this.head.down;
                this.tail = this.tail.down;
                this.head.up = this.head.up.right = null;
                this.tail.up = this.tail.up.left = null;
            }
            // Value somewhere in the middle
            else {
                temp.left.right = temp.right;
                temp.right.left = temp.left;
                temp = temp.down;
                temp.up.down = null;
                temp.up = temp.up.left = temp.up.right = null;
            }
        }
        return temp;
    }

    // Method used to search for a value
    public Node search(int value) {
        Node temp = head.down;

        // Loop through the values until finding the neccessary node to return
        for (int i = 0; i < this.height; i++) {
            if (value < temp.right.value && temp.down != null) {
                temp = temp.down;
            } else if (value > temp.right.value) {
                while (value > temp.right.value && temp.right.value != 700001) {
                    temp = temp.right;
                }

                if (temp.down != null) {
                    temp = temp.down;
                }
            } else if (value == temp.right.value) {
                return temp.right;
            }

            if (temp.value > value) {
                return null;
            }

            if (temp.right != null) {
                if (value == temp.right.value) {
                    return temp;
                }
            }

            if (temp.right == null) {
                break;
            }
        }
        return null;
    }

    // Method used for randomization
    public int flipCoin() {
        return Math.abs(r.nextInt() % 2);
    }

    // Method used to print the skiplist
    public void printAll() {
        Node temp1 = this.head;
        Node temp2;

        while (temp1.down != null) {
            temp1 = temp1.down;
        }
        temp1 = temp1.right;
        temp2 = temp1;

        System.out.println("the current Skip List is shown below:");
        System.out.println("---infinity");

        while (temp1.right.value != 700001) {
            System.out.print("\t" + temp2.value + ";");

            while (temp2.up != null) {
                temp2 = temp2.up;
                System.out.print("\t" + temp2.value + ";");
            }
            System.out.println();
            temp2 = temp1.right;
            temp1 = temp1.right;
        }

        System.out.print("\t" + temp2.value + ";");
        while (temp2.up != null) {
            temp2 = temp2.up;
            System.out.print("\t" + temp2.value + ";");
        }
        System.out.println();
        System.out.println("+++infinity");
        System.out.println("---End of Skip List---");
    }


    // main method
    public static void main(String[] args) {

        SkipList skipList = new SkipList();

        // Create an ArrayList to store the data read from the file
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
                if (br != null) {
                    br.close();
                }
            } catch (IOException ioe) {
                // Error message
                System.out.println("BufferedReader failed to close");
            }
        }

        System.out.println("For the input file named " + args[0]);
        System.out.println("With the RNG unseeded,");

        for (String temp : entries) {
            // Used to split the String in two substrings for insert, search and delete functions
            String[] tempArr;
            int intValue;

            // For inserts, split command, parse data, and insert value
            if (temp.charAt(0) == 'i') {
                tempArr = temp.split(" ", 2);
                intValue = Integer.parseInt(tempArr[1]);
                skipList.insert(intValue);
            }
            // For deletes, split command, parse data, and delete value
            else if (temp.charAt(0) == 'd') {
                tempArr = temp.split(" ", 2);
                intValue = Integer.parseInt(tempArr[1]);
                Node tempNode = skipList.search(intValue);

                if (tempNode == null) {
                    System.out.println(intValue + " integer not found - delete not successful");
                } else {
                    System.out.println(intValue + " deleted");
                    skipList.delete(intValue);
                }
            }
            // For searchs, split command, parse data, and verify if found the value
            else if (temp.charAt(0) == 's') {
                tempArr = temp.split(" ", 2);
                intValue = Integer.parseInt(tempArr[1]);
                Node tempNode = skipList.search(intValue);

                if (tempNode == null) {
                    System.out.println(intValue + " integer not found");
                } else {
                    System.out.println(intValue + ": found");
                }
            }
            // Print the tree
            else if (temp.charAt(0) == 'p') {
                skipList.printAll();
            }
            // Exit the program
            else if (temp.charAt(0) == 'q') {
                System.exit(0);
            }
        }

    }
}
