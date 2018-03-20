package hayen.robot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

import hayen.robot.graphisme.Animation;
import hayen.robot.graphisme.Animateur;
import hayen.robot.graphisme.Drawable;
import hayen.robot.graphisme.Grille;
import hayen.robot.graphisme.Vector2;
import hayen.robot.programme.Application;

public class Robot implements Drawable {
	
	public static double Vitesse = 2; 
	public static double VitesseRotation = 2;
	
	private double _rotationAnim; // utilisé pour les animations
	private Vector2 _translationAnim;
	private boolean _animer;
	
	private Color _couleur;
	private Animateur _animateur;
	private Application _app;
	
	// position et orientation réelles du robot
	private Point _position;
	private int _orientation;
	
	public Robot(Application app){
		_rotationAnim = 0;
		_translationAnim = new Vector2();
		_animer = false;
		_couleur = Color.blue;
		_animateur = new Animateur(this, 20, 300);
		_app = app;
		_orientation = 0;
		_position = new Point(0, 0);
	}
	
	/**
	 * @return la position du robot
	 */
	public synchronized Point getPosition(){
		return _position;
	}
	/**
	 * @return l'orientation du robot
	 */
	public synchronized int getOrientation(){ 
		return _orientation; 
	}
	
	public synchronized Vector2 getTranslation(){
		return _translationAnim;
	}
	
	public synchronized double getRotation(){
		return _rotationAnim;
	}
	
	public synchronized Application getApp(){
		return _app;
	}
	
	public synchronized Robot setAnimer(boolean v){
		_animer = v;
		return this;
	}
	public synchronized Robot setApp(Application app){
		_app = app;
		return this;
	}
	
	public synchronized boolean avancer(){
		Point direction = new Point(-(getOrientation()-1)%2, -(getOrientation()-2)%2);
		Point newPos = new Point(direction.x+_position.x, direction.y + _position.y);
		if (newPos.x < 0 || newPos.x > _app.getGrille().getTailleGrille().getX() || newPos.y < 0 || newPos.y > _app.getGrille().getTailleGrille().getY())
			return false;
		else{
			
			_position = newPos;
			if (!_animer)
				_app.getPanel().repaint();
			else {
				_app.setEnPause(true);
				_translationAnim = new Vector2(direction.x, direction.y);
				_animateur.playAnimation(getAnimDeplacement(direction));
			}
			_app.getProgramme().assigner("posx", newPos.x);
			_app.getProgramme().assigner("posy", newPos.y);
			return true;
		}
	}

	/**
	 * change la direction du robot
	 * @param direction : la direction vers laquel il faut tourne, n'accepte que Direction.Gauche et Direction.Droite: les autres ne font rien
	 * @return le Robot
	 */
	public synchronized Robot tourner(int direction){
		
		int dir = 0;
		switch(direction){
		case Direction.Droite:
			dir = 1;
			break;
		case Direction.Gauche:
			dir = -1;
			break;
		}
		
		if (dir != 0){
			_orientation = ((4 + _orientation + dir)%4);
			if (!_animer)
				_app.getPanel().repaint();
			else {
				_app.setEnPause(true);
				_rotationAnim = dir;
				_animateur.playAnimation(getAnimRotation(dir));
			}
		}
		
		return this;
	}
	
	public synchronized Vector2 getPositionAbsolue(){
		return Vector2.Multiply(new Vector2(_position.x-_translationAnim.getX(), _position.y-_translationAnim.getY()), Grille.TailleCarre + Grille.EspaceEntreCase).add(new Vector2(Grille.TailleCarre/2 + Grille.EspaceEntreCase, Grille.TailleCarre/2 + Grille.EspaceEntreCase));
	}
	
	@Override
	public void draw(Graphics2D g2){
		g2.setColor(_couleur);

		int[] centre = getPositionAbsolue().toIntArray();
		
		int[] xs = {centre[0]-15, centre[0]+15, centre[0]-15}, ys = {centre[1]+15, centre[1], centre[1]-15};
		Polygon robot = new Polygon(xs, ys, 3);
		
		double rot = _orientation - _rotationAnim;
		double theta = Math.toRadians(90 * rot);
		
		g2.rotate(theta, centre[0], centre[1]);
		g2.fillPolygon(robot);
		g2.drawPolygon(robot);
		g2.rotate(-theta, centre[0], centre[1]);
	}
	
	private Animation getAnimDeplacement(final Point direction){
		Animation anim = new Animation(){
			double f = 0;
			Point dir = direction;
			
			@Override
			public boolean run(Robot obj, int dt){
				double dep = (dt*0.001)*Robot.Vitesse;
				f += dep;
				if (f > 1)
					f = 1;
				obj._translationAnim = new Vector2((1-f)*dir.x, (1-f)*dir.y);
				
				if (f >= 1)
					return true;
				else
					return false;

			}
		};
		return anim;
	}
	
	private Animation getAnimRotation(final int direction){
		Animation anim = new Animation(){
			double f = 0;
			final int dir = direction;
			
			@Override
			public boolean run(Robot obj, int dt){
				double dep = ((double)dt/1000)*Robot.VitesseRotation;
				f += dep;
				if (f > 1)
					f = 1;
				obj._rotationAnim = (1-f)*dir;
				
				if (f >= 1)
					return true;
				else
					return false;
			}
			
		};
		return anim;
	}

	public synchronized void reset(){
		_animateur.stop();
		_translationAnim = Vector2.zero;
		_rotationAnim = 0;
		_orientation = Direction.Est;
		_position = new Point(0, 0);
	}
	
}

