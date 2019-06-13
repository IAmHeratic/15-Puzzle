/* Author: Jose Rodriguez
 * CS 342 Project 2: Fifteen Puzzle
 * University of Illinois at Chicago
 *
 *
 * This program is a GUI implementation of the game Fifteen puzzle in
 * which the player clicks on buttons to swap an empty button around
 * the buttons are in numeric order from top left to bottom right.
 * The player can also undo moves and try a different puzzle from
 * scratch, as well as solve the puzzle.
 *
 * Internally, int arrays represent the button numbers displayed,
 * a linked list stack contains all the states of the game (all turns),
 * and BFS is used to solve the puzzle which is randomly generated
 * prior to the game starting.
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FourByFourGrid extends JFrame implements ActionListener {

   private MyButton     buttons[];
   private final String names[] = { "1", "2", "3", "4", "5", "6", "7", "8",
                                    "9", "10", "11", "12", "13", "14", "15", "" };
   private Container    container;
   private GridLayout   grid;

   private JMenuBar     menuBar;
   private JLabel       puzzleData;

   private Puzzle       myPuzzle;
   private MyStack      stack;
   private Timer        timer;
   private int          numberOfSwaps;


   // Set up GUI
   public FourByFourGrid()
   {
     super( "Fifteen Puzzle" );
     myPuzzle = new Puzzle();
     stack    = new MyStack();

     setupPuzzle(myPuzzle);
     stack.push(myPuzzle.getPuzzleNumbers());
     numberOfSwaps = 0;

     // Set up 4x4 Layout
     grid = new GridLayout(4, 4);

     // get content pane and set its layout
     container = getContentPane();
     container.setLayout( grid );

     // Create a File menu item
     JMenu fileMenu = new JMenu("File");
     fileMenu.setMnemonic('F');

     // set up About (File) menu item
     JMenuItem aboutItem = new JMenuItem( "About" );
     aboutItem.setMnemonic( 'A' );
     fileMenu.add( aboutItem );
     aboutItem.addActionListener(
                                 
        new ActionListener() {  // anonymous inner class
       
           public void actionPerformed( ActionEvent event )
           {
              JOptionPane.showMessageDialog( FourByFourGrid.this,
                                       "Author: Jose Rodriguez\nDate: 9-30-17\n\nCS 342 Project 2\nNo Extra Credit Attempted" +
                                       "\n\nDeveloped using DrJava on Windows.",
                                       "About", JOptionPane.PLAIN_MESSAGE );
           }
           
        }  // end anonymous inner class
                                 
     ); // end call to addActionListener
     
     
     // Set up Help (File) menu item
     JMenuItem helpItem = new JMenuItem( "Help" );
     helpItem.setMnemonic( 'H' );
     fileMenu.add( helpItem );
     helpItem.addActionListener(
                                 
        new ActionListener() {  // anonymous inner class
       
           public void actionPerformed( ActionEvent event )
           {
              JOptionPane.showMessageDialog( FourByFourGrid.this,
                                        "Fifteen Puzzle: The objective is to swap the\nempty button with the buttons with numbers on\nthem " +
                                        "such that the numbers are in numeric\norder starting from the top left." + "Click on a button\nabove, " +
                                        "below, left, or right of the empty button to\nswap them." +
                                        "\n\nAbout: Displays my information.\n\nHelp: You are currently reading the help button!" +
                                        "\n\nQuit: Exits the game.\n\nUndo Prev: Undo the last move made.\n\nUndo All: Undo ALL moves made." +
                                        "\n\nRestart: Start the game over with a new puzzle.\n\nSolve: Display the solution to the puzzle.",
                                       "Help", JOptionPane.PLAIN_MESSAGE );
           }
           
        }  // end anonymous inner class
                              
     ); // end call to addActionListener
     
     
     // Set up Quit (File) menu item
     JMenuItem quitItem = new JMenuItem( "Quit" );
     quitItem.setMnemonic( 'Q' );
     fileMenu.add( quitItem );
     quitItem.addActionListener(
                                 
        new ActionListener() {  // anonymous inner class
       
           public void actionPerformed( ActionEvent event )
           {
              System.exit(0);
           }
           
        }  // end anonymous inner class
                                 
     ); // end call to addActionListener
     
     
     // Create an Undo menu item
     JMenu undoMenu = new JMenu("Undo");
     undoMenu.setMnemonic('U');
     
     // Set up Previous (File) menu item
     JMenuItem previousItem = new JMenuItem( "Previous" );
     previousItem.setMnemonic( 'P' );
     undoMenu.add( previousItem );
     previousItem.addActionListener(
                                 
        new ActionListener() {  // anonymous inner class
       
           public void actionPerformed( ActionEvent event )
           {
             if(numberOfSwaps == 0){
               JOptionPane.showMessageDialog( FourByFourGrid.this,
                                       "There are no more turns to undo!",
                                       "Previous", JOptionPane.PLAIN_MESSAGE );
               return;
             }
             
             undo();
             numberOfSwaps--;
             puzzleData.setText("Swaps: " + numberOfSwaps + "   Complexity: " + myPuzzle.getComplexity());
             
             JOptionPane.showMessageDialog( FourByFourGrid.this,
                                       "Successfully returned to previous puzzle state!",
                                       "Previous", JOptionPane.PLAIN_MESSAGE );
           }
           
        }  // end anonymous inner class
                                 
     ); // end call to addActionListener
     
     
     // Set up All (File) menu item
     // Create a timer so the Undo All seems
     // to have an animation (1 sec per undo)
     int delay = 1000; // One second
     timer = new Timer( delay, new TimerHandler() );
     
     JMenuItem allItem = new JMenuItem( "All" );
     allItem.setMnemonic( 'A' );
     undoMenu.add( allItem );
     allItem.addActionListener(
                                 
        new ActionListener() {  // anonymous inner class
       
           public void actionPerformed( ActionEvent event )
           {
              JOptionPane.showMessageDialog( FourByFourGrid.this,
                                       "Undo-ing all moves...",
                                       "All", JOptionPane.PLAIN_MESSAGE );
              
              timer.start();
           }
           
        }  // end anonymous inner class
                                 
     ); // end call to addActionListener
     
     
     
     // Create an Game menu item
     JMenu gameMenu = new JMenu("Game");
     gameMenu.setMnemonic('G');
     
     // Set up Restart (File) menu item
     JMenuItem restartItem = new JMenuItem( "Restart" );
     restartItem.setMnemonic( 'R' );
     gameMenu.add( restartItem );
     restartItem.addActionListener(
                                 
        new ActionListener() {  // anonymous inner class
       
           public void actionPerformed( ActionEvent event )
           {
              setupPuzzle(myPuzzle);
              restartGrid();
              stack.destroyStack();
              stack.push(myPuzzle.getPuzzleNumbers());
              numberOfSwaps = 0;
              
              JOptionPane.showMessageDialog( FourByFourGrid.this,
                                       "Puzzle was successfully restarted!",
                                       "Restart", JOptionPane.PLAIN_MESSAGE );
           }
           
        }  // end anonymous inner class
                                 
     ); // end call to addActionListener
     
     
     // TODO -------------->
     //
     //
     // Set up Solve (File) menu item
     //
     //
     // TODO -------------->
     JMenuItem solveItem = new JMenuItem( "Solve" );
     solveItem.setMnemonic( 'S' );
     gameMenu.add( solveItem );
     solveItem.addActionListener(
                                 
        new ActionListener() {  // anonymous inner class
       
           public void actionPerformed( ActionEvent event )
           {
              JOptionPane.showMessageDialog( FourByFourGrid.this,
                                       "This is the Solve menu item!",
                                       "Solve", JOptionPane.PLAIN_MESSAGE );
           }
           
        }  // end anonymous inner class
                                 
     ); // end call to addActionListener
     
     
     // Add buttons that display turns (swaps) and complexity
     puzzleData = new JLabel("Swaps: 0   Complexity: " + myPuzzle.getComplexity());
     container.add (puzzleData);
     
     
     /* create menu bar and attach it to MenuTest window
      * and add the File, Undo, and Game menus */
     menuBar = new JMenuBar();  
     setJMenuBar( menuBar );
     
     menuBar.add(fileMenu);
     menuBar.add(undoMenu);
     menuBar.add(gameMenu);
     
     menuBar.add(puzzleData);
     
     // create and add buttons (to the container)
     // for every button give it a name (one, two, etc)
     // Also add the property to have an action (click on it!)
     int length = myPuzzle.getPuzzleNumbers().length - 1;
     buttons = new MyButton[ myPuzzle.getPuzzleNumbers().length ];
     
     for ( int count = 0; count < length; count++ ) {
       
       buttons[ count ] = new MyButton( String.valueOf(myPuzzle.getPuzzleNumbers()[ count ]), myPuzzle.getPuzzleNumbers()[count]);
       
       buttons[ count ].addActionListener( this );
       container.add( buttons[ count ] );
     }
     
     // Create empty button and add it to container
     buttons[length] = new MyButton("", myPuzzle.getPuzzleNumbers()[length] );
     buttons[length].addActionListener( this );
     container.add( buttons[length] );
     
     setSize( 700, 350 );
     setVisible( true );

   } // end constructor GridLayoutDemo
   
   
   
   // handle button events by swapping buttons
   // if swap occurs, add current grid to stack
   public void actionPerformed( ActionEvent event )
   {
     container.validate();
     
     // Check if puzzle was solved
     if(puzzleSolved()){
       
       JOptionPane.showMessageDialog( this, "Congrats, you solved the puzzle!\nMoves made: " + numberOfSwaps + "\nComplexity: " + myPuzzle.getComplexity());
       System.exit(0);
     }
     
     
     // Get the button the event was triggered by
     MyButton temp = (MyButton) event.getSource();
     
     if(temp.getPuzzleValue() == 16){
       
       JOptionPane.showMessageDialog( this, "You pressed the empty button!");
       return;
     }
     
     
     // Find index of clicked button
     int buttonPos = -1;
     
     for ( int index = 0; index < buttons.length; index++ ) {
       if ( temp.equals( buttons[index] ) )
         buttonPos = index;
     }
     
     
     // Attempt to swap buttons
     boolean successfulSwap = findEmptyButton(buttonPos);
     
     if(successfulSwap){
       
       // Add grid to stack!
       numberOfSwaps++;
       int[] currentGrid = new int[16];
       
       for (int index = 0; index < buttons.length; ++index) {
         
         currentGrid[index] = buttons[index].getPuzzleValue();
       }
       
       stack.push(currentGrid);
       puzzleData.setText("Swaps: " + numberOfSwaps + "   Complexity: " + myPuzzle.getComplexity());
       
       JOptionPane.showMessageDialog( this, "Swapped with empty button!\nSwap #: " + numberOfSwaps);
     }
     
   }  // End action performed
   
   
   // Inner class for timer event handling
   // for the amount of turns, undo last move
   private class TimerHandler implements ActionListener {
     
     // handle button event
     public void actionPerformed( ActionEvent event )
     {
       if(numberOfSwaps == 0){
         
         JOptionPane.showMessageDialog( FourByFourGrid.this,
                                       "All moves were succesfully undone!"  );
         timer.stop();
       } else {
         
         undo();
         numberOfSwaps--;
         puzzleData.setText("Swaps: " + numberOfSwaps + "   Complexity: " + myPuzzle.getComplexity());
       }
       
     }
   }
   
   
   // Compares current grid arrangement to the solution
   // return true if puzzle was solved
   public boolean puzzleSolved()
   {
     int length      = stack.top().length;
     int [] solution = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
     
     for ( int count = 0; count < length; count++ ) {
       
       if( (buttons[count].getPuzzleValue()) !=  solution[count] ){
         return false;
       }
     }
     System.out.println("Solution was FOUND! Congratulations!");
     return true;
   }
   
   
   // This method changes buttons to the new puzzle
   public void restartGrid()
   {
     int length = myPuzzle.getPuzzleNumbers().length - 1;
     
     for ( int count = 0; count < length; count++ ) {
       
       buttons[ count ].setText( String.valueOf(myPuzzle.getPuzzleNumbers()[ count ]));
       buttons[ count ].setPuzzleValue(myPuzzle.getPuzzleNumbers()[count]);
     }
     
     buttons[ length ].setText("");
     buttons[ length ].setPuzzleValue(myPuzzle.getPuzzleNumbers()[length]);
   }
   
   
   // First pops from the stack's then
   // Changes puzzle to previous state (Undo 1 swap)
   public void undo()
   {
     stack.pop();
     System.out.println("Popped Stack! Nodes: " + stack.getNumElements());
     
     int length  = stack.top().length;
     int [] temp = stack.top();
     
     for ( int count = 0; count < length; count++ ) {
       
       buttons[ count ].setText( String.valueOf(temp[ count ]) );
       buttons[ count ].setPuzzleValue(temp[count]);
       
       if( (buttons[ count ].getText()).equals("16") ){
         buttons[ count ].setText("");
       }
     }
   }
   
   
   // Find empty button (up, down, left, right)
   // if found, swap puzzle values
   public boolean findEmptyButton(int source)
   {
     // Right button
     if( validAdjacentButton(source + 1) ){
       if((buttons[source + 1].getText()).equals("")){
         
         System.out.println("Found empty button! (Right)");
         swapButtons(source, source + 1);
         return true;
       }
     }
     
     
     // Left button
     if( validAdjacentButton(source - 1) ){
       if((buttons[source - 1].getText()).equals("")){
         
         System.out.println("Found empty button! (Left)");
         swapButtons(source, source - 1);
         return true;
       }
     }
     
     
     // Up button
     if(validAdjacentButton(source - 4)){
       if((buttons[source - 4].getText()).equals("")){
         
         System.out.println("Found empty button! (Up)");
         swapButtons(source, source - 4);
         return true;
       }
     }
     
     
     // Down button
     if(validAdjacentButton(source + 4)){
       if((buttons[source + 4].getText()).equals("")){
         
         System.out.println("Found empty button! (Down)");
         swapButtons(source, source + 4);
         return true;
       }
     }
     
     return false;
   }
   
   
   // Check that surrounding possible button is not out of bounds
   public boolean validAdjacentButton(int dest)
   {
     if(dest < 0){
       return false;
     } else if (dest > names.length - 1){
       return false;
     }
     return true;
   }
   
   
   // Swap the buttons text and puzzle values
   public void swapButtons(int source, int dest)
   {
     int helperValue = buttons[source].getPuzzleValue();
     int destValue   = buttons[dest].getPuzzleValue();
     
     String helperText = buttons[source].getText();
     String destText   = buttons[dest].getText();
     
     // Swap buttons
     buttons[source].setPuzzleValue( destValue );
     buttons[source].setText( destText );
     
     buttons[dest].setPuzzleValue( helperValue );
     buttons[dest].setText( helperText );
   }
   
   
   // Main method
   public static void main( String args[] )
   {
     
     
     
     FourByFourGrid application = new FourByFourGrid();
     application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
     
     
     
   }  // End main
   
   
   // Shuffle the puzzle elements until a solvable puzzle is generated
   public static void setupPuzzle(Puzzle myPuzzle)
   {
     int complexity   = 0;
     boolean solvable = false;
     
     while(!solvable){
       
       myPuzzle.shuffle();
       
       myPuzzle.displayPuzzleInfo();
       
       complexity = myPuzzle.getInversionCount();
       solvable   = myPuzzle.solvablePuzzle(complexity);
       
       if(solvable){
         System.out.println("This puzzle is solvable! Complexity = " + complexity);
       } else {
         System.out.println("This puzzle is NOT solvable! Complexity = " + complexity);
       }
     }
     
     myPuzzle.setComplexity(complexity);
   }  // End setupPuzzle
   
   
}  // End FourByFourGrid
