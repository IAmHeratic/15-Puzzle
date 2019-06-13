/* Nodes for the linked list stack implementations
 * the nodes contain a state (turn) in the puzzle game.
 */
public class MyNode
{
  private int[]  puzzleNumbers;
  private MyNode next;
  
  // Default constructor
  public MyNode()
  {
    puzzleNumbers = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
    next = null;
  }
  
  
  // New node with specified data
  public MyNode(int[] newPuzzle)
  {
    puzzleNumbers = newPuzzle;
    next = null;
  }
  
  
  // New node with specified data, and pointer to specified node
  public MyNode(int[] newPuzzle, MyNode n)
  {
    puzzleNumbers = newPuzzle;
    next = n;
  }
  
  
  // Set the data of a node to specified data
  public void setElem(int[] newPuzzle)
  {
    puzzleNumbers = newPuzzle;
  }
  
  
  // Return puzzle numbers
  public int[] getElem( )
  {
    return puzzleNumbers;
  }
  
  
  // Make node point to given node
  public void setNext(MyNode n)
  {
    next = n;
  }
  
  
  // Return pointer to next node
  public MyNode getNext ( )
  {
    return next;
  }

}  // End MyNode