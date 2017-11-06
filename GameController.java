import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.io.*;

import javax.swing.*;


/**
 * The class <b>GameController</b> is the controller of the game. It has a method
 * <b>selectColor</b> which is called by the view when the player selects the next
 * color. It then computesthe next step of the game, and  updates model and view.
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */


public class GameController implements ActionListener {

	private boolean[] options = new boolean[4];


	private Settings setting; 

    /**
     * Reference to the view of the board
     */
    private GameView gameView;
    /**
     * Reference to the model of the game
     */
    private GameModel gameModel;
 
    /**
     * Constructor used for initializing the controller. It creates the game's view 
     * and the game's model instances
     * 
     * @param size
     *            the size of the board on which the game will be played
     */
    public GameController(int size) {
    	options[0]=true;//plane
    	options[1]=false;//tortus
    	options[2]=true;//orthogonal
    	options[3]=false;//diagonal
        gameModel = new GameModel(size);
        gameView = new GameView(gameModel, this);
        setting = new Settings(this);
        flood();
        gameView.update();
    }

    /**
     * resets the game
     */
    public void reset(){
        gameModel.reset();
        flood();
        gameView.update();
    }

    /**
     * Callback used when the user clicks a button (reset or quit)
     *
     * @param e
     *            the ActionEvent
     */

    public void actionPerformed(ActionEvent e) {
        DotButton dot;
        if(e.getSource() instanceof DotButton && gameModel.getNumberOfSteps()==0){
            dot=(DotButton)(e.getSource());
            gameModel.capture(dot.getRow(),dot.getColumn());
            selectColor(dot.getColor());
        }if (e.getSource() instanceof AbstractButton){
        	AbstractButton selected = (AbstractButton) e.getSource();
        	    //if(selected.isSelected()){
            		if (selected.getText().equals("Plane")){
            			//options[0]=selected.isSelected();

            			//setting.tortus = new JRadioButton("Tortus" , !selected.isSelected());

            		}
            		if(selected.getText().equals("Tortus")){
            			options[1]=selected.isSelected();
            			//setting.plane = new JRadioButton("Plane" , !selected.isSelected());
            			//System.out.println(options[1]);

            		}if(selected.getText().equals("Orthogonal")){
            			options[2]=selected.isSelected();

            		}if(selected.getText().equals("Diagonal")){
            			options[3]=selected.isSelected();

            		}
            //}
        }
        
        if (e.getSource() instanceof DotButton) {
            selectColor(((DotButton)(e.getSource())).getColor());
            //System.out.println("ho");
        } else if (e.getSource() instanceof JButton) {
            JButton clicked = (JButton)(e.getSource());

            if (clicked.getText().equals("Quit")) {
            	save();
                System.exit(0);
             } else if (clicked.getText().equals("Reset")){
                reset();
             }
            if(clicked.getText().equals("Settings")){
            	setting.setVisible(true);
            }
            
        }
 
    }

    /**
     * <b>selectColor</b> is the method called when the user selects a new color.
     * If that color is not the currently selected one, then it applies the logic
     * of the game to capture possible locations. It then checks if the game
     * is finished, and if so, congratulates the player, showing the number of
     * moves, and gives to options: start a new game, or exit
     * @param color
     *            the newly selected color
     */
    public void selectColor(int color){
        if(color != gameModel.getCurrentSelectedColor()) {
            gameModel.setCurrentSelectedColor(color);
            flood();
            gameModel.step();
            gameView.update();

            if(gameModel.isFinished()) {
                      Object[] options = {"Play Again",
                                "Quit"};
                        int n = JOptionPane.showOptionDialog(gameView,
                                "Congratulations, you won in " + gameModel.getNumberOfSteps() 
                                    +" steps!\n Would you like to play again?",
                                "Won",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]);
                        if(n == 0){
                            reset();
                        } else{
                            System.exit(0);
                        }   
                }            
            }        
    }

   /**
     * <b>flood</b> is the method that computes which new dots should be ``captured'' 
     * when a new color has been selected. The Model is updated accordingly
     */
     private void flood() {

        Stack<DotInfo> stack = new GenericArrayStack<DotInfo>(gameModel.getSize()*gameModel.getSize());
        for(int i =0; i < gameModel.getSize(); i++) {
           for(int j =0; j < gameModel.getSize(); j++) {
                if(gameModel.isCaptured(i,j)) {
                    stack.push(gameModel.get(i,j));
                }
           }
        }

        while(!stack.isEmpty()){
        	
	        DotInfo DotInfo = stack.pop();



	        if(options[1]==true){
	            if((DotInfo.getX() == 0) && shouldBeCaptured (gameModel.getSize()-1, DotInfo.getY())) {
	                gameModel.capture(gameModel.getSize()-1, DotInfo.getY());
	                stack.push(gameModel.get(gameModel.getSize()-1, DotInfo.getY()));
	            }  
	            if((DotInfo.getX() == gameModel.getSize()-1) && shouldBeCaptured (0, DotInfo.getY())) {
	                gameModel.capture(0, DotInfo.getY());
	                stack.push(gameModel.get(0, DotInfo.getY()));
	            }
	            if((DotInfo.getY() == 0) && shouldBeCaptured (DotInfo.getX(), gameModel.getSize()-1)) {
	                gameModel.capture(DotInfo.getX(), gameModel.getSize()-1);
	                stack.push(gameModel.get(DotInfo.getX(), gameModel.getSize()-1));
	            }  
	            if((DotInfo.getY() == gameModel.getSize()-1) && shouldBeCaptured (DotInfo.getX(), 0)) {
	                gameModel.capture(DotInfo.getX(), 0);
	                stack.push(gameModel.get(DotInfo.getX(), 0));
	            }
        	}if(options[2]==true){
	        	  	if((DotInfo.getX() > 0) && shouldBeCaptured (DotInfo.getX()-1, DotInfo.getY())) {
		                gameModel.capture(DotInfo.getX()-1, DotInfo.getY());
		                stack.push(gameModel.get(DotInfo.getX()-1, DotInfo.getY()));
		            }  
		            if((DotInfo.getX() < gameModel.getSize()-1) && shouldBeCaptured (DotInfo.getX()+1, DotInfo.getY())) {
		                gameModel.capture(DotInfo.getX()+1, DotInfo.getY());
		                stack.push(gameModel.get(DotInfo.getX()+1, DotInfo.getY()));
		            }
		            if((DotInfo.getY() > 0) && shouldBeCaptured (DotInfo.getX(), DotInfo.getY()-1)) {
		                gameModel.capture(DotInfo.getX(), DotInfo.getY()-1);
		                stack.push(gameModel.get(DotInfo.getX(), DotInfo.getY()-1));
		            }  
		            if((DotInfo.getY() < gameModel.getSize()-1) && shouldBeCaptured (DotInfo.getX(), DotInfo.getY()+1)) {
		                gameModel.capture(DotInfo.getX(), DotInfo.getY()+1);
		                stack.push(gameModel.get(DotInfo.getX(), DotInfo.getY()+1));
		            
	        	}
        	}if(options[3]==true){
        			if((DotInfo.getX() > 0) && (DotInfo.getY() > 0) 
        			&& shouldBeCaptured (DotInfo.getX()-1, DotInfo.getY()-1)) {//under
		                gameModel.capture(DotInfo.getX()-1, DotInfo.getY()-1);
		                stack.push(gameModel.get(DotInfo.getX()-1, DotInfo.getY()-1));
		            }


		            if((DotInfo.getX() < gameModel.getSize()-1) && (DotInfo.getY() < gameModel.getSize()-1) 
		            && shouldBeCaptured (DotInfo.getX()+1, DotInfo.getY()+1)) {//right
		                gameModel.capture(DotInfo.getX()+1, DotInfo.getY()+1);
		                stack.push(gameModel.get(DotInfo.getX()+1, DotInfo.getY()+1));
		            }
		            if((DotInfo.getY() > 0) && (DotInfo.getX() < gameModel.getSize()-1)
		            && shouldBeCaptured (DotInfo.getX()+1, DotInfo.getY()-1)) {//under
		                gameModel.capture(DotInfo.getX()+1, DotInfo.getY()-1);
		                stack.push(gameModel.get(DotInfo.getX()+1, DotInfo.getY()-1));
		            }  
		            if((DotInfo.getY() < gameModel.getSize()-1) && (DotInfo.getX() > 0)
		            &&shouldBeCaptured (DotInfo.getX()-1, DotInfo.getY()+1)) {//left
		                gameModel.capture(DotInfo.getX()-1, DotInfo.getY()+1);
		                stack.push(gameModel.get(DotInfo.getX()-1, DotInfo.getY()+1));
		           	}
        	}
        }
    }


    /**
     * <b>shouldBeCaptured</b> is a helper method that decides if the dot
     * located at position (i,j), which is next to a captured dot, should
     * itself be captured
     * @param i
     *            row of the dot
     * @param j
     *            column of the dot
     */
    
   private boolean shouldBeCaptured(int i, int j) {
        if(!gameModel.isCaptured(i, j) &&
           (gameModel.getColor(i,j) == gameModel.getCurrentSelectedColor())) {
            return true;
        } else {
            return false;
        }
    }

   private void save(){
   	String fileName = "savedGame.ser";
   	try{
   		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName));
   		os.writeObject(this.gameModel);
   		os.close();
   	} catch(FileNotFoundException e){
   		e.printStackTrace();
   	} catch(IOException e){
   		e.printStackTrace();
   	}
   }
   public void load(GameModel e){
   	this.gameModel=e;
   	gameView.setModel(e);
   	gameView.update();
   	deleteFile();
   	//System.out.println(this.gameModel);
   }
   private void deleteFile(){
   		File fileName = new File("savedGame.ser");
   		try{
   			//fileName.list();
   			fileName.delete();
   		}catch(Exception e){
   			e.printStackTrace();
   		} 
   }


}
