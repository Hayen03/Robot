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
//		System.out.print("<COMPARE ");
		boolean b = comparerExpression(p, _expression);
//		System.out.print("<" + b + ">");
		if (b) return _blocSi.setParent(p).executer();
		else if (_blocSinon != null) return _blocSinon.setParent(p).executer();
		else return true;
	}
	
	public static boolean comparerExpression(Bloc p, Object... termes){
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
		return "Condition: (" + _expression + ")"; 
	}

}
