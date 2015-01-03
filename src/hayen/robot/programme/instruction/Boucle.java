package hayen.robot.programme.instruction;

import hayen.robot.programme.Bloc;

public class Boucle extends Instruction {

	private static final long serialVersionUID = -2859171750583912735L;
	
	private Object[] _expression;
	private Bloc _bloc;
	
	public Boucle(Bloc bloc, Object... expression){
		_expression = expression;
		_bloc = bloc;
	}
	
	@Override
	public boolean executer(Object... params){
		Bloc b = (Bloc)params[0];
		while (Condition.comparerExpression(b, _expression)) _bloc.setParent(b).executer();
		return true;
	}
	
	@Override
	public String toString(){
		return "Boucle: while " + _expression;
	}

}
