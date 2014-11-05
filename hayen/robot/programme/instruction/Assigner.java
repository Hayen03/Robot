package hayen.robot.programme.instruction;

import hayen.robot.programme.Programme;

public class Assigner extends Instruction {
	
	private String[] _ids;
	private Object[] _termes;
	
	public Assigner(String[] identificateurs, Object[] termes){
		_ids = identificateurs;
		_termes = termes;
	}

	@Override
	public boolean run(Programme p) {
		
		if (_termes.length == 1){
			Integer v;
			if (_termes[0].getClass().equals(String.class)) v = p.getVariable((String)_termes[0]);
			else v = (Integer)_termes[0];
			for (String id : _ids) p.assigner(id, v);
		}
		else{
			int[] stack = new int[2];
			for (String id : _ids){
				stack[0] = stack[1] = p.getVariable(id);
				for (Object t : _termes){
					if (!t.getClass().equals(Character.class)){ // si c'est une valeur
						int v;
						if (t.getClass().equals(String.class)) v = p.getVariable((String)t);
						else v = (Integer)t;
//						stack[0] = stack[1];
//						stack[1] = v;
						stack = push(v, stack);
					}
					else{ // si c'est un operateur
						int resultat = 0;
						char op = (Character)t;
						switch(op){
						case '+':
							resultat = stack[0] + stack[1];
							break;
						case '-':
							resultat = stack[0] - stack[1];
							break;
						case '*':
							resultat = stack[0] * stack[1];
							break;
						case '/':
							resultat = stack[0] / stack[1];
							break;
						case '%':
							resultat = stack[0] % stack[1];
							break;
						}
						
//						stack[0] = stack[1];
//						stack[1] = resultat;
						stack = push(resultat, stack);
					}
				}
				p.assigner(id, stack[1]);
			}
		}
		
		return true;
	}
	
	private int[] push(int v, int[] s){
		s[0] = s[1];
		s[1] = v;
		return s;
	}
	
	public String toString(){
		String s = "Assign: ";
		for (String n : _ids) s += n + ", ";
		return s;
	}

}
