package hayen.robot;

import hayen.robot.graphisme.Grille;

public class Robot {
	
	private int _orientation;
	private int _x, _y;
	
	private Grille _grille;
	
	public Robot(Grille grille){
		_orientation = Direction.Est;
		_x = _y = 0;
		_grille = grille;
	}
	
	/**
	 * @return la position en x du robot
	 */
	public int getX(){ return _x; }
	/**
	 * @return la position en y du robot
	 */
	public int getY(){ return _y; }
	/**
	 * @return l'orientation du robot
	 */
	public int getOrientation(){ return _orientation; }
	
	/**
	 * Avance le robot d'une case dépendant de son orientation
	 * @return vrai si le déplacement est réussit, faux si il y a un obstacle
	 */
	public boolean avancer(){
		int[] tailleGrille = _grille.getTailleGrille();
		
		switch(_orientation){
		
		case Direction.Nord:
			if (--_y < 0){
				_y++;
				return false;
			}
			break;
			
		case Direction.Sud:
			if (++_y >= tailleGrille[1]){
				_y--;
				return false;
			}
			break;
			
		case Direction.Est:
			if (++_x >= tailleGrille[0]){
				_x--;
				return false;
			}
			break;
			
		case Direction.Ouest:
			if (--_x < 0){
				_x++;
				return false;
			}
			break;
		
		}
		
		return true;
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
		if (_orientation < 0) _orientation = 4 - _orientation;
		
		return this;
	}
	
}
