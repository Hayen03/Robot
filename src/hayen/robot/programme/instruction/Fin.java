package hayen.robot.programme.instruction;

import hayen.robot.programme.Bloc;

public class Fin extends Instruction {
	
	private static final long serialVersionUID = 358290877192354208L;
	
	private int _indentation;
	
	public Fin(int ind){
		_indentation = ind;
	}
	
	@Override
	public boolean executer(Object... params){
		Bloc p = (Bloc)params[0];
		return p.getIndentation() == _indentation;
	}
	
	@Override
	public String toString(){
		return "End: (" + _indentation + ")";
	}

}
