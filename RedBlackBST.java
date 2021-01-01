import java.util.NoSuchElementException;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.*;
public class RedBlackBST {

    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    private Node root;     // root of the BST
	int redNodes;          // the amount of red nodes in the red-black tree.

    // BST helper node data type
    private class Node {
        private int key;           // key
        private Node left, right;  // links to left and right subtrees
        private boolean color;     // color of parent link
        private int size;          // subtree count

        public Node(int key, boolean color, int size) {
            this.key = key;
            this.color = color;
            this.size = size;
        }
    }

	public RedBlackBST() {

	}
   /***************************************************************************
    *  Node helper methods.
    ***************************************************************************/
    // is node x red; false if x is null ?
    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    // number of node in subtree rooted at x; 0 if x is null
    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    } 


    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return size(root);
    }

   /**
     * Is this symbol table empty?
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }

   /***************************************************************************
    *  Red-black tree insertion.
    ***************************************************************************/

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old 
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param key the key
     * @param val the value
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public void put(int key) {
        root = put(root, key);
		if(root.color){
			redNodes--;
		}
        root.color = BLACK;
    }

    // insert the key-value pair in the subtree rooted at h
    private Node put(Node h, int key) { 
        if (h == null){ 
			redNodes++;
		return new Node(key, RED, 1);
		}
        int cmp = key - h.key;
        if      (cmp < 0) h.left  = put(h.left,  key); 
        else if (cmp > 0) h.right = put(h.right, key); 
        else              h.key   = key;

        // fix-up any right-leaning links
        if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
        if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);
        h.size = size(h.left) + size(h.right) + 1;

        return h;
    }


   /***************************************************************************
    *  Red-black tree helper functions.
    ***************************************************************************/
	public int percentRed(){
	double redVal = redNodes;
	double sizeOftree = size();
	int numOfRedNodes = (int) Math.round(100*redVal/sizeOftree);
	return numOfRedNodes;
}

    // make a left-leaning link lean to the right
    private Node rotateRight(Node h) {
        // assert (h != null) && isRed(h.left);
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // make a right-leaning link lean to the left
    private Node rotateLeft(Node h) {
        // assert (h != null) && isRed(h.right);
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node h) {
        // h must have opposite color of its two children
        // assert (h != null) && (h.left != null) && (h.right != null);
        // assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
        //    || (isRed(h)  && !isRed(h.left) && !isRed(h.right));
		redNodes--;
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }


    /**
     * Unit tests the {@code RedBlackBST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) { 
		RedBlackBST st = new RedBlackBST();
		if(args.length >0){
            try{
                File test = new File(args[0]);
                Scanner scan = new Scanner(test);
                System.out.println("reading input values from "+args[0]);
                while (scan.hasNextInt()) {
                    int num = scan.nextInt();
                    st.put(num);
                }
                System.out.println("Percent of Red Nodes: "+st.percentRed());
            } catch (FileNotFoundException e){			
                System.out.printf("does not exist." + st.percentRed());
                return;
            }
		}else{
			//test 1
			
			 Random randomVal = new Random();
			int n=0;
			while(n<10000){
				st.put(randomVal.nextInt());
			n++;
			}
			System.out.println("Percent of red nodes when n = 10^4: " +st.percentRed());
			
		 //test 2
			while(n<100000){
				st.put(randomVal.nextInt());
			n++;
			}		
			System.out.println("Percent of red nodes when n = 10^5: " +st.percentRed());
			
			//test3
			while(n<1000000){
				st.put(randomVal.nextInt());
			n++;
			}		
			System.out.println("Percent of red nodes when n = 10^6: " +st.percentRed());
       }
	}
        
 
}

