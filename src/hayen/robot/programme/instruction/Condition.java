package hayen.robot.programme.instruction;

import hayen.robot.programme.Application;
import hayen.robot.programme.Programme;
import hayen.robot.util.Util;

public class Condition extends Instruction {
	
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
	public void executer(Application app){
		Programme p = app.getProgramme();
		int a = Util.evaluer(_op1, p);
		int b = Util.evaluer(_op2, p);
		
//		return Util.comparerExpression(a, b, _op); // TODO: changement de ligne du programme
	}
	
	@Override
	public String toString(){
		return "Condition: (" + Util.Array2String(_op1) + " " + _op + " " + Util.Array2String(_op2) + ")"; 
	}

}
