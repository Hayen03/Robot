package hayen.robot.util;

import hayen.robot.programme.Programme;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

public class Util {
	
	public static boolean isDigit(String str){
		for (char c : str.toCharArray()) if (!Character.isDigit(c)) return false;
		return true;
	}
	
	public static boolean isAlphaNumeric(String str){
		for (char c : str.toCharArray()) if (!(Character.isDigit(c) || Character.isAlphabetic(c))) return false;
		return true;
	}
	
	public static <T> ArrayList<T> subArray(T[] original, int debut, int fin){
		ArrayList<T> retour = new ArrayList<T>();
		for (int i = 0; i < fin - 1; i++){
			retour.add(original[debut + i]);
		}
		return retour;
	}
	
	public static <T> boolean contains(T[] array, T obj){
		int i = 0;
		while (i < array.length){
			if (array[i].equals(obj))
				return true;
			i++;
		}
		return false;
	}
	
	public static <T> String Array2String(T[] a){
		String s = "";
		for (Object o : a)
			if (o != null)
				s += o + ", ";
			else
				s += "null, ";
		return s;
	}
	
	public static Object[] string2Operation(String op){
		Vector<Object> terme = new Vector<Object>();

		String mot = "";
		for (char c : op.toCharArray()){
			if (Character.isAlphabetic(c) || Character.isDigit(c))
				mot += c;
			else {
				if (!mot.equals("")){
					if (Util.isDigit(mot))
						terme.add(Integer.parseInt(mot));
					else
						terme.add(mot);
					mot = "";
				}
				if (c == '+' || c == '-' || c == '/' || c == '*' || c == '%' || c == '(' || c == ')')
					terme.add(c);
				else if (!(c == ' '))
					return null; // ERREUR TODO: rajouter une erreur
			}
		}
		if (!mot.equals("")){
			if (Util.isDigit(mot))
				terme.add(Integer.parseInt(mot));
			else
				terme.add(mot);
			mot = "";
		} 
		
		return terme.toArray();
	}
	
	public static int evaluer(String exp, Programme p){
		return evaluer(string2Operation(exp), p);
	}
	public static int evaluer(String exp, Hashtable<String, Integer> v){
		return evaluer(string2Operation(exp), v);
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

		/* 0. Vérifier si l'expression ne comporte qu'un seul terme, si oui, retourner la valeur de ce terme
		 * 1. Alléger l'expression (évaluer les parenthèses et remplacer les variables pour leur valeurs)
		 * TODO: 2.0 Effectuer les exposants
		 * 2.1 Effectuer les changement de signe (+/- multiplicatif)
		 * 2.2 Effectuer les multiplication/division/modulo de gauche à droite
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
							else if (((Character)terme[i+j]).equals(')')){ // fin d'une parenthèse
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
					i += j;
					int tmp = evaluer(b.toArray(), p);
					a.add(tmp); // on ajoute à a la valeur de l'expression entre parenthèse
					b.clear();

				}
				else // opérateur (+, -, *, /, %)
					a.add(t);
			}

			i++;
		}
		b.clear();

		// 2.1 (vérifiaction pour les +/- mutiplicatif)
		i = 0;
		while (i < a.size()){
			Object obj = a.get(i);
			if (obj.getClass().equals(Character.class)){
				if (((Character)obj).equals('-') || ((Character)obj).equals('+')){
					if (b.size() == 0 || !b.get(b.size()-1).getClass().equals(Integer.class)){
						// trouver le prochain nombre
						int j = i;
						Integer n = null;
						while (j < a.size() && n == null){
							if (a.get(j).getClass().equals(Integer.class))
								n = (Integer)a.get(j);
							j++;
						}
						a.set(j-1, (((Character)a.get(i)).equals('-')? -1 : 1)*n);
					}
					else
						b.add(obj);
				}
				else
					b.add(obj);
			}
			else
				b.add(obj);
			i++;
		}
		a = b;
		b = new Vector<Object>();
		// 2.2
		i = 0;
		while (i < a.size()){
			if (a.get(i).getClass().equals(Integer.class))
				b.add(a.get(i));

			else if (!(((Character)a.get(i)).equals('-') || ((Character)a.get(i)).equals('+'))){
				char c = (Character)a.get(i);
				int x = (Integer)b.remove(b.size()-1);
				int y = (Integer)a.get(i+1);
				switch (c){
				case '*':
					b.add(x*y);
					break;
				case '/':
					b.add(x/y);
					break;
				case '%':
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
			case '-':
				b.add(x-y);
				break;
			case '+':
				b.add(x+y);
				break;
			}
			b.addAll(a.subList(3, a.size()));
			a = b;
			b = new Vector<Object>();
		}

		return (Integer)a.get(0);
	}
	public static int evaluer(Object[] terme, Hashtable<String, Integer> v){
		Vector<Object> a = new Vector<Object>();
		Vector<Object> b = new Vector<Object>();

		if (terme.length == 1){
			if (terme[0].getClass().equals(String.class))
				return v.get((String)terme[0]);
			else
				return (Integer)terme[0];
		}

		/* 0. Vérifier si l'expression ne comporte qu'un seul terme, si oui, retourner la valeur de ce terme
		 * 1. Alléger l'expression (évaluer les parenthèses et remplacer les variables pour leur valeurs)
		 * TODO: 2.0 Effectuer les exposants
		 * 2.1 Effectuer les changement de signe (+/- multiplicatif)
		 * 2.2 Effectuer les multiplication/division/modulo de gauche à droite
		 * 3. Effectuer les additions/soustractions de gauche à droite
		 */

		// 1.
		int i = 0;
		while (i < terme.length){
			Object t = terme[i];

			if (t.getClass().equals(Integer.class)) // constante
				a.add(t);
			else if (t.getClass().equals(String.class)) // variable
				a.add(v.get((String)t));

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
							else if (((Character)terme[i+j]).equals(')')){ // fin d'une parenthèse
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
					i += j;
					int tmp = evaluer(b.toArray(), v);
					a.add(tmp); // on ajoute à a la valeur de l'expression entre parenthèse
					b.clear();

				}
				else // opérateur (+, -, *, /, %)
					a.add(t);
			}

			i++;
		}
		b.clear();

		// 2.1 (vérifiaction pour les +/- mutiplicatif)
		i = 0;
		while (i < a.size()){
			Object obj = a.get(i);
			if (obj.getClass().equals(Character.class)){
				if (((Character)obj).equals('-') || ((Character)obj).equals('+')){
					if (b.size() == 0 || !b.get(b.size()-1).getClass().equals(Integer.class)){
						// trouver le prochain nombre
						int j = i;
						Integer n = null;
						while (j < a.size() && n == null){
							if (a.get(j).getClass().equals(Integer.class))
								n = (Integer)a.get(j);
							j++;
						}
						a.set(j-1, (((Character)a.get(i)).equals('-')? -1 : 1)*n);
					}
					else
						b.add(obj);
				}
				else
					b.add(obj);
			}
			else
				b.add(obj);
			i++;
		}
		a = b;
		b = new Vector<Object>();
		// 2.2
		i = 0;
		while (i < a.size()){
			if (a.get(i).getClass().equals(Integer.class))
				b.add(a.get(i));

			else if (!(((Character)a.get(i)).equals('-') || ((Character)a.get(i)).equals('+'))){
				char c = (Character)a.get(i);
				int x = (Integer)b.remove(b.size()-1);
				int y = (Integer)a.get(i+1);
				switch (c){
				case '*':
					b.add(x*y);
					break;
				case '/':
					b.add(x/y);
					break;
				case '%':
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
			case '-':
				b.add(x-y);
				break;
			case '+':
				b.add(x+y);
				break;
			}
			b.addAll(a.subList(3, a.size()));
			a = b;
			b = new Vector<Object>();
		}

		return (Integer)a.get(0);
	}
	public static boolean comparerExpression(int a, int b, char op){
		switch (op){
		case '<':
			return (a < b);
		case '>':
			return (a > b);
		case '=':
			return (a == b);
		}
		return false;
	}
}
