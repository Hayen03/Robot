package hayen.robot.graphisme;


import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Boutton extends Button implements ActionListener{
	
	private Color _couleur;
	
	public Boutton(){
		super();
		_couleur = Color.white;
		addActionListener(this);
	}
	
	public Boutton setCouleur(Color couleur){
		_couleur = couleur;
		return this;
	}
	
	public Color getCouleur(){
		return _couleur;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Boutton cliquée");		
	}
	
}
