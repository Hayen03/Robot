package hayen.robot.programme.instruction;

import hayen.robot.Direction;
import hayen.robot.Robot;
import hayen.robot.programme.Programme;

public class Tourner extends Instruction {

	private static final long serialVersionUID = -1439872705896478172L;
	
	private Object _direction;
	
	public Tourner(int d){
		_direction = new Integer(d);
	}
	public Tourner(String d){
		_direction = d;
	}
	
	// TODO: va être à réparer parce que je vais le briser dans pas long
	@Override
	public boolean executer(Object... params) {
		Programme p = (Programme)params[0];
		Robot r = p.getGrille().getRobot();
		int n;
		if (_direction.getClass().equals(Integer.class))
			n = (Integer)_direction;
		else {
			String s = (String)_direction;
			if (s.charAt(0) == '-')
				n = -p.getVariable(s.substring(1));
			else
				n = p.getVariable(s);
		}
		
		// C'est ici que l'on tourne le robot :)
		if (n > 0){
			r.tourner(Direction.Gauche, p);
			p.assigner("orientation", r.getOrientation());
		}	
		else if (n < 0){
			r.tourner(Direction.Droite, p);
			p.assigner("orientation", r.getOrientation());
		}
		
		return true;
	}

}
