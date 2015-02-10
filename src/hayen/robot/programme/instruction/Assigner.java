package hayen.robot.programme.instruction;

import java.util.Vector;

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
		Vector<Object> a = new Vector<Object>();
		Vector<Object> b = new Vector<Object>();
		
		if (terme.length == 1){
			if (terme[0].getClass().equals(String.class))
				return p.getVariable((String)terme[0]);
			else
				return (Integer)terme[0];
		}
		
		/*
		 * 1. Alléger l'expression (évaluer les parenthèses et remplacer les variables pour leur valeurs)
		 * 2. Effectuer les multiplication/division/modulo de gauche à droite
		 * 3. Effectuer les additions/soustractions de gauche à droite
		 */
		
		// 1.
		int i = 0;
		while (i < terme.length){
			Object t = terme[i];
			
			if (t.getClass().equals(Integer.class)) // constante
				a.add(t);
			else if (t.getClass().equals(String.class)) // variable
				a.add(p.getVariable((String)t));
			
			else if (t.getClass().equals(Character.class)){
				char c = (Character)t;
				if (c == '('){ // opérateur ou parenthèse
					
					int nb = 0; // compteur de parenthèse dans l'opération
					int j = 1;
					while (i+j < terme.length){
						if (terme[i+j].getClass().equals(Character.class)){
							if (((Character)terme[i+j]).equals('(')){ // parenthèse dans la parenthèse
								nb++;
								b.add(terme[i+j]);
							}
							if (((Character)terme[i+j]).equals(')')){ // fin d'une parenthèse
								if (nb == 0) // si c'est la fin de la parenthèse original, on quitte la boucle
									break;
								else { // sinon, on l'ajoute à b
									nb--;
									b.add(terme[i+j]);
								}
							}
							else // c'était surement un opérateur
								b.add(terme[i+j]);
						}
						else // variable ou constante
							b.add(terme[i+j]);
						j++;
						
					}
					
					a.add(evaluer(b.toArray(), p)); // on ajoute à a la valeur de l'expression entre parenthèse
					b.clear();
					
				}
				else // opérateur (+, -, *, /, %)
					a.add(t);
			}

			i++;
		}
		
		i = 0;
		while (i < a.size()){
			if (a.get(i).getClass().equals(Integer.class))
				b.add(a.get(i));
			
			else if (!(((Character)a.get(i)).equals(opMin) || ((Character)a.get(i)).equals(opAdd))){
				char c = (Character)a.get(i);
				int x = (Integer)b.remove(b.size()-1);
				int y = (Integer)a.get(i+1);
				switch (c){
				case opMul:
					b.add(x*y);
					break;
				case opDiv:
					b.add(x/y);
					break;
				case opMod:
					b.add(x%y);
					break;
				}
				i++;
			}
			else
				b.add(a.get(i));
				
			i++;
		}
		a = b;
		b = new Vector<Object>();
		
		// 3.
		while (a.size() > 1){
			int x = (Integer)a.get(0);
			char c = (Character)a.get(1);
			int y = (Integer)a.get(2);
			
			switch (c){
			case opMin:
				b.add(x-y);
				break;
			case opAdd:
				b.add(x+y);
				break;
			}
			b.addAll(a.subList(3, a.size()));
			a = b;
			b = new Vector<Object>();
		}
		
		return (Integer)a.get(0);
	}

}
