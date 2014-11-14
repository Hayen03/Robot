package hayen.robot.programme.instruction;

//import java.io.DataOutputStream;
//import java.io.IOException;

import hayen.robot.programme.Bloc;

public class Sinon extends Instruction {
	
	private static final long serialVersionUID = -856650277948777509L;
	
	private int _indentation;
	
	public Sinon(int ind){
		_indentation = ind;
	}
	
	@Override
	public boolean executer(Object... params) {
		return ((Bloc)params[0]).getIndentation() == _indentation;
	}

	public int getIndentation(){ return _indentation; }
	
	@Override
	public String toString(){
		return "Else: (" + _indentation + ")";
	}

}
