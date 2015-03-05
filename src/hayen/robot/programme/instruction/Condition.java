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
		
		boolean v = Util.comparerExpression(a, b, _op);
		if (!v){ // si v est faux, on va au fin correspondant
			Instruction i = null;
			int ln = p.getLigne();
			int n = 0;
			int deltaLn = 1;
			while (ln+deltaLn < p.longueur()){
				i = p.getInstructionA(ln + deltaLn);
				if (i.getClass().equals(Condition.class)){
					n++;
				}
				else if (i.getClass().equals(Fin.class)){
					if (n == 0)
						break;
					else
						n--;
				}
				deltaLn++;
			}
			p.setLigne(ln + deltaLn);
		}
		else {
			// sinon, on ne fait rien et le programme passe à la ligne suivante
			p.incremente();
		}
	}
	
	@Override
	public String toString(){
		return "Condition: (" + Util.Array2String(_op1) + " " + _op + " " + Util.Array2String(_op2) + ")"; 
	}

}
