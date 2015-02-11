package hayen.robot.programme.instruction;

import hayen.robot.programme.Programme;
import hayen.robot.util.Util;

public class Condition extends Instruction {
	
	private static final long serialVersionUID = -3241751628795007765L;
	
	public static final char PlusGrandQue = '>';
	public static final char PlusPetitQue = '<';
	public static final char Egal = '=';
	
	private Object[] _op1;
	private Object[] _op2;
	private char _op;
	
	public Condition(Object[] op1, char op, Object[] op2){
		_op = op;
		_op1 = op1;
		_op2 = op2;
	}

	@Override
	public boolean executer(Object... params){
		Programme p = (Programme)params[0];
		int a = Assigner.evaluer(_op1, p);
		int b = Assigner.evaluer(_op2, p);
		
		return comparerExpression(a, b, _op);
	}
	
	public static boolean comparerExpression(int a, int b, char op){
			switch (op){
			case PlusPetitQue:
				return (a < b);
			case PlusGrandQue:
				return (a > b);
			case Egal:
				return (a == b);
			}
			return false;
	}
	
	@Override
	public String toString(){
		return "Condition: (" + Util.Array2String(_op1) + " " + _op + " " + Util.Array2String(_op2) + ")"; 
	}

}
