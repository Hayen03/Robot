package hayen.robot.programme.instruction;

import hayen.robot.programme.Programme;

public abstract class Instruction {
	public Instruction(){
		
	}
	public abstract boolean run(Programme p);
}
