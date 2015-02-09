package hayen.robot.programme.instruction;

import hayen.robot.programme.Bloc;
import hayen.robot.programme.Programme;

public class Assigner extends Instruction {
	
	private static final long serialVersionUID = -1252954201912565150L;
	
	public static final char opAdd = '+';
	public static final char opMin = '-';
	public static final char opMul = '*';
	public static final char opDiv = '/';
	public static final char opMod = '%';
	
	private String[] _ids;
	private Object[] _termes;
	
	public Assigner(String[] identificateurs, Object[] termes){
		_ids = identificateurs;
		_termes = termes;
	}

	@Override
	public boolean executer(Object... params){
		Bloc b = (Bloc)params[0];
//		Programme p = b.getProgramme();
		
		// si il n'y a qu'un terme, on assigne sa valeur aux variables appropri�s
		if (_termes.length == 1){
			Integer v;
			if (_termes[0].getClass().equals(String.class)) v = b.getVariable((String)_termes[0]);
			else v = (Integer)_termes[0];
			for (String id : _ids) b.assigner(id, v);
		}
		else{
			int[] nouvelleValeur = new int[_ids.length];
			
			int[] stack = new int[2];
			for (int i = 0; i < _ids.length; i++){
				String id = _ids[i];
				stack[0] = stack[1] = b.getVariable(id);
				for (Object t : _termes){
					if (!t.getClass().equals(Character.class)){ // si c'est une valeur
						int v;
						if (t.getClass().equals(String.class)) v = b.getVariable((String)t);
						else v = (Integer)t;
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
						
						stack = push(resultat, stack);
					}
				}
				nouvelleValeur[i] = stack[1];
			}
			for (int i = 0; i < _ids.length; i++) 
				b.assigner(_ids[i], nouvelleValeur[i]);
		}
		
		return true;
	}
	
	private int[] push(int v, int[] s){
		s[0] = s[1];
		s[1] = v;
		return s;
	}
	
	@Override
	public String toString(){
		String s = "Assign: ";
		for (String n : _ids) s += n + ", ";
		return s;
	}
	
	public static int evaluer(Object[] terme, Programme p){
		int resultat;
		
		
		
		return 0;
	}

}
