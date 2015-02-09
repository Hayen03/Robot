package hayen.robot.programme;

import hayen.robot.programme.instruction.Afficher;
import hayen.robot.programme.instruction.Assigner;
import hayen.robot.programme.instruction.Avancer;
import hayen.robot.programme.instruction.Boucle;
import hayen.robot.programme.instruction.Condition;
import hayen.robot.programme.instruction.Declarer;
import hayen.robot.programme.instruction.Instruction;
import hayen.robot.programme.instruction.Placer;
import hayen.robot.programme.instruction.Tourner;
import hayen.robot.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
	private static final String motCommentaire = "#";
	public static final String motCaractere = ".";

	private static final String operateur = "+-/*%";
	private static final String comparaison = "><=";

	public static final String operateurAdd = "+";
	public static final String operateurMin = "-";
	public static final String operateurMul = "*";
	public static final String operateurDiv = "/";
	public static final String operateurMod = "%";

	public static final String comparaisonGrand = ">";
	public static final String comparaisonPetit = "<";
	public static final String comparaisonEgal = "=";

	private static final String exension = ".pr";

	private static final String[] variablesPreEnregistre = {"hauteur", "largeur", "posx", "posy", "gauche", "droite", "orientation", "nord", "est", "ouest", "sud"};

	/** compile un programme à partir d'un fichier .pr
	 * @param adresse : l'adresse du fichier à compiler
	 * @return le programme compilé
	 * @throws IOException
	 * @throws FichierIncorrectException
	 * @throws OperationInvalideException 
	 */
	public static Instruction[] compileFichier(String adresse) throws IOException, FichierIncorrectException, OperationInvalideException{
		Vector<String> lignes = new Vector<String>();

		if (! adresse.endsWith(exension)){
			throw new FichierIncorrectException("extension invalide");
		}

		else{

			File fichier = new File(adresse); // le fichier à lire
			FileReader fr = new FileReader(fichier);
			BufferedReader br = new BufferedReader(fr);

			String ligne;
			while((ligne = br.readLine()) != null){
				lignes.add(ligne);
			}

			br.close();
			fr.close();

		}
		ArrayList<String> tmp = new ArrayList<String>(lignes);
		return compile(tmp);
	}

	/**
	 * enregistre les instructions d'un programme dans un fichier
	 * @param adresse : l'adresse du fichier de destination
	 * @param instructions : le bloc d'instructions à enregistrer
	 * @throws IOException
	 */
	public static void compileVersFichier(String adresse, Bloc instructions) throws IOException{
		ObjectOutputStream fichier = new ObjectOutputStream(new FileOutputStream(adresse));
		fichier.writeObject(version);
		fichier.writeObject(instructions);
		fichier.close();
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

		int v = 0; // l'indice du vecteur de variable à utiliser
		Vector<Vector<String>> var = new Vector<Vector<String>>();
		var.add(new Vector<String>());
		
		final int Main = 0, Condition = 1, Boucle = 2, Sinon = 3;
		Vector<Integer> typeBloc = new Vector<Integer>();
		typeBloc.add(Main);

		int ln = 0; // le numero de la ligne
		int x = 0; // le nombre de ligne sauté
		do {

			// séparer la ligne en mot
			String[] inst = separe(texte[ln]);
			if (inst == null)
				throw new CaractereInvalideException("Erreur, caractère invalide à la ligne " + (ln+1));

			if (inst.length == 0){ // on ignore la ligne si aucune instruction n'est donnée
				x++;
				ln++;
			}
			else {
				String premierMot = inst[0];
				/* ---------------------------------------------------- AFFICHER ----------------------------------------------- */
				if (premierMot.equals(motAfficher)){
					String[] param = Util.subArray(inst, 1, inst.length).toArray(new String[inst.length - 1]);
					for (int j = 1; j < inst.length; j++){
						// une variable serait ce qui n'est pas composé seulement de chiffre, et on ne veut pas afficher de mot réservé
						// et je ne permet malheureusement pas encore les expression lors de l'affichage (print(3 + 5))
						String m = inst[j];
						if (!Util.isDigit(m)){ // test pour s'assurer que le mot est correcte
							if (isMotReserve(m)) 
								throw new OperationInvalideException("ERREUR: Il est impossible d'afficher un mot reservé\n" + "ln." + (ln+1));
							else if (!(Util.isAlphaNumeric(m) || motCaractere.equals(m))) 
								throw new OperationInvalideException("ERREUR: Caractère invalide\n" + "ln." + (ln+1));
							else if (!(var.contains(m) || motCaractere.equals(m))) 
								throw new OperationInvalideException("ERREUR: variable non déclaré\n" + "ln." + (ln+1));
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
								throw new OperationInvalideException("ERREUR: nom de variable invalide\n" + "ln." + (ln+1));
							// envoyer une erreur si l'identificateur existe déjà
							else if (estDeclarer(var, inst[j])) 
								throw new OperationInvalideException("ERREUR: la variable <" + inst[j] + "> a déjà été déclarée\n" + "ln." + (ln+1));
							else {
								var.get(var.size()-1).add(inst[j]);
								params.add(inst[j]);
							}
						}
						Instruction toAdd = new Declarer(params.toArray(new String[params.size()]));
						instructions.add(toAdd);
					}
					else 
						throw new OperationInvalideException("ERREUR: expression invalide (une déclaration doit être fait sous la forme: var <nom>)\n" + "ln." + (ln+1));
				}

				/* ---------------------------------------------------- BOUCLE ----------------------------------------------- */
				else if (premierMot.equals(motBoucle)){ // fonctionne de la même manière qu'un condition
					// TODO
				}

				/* ---------------------------------------------------- CONDITION ----------------------------------------------- */
				else if (premierMot.equals(motCondition)){
					/*
					 * 1.1- Compter le nombre de terme suivant l'opérateur
					 * 1.2- S'il est égal à 0, retourner une erreur
					 * 2- déterminer les opérateurs de comparaison et séparer les opérations à vérifier
					 * 3- vérifier les opérations
					 * 4- rassembler le tout, faire l'opération
					 * 5.1- générer le nouveau vecteur de variable
					 * 5.2- changer le type de bloc
					 */
					
					//1.1
					
					
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
					// TODO
				}

				/* ---------------------------------------------------- FIN ----------------------------------------------- */
				else if (premierMot.equals(motFin)){
					/*
					 * 1- vérifier le type de bloc (condition, boucle, sinon)
					 * 2- pacter le tout
					 * 3- enlever une couche du vecteur de variable
					 * 4- changer le type de bloc
					 */
					// TODO
				}

				/* ---------------------------------------------------- AVANCER/PLACER/ENLEVER ----------------------------------------------- */
				// ils sont tellement semblable que l'on peut se permettre de vérifier les trois d'un seul coup
				else if (premierMot.equals(motDeplacer) || premierMot.equals(motPlacer) || premierMot.equals(motEnlever)){
					// La seule conttrainte est qu'il ne doit y avoir rien qui suit l'operation
					if (inst.length > 1)
						throw new OperationInvalideException("ERREUR: Operation invalide\nln." + (ln+1));
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
					if (inst.length > 3 || inst.length < 2) // la limite est à 3 au cas où c'est un nombre négatif (EX: -1 --> le coupeur de mot va le séparer ainsi : -, 1, donc deux mots)
						throw new OperationInvalideException("ERREUR: Operation invalide\nln." + (ln+1));
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
								throw new OperationInvalideException("ERREUR: Operation invalide\nln." + (ln+1));
							else {
								s = inst[2];
								if (Util.isDigit(s))
									toAdd = new Tourner(-Integer.parseInt(s));
								else if (!var.contains(s))
									throw new OperationInvalideException("ERREUR: Variable non declar�\nln." + (ln+1));
								else 
									toAdd = new Tourner("-" + s);
							}
						}

						else {
							s = inst[1];
							if (Util.isDigit(s))
								toAdd = new Tourner(Integer.parseInt(s));
							else if (!var.contains(s))
								throw new OperationInvalideException("ERREUR: Variable non declar�\nln." + (ln+1));
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
					Vector<Object> terme = new Vector<Object>(); // les termes de l'expression (String: variable, int: nombre, char: operateur)
					int j = 0;

					// trouver les identificateurs
					//				System.out.println("Cherche les identificateurs...");
					for (; j < inst.length; j++){
						if (inst[j].equals(motAssignation)){
							// si le premier terme est le signe d'assignation, il y a un problème
							if (j == 0) 
								throw new OperationInvalideException("ERREUR: aucun identificateur trouvé pour l'assignation\nln." + (ln+1));
							j++;
							break; // sinon, on a terminer de chercher les identificateurs
						}

						else if (!Util.isAlphaNumeric(inst[j])) 
							throw new OperationInvalideException("ERREUR: identificateur invalide\nln." + (ln+1));

						// l'identificateur n'existe pas
						else if (!estDeclarer(var, inst[j])) 
							throw new OperationInvalideException("ERREUR: l'identificateur indiqué n'a pas été déclaré\nln." + (ln+1));

						// les variables pre enregistrée ne peuvent être modifié par l'utilisateur
						else if (Util.contains(variablesPreEnregistre, inst[j]))
							throw new OperationInvalideException("ERREUR: la variable " + inst[j] + " ne peut pas être modifier\nln." + (ln+1));

						else 
							ids.add(inst[j]);
					}

					// System.out.println("Cherche les termes");
					for (; j < inst.length; j++){
						// operateur
						if (operateur.contains(inst[j])) 
							terme.add(new Character(inst[j].charAt(0)));

						// nombre
						else if (Util.isDigit(inst[j])) 
							terme.add(new Integer(inst[j]));

						// variable existante
						else if (estDeclarer(var, inst[j])) 
							terme.add(inst[j]);

						// variable non-existante
						else 
							throw new OperationInvalideException("ERREUR: l'identificateur indiqué n'a pas été déclaré\nln." + (ln+1));
					}

					// s'il n'y a plus de mots, il y a une erreur (EX: bla : )
					if (terme.size() == 0) 
						throw new OperationInvalideException("ERREUR: aucune expression pour l'assignation\nln." + (ln+1));

					// vérifier l'ordre des termes, si ils sont plus nombreux que 1, sinon, on ne fait que vérifier le dit terme
					if (terme.size() == 1){
						if (terme.get(0).getClass().equals(Character.class))
							throw new OperationInvalideException("ERREUR: operation invalide\nln." + (ln+1));
					}
					else {
						// vérification des extrémité: 0 -> valeur; -1 -> operateur
						if (terme.get(0).getClass().equals(Character.class) && !terme.get(terme.size() - 1).getClass().equals(Character.class))
							throw new OperationInvalideException("ERREUR: operation invalide\nln." + (ln+1));
						// VRAIMENT vérifier l'ordre des termes (v o v o v o...), commençant par le deuxième
						boolean valeur = !terme.get(1).getClass().equals(Character.class);
						for (int l = 1; l < terme.size(); l++){
							boolean isOp = terme.get(l).getClass().equals(Character.class);
							if (valeur == isOp)
								throw new OperationInvalideException("ERREUR: expression invalide\nln." + (ln+1));
							else
								valeur = !valeur;
						}
					}

					Instruction toAdd = new Assigner(ids.toArray(new String[ids.size()]), terme.toArray());
					instructions.add(toAdd);

				}

				ln++;
			}
		} while (ln > 0);

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
		TypeMot type = TypeMot.Inconnu;
		String mot = "";

		for (int i = 0; i < texte.length(); i++){
			char l = texte.charAt(i);
			// si la lettre est le commencement d'un commentaire, on arrête la boucle
			if (motCommentaire.equals(l)) break;

			// cas ou le type du mot est un identificateur
			if (type == TypeMot.Identificateur){
				// marche uniquement si c'est un alphanumérique
				if (Character.isLetter(l) || Character.isDigit(l)){
					mot += l;
				}
				// sinon, on considère que c'est un nouveau mot
				else{
					type = TypeMot.Inconnu;
					mots.add(mot);
					mot = "";
				}
			}

			// cas ou c'est un nombre
			if (type == TypeMot.Nombre){
				// marche uniquement si c'est un caractère numérique
				if (Character.isDigit(l)){
					mot += l;
				}
				// sinon, on considère que c'est un nouveau mot
				else{
					type = TypeMot.Inconnu;
					mots.add(mot);
					mot = "";
				}
			}

			// nouveau mot
			if (type == TypeMot.Inconnu){
				if (Character.isLetter(l)){
					type = TypeMot.Identificateur; // si le mot commence par une lettre, c'est un identificateur
					mot += l;
				}
				else if (Character.isDigit(l)){
					type = TypeMot.Nombre; // si c'est par un chiffre, c'est un nombre
					mot += l;
				}
				// si c'est un commentaire, on quitte la boucle, ce qui va finaliser la liste de mot
				else if (motCommentaire.equals("" + l)) break;

				else{ // autre cas spéciaux

					// si c'est un operateur, ou un mot de comparaison, ou le mot d'assignation, on pousse le mot
					if (operateur.contains("" + l) || comparaison.contains("" + l) || motAssignation.equals("" + l) || motCaractere.equals("" + l)){
						mots.add("" + l);
					}

					// si c'est un espace ou une tabulation, on avance sans rien faire
					else if (l == ' ' || l == '\t'){
					}

					// si c'est autre chose, c'est une erreur, et on l'affiche
					else{
						return null;
					}

				}
			}

		}

		// pousse le mot s'il n'était pas vide
		if (!mot.equals(""))mots.add(mot);

		return mots.toArray(new String[mots.size()]);
	}

	/**
	 * indique si le mot passé en paramètre figure dans la liste des mots réservés
	 * @param m : le mot à vérifier
	 * @return vrai si m est reservé, faux autrement
	 */
	public static boolean isMotReserve(String m){
		for (String mr : motReserve){
			if (mr.equals(m)) return true;
		}
		return false;
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

	private static boolean estDeclarer(Vector<Vector<String>> var, String id){
		int j = var.size()-1;
		while (j >= 0){
			if (var.get(j).contains(id))
				return true;
			j--;
		}
		return false;
	}
	
}
