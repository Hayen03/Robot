package hayen.robot.programme.instruction;

import hayen.robot.programme.Bloc;

public class Condition extends Instruction {
	
	private static final long serialVersionUID = -3241751628795007765L;
	
	private Object[] _expression;
	private Bloc _blocSi;
	private Bloc _blocSinon;
	
	public Condition(Bloc blocSi, Bloc blocSinon,Object... params){
		_expression = params;
		_blocSi = blocSi;
		_blocSinon = blocSinon;
	}
	public Condition(Bloc blocSi, Object...params){
		_expression = params;
		_blocSi = blocSi;
		_blocSinon = null;
	}

	@Override
	public boolean executer(Object... params){
		Bloc p = (Bloc)params[0];
		if (comparerExpression(p, _expression)) return _blocSi.setParent(p).executer();
		else if (_blocSinon != null) return _blocSinon.setParent(p).executer();
		else return true;
	}
	
	public static boolean comparerExpression(Bloc p, Object... expression){
		boolean retour = true;
		int tmp1 = 0;
		
		Object t1;
		int i = 0;
		while (i < expression.length){
			t1 = expression[i];
			if (!t1.getClass().equals(Character.class)){ // si c'est une valeur
				if (t1.getClass().equals(String.class)) tmp1 = p.getVariable((String)t1);
				else tmp1 = (Integer)t1;
			}
			else { // si c'est un opérateur
				int tmp2 = 0;
				Object t2 = expression[i+1];
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
	
	@Override
	public String toString(){
		return "Condition: (" + _expression + ")"; 
	}

}
