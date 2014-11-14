package hayen.robot.programme.instruction;

import hayen.robot.programme.Bloc;

public class Condition extends Instruction {
	
	private static final long serialVersionUID = -3241751628795007765L;
	
	private Object[] _expression;
	private int _indentation;
	
	public Condition(int ind, Object... params){
		_expression = params;
		_indentation = ind;
	}

	@Override
	public boolean executer(Object... params){
		Bloc p = (Bloc)params[0];
		boolean retour = true;
		int tmp1 = 0;
		
		Object t1;
		int i = 0;
		while (i < _expression.length){
			t1 = _expression[i];
			if (!t1.getClass().equals(Character.class)){ // si c'est une valeur
				if (t1.getClass().equals(String.class)) tmp1 = p.getVariable((String)t1);
				else tmp1 = (Integer)t1;
			}
			else { // si c'est un opérateur
				int tmp2 = 0;
				Object t2 = _expression[i+1];
				if (t2.getClass().equals(String.class)) tmp1 = p.getVariable((String)t2);
				else tmp2 = (Integer)t2;
				
				char op = (Character)t1;
				switch (op){
				case '<':
					retour = retour && tmp1 < tmp2;
					break;
				case '>':
					retour = retour && tmp1 > tmp2;
					break;
				case '=':
					retour = retour && tmp1 == tmp2;
					break;
				}
				
			}
			i++;
		}
		
		return retour;
	}
	
	public int getIndentation(){
		return _indentation;
	}
	
	@Override
	public String toString(){
		return "Condition: (" + _indentation + ")"; 
	}

}
