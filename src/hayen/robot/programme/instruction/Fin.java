package hayen.robot.programme.instruction;

import hayen.robot.programme.Application;

public class Fin extends Instruction {

	public static final int codeSinon = -2;
	public static final int codeFinCondition = -1;
	
	private int _code;
	
	public Fin(int code){
		_code = code;
	}
	
	@Override
	public void executer(Application app) {
		// TODO Auto-generated method stub
	}

}
