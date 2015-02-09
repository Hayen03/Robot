package hayen.robot.programme.instruction;

import hayen.robot.programme.Bloc;
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
		Bloc b = (Bloc)params[0];
//		System.out.print("<COMPARE ");
		boolean vrai = comparerExpression(b, _expression);
//		System.out.print("<" + b + ">");
		if (vrai) 
			return _blocSi.setParent(b).executer();
		else if (_blocSinon != null) 
			return _blocSinon.setParent(b).executer();
		else
			return true;
	}
	
	public static boolean comparerExpression(Bloc p, Object... termes){
		
		if (termes.length == 1){
			int val;
			if (termes[0].getClass().equals(Integer.class))
				val = (Integer)termes[0];
			else
				val = p.getVariable((String)termes[0]);
			return val > 0;
		}
		
		boolean retour = true;
		int t1, t2, i = 0;
		char op;
		Object buffer;
		
		buffer = termes[i++];
		if (buffer.getClass().equals(Integer.class))
			t1 = (Integer)buffer;
		else
			t1 = p.getVariable((String)buffer);
		
		do{
			op = (Character)termes[i++];
			
			buffer = termes[i++];
			if (buffer.getClass().equals(Integer.class))
				t2 = (Integer)buffer;
			else
				t2 = p.getVariable((String)buffer);
			
			switch (op){
			case '<':
				retour = retour && (t1 < t2);
				break;
			case '>':
				retour = retour && (t1 > t2);
				break;
			case '=':
				retour = retour && (t1 == t2);
				break;
			}
			
			t1 = t2;
		} while (i < termes.length);
		
		return retour;
	}
	
	@Override
	public String toString(){
		return "Condition: (" + Util.Array2String(_op1) + " " + _op + " " + Util.Array2String(_op2) + ")"; 
	}

}
