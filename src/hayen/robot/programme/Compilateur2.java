package hayen.robot.programme;

import hayen.robot.programme.instruction.Afficher;
import hayen.robot.programme.instruction.Assigner;
import hayen.robot.programme.instruction.Avancer;
import hayen.robot.programme.instruction.Condition;
import hayen.robot.programme.instruction.Fin;
import hayen.robot.programme.instruction.Instruction;
import hayen.robot.programme.instruction.Placer;
import hayen.robot.programme.instruction.Tourner;
import hayen.robot.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class Compilateur2 {

	public static final String version = "0.5";

	// liste de mots réservés
	private static final String[] motReserve = new String[10];
	private static final String motDeclaration = ajouterMotReserve("var");
	private static final String motCondition = ajouterMotReserve("si");
	private static final String motSinon = ajouterMotReserve("sinon");
	private static final String motFin = ajouterMotReserve("fin");
	private static final String motAfficher = ajouterMotReserve("afficher");
	private static final String motTourner = ajouterMotReserve("tourner");
	private static final String motDeplacer = ajouterMotReserve("avancer");
	private static final String motBoucle = ajouterMotReserve("tantque");
	private static final String motPlacer = ajouterMotReserve("placer");
	private static final String motEnlever = ajouterMotReserve("enlever");

	public static final String motAssignation = ":";
	public static final String motCommentaire = "#";
	public static final String motCaractere = ".";

	public static final String operateurAdd = "+";
	public static final String operateurMin = "-";
	public static final String operateurMul = "*";
	public static final String operateurDiv = "/";
	public static final String operateurMod = "%";

	public static final String comparaisonGrand = ">";
	public static final String comparaisonPetit = "<";
	public static final String comparaisonEgal = "=";

	public static final String parentheseIn = "(";
	public static final String parentheseOut = ")";
	public static final String crochetIn = "[";
	public static final String crochetOut = "]";

	private static final String[] operateurs = {motAssignation, motCommentaire, motCaractere, operateurAdd, operateurMin, operateurMul, operateurDiv, operateurMod, comparaisonGrand, comparaisonPetit, comparaisonEgal, parentheseIn, parentheseOut, crochetIn, crochetOut};

	//	private static final String exension = ".pr";

	private static final String[] variablesPreEnregistre = {"hauteur", "largeur", "posx", "posy", "gauche", "droite", "orientation", "nord", "est", "ouest", "sud"};

	private static int _ln = 0;

	/** compile un programme à partir d'un fichier .pr
	 * @param adresse : l'adresse du fichier à compiler
	 * @return le programme compilé
	 * @throws IOException
	 * @throws FichierIncorrectException
	 * @throws OperationInvalideException 
	 */
	public static Instruction[] compileFichier(String adresse) {
		// TODO: à refaire parce que je continue de briser des trucs
		return null;
	}

	/**
	 * enregistre les instructions d'un programme dans un fichier
	 * @param adresse : l'adresse du fichier de destination
	 * @param instructions : le bloc d'instructions à enregistrer
	 * @throws IOException
	 */
	public static void compileVersFichier(String adresse, Instruction[] instructions) {
		// TODO: same thing
	}

	/**
	 * compile une liste de String représentant des instructions afin de former un programme executable
	 * @param programme : les instructions qui doivent être compilé
	 * @return le programme compilé
	 * @throws OperationInvalideException 
	 */
	public static Instruction[] compile(ArrayList<String> programme) throws OperationInvalideException{
		return compile(programme.toArray(new String[programme.size()]));
	}
	/**
	 * compile une liste de String représentant des instructions afin de former un programme executable
	 * @param programme : les instructions qui doivent être compilé
	 * @return le programme compilé
	 * @throws OperationInvalideException 
	 */
	public static Instruction[] compile(String programme) throws OperationInvalideException {
		return compile(programme.split("\n"));
	}

	/**
	 * compile une liste de String représentant des instructions afin de former un programme executable
	 * @param typeBloc : Le type de bloc qui doit être compilé. Les constantes de la classe Compilateur blocMain, blocSi, blocSinon et blocBoucle sont recommendé
	 * @param preVar : Les variables déclarés dans le bloc qui contenait celui qui va être compilé
	 * @param texte : les instructions qui doivent être compilé
	 * @param taille : la taille du programme (utilisé pour identifié les erreurs)
	 * @return le programme compilé
	 * @throws CaractereInvalideException 
	 */
	private static Instruction[] compile(String[] texte) throws OperationInvalideException{
		Vector<Instruction> instructions = new Vector<Instruction>();

		Vector<Vector<String>> var = new Vector<Vector<String>>();
		var.add(new Vector<String>());
		for (int i = 0; i < variablesPreEnregistre.length; i++) // ajouter les variables déjà existentes
			var.get(0).add(variablesPreEnregistre[i]);

		final int Main = -1, Condition = -2, Sinon = -3; // >= 0 pour une boucle (numéro de la ligne)
		Vector<Integer> typeBloc = new Vector<Integer>();
		typeBloc.add(Main);

		_ln = 0; // le numero de la ligne
		int x = 0; // le nombre de ligne sauté
		while (_ln < texte.length){

			// séparer la ligne en mot
			String[] inst = separe(texte[_ln]);
			if (inst == null)
				throw new CaractereInvalideException("Erreur, caractère invalide à la ligne " + (_ln+1));

			if (inst.length == 0){ // on ignore la ligne si aucune instruction n'est donnée
				x++;
				_ln++;
			}
			else {
				String premierMot = inst[0];
				/* ---------------------------------------------------- AFFICHER ----------------------------------------------- */
				if (premierMot.equals(motAfficher)){ // TODO: Modifier por qu'il soit possible d'afficher le résultat d'opération
					String[] param = Util.subArray(inst, 1, inst.length).toArray(new String[inst.length - 1]);
					for (int j = 1; j < inst.length; j++){
						// une variable serait ce qui n'est pas composé seulement de chiffre, et on ne veut pas afficher de mot réservé
						// et je ne permet malheureusement pas encore les expression lors de l'affichage (print(3 + 5))
						String m = inst[j];
						if (!Util.isDigit(m)){ // test pour s'assurer que le mot est correcte
							if (isMotReserve(m)) 
								throw new OperationInvalideException("ERREUR: Il est impossible d'afficher un mot reservé\n" + "ln." + (_ln+1));
							else if (!(Util.isAlphaNumeric(m) || motCaractere.equals(m))) 
								throw new OperationInvalideException("ERREUR: Caractère invalide\n" + "ln." + (_ln+1));
							else if (!(estDeclarer(var, m) || motCaractere.equals(m))) 
								throw new OperationInvalideException("ERREUR: variable non déclaré\n" + "ln." + (_ln+1));
							// si toutes ces étapes sont passé, le terme est correcte
						}
						param[j-1] = inst[j];
					}
					Instruction toAdd = new Afficher(param);
					instructions.add(toAdd);
				}

				/* ---------------------------------------------------- DECLARATION ----------------------------------------------- */
				else if (premierMot.equals(motDeclaration)){
					/*
					 * Une variable doit avoir un nom alpha numérique et respecter la syntaxe: var <nom>
					 * (pas d'assignation) + (plusieurs déclaration en même temps)
					 */
					if (inst.length >= 2){
						Vector<String> params = new Vector<String>();
						for (int j = 1; j < inst.length; j++){
							// envoyer une erreur si le nom est invalide
							if ( !(Character.isAlphabetic(inst[j].charAt(0)) && Util.isAlphaNumeric(inst[j])) ) 
								throw new OperationInvalideException("ERREUR: nom de variable invalide\n" + "ln." + (_ln+1));
							// envoyer une erreur si l'identificateur existe déjà
							else if (estDeclarer(var, inst[j])) 
								throw new OperationInvalideException("ERREUR: la variable <" + inst[j] + "> a déjà été déclarée\n" + "ln." + (_ln+1));
							else {
								var.get(var.size()-1).add(inst[j]);
								params.add(inst[j]);
							}
						}
						Instruction toAdd = new Assigner(params.toArray(new String[params.size()]), null);
						instructions.add(toAdd);
					}
					else 
						throw new OperationInvalideException("ERREUR: expression invalide (une déclaration doit être fait sous la forme: var <nom>)\n" + "ln." + (_ln+1));
				}

				/* ----------------------------------------------- CONDITION & BOUCLE ------------------------------------------- */
				else if (premierMot.equals(motCondition) || premierMot.equals(motBoucle)){
					/*
					 * 1.1- Compter le nombre de terme suivant l'opérateur
					 * 1.2- S'il est égal à 0, retourner une erreur
					 * 2- déterminer les opérateurs de comparaison et séparer les opérations à vérifier
					 * 3- vérifier les opérations
					 * 4- rassembler le tout, faire l'opération
					 * 5.1- générer le nouveau vecteur de variable
					 * 5.2- changer le type de bloc
					 */

					// 1
					if (inst.length == 1)
						throw new OperationInvalideException("ERREUR: instruction invalide\nln." + (_ln+1)); // ERREUR 

					// 2
					Object[] op1 = null, op2 = null;
					Character op = null;
					{
						Vector<Object> tmp1 = new Vector<Object>(), tmp2 = new Vector<Object>();
						for (int i = 1; i < inst.length; i++){ // op1 & op
							String s = inst[i];
							if ("<>&|!=".contains(s)){
								op = s.charAt(0);
							}
							else if ("+-*/%".contains(s)){
								if (op == null)
									tmp1.add(s.charAt(0));
								else
									tmp2.add(s.charAt(0));
							}
							else if (Util.isDigit(s)){
								if (op == null)
									tmp1.add(Integer.parseInt(s));
								else
									tmp2.add(Integer.parseInt(s));
							}
							else if (!isMotReserve(s)){
								if (op == null)
									tmp1.add(s);
								else
									tmp2.add(s);
							}
							else 
								throw new OperationInvalideException("ERREUR: expression invalide (s)\nln." + (_ln+1));
						}
						op1 = tmp1.toArray();
						op2 = tmp2.toArray();
					}
					if (op1.length == 0 || op2.length == 0 || op == 0)
						throw new OperationInvalideException("ERREUR: instruction invalide\nln." + (_ln+1));

					// 3
					if (!(verifierOperation(op1) || verifierOperation(op2)))
						throw new OperationInvalideException("ERREUR: instruction Invalide\nln." + (_ln+1));

					// 4
					instructions.add(new Condition(op1, op, op2));

					// 5
					var.add(new Vector<String>());
					typeBloc.add(premierMot.equals(motCondition) ? Condition : _ln);

				}

				/* ---------------------------------------------------- SINON ----------------------------------------------- */
				else if (premierMot.equals(motSinon)){
					/*
					 * 1- vérifier si le type de bloc est condition
					 * 2- vérifier si rien ne suit l'opération
					 * 3- Pacter le tout
					 * 4- changer le vecteur de variable
					 * 5- changer le type de bloc
					 */

					if (!typeBloc.get(typeBloc.size()-1).equals(Condition) || inst.length > 1)
						throw new OperationInvalideException("ERREUR: Operation invalide\nln." + (_ln+1));
					else {
						instructions.add(new Fin(Fin.codeSinon));

						var.remove(var.size()-1);
						typeBloc.remove(typeBloc.size()-1);

						var.add(new Vector<String>());
						typeBloc.add(Sinon);
					}

				}

				/* ---------------------------------------------------- FIN ----------------------------------------------- */
				else if (premierMot.equals(motFin)){
					/*
					 * 1- vérifier le type de bloc (condition, boucle, sinon)
					 * 2- pacter le tout
					 * 3- enlever une couche du vecteur de variable
					 * 4- changer le type de bloc
					 */

					if (typeBloc.get(typeBloc.size()-1).equals(Main))
						throw new OperationInvalideException("ERREUR: Operation invalide\nln." + (_ln+1));
					else { 
						if (typeBloc.get(typeBloc.size()-1).intValue() >= 0)
							instructions.add(new Fin(typeBloc.get(typeBloc.size()-1)-x));
						else
							instructions.add(new Fin(Fin.codeFinCondition));
						var.remove(var.size()-1);
						typeBloc.remove(typeBloc.size()-1);
					}

				}

				/* ---------------------------------------------------- AVANCER/PLACER/ENLEVER ----------------------------------------------- */
				// ils sont tellement semblable que l'on peut se permettre de vérifier les trois d'un seul coup
				else if (premierMot.equals(motDeplacer) || premierMot.equals(motPlacer) || premierMot.equals(motEnlever)){
					// La seule conttrainte est qu'il ne doit y avoir rien qui suit l'operation
					if (inst.length > 1)
						throw new OperationInvalideException("ERREUR: Operation invalide\nln." + (_ln+1));
					else { // ici on fait le tri de quoi est quoi
						if (premierMot.equals(motDeplacer))
							instructions.add(new Avancer());
						else if (premierMot.equals(motPlacer))
							instructions.add(new Placer(true));
						else
							instructions.add(new Placer(false));
					}
				}

				/* ---------------------------------------------------- TOURNER ----------------------------------------------- */
				else if (premierMot.equals(motTourner)){
					// ce qui suit l'opération doit être un nombre ou une variable (un seul(pour le moment))
					if (inst.length != 3 && inst.length != 2) // la limite est à 3 au cas où c'est un nombre négatif (EX: -1 --> le coupeur de mot va le séparer ainsi : -, 1, donc deux mots)
						throw new OperationInvalideException("ERREUR: Operation invalide\nln." + (_ln+1) + " (" + Util.Array2String(inst) + ")");
					// ici, on décide de quel bord tourner
					else {
						String s;
						Tourner toAdd;

						/* CE QUI CE PASSE ICI
						 * dans les deux bloc suivant, la même étape se passe: il y a l'habituelle vérification de si la valeur entrée est soit
						 * 	a) un nombre
						 * 	b) une variable valide
						 * 	c) une variable invalide
						 * et agit en conséquence. La différence est que dans le premier bloc, il y a possibilité d'une valeur négative parce que trois terme sont entré (op - val)
						 * et il faut vérifier que le deuxième terme soit bien le signe - et agir en conséquence
						 */

						if (inst.length == 3){
							if (!inst[1].equals("-"))
								throw new OperationInvalideException("ERREUR: Operation invalide\nln." + (_ln+1));
							else {
								s = inst[2];
								if (Util.isDigit(s))
									toAdd = new Tourner(-Integer.parseInt(s));
								else if (!var.contains(s))
									throw new OperationInvalideException("ERREUR: Variable non declar�\nln." + (_ln+1));
								else 
									toAdd = new Tourner("-" + s);
							}
						}

						else {
							s = inst[1];
							if (Util.isDigit(s))
								toAdd = new Tourner(Integer.parseInt(s));
							else if (!estDeclarer(var, s))
								throw new OperationInvalideException("ERREUR: Variable non declar�\nln." + (_ln+1));
							else 
								toAdd = new Tourner(s);
						}

						instructions.add(toAdd);
					}
				}

				/* ---------------------------------------------------- ASSIGNATION ----------------------------------------------- */
				else { 
					// on peut assigner plusieurs variable en même temps
					// syntaxe: <identificateurs> : <expression>

					Vector<String> ids = new Vector<String>(); // les identificateurs
					Object[] terme; // les termes de l'expression (String: variable, int: nombre, char: operateur)
					int j = 0;

					// trouver les identificateurs
					while (j < inst.length){
						if (inst[j].equals(motAssignation)){
							// si le premier terme est le signe d'assignation, il y a un problème
							if (j == 0) 
								throw new OperationInvalideException("ERREUR: aucun identificateur trouvé pour l'assignation\nln." + (_ln+1));
							j++;
							break; // sinon, on a terminer de chercher les identificateurs
						}

						else if (!Util.isAlphaNumeric(inst[j])) 
							throw new OperationInvalideException("ERREUR: identificateur invalide (" + inst[j] + ")\nln." + (_ln+1));

						// l'identificateur n'existe pas
						else if (!estDeclarer(var, inst[j])) 
							throw new OperationInvalideException("ERREUR: l'identificateur indiqué n'a pas été déclaré\nln." + (_ln+1));

						// les variables pre enregistrée ne peuvent être modifié par l'utilisateur
						else if (Util.contains(variablesPreEnregistre, inst[j]))
							throw new OperationInvalideException("ERREUR: la variable " + inst[j] + " ne peut pas être modifier\nln." + (_ln+1));

						else 
							ids.add(inst[j]);
						j++;
					}

					// Trouver les termes
					terme = Util.string2Operation(Util.subArray(inst, j, inst.length).toArray(new String[inst.length-j]));

					// s'il n'y a plus de mots, il y a une erreur (EX: bla : )
					if (terme == null || terme.length == 0) 
						throw new OperationInvalideException("ERREUR: aucune expression pour l'assignation\nln." + (_ln+1));

					// vérifier l'ordre des termes et l'existence des variables
					else if (!verifierOperation(terme, var)){
						throw new OperationInvalideException("ERREUR: operation invalide\nln." + (_ln+1));
					}

					Instruction toAdd = new Assigner(ids.toArray(new String[ids.size()]), terme);
					instructions.add(toAdd);

				}

				_ln++;
			}
		}

		return instructions.toArray(new Instruction[instructions.size()]);
	}

	/**
	 * Separe les mots d'une instruction et vérifie qu'il n'y a pas de caractère invalide
	 * @param texte : l'instruction à séparer
	 * @param ligne : la ligne à laquelle se trouve l'instruction
	 * @return Un tableau de String correspondant au mot qui construisent l'instruction
	 * @throws CaractereInvalideException
	 */
	public static String[] separe(String texte) throws CaractereInvalideException{
		Vector<String> mots = new Vector<String>();

		int i = 0;
		String c;
		while (i < texte.length()){
			c = "" + texte.charAt(i);

			// si c marque le début d'un commentaire, quitte la boucle
			if (c.equals(motCommentaire))
				break;

			// sinon, si c est alphanumérique, cherche le mot au complet
			else if (Util.isAlphaNumeric(c)){
				String mot = "";
				//				System.out.println("In boucle");
				while (i < texte.length()){
					c = "" + texte.charAt(i);
					if (Util.isAlphaNumeric(c)){
						mot += c;
						i++;
					}
					else {
						break;
					}
				}
				mots.add(mot);
			}
			else {
				// si c'est un espace ou un opérateur existant
				if (Util.contains(operateurs, c))
					mots.add(c);
				else if (!(c.equals(" ") || c.equals("\t")))
					throw new CaractereInvalideException("Le caractère " + c + " est invalide.");
				i++;
			}

		}

		return mots.toArray(new String[mots.size()]);
	}

	/**
	 * indique si le mot passé en paramètre figure dans la liste des mots réservés
	 * @param m : le mot à vérifier
	 * @return vrai si m est reservé, faux autrement
	 */
	public static boolean isMotReserve(String m){
		return Util.contains(motReserve, m);
	}

	/**
	 * ajoute un mot à la liste de mot reservés
	 * @param mot : le mot à ajouter
	 * @return le mot
	 */
	private static String ajouterMotReserve(String mot){
		int i = 0;
		while (motReserve[i] != null) i++;
		motReserve[i] = mot;
		return mot;
	}

	/**
	 * Vérifie que la variable id a bel et bien été déclaré
	 * @param var : les variables qui ont été déclarer
	 * @param id : l'identificateur de la variale à vérifier
	 * @return true si la variable a été déclaré, sinon false
	 */
	private static boolean estDeclarer(Vector<Vector<String>> var, String id){
		int j = var.size()-1;
		while (j >= 0){
			if (var.get(j).contains(id))
				return true;
			j--;
		}
		return false;
	}

	/**
	 * Vérifie qu'une expression arithmétique est correctement composé. Ne vérifie pas les variables
	 * @param terme : l'expression à vérifier
	 * @return true si l'expression est correct, sinon false
	 */
	public static boolean verifierOperation(Object[] terme){
		// une opération est valide si elle ne finit pas par un opérateur arithmétique, que les opérateurs *, / et % ne sont pas doublé,
		// que les valeurs (constante/variable) ne sont pas doublé et que toute les parenthèse sont fermé

		if (terme[terme.length-1].getClass().equals(Character.class) && !terme[terme.length-1].toString().equals(parentheseOut)){
			return false;
		}

		boolean op = true;
		int i = 0;
		while (i < terme.length){
			if (op == terme[i].getClass().equals(Character.class)){
				String c = terme[i].toString();

				if (!op){ // double valeur --> Erreur automatique
					return false;
				}
				else if (c.equals(parentheseIn)){ // dans le cas d'une parenthèse
					// cas plus compliquer: il faut aller chercher la fin de la parenthèse et vérifier ce qu'il y a entre les deux
					int j = 1;
					int inc = 0;
					Integer indice = null; // l'indice de la fin de parenthèse
					while (j+i < terme.length && indice == null){
						String s = terme[i+j].toString();
						if (s.equals(parentheseIn)){
							inc++;
						}
						else if (s.equals(parentheseOut)){
							if (inc == 0){
								indice = i+j;
							}
							else{
								inc--;
							}
						}
						j++;
					}
					if (indice == null) // il manque la fin de parenthèse
						return false;
					else {
						if (!verifierOperation(Util.subArray(terme, i+1, indice).toArray())) // l'expression dans la parenthèse n'est pas valide
							return false;
						i = indice;
						op = false;
					}
				}
				else if (!(c.equals(operateurAdd) || c.equals(operateurMin))){ // double opérateur autre aue + et -
					return false;
				}
			}
			else
				op = !op;
			i++;
		}
		return true;
	}
	/**
	 * Vérifie qu'une expression arithmétique est correctement composé. Vérifie les variables
	 * @param terme : l'expression à vérifier
	 * @param var : les variables déclarer
	 * @return true si l'expression est correct, sinon false
	 * @throws OperationInvalideException si une variable n'était pas déclarer
	 */
	public static boolean verifierOperation(Object[] terme, Vector<Vector<String>> var) throws OperationInvalideException{
		// une opération est valide si elle ne finit pas par un opérateur arithmétique, que les opérateurs *, / et % ne sont pas doublé,
		// que les valeurs (constante/variable) ne sont pas doublé et que toute les parenthèse sont fermé
		if (terme[terme.length-1].getClass().equals(Character.class) && !terme[terme.length-1].toString().equals(parentheseOut)){
			return false;
		}

		boolean op = true;
		int i = 0;
		while (i < terme.length){
			if (op == terme[i].getClass().equals(Character.class)){
				String c = terme[i].toString();

				if (!op){
					return false;
				}
				else if (c.equals(parentheseIn)){ // dans le cas d'une parenthèse
					// cas plus compliquer: il faut aller chercher la fin de la parenthèse et vérifier ce qu'il y a entre les deux
					int j = 1;
					int inc = 0;
					Integer indice = null; // l'indice de la fin de parenthèse
					while (j+i < terme.length && indice == null){
						String s = terme[i+j].toString();
						if (s.equals(parentheseIn)){
							inc++;
						}
						else if (s.equals(parentheseOut)){
							if (inc == 0){
								indice = i+j;
							}
							else{
								inc--;
							}
						}
						j++;
					}
					if (indice == null) // il manque la fin de parenthèse
						return false;
					else {
						if (!verifierOperation(Util.subArray(terme, i+1, indice).toArray()))
							return false;
						i = indice;
						op = false;
					}
				}
				else if (!(c.equals(operateurAdd) || c.equals(operateurMin))){
					return false;
				}
			}
			else {
				// vérification des variables
				if (terme[i].getClass().equals(String.class)){
					String v = (String)terme[i];
					if (!estDeclarer(var, v))
						throw new OperationInvalideException("ERREUR: la variable" + v + "n'a pas été déclaré\nln." + (_ln+1));; // ERREUR 2
				}
				op = !op;
			}

			i++;
		}
		return true;
	}
}
