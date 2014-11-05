package hayen.robot.programme.instruction;

import hayen.robot.programme.Programme;

public class Sinon extends Instruction {

	private int _indentation;
	
	public Sinon(int ind){
		_indentation = ind;
	}
	
	@Override
	public boolean run(Programme p) {
		return p.getIndentation() == _indentation;
	}

	public int getIndentation(){ return _indentation; }
	
	public String toString(){
		return "Else: (" + _indentation + ")";
	}
	
}
