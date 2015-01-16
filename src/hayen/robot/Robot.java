package hayen.robot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import hayen.robot.graphisme.Animation;
import hayen.robot.graphisme.AnimationThread;
import hayen.robot.graphisme.Drawable;
import hayen.robot.graphisme.Grille;
import hayen.robot.graphisme.Panel;
import hayen.robot.graphisme.Vector2;
import hayen.robot.programme.Programme;

public class Robot implements Drawable {
	
	public static double Vitesse = 0.05; // pixel par seconde
	
	private double _orientation;
	private Vector2 _position;
	private boolean _animer;
	private Color _couleur;
	
	private Grille _grille;
	private Panel _panel;
	
	public Robot(Grille grille){
		_orientation = Direction.Est;
		_position = new Vector2();
		_grille = grille;
		_animer = false;
		_couleur = Color.blue;
		_panel = null;
	}
	
	/**
	 * @return la position en x du robot
	 */
	public int getX(){ return (int)_position.getX(); }
	/**
	 * @return la position en y du robot
	 */
	public int getY(){ return (int)_position.getY(); }
	
	public Vector2 getPosition(){
		return _position;
	}
	public void setPosition(int x, int y){
		_position = new Vector2(x, y);
	}
	public void setPosition(Vector2 pos){
		_position = pos;
	}
	
	/**
	 * @return l'orientation du robot
	 */
	public int getOrientation(){ return (int)_orientation; }
	public double getRotation(){ return _orientation; }
	
	public Robot setAnimer(boolean v){
		_animer = v;
		return this;
	}
	
	public boolean avancer(Programme p){
		Vector2 direction = new Vector2(-(_orientation-1)%2, -(_orientation-2)%2);
		Vector2 newPos = Vector2.Add(_position, direction).toIntVector();
		if (newPos.getX() < 0 || newPos.getX() > _grille.getTailleGrille().getX() || newPos.getY() < 0 || newPos.getY() > _grille.getTailleGrille().getY())
			return false;
		else{
			if (!_animer){
				_position = newPos;
				_panel.repaint();
			}
			else {
				p.pause();
				Robot tmp = this;
				Animation anim = new Animation(){
					Robot r = tmp;
					Vector2 destination = newPos;
					Vector2 dir = direction;
					Programme po = p;
					
					@Override
					public boolean run(){
						boolean retour = false;
						Vector2 pos = r.getPosition();
						
						r.getPosition().add(Vector2.Multiply(dir, Vitesse));
						if (	dir.getY() < 0 && pos.getY() < destination.getY() ||
								dir.getY() > 0 && pos.getY() > destination.getY() ||
								dir.getX() < 0 && pos.getX() < destination.getX() ||
								dir.getX() > 0 && pos.getX() > destination.getX()){
							r.setPosition((int)destination.getX(), (int)destination.getY());
							po.resume();
							retour =  true;
						}
						
						r.getPanel().repaint();
						return retour;
					}
				};
				new AnimationThread(anim, 20).start();
			}
			p.assigner("posx", (int)newPos.getX());
			p.assigner("posy", (int)newPos.getY());
			return true;
		}
	}
	
	protected Grille getGrille() {
		return _grille;
	}

	/**
	 * change la direction du robot
	 * @param direction : la direction vers laquel il faut tourne, n'accepte que Direction.Gauche et Direction.Droite: les autres ne font rien
	 * @return le Robot
	 */
	public Robot tourner(int direction){
		
		switch(direction){
		case Direction.Droite:
			_orientation++;
			break;
		case Direction.Gauche:
			_orientation--;
			break;
		}
		
		_orientation %= 4;
		if (_orientation < 0) _orientation = 4 + _orientation;
		
		return this;
	}
	
	public Vector2 getPositionAbsolue(){
		return Vector2.Multiply(_position, Grille.TailleCarre + Grille.EspaceEntreCase).add(new Vector2(Grille.TailleCarre/2 + Grille.EspaceEntreCase, Grille.TailleCarre/2 + Grille.EspaceEntreCase));
	}
	
	@Override
	public void draw(Graphics2D g2){
		g2.setColor(_couleur);

		int[] centre = getPositionAbsolue().toIntArray();
		
		int[] xs = {centre[0]-15, centre[0]+15, centre[0]-15}, ys = {centre[1]+15, centre[1], centre[1]-15};
		Polygon robot = new Polygon(xs, ys, 3);
		
		double theta = Math.toRadians(90 * _orientation);
		
		g2.rotate(theta, centre[0], centre[1]);
		g2.fillPolygon(robot);
		g2.rotate(-theta, centre[0], centre[1]);
	}
	@Override
	public Panel getPanel(){
		return _panel;
	}
	@Override
	public void setPanel(Panel p){
		_panel = p;
	}
	
}
