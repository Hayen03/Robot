package hayen.robot.graphisme;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Grille extends JPanel {
	
	public static final int TailleParDefaut = 10;
	public static final Color CouleurActifDefaut = Color.yellow;
	public static final Color CouleurNonActif = Color.black;
	public static final int TailleCarre = 50;
	public static final int EspaceEntreCase = 5;

	boolean[][] _grille;
	private int _x;
	private int _y;
	private Color _couleur;
	
	public Grille(){
		this(TailleParDefaut, TailleParDefaut);
	}
	public Grille(int x, int y){
		super();
		_grille = new boolean[x][y];
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++)
				_grille[i][j] = false;
		_x = x;
		_y = y;
		_couleur = CouleurActifDefaut;
	}
	
	public void setCaseActif(int x, int y, boolean v){
		_grille[x][y] = v;
	}
	
	public int[] getTailleGrille(){
		return new int[]{_x, _y};
	}
	
	@Override
	public Dimension getPreferredSize() {
	      return new Dimension(_x * (TailleCarre + EspaceEntreCase) + EspaceEntreCase, _y * (TailleCarre + EspaceEntreCase) + EspaceEntreCase);
	   }
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		for (int x = 0; x < _x; x++)
			for (int y = 0; y < _y; y++){
				if (_grille[x][y]) 
					g2.setColor(_couleur);
				else
					g2.setColor(CouleurNonActif);
				g2.fillRect(x*(TailleCarre + EspaceEntreCase) + EspaceEntreCase, y*(TailleCarre + EspaceEntreCase) + EspaceEntreCase, TailleCarre, TailleCarre);
			}
	}
	
	public void changerCouleur(int r, int g, int b){
		_couleur = new Color(r, g, b);
	}
	
}
