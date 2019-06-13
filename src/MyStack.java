/* A typical Stack, for Nodes.
 * Includes push, pop, top, isEmpty */
public class MyStack
{
  private MyNode head;
  private int numElements;
  
  // Constructor
  public MyStack()
  {
    head = null;
    numElements = 0;
  }
  
  
  // Remove all elements from stack
  public void destroyStack()
  {
    head = null;
    numElements = 0;
  }
  
  
  // Check if stack is empty
  public boolean isEmpty()
  {
    if (head == null)
      return true;
    else
      return false;
  }
  
  
  // Add the data to the top of the stack
  public void push(int[] newPuzzle)
  {
    MyNode temp = new MyNode (newPuzzle, head);
    head = temp;
    numElements++;
  }
  
  
  // Remove the data at the top of the stack
  public void pop()
  {
    if(this.isEmpty()){
      return;
    }
    head = head.getNext();
    numElements--;
  }
  
  
  // Return data value from top of the stack
  public int[] top()
  { 
    return head.getElem();
  }
  
  
  // returns number of elements in stack
  public int getNumElements()
  {
    return numElements;
  }
  
  // Display the puzzle grid values at top of stack
  public void displayTop()
  {
    int[] tempGrid = this.top();
    int length = tempGrid.length;
    
    System.out.println("Current Grid (" + numElements + "):");
    
    for(int count = 0; count < length; ++count){
      
      System.out.print(tempGrid[count] + " ");
      if((count+1) % 4 == 0)
        System.out.println("\n");
    }
  }
  
}  // End MyStack