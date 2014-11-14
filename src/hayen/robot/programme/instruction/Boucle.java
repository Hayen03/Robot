package hayen.robot.programme.instruction;

import hayen.robot.programme.Bloc;

public class Boucle extends Instruction {

	private static final long serialVersionUID = -2859171750583912735L;
	
	private int _ln;
	
	public Boucle(int ln){
		_ln = ln-1;
	}
	
	@Override
	public boolean executer(Object... params){
		Bloc p = (Bloc)params[0];
		p.setLigne(_ln);
		return true;
	}
	
	@Override
	public String toString(){
		return "Boucle: go to " + _ln;
	}

}
