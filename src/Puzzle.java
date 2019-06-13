/* This class generates a solvable puzzle for the player
 * by determining inversion count. This is done by randomly
 * shuffling values in an array until a solvable puzzle is
 * generated. The complexity is the inversion count.
 */
public class Puzzle {
  
  private int puzzleNumbers[];
  private int complexity;
  
  // Basic constructor for puzzle
  public Puzzle() {
    
    puzzleNumbers = new int[16];
    complexity    = 1;
    
    for(int index = 0; index < 16; ++index){      
      puzzleNumbers[index] = index + 1;
    }
  }
  
  
  // Set the Puzzles Complexity (Inversion count)
  public void setComplexity(int newComplexity)
  {
    complexity = newComplexity;
  }
  
  
  // Return the complexity of puzzle
  public int getComplexity()
  {
    return complexity;
  }
  
  
  // Return array of puzzle numbers
  public int[] getPuzzleNumbers()
  {
    return puzzleNumbers;
  }
  
  
  // Based on inversion count, is the puzzle solvable?
  public boolean solvablePuzzle(int inversionCount)
  {
    if(inversionCount % 2 == 0){
      return true;
    } else {
      return false;
    }
  }
  
  
  // Determines and sums the inversion count of each element
  // Inversion count is the number of elements smaller
  // than the current element in the rest of the array
  public int getInversionCount()
  {
    int length = puzzleNumbers.length - 1;
    int indexCount      = 0;
    int totalInversions = 0;
    
    for(int index = 0; index < length; ++index){
      
      for(int higherIndex = index + 1; higherIndex < length; ++higherIndex){
        
        if(puzzleNumbers[index] > puzzleNumbers[higherIndex])
          indexCount++;
      }
      
      totalInversions += indexCount;
      indexCount = 0;
    }
    return totalInversions;
  }
  
  
  // Loop through each value in puzzle array
  // Randomly select a different element and swap
  public void shuffle()
  {
    int length = puzzleNumbers.length - 1;
    
    for (int index = 0; index < length; ++index) {
      
      int change = (int)(Math.random() * (length));
      swap(index, change);
    }
  }
  
  
  // Swaps the selected values in the array
  public void swap(int old, int change)
  {
    int temp = puzzleNumbers[old];
    puzzleNumbers[old]    = puzzleNumbers[change];
    puzzleNumbers[change] = temp;
  }
  
  
  
  // TODO: Delete
  public void displayPuzzleInfo(){
    
    int length = puzzleNumbers.length;
    System.out.print("Puzzle:");
    
    for(int i = 0; i < length; ++i){
      
      System.out.print(" (" + puzzleNumbers[i] + ")");
    }
    System.out.println("");
  }
  
}  // End Puzzle class