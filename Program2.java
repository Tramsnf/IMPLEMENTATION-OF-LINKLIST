import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class Program2 {

    static StorageBlock blockHead ;

    private static class StorageBlock{

        StorageBlock next;

        LinkedList<String> linesList;

        public StorageBlock(String line) {
            linesList = new LinkedList<>();
            setNewContent(line);
        }

        public String getContent() {
            String line = "";

            Iterator i = linesList.iterator();
            while (i.hasNext()) {
                String l = i.next().toString();
                line = line+l;
            }

            return line;
        }

        public void setNewContent(String content) {
            String strToStore = null;
            if(content.length() <= 50){
                strToStore = content;
                content = null;
            }else{
                strToStore = content.substring(0, 50);
                content = content.substring(50);
            }

            //clear the linkedlist before setting new content
            linesList.clear();
            //loop through adding new content
            for(String l : strToStore.split("(?<=\\.)")){
                linesList.add(l);
            }
            //check if string exceeds 50 chars, insert extra chars at beginning of next node
            if (content != null){
                // if next node is null, create a new node
                if(getNext() == null){
                    setNext(new StorageBlock(content));
                }else{
                    getNext().setNewContentBefore(content);
                }
            }
        }

        public void insertNewLine(int position, String content){
            // avoid creation of empty blocks if no content is added
            if (content.isEmpty())
                return;
            System.out.println("inserting line after: "+position);
            System.out.println(linesList.get(position));
            //generate content of next part 2 block from remaining strings
            String line = "";
            for (int i = position+1; i< linesList.size(); i++){
                //add to line
                line  = line+linesList.get(i);
                //clear the position
                linesList.remove(i);
            }
            if (line != "") {
                //add part after paragraph to insert in new empty node
                StorageBlock part2 = new StorageBlock(line);
                //if current node has a next, set reference of current next as next for part2
                if(getNext()!=null){
                    part2.setNext(getNext());
                }
                //set our current next as part2
                setNext(part2);
            }
            //insert content in new blocks
            StorageBlock newTextBlock = new StorageBlock(content);
            //if the new content contains more than 50 characters, more blocks will be added
            //find tail of the new blocks
            StorageBlock tail = newTextBlock;
            while(tail.getNext() != null){
                tail = tail.getNext();
            }
            // point tail of inserted blocks to current next block
            tail.setNext(getNext());
            // point next of current block to the newTextBlock
            setNext(newTextBlock);
        }

        public void setNewContentBefore(String content) {
            content = content +" "+ getContent();
            setNewContent(content);
        }

        public void setNewContentAfter(String content) {
            content = getContent() +" "+ content;
            setNewContent(content);
        }

        @Override
        public String toString() {
            return getContent();
        }

        //counts the number of lines in a string.
        public int getNumberOfLines(){
            String line = getContent();
            int count = 0;
            for (char c : line.toCharArray()){
                if (c == '.')
                    count+=1;
            }
            return count;
        }

        public StorageBlock getNext() {
            return next;
        }

        public void setNext(StorageBlock next) {
            this.next = next;
        }


    }
    /**
     * method gets input from user.
     * @return an integer
     */
    static int askUserForNumberInput(String request){
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.print(request);
        return sc.nextInt();
    }

    /**
     * method gets input from user.
     * @return an integer
     */
    static String askUserForStringInput(String request){
        Scanner sc = new Scanner(System.in);
        System.out.print(request);
        return sc.nextLine();
    }

    static void insertNode(StorageBlock node){
        node.setNext(null);
        StorageBlock current = blockHead;
        //check if the head is null / if storage is empty
        if(current == null){
            //if it is null, set node to blockhead
            blockHead = node;
        }
        // if it is not null, find the last storage node
        else{
            StorageBlock lastNode = blockHead;
            while(lastNode.getNext() != null){
                lastNode = lastNode.getNext();
            }
            lastNode.setNext(node);
        }
    }
    /**
     * method picks the first 50 characters of input(content) and inserts them into a storage block.
     * if content has more than 50 characters, the excess characters are returned as string
     * @param content is the input text/string to be inserted into storage blocks
     * @return the excess text content
     */
    static String insertIntoStorage(String content){

        if(content.length() <= 50){
            String strToStore = content;
//            System.out.println(strToStore);
            insertNode(new StorageBlock(strToStore));
            content = null;
        }else{
            String strToStore = content.substring(0, 50);
//            System.out.println(strToStore);
            insertNode(new StorageBlock(strToStore));
            content = content.substring(50);
        }
        return content;
    }

    /**
     * method prints contents of our linked-list
     */
    static void printContentsInStorage(){
        //set the current node to the head
        StorageBlock currNode = blockHead;
        // traverse through our linked list printing their contents
        while (currNode != null){
            System.out.print(currNode);
            // Go to the next node
            currNode = currNode.getNext();
        }
        System.out.println();
    }

    /**
     * method adds a string/sentence at the end of our linked-list
     */
    static void addContentAtEndOfFile(){
        // ask for new input content from user
        String content = askUserForStringInput("Enter the new content: ");
        // find the last node
        StorageBlock current = blockHead;
        //check if the head is null / if head is last node
        if(current == null){
            //if it is null, set content to the head
            blockHead = new StorageBlock(content);
        }
        // if it is not null, find the last storage node
        else{
            StorageBlock lastNode = blockHead;
            while(lastNode.getNext() != null){
                lastNode = lastNode.getNext();
            }
            lastNode.setNewContentAfter(content);
        }

    }

    /**
     * Method inserts a method in between the content, in a position specified by the user
     */
    static void addContentAtAnyPostion(){
        int position = askUserForNumberInput("After which sentence you want to insert a new line (number only): ");
        // declare a variable to track line number in each storage block
        int trackLineNumber = 0;
        // ask for new input content from user
        String content = askUserForStringInput("Enter the new line: ");
        //add a space character before new inserted string
        if(content.charAt(0) != ' '){
            content = " "+content;
        }
        // find the node with the position line number
        StorageBlock current = blockHead;
        //check if the head is null / if head is last node
        if(current == null){
            //if it is null, set content to the head
            blockHead = new StorageBlock(content);
        }
        else if(position <= 0){ // do not accept any position input below 1
            System.out.println("Invalid line number");
        }
        // if it is not null, find the last storage node
        else{
            if (blockHead.getNumberOfLines() >= position){ // check if line number is located in the head, if so, add line to the head node
                // insert line in this node
                blockHead.insertNewLine((blockHead.getNumberOfLines() - position), content);
            }else{ // else parse through all other nodes in order, checking if line number can be inserted in one of the nodes
                StorageBlock lastNode = blockHead;
                while(lastNode.getNext() != null && trackLineNumber < position  ){ // repeat block while next is not null and total number of lines parsed so far is less than position
                    lastNode = lastNode.getNext(); // reset the last node
                    trackLineNumber += lastNode.getNumberOfLines(); // adjust the number of lines parsed so far
                }
                if (trackLineNumber >= position){ // recheck if position is more or less than number of lines, to determine whether we did find a node or parsed to the very last element
                    // insert line in this node
                    lastNode.insertNewLine((trackLineNumber - position), content);

                }else{ // executes if we completed the while loop but no insert position discovered
                    //inform user of invalid line number
                    System.out.println("Invalid line number");
                }
            }
        }
    }

    static int countNumberOfNodes(){
        int count = 0;
        StorageBlock current = blockHead;
        while (current!=null){
            count+=1;
            current = current.getNext();
        }
        return count;
    }
    private static void printHeadAddressAndNodesCount() {
        System.out.println("Address of the first block: " + blockHead.hashCode());
        System.out.println("Total number of blocks: : " + countNumberOfNodes());

    }
    public static void main(String ... args) throws IOException {

        //initialize our implemented linked list head and tail
        blockHead = null;
        // get contents of the file into a string
        String content = new String(Files.readAllBytes(Paths.get(askUserForStringInput("Enter the file name: "))));
        //loop to store the content in the storage in chunks of 50 characters, each chunk with their own storage block
        while (content != null) {
            content = insertIntoStorage(content);
        }



        while (true) {
            //Display menu and ask for user input
            int command = askUserForNumberInput(
                    "\nMenu:\n" +
                            "1) add new content at the end of the list\n" +
                            "2) add new content at any position of the list\n" +
                            "3) print the whole content of the list\n" +
                            "4) print the address of the first data block and the total number of data blocks\n" +
                            "5) Terminate the program\n" +
                            "Input: "
            );

            System.out.println();

            switch (command){
                case 1: {
                    addContentAtEndOfFile();
                    break;
                }
                case 2: {
                    addContentAtAnyPostion();
                    break;
                }
                case 3: {
                    // print the contents in the storage
                    printContentsInStorage();
                    break;
                }
                case 4: {
                    printHeadAddressAndNodesCount();
                    break;
                }
                case 5: {
                    System.out.println("Thank you!");
                    // terminate the program
                    System.exit(0);
                    break;
                }
                default:
                    System.out.println("Incorrect input, select a number between 1 and 5");
            }
        }
    }

}
