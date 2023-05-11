/**
 * @LinkedListAssignment
 * @Author: Jack Jiang
 * @Date: 2023/05/10
 * @Description: This is a LinkedList class that I implemented from scratch, it represents a doubly linked list data structure and provide basic functionalities
 * to add, remove, and manipulate elements inside the list. The list maintains a reference to the head and tail nodes for efficient operations that supports
 * stack and queue implementations. Note that this class assumes integer as the element type.
 */

package LinkedListAssignment;
import java.util.EmptyStackException;

class Main {
    public static void main(String[] args) {
        LinkedList test = new LinkedList();
        test.enqueue(90);
        test.enqueue(105);
        test.enqueue(108);
        test.push(85);
        test.enqueue(120);

        test.reverse();
        System.out.println(test);

        test.sortedInsert(110);
        System.out.println(test);

        test.sortedInsert(84);
        System.out.println(test);

        test.sortedInsert(121);
        System.out.println(test);
    }
}

class LinkedList {
    private Node head, tail;

    public LinkedList() {
        head = null;
        tail = null;
    }
    public Node getHead(){
        return head;
    }
    public Node getTail(){return tail;}

    public void add(int n) {
        Node tmp = new Node(null, n, head);  //create a new node with the given value
        if (head == null){
            // if the list has nothing inside, then both tail and head will be pointing at this new node
            tail = tmp;
        }

        else{
            head.setPrevious(tmp);  //update the previous reference of the previous head node to the new temporary node
        }

        head = tmp; //update the head to the new temporary node
    }

    /**
     * @push: add an element to the top of a stack.
     */
    public void push(int num) {
        add(num); //use the add method to push an element to the list as these two method perform the same result
    }

    /**
     * @pop: remove an element from the top of the stack and return the element
     */
    public int pop() {
        if (head != null) {
            int num = head.getVal();  //store the data from the head node
            head = head.getNextNode();  //pop out the head and make the second node the new head of the stack
            return num;
        }
        throw new RuntimeException("No node found inside the list");
    }

    /**
     * @enqueue: Add an element to the back of the list (at tail)
     */
    public void enqueue(int num){
        if (head == null){
            //if there has no head in the list, then just set the head to the new node
            add(num);
        }
        else {
            Node tempNode = new Node(tail, num, null); //this node is pointing at null since it's adding this node to the end of the list
            tail.setNext(tempNode);   //connect the new node at the end of the current tail node by resets the tail pointer to the temporary node
            tempNode.setPrevious((tail)); //set the previous reference of the new node toe the previous tail node
            tail = tempNode;
        }
    }

    /**
     * @dequeue: remove an element from the front of the queue and return the element.
     */
    public int dequeue(){
        return pop();  //call the pop method as dequeue and pop basically perform the same result
    }

    public void delete(int num){
        if (head == null) {
            throw new RuntimeException("Empty list, currently no item available to delete");
        }
        boolean found = false; //indicate if the target value passed in is found
        Node temp = head;

        while (temp != null){
            if(temp.getVal() == num){
                found = true;
                Node previousNode = temp.getPrevious(), nextNode = temp.getNextNode();

                if (previousNode != null){
                    previousNode.setNext(nextNode); //update the next reference of the previous node
                }
                else{
                    head = nextNode; //if the current node is the head, update the head to the next node
                }

                if(nextNode != null){
                    nextNode.setPrevious(previousNode); //update the previous reference of the next node
                }
                else{
                    tail = previousNode; //if the current node is the tail, update the tail to the previous node
                }
                break;
            }
            temp = temp.getNextNode(); //moving to the next node in the list
        }

        if(!found)  throw new RuntimeException("No such value found inside the list");
    }

    //overloading method
    public void delete(Node node){
        if (head == null) throw new EmptyStackException();

        if (node.getPrevious() == null && node.getNextNode() == null){
            //only one node inside the list
            head = null;
            tail = null;
        }
        else if (node == head){
            head = head.getNextNode();  //if the node is the head, after deleting it, the second node will become the new head node
            head.setPrevious(null);
        }
        else if(node == tail){
          tail = tail.getPrevious();  //similarly, if the node is the tail, after deleting it, the second last node will become the new tail node that is pointing at null
          tail.setNext(null);
        }
        else{
            Node previousNode = node.getPrevious(), nextNode = node.getNextNode();
            //connecting the node prior than the node that being passed in as the parameter with the node after by updating the nodes pointers
            node.getPrevious().setNext(nextNode);
            node.getNextNode().setPrevious(previousNode);
        }
    }

    /**
     * @deleteAt: removes the element from the list at the specified position
     * @param pos: position (starts from 0)
     */
    public void deleteAt(int pos){
        Node temp = head;
        if(pos < 0) throw new IndexOutOfBoundsException("index out of bound"); //negative index

        for (int counter = 0; counter < pos; counter++){
            //if temp is null that means the index is outside the range
            if (temp == null) throw new IndexOutOfBoundsException("index out of bound");
            temp = temp.getNextNode();
        }

        delete(temp);
    }

    /**
     * @sortedInsert: adds a new node into the correct position assuming that the list is already in sorted order (ascending from tail to head)
     * @param num: the integer to be inserted into the list
     */
    public void sortedInsert(int num){
        Node cur_node = head;
        if (head == null && tail == null) return;  //the list is empty

        assert head != null; //verify that the head of the list is not null before performing further operations.
        if (head.getVal() < num){
            push(num);  //since the list is sorted, if the number being passed in is greater than the max of the current then it will become the new head
            return;
        }
        else if (tail.getVal() > num) { //similarly, if the number is less than the minimum number in the list, then it will be inserted at the end of the list
            enqueue(num);
            return;
        }
        else{
            while(cur_node.getVal() >= num){
                //iterate through and comparing the numbers being stored inside each node with the parameter to find where I should insert this number
                cur_node = cur_node.getNextNode();
            }
        }
        Node nodeToAdd = new Node(cur_node.getPrevious(),num, cur_node);
        cur_node.getPrevious().setNext(nodeToAdd);
        cur_node.setPrevious(nodeToAdd);

    }

    /**
     * @removeDuplicates: this method removes the duplicate values from the list.
     */
    public void removeDuplicates() {
        LinkedList newEmptyList = new LinkedList(); //create an empty list to store unique elements

        Node temp = head;
        while(temp != null){
            if(! newEmptyList.contains(temp.getVal())){
                newEmptyList.enqueue(temp.getVal()); //append the value only if the value doesn't exist in the new empty list
            }
            temp = temp.getNextNode();
        }

        //update the new list to the existing list
        head = newEmptyList.getHead();
        tail = newEmptyList.getTail();
    }

    /**
     * @contains: helper method to check if a value already exist in the current list
     * @param value: the number stored in the node
     */
    private boolean contains(int value) {
        Node temp = head;  //start iterating from the head of the current list
        while (temp != null) {
            //compare the value of the current node with the parameter (the target value)
            if (temp.getVal() == value) {
                //if the value is found, return true to indicate it is a duplicate
                return true;
            }
            temp = temp.getNextNode();
        }
        return false; //return false if there is no duplicate detected
    }

    /**
     * @clone: this clone returns a new copy of the list.
     */
    public LinkedList clone(){
        LinkedList copy = new LinkedList();
        Node temp = head;  //start from head node

        while(temp != null){
            copy.enqueue(temp.getVal()); //append the value held in every node from the head to the null node from the original to the copied list
            temp = temp.getNextNode();
        }

        return copy;
    }

    /**
     * @reverse: this method reverses the order of the nodes in the list
     */
    public void reverse(){
        LinkedList newEmptyList = new LinkedList();
        for (Node backwards = tail; backwards != null; backwards = backwards.getPrevious()){
            newEmptyList.enqueue(backwards.getVal());
        }

        //Update the new list to the existing list
        head = newEmptyList.getHead();
        tail = newEmptyList.getTail();
    }

    @Override
    public String toString() {
        String toReturn = "";
        Node currentNode = head;

        while (currentNode != null) {  //if the current null is NOT "null"
            toReturn += currentNode.getVal() + ", ";   //then concatenate the value in to the result string
            currentNode = currentNode.getNextNode();
        }

        if (!toReturn.equals("")) {
            toReturn = toReturn.substring(0, toReturn.length() - 2);
        }
        return "[" + toReturn + "]";
    }

}

class Node {
    private int val;
    private Node next, previous;

    public Node(Node previous, int value, Node next) {
        this.val = value;
        this.next = next;
        this.previous = previous;
    }

    //getter methods
    public int getVal() {
        return val;
    }

    public Node getNextNode() {
        return next;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setNext(Node node) {
        next = node;
    }

    public void setPrevious(Node node) {
        previous = node;
    }

}

