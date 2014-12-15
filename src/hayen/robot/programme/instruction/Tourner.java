package hayen.robot.programme.instruction;

import hayen.robot.Direction;
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
	
	@Override
	public boolean executer(Object... params) {
		Programme p = (Programme)params[0];
		int n;
		if (_direction.getClass().equals(Integer.class))
			n = (Integer)_direction;
		else {
			String s = (String)_direction;
			if (s.charAt(0) == '-')
				n = -p.getVariable(s.substring(1));
			else
				n = p.getVariable(s.substring(1));
		}
		
		if (n < 0)
			p.getGrille().getRobot().tourner(Direction.Gauche);
		else if (n > 0)
			p.getGrille().getRobot().tourner(Direction.Droite);
		else
			p.getGrille().getRobot().tourner(Direction.Avant);
		
		return true;
	}

}
