package hayen.robot.graphisme;

import hayen.robot.programme.Application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

public class Grille implements Drawable {
	
	public static final int TailleParDefaut = 10;
	public static final Color CouleurActifDefaut = Color.yellow;
	public static final Color CouleurNonActif = Color.black;
	public static final Color CouleurRobot = new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
	public static final int TailleCarre = 50;
	public static final int TailleRobot = 25;
	public static final int EspaceEntreCase = 5;

	boolean[][] _grille;
	private int _x;
	private int _y;
	private Color _couleur;
	private Application _app;
	
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
		_app = null;
	}
	
	/**
	 * Assigne la case situ� � (x, y) � v
	 * @param x : la position en x de la case
	 * @param y : la position en y de la case
	 * @param v : la valeur � assigner � la case
	 */
	public synchronized void setCaseActif(Point pos, boolean v){
		_grille[pos.x][pos.y] = v;
	}
	public synchronized Grille setApp(Application app){
		_app = app;
		return this;
	}
	
	/**
	 * @return la taille de la grille sous la forme {x, y}
	 */
	public synchronized Vector2 getTailleGrille(){
		return new Vector2(_x, _y);
	}
	public synchronized Application getApp(){
		return _app;
	}
	
	public synchronized Dimension getPreferredSize() {
	      return new Dimension(_x * (TailleCarre + EspaceEntreCase) + EspaceEntreCase, _y * (TailleCarre + EspaceEntreCase) + EspaceEntreCase);
	}
	
	/**
	 * Dessine la grille à l'écran avec les bonnes dimensions et couleurs
	 */
	@Override
	public void draw(Graphics2D g2){
		for (int x = 0; x < _x; x++)
			for (int y = 0; y < _y; y++){
				if (_grille[x][y])
					g2.setColor(_couleur);
				else
					g2.setColor(CouleurNonActif);
				g2.fillRect(x*(TailleCarre + EspaceEntreCase) + EspaceEntreCase, y*(TailleCarre + EspaceEntreCase) + EspaceEntreCase, TailleCarre, TailleCarre);
			}
	}	
	
	/**
	 * Change la couleur d'affichage
	 * @param r : [0, 255] al quantit� de rouge dans la couleur
	 * @param g : [0, 255] al quantit� de vert dans la couleur
	 * @param b : [0, 255] al quantit� de bleu dans la couleur
	 * @return retourne la grille
	 */
	public synchronized Grille changerCouleur(int r, int g, int b){
		_couleur = new Color(r, g, b);
		return this;
	}
	
	public synchronized void reset(){
		for (int i = 0; i < _grille.length; i++)
			for (int j = 0; j < _grille[i].length; j++)
				_grille[i][j] = false;
	}
	
}
