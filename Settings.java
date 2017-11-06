import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;



public class Settings extends JFrame {
 private GameController gameController;
 public JRadioButton plane;
 public JRadioButton tortus;

	public Settings(GameController gameController){

		super("Settings");
		this.gameController= gameController;
		//JPanel shape = new JPanel();
		//shape.setBackground(Color.WHITE);
		//shape.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel playOn = new JLabel("Play on a tortus?");
        JLabel playWith = new JLabel("Play with Diagonals?");
		plane = new JRadioButton("Plane" , true);
		tortus = new JRadioButton("Tortus" , false);
		ButtonGroup planeOrTortus = new ButtonGroup();
		planeOrTortus.add(plane);
		planeOrTortus.add(tortus);
		JRadioButton orthogonal = new JRadioButton("Orthogonal" , true);
		JRadioButton diagonal = new JRadioButton("Diagonal" , false);
		plane.addActionListener(gameController);
		tortus.addActionListener(gameController);
		orthogonal.addActionListener(gameController);
		diagonal.addActionListener(gameController);
		Box shape = Box.createVerticalBox();
		shape.add(playOn);
		shape.add(plane);
		shape.add(tortus);
		shape.add(playWith);
		shape.add(orthogonal);
		shape.add(diagonal);


		add(shape,BorderLayout.CENTER);
		pack();
		setVisible(false);


	} 

}
