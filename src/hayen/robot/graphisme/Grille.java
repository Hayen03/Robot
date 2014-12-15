package hayen.robot.graphisme;

import hayen.robot.Robot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import javax.swing.JPanel;

public class Grille extends JPanel {
	
	public static final int TailleParDefaut = 10;
	public static final Color CouleurActifDefaut = Color.yellow;
	public static final Color CouleurNonActif = Color.black;
	public static final Color CouleurRobot = new Color(2704 % 255, 645 % 255, 465 % 255);
	public static final int TailleCarre = 50;
	public static final int TailleRobot = 25;
	public static final int EspaceEntreCase = 5;

	boolean[][] _grille;
	private int _x;
	private int _y;
	private Color _couleur;
	private Robot _robot;
	
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
		_robot = new Robot(this);
	}
	
	/**
	 * Assigne la case situé à (x, y) à v
	 * @param x : la position en x de la case
	 * @param y : la position en y de la case
	 * @param v : la valeur à assigner à la case
	 */
	public void setCaseActif(int x, int y, boolean v){
		_grille[x][y] = v;
	}
	
	/**
	 * Active ou désactive la case où se trouve le robot
	 * @param v : boolean - Active/Désactive
	 */
	public void setCaseActif(boolean v){
		_grille[_robot.getX()][_robot.getY()] = v;
	}
	
	/**
	 * @return la taille de la grille sous la forme {x, y}
	 */
	public int[] getTailleGrille(){
		return new int[]{_x, _y};
	}
	
	/**
	 * @return Le robot associé à la grille, null si il n'y en a pas
	 */
	public Robot getRobot(){
		return _robot;
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
		
		dessinerRobot(g2, _robot.getX(), _robot.getY());
		
	}
	
	private void dessinerRobot(Graphics2D g2, int x, int y){
		g2.setColor(CouleurRobot);
//		g2.fillOval(x*TailleCarre + (x+1)*EspaceEntreCase + (TailleCarre - TailleRobot)/2, (y)*TailleCarre + (y+1)*EspaceEntreCase + (TailleCarre - TailleRobot)/2, TailleRobot, TailleRobot);
		int tmpX = x*TailleCarre + (x+1)*EspaceEntreCase + TailleCarre/2;
		int tmpY = (y)*TailleCarre + (y+1)*EspaceEntreCase + TailleCarre/2;
		int[] xs = {tmpX+15, tmpX, tmpX-15}, ys = {tmpY+15, tmpY-15, tmpY+15};
		Polygon robot = new Polygon(xs, ys, 3);
		double theta = Math.toRadians(-90 * _robot.getOrientation());
		g2.rotate(theta);
		g2.fillPolygon(robot);
		g2.rotate(-theta);
	}
	
	/**
	 * Change la couleur d'affichage
	 * @param r : [0, 255] al quantité de rouge dans la couleur
	 * @param g : [0, 255] al quantité de vert dans la couleur
	 * @param b : [0, 255] al quantité de bleu dans la couleur
	 * @return retourne la grille
	 */
	public Grille changerCouleur(int r, int g, int b){
		_couleur = new Color(r, g, b);
		return this;
	}
	
}
