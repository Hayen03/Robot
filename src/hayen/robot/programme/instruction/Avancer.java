package hayen.robot.programme.instruction;

import hayen.robot.programme.Bloc;

public class Avancer extends Instruction {

	private static final long serialVersionUID = 6462835422815333407L;

	public Avancer(){
		super();
	}
	
	@Override
	public boolean executer(Object... params) {
		Bloc p = (Bloc)params[0];
		
		return true;
	}

}
