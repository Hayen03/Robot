package hayen.robot.programme.instruction;

import hayen.robot.programme.Programme;

public class Boucle extends Instruction {

	private int _ln;
	
	public Boucle(int ln){
		_ln = ln-1;
	}
	
	@Override
	public boolean run(Programme p) {
		p.setLigne(_ln);
		return true;
	}
	
	public String toString(){
		return "Boucle: go to " + _ln;
	}

}
