package hayen.robot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import hayen.robot.graphisme.Animation;
import hayen.robot.graphisme.Animateur;
import hayen.robot.graphisme.Drawable;
import hayen.robot.graphisme.Grille;
import hayen.robot.graphisme.Panel;
import hayen.robot.graphisme.Transform;
import hayen.robot.graphisme.Vector2;
import hayen.robot.programme.Programme;

public class Robot implements Drawable {
	
	public static double Vitesse = 2; 
	public static double VitesseRotation = 2;
	
	private Transform _transform;
	private boolean _animer;
	private Color _couleur;
	private Animateur _animateur;
	
	private Grille _grille;
	private Panel _panel;
	
	public Robot(Grille grille){
		_transform = new Transform();
		_grille = grille;
		_animer = false;
		_couleur = Color.blue;
		_panel = null;
		_animateur = new Animateur(_transform, null, 20, 300);
	}
	
	/**
	 * @return la position en x du robot
	 */
	public int getX(){ 
		return (int)_transform.getPosition().getX(); 
	}
	/**
	 * @return la position en y du robot
	 */
	public int getY(){ 
		return (int)_transform.getPosition().getY(); 
	}
	
	public Transform getTransform(){
		return _transform;
	}
	
	/**
	 * @return l'orientation du robot
	 */
	public int getOrientation(){ 
		return (int)_transform.getRotation()%4; 
	}
	
	public Robot setAnimer(boolean v){
		_animer = v;
		return this;
	}
	
	public boolean avancer(Programme p){
		Vector2 direction = new Vector2(-(getOrientation()-1)%2, -(getOrientation()-2)%2);
		Vector2 newPos = Vector2.Add(_transform.getPosition(), direction).toIntVector();
		if (newPos.getX() < 0 || newPos.getX() > _grille.getTailleGrille().getX() || newPos.getY() < 0 || newPos.getY() > _grille.getTailleGrille().getY())
			return false;
		else{
			if (!_animer){
				_transform.setPosition(newPos);
				_panel.repaint();
			}
			else {
//				p.pause();
				Animation anim = getAnimDeplacement(newPos, direction, p);
				_animateur.playAnimation(anim);
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
	public Robot tourner(int direction, Programme p){
		
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
			if (!_animer)
				_transform.setRotation((int)((4 + _transform.getRotation() + dir)%4));
			else {
//				System.out.println("ANIMATION TOURNANT COMMENCER!");
//				p.pause();
				_animateur.playAnimation(getAnimRotation(dir, p));
			}
		}
		
		return this;
	}
	
	public Vector2 getPositionAbsolue(){
		return Vector2.Multiply(_transform.getPosition(), Grille.TailleCarre + Grille.EspaceEntreCase).add(new Vector2(Grille.TailleCarre/2 + Grille.EspaceEntreCase, Grille.TailleCarre/2 + Grille.EspaceEntreCase));
	}
	
	@Override
	public void draw(Graphics2D g2){
		g2.setColor(_couleur);

		int[] centre = getPositionAbsolue().toIntArray();
		
		int[] xs = {centre[0]-15, centre[0]+15, centre[0]-15}, ys = {centre[1]+15, centre[1], centre[1]-15};
		Polygon robot = new Polygon(xs, ys, 3);
		
		double theta = Math.toRadians(90 * _transform.getRotation());
		
		g2.rotate(theta, centre[0], centre[1]);
		g2.fillPolygon(robot);
		g2.drawPolygon(robot);
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
	
	private Animation getAnimDeplacement(Vector2 dest, Vector2 direction, Programme p){
		Animation anim = new Animation(){
			double f = 0;
			Vector2 dir = direction;
			Programme po = p;
			
			@Override
			public boolean run(Transform obj, int dt){
				double dep = ((double)dt/1000)*Robot.Vitesse;
				double r = 1-f;
				if (dep > r)
					dep = r;
				obj.translate(Vector2.Multiply(dir, dep));
				f += dep;
				
				if (f >= 1){
//					po.resume();
					return true;
				}
				else
					return false;

			}
		};
		return anim;
	}
	
	private Animation getAnimRotation(int direction, Programme p){
		Animation anim = new Animation(){
			
			double f = 0;
			int dir = direction;
			Programme po = p;
			
			@Override
			public boolean run(Transform obj, int dt){
				double dep = ((double)dt/1000)*Robot.VitesseRotation;
				double r = 1-f;
				if (dep > r)
					dep = r;
				obj.setRotation((4 + obj.getRotation() + dir*dep)%4);
				f += dep;
				
				if (f >= 1){
//					po.resume();
					return true;
				}
				else
					return false;

			}
			
		};
		return anim;
	}
	
}

