/* This class is my own implementation of JButton
 * Added a field that contains the buttons puzzle value */
import javax.swing.*;

public class MyButton extends JButton {
  
  private int puzzleValue;
  
  // Constructor
  public MyButton(String text, int newValue)
  {
    super(text);
    puzzleValue = newValue;
  }
  
  
  // Sets the puzzle value
  public void setPuzzleValue(int newValue)
  {
    puzzleValue = newValue;
  }
  
  
  // Returns value of puzzle button
  public int getPuzzleValue()
  {
    return puzzleValue;
  }
  
}  // End myButton class