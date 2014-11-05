package hayen.robot.programme.instruction;

import hayen.robot.programme.Programme;

public class Fin extends Instruction {
	
	private int _indentation;
	
	public Fin(int ind){
		_indentation = ind;
	}
	
	public boolean run(Programme p){
		return p.getIndentation() == _indentation;
	}
	
	public String toString(){
		return "End: (" + _indentation + ")";
	}
	
}
