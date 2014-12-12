package hayen.robot.programme;

import hayen.robot.util.Util;
import hayen.robot.programme.instruction.*;

import java.io.BufferedReader;
//import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;

public class Compilateur {

	public static final String version = "0.2";

	// liste de mots réservés
	private static final String[] motReserve = new String[9];
	public static final String motDeclaration = ajouterMotReserve("var");
	public static final String motCondition = ajouterMotReserve("si");
	public static final String motSinon = ajouterMotReserve("sinon");
	public static final String motFin = ajouterMotReserve("fin");
	public static final String motAfficher = ajouterMotReserve("afficher");
	public static final String motTourner = ajouterMotReserve("tourner");
	public static final String motDeplacer = ajouterMotReserve("deplacer");
	public static final String motBoucle = ajouterMotReserve("tantque");
	public static final String motQuitter = ajouterMotReserve("retour");
	public static final String motAssignation = ":";
	public static final String motCommentaire = "#";
	public static final String motCaractere = ".";

	public static final String operateur = "+-/*%";
	public static final String comparaison = "><=";

	public static final String operateurAdd = "+";
	public static final String operateurMin = "-";
	public static final String operateurMul = "*";
	public static final String operateurDiv = "/";
	public static final String operateurMod = "%";

	public static final String comparaisonGrand = ">";
	public static final String comparaisonPetit = "<";
	public static final String comparaisonEgal = "=";

	public static final String exension = ".pr";

	public static final int blocMain = 0;
	public static final int blocSi = 1;
	public static final int blocSinon = 2;
	public static final int blocBoucle = 3;

	/** compile un programme à partir d'un fichier .pr
	 * @param adresse : l'adresse du fichier à compiler
	 * @return le programme compilé
	 * @throws IOException
	 * @throws FichierIncorrectException
	 * @throws OperationInvalideException 
	 */
	public static Bloc compileFichier(String adresse) throws IOException, FichierIncorrectException, OperationInvalideException{
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
	public static Bloc compile(ArrayList<String> programme) throws OperationInvalideException{
		return compile(blocMain, null, programme, programme.size());
	}

	public static Bloc compile(String programme) throws OperationInvalideException {
		ArrayList<String> tmp = new ArrayList<String>();
		for (String s : programme.split("\n")) tmp.add(s);
		return compile(0, null, tmp, tmp.size());
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
	private static Bloc compile(int typeBloc, Vector<String> preVar, ArrayList<String> texte, int taille) throws OperationInvalideException{
		Vector<Instruction> instructions = new Vector<Instruction>();
		@SuppressWarnings("unchecked")
		Vector<String> var = preVar != null ? (Vector<String>)preVar.clone() : new Vector<String>();
		Bloc retour;

		int i; // le numero de la ligne
		while (texte.size() > 0){
			i = taille - texte.size();
			String[] inst = separe(texte.get(0), i);
			if (inst == null || inst.length == 0){ // on ignore la ligne si aucune instruction n'est donnée
				texte.remove(0);
				continue; 
			}

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
							throw new OperationInvalideException("ERREUR: Il est impossible d'afficher un mot reservé\n" + "ln." + (i+1));
						else if (!(Util.isAlphaNumeric(m) || motCaractere.equals(m))) 
							throw new OperationInvalideException("ERREUR: Caractère invalide\n" + "ln." + (i+1));
						else if (!(var.contains(m) || motCaractere.equals(m))) 
							throw new OperationInvalideException("ERREUR: variable non déclaré\n" + "ln." + (i+1));
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
							throw new OperationInvalideException("ERREUR: nom de variable invalide\n" + "ln." + (i+1));
						// envoyer une erreur si l'identificateur existe déjà
						else if (var.contains(inst[j])) 
							throw new OperationInvalideException("ERREUR: la variable <" + inst[j] + "> a déjà été déclarée\n" + "ln." + (i+1));
						else {
							var.add(inst[j]);
							params.add(inst[j]);
						}
					}
					Instruction toAdd = new Declarer(params.toArray(new String[params.size()]));
					instructions.add(toAdd);
				}
				else 
					throw new OperationInvalideException("ERREUR: expression invalide (une déclaration doit être fait sous la forme: var <nom>)\n" + "ln." + (i+1));
			}

			/* ---------------------------------------------------- BOUCLE ----------------------------------------------- */
			else if (premierMot.equals(motBoucle)){
				// le début est très semblable à si c'était une condition
				if (inst.length < 2) 
					throw new OperationInvalideException("ERREUR: Expression invalide\n" + "ln." + (i+1));

				// 1. Vérifier si la condition est correcte
				// 1.1 Vérifier que les termes sont légals
				Vector<Object> termes = new Vector<Object>(); // termes de la condition
				for (int j = 1; j < inst.length; j++){
					String terme = inst[j];

					// si c'est un opérateur
					if (comparaison.contains(terme)) termes.add(terme.charAt(0));
					// si c'est un nombre
					else if (Util.isDigit(terme)) termes.add(new Integer(terme));
					// si c'est un identificateur non déclaré
					else if (!var.contains(terme)) 
						throw new OperationInvalideException("ERREUR: variable non déclaré\n" + "ln." + (i+1));
					else termes.add(terme);
				}
				// 1.2 Vérifier que les termes sont dans l'ordre
				if (termes.get(0).getClass().equals(Character.class) || termes.get(termes.size() - 1).getClass().equals(Character.class))
					throw new OperationInvalideException("ERREUR: une condition ne peut commencer ou finir par un operateur\nln." + (i+1));
				// les termes doivent s'enchainer de la manière suivante v o v o v o v o v...
				boolean valeur = true;
				for (Object t : termes.toArray()){
					boolean isOp = t.getClass().equals(Character.class);
					if (isOp == valeur)
						throw new OperationInvalideException("ERREUR: operation invalide\nln." + (i+1));
					valeur = !valeur;
				}

				// 2. Trouver le bloc à executer dans la boucle
				texte.remove(0);
				Bloc bloc = compile(Compilateur.blocBoucle, var, texte, taille);
				// si il n'y a rien après le bloc, c'est une erreur
				if (texte.size() < 1) 
					throw new OperationInvalideException("ERREUR: 'fin' manquant à la fin de la condition\n" + "ln." + (i+1));

				// 3. Vérifier si un fin est à la suite
				String[] l = separe(texte.get(0), i);
				if (l[0].equals(motFin) && l.length == 1){
					instructions.add(new Boucle(bloc, termes.toArray()));
				}

				// 4. les autres cas sont des erreurs
				else
					throw new OperationInvalideException("ERREUR: Instruction invalide\n" + "ln." + (i+1));
			}

			/* ---------------------------------------------------- CONDITION ----------------------------------------------- */
			else if (premierMot.equals(motCondition)){
				if (inst.length < 2) 
					throw new OperationInvalideException("ERREUR: Expression invalide\n" + "ln." + (i+1));

				// 1. Vérifier si la condition est correcte
				// 1.1 Vérifier que les termes sont légals
				Vector<Object> termes = new Vector<Object>(); // termes de la condition
				for (int j = 1; j < inst.length; j++){
					String terme = inst[j];

					// si c'est un opérateur
					if (comparaison.contains(terme)) termes.add(terme.charAt(0));
					// si c'est un nombre
					else if (Util.isDigit(terme)) termes.add(new Integer(terme));
					// si c'est un identificateur non déclaré
					else if (!var.contains(terme)) 
						throw new OperationInvalideException("ERREUR: variable non déclaré\n" + "ln." + (i+1));
					else termes.add(terme);
				}
				// 1.2 Vérifier que les termes sont dans l'ordre
				if (termes.get(0).getClass().equals(Character.class) || termes.get(termes.size() - 1).getClass().equals(Character.class))
					throw new OperationInvalideException("ERREUR: une condition ne peut commencer ou finir par un operateur\nln." + (i+1));
				// les termes doivent s'enchainer de la manière suivante v o v o v o v o v...
				boolean valeur = true;
				for (Object t : termes.toArray()){
					boolean isOp = t.getClass().equals(Character.class);
					if (isOp == valeur)
						throw new OperationInvalideException("ERREUR: operation invalide\nln." + (i+1));
					valeur = !valeur;
				}

				// 2. Trouver le bloc à executer si la condition est vrai
				texte.remove(0);
				Bloc blocSi = compile(Compilateur.blocSi, var, texte, taille);
				// si il n'y a rien après le bloc-si, c'est une erreur
				if (texte.size() < 1) 
					throw new OperationInvalideException("ERREUR: 'fin' manquant à la fin de la condition\n" + "ln." + (i+1));

				// 3. Trouver si il y a un bloc à executer si la condition est fausse
				String[] l = separe(texte.get(0), i);
				if (l[0].equals(motSinon) && l.length == 1){

					// 3.1 compiler le bloc à executer si la condition est fausse
					texte.remove(0);
					Bloc blocSinon = compile(Compilateur.blocSinon, var, texte, taille);

					// 3.2 Vérifier qu'un 'fin' est à la fin du bloc
					if (texte.size() < 1) 
						throw new OperationInvalideException("ERREUR: 'fin' manquant à la fin de la condition\n" + "ln." + (i+1));
					String[] l2 = separe(texte.get(0), i);
					if (l2[0].equals(motFin) && l2.length == 1){
						instructions.add(new Condition(blocSi, blocSinon, termes.toArray()));
					}
					// autre chose que juste 'fin' = erreur
					else 
						throw new OperationInvalideException("ERREUR: Instruction invalide\n" + "ln." + (i+1));
				}

				// 4. Est-ce un 'fin'
				else if (l[0].equals(motFin) && l.length == 1){
					instructions.add(new Condition(blocSi, termes.toArray()));
				}

				// 5. les autres cas sont des erreurs
				else 
					throw new OperationInvalideException("ERREUR: Instruction invalide\n" + "ln." + (i+1));

			}

			/* ---------------------------------------------------- SINON ----------------------------------------------- */
			else if (premierMot.equals(motSinon)){
				// "sinon" ne peut être utilisé que dans un bloc si. Les autres cas sont des erreurs
				if (typeBloc != Compilateur.blocSi) 
					throw new OperationInvalideException("ERREUR: Instruction invalide\n" + "ln." + (i+1));

				// si tout est correcte, il agit comme un fins
				retour = new Bloc(instructions.toArray(new Instruction[instructions.size()]));
				return retour;
			}

			/* ---------------------------------------------------- FIN ----------------------------------------------- */
			else if (premierMot.equals(motFin)){
				// si il y a des parametre après fin, c'est une erreur
				if (inst.length != 1) 
					throw new OperationInvalideException("ERREUR: Instruction invalide\n" + "ln." + (i+1));

				// autrement, le mot fin met... fin au bloc
				retour = new Bloc(instructions.toArray(new Instruction[instructions.size()]));
				return retour;
			}

			/* ---------------------------------------------------- ASSIGNATION ----------------------------------------------- */
			else { 
				// on peut assigner plusieurs variable en même temps
				// syntaxe: <identificateurs> : <expression>

				Vector<String> ids = new Vector<String>(); // les identificateurs
				Vector<Object> terme = new Vector<Object>(); // les termes de l'expression (String: variable, int: nombre, char: operateur)
				int j = 0;

				// trouver les identificateurs
				for (; j < inst.length; j++){
					if (inst[j].equals(motAssignation)){
						// si le premier terme est le signe d'assignation, il y a un problème
						if (j == 0) 
							throw new OperationInvalideException("ERREUR: aucun identificateur trouvé pour l'assignation\nln." + (i+1));
						j++;
						break; // sinon, on a terminer de chercher les identificateurs
					}

					else if (!Util.isAlphaNumeric(inst[j])) 
						throw new OperationInvalideException("ERREUR: identificateur invalide\nln." + (i+1));

					// l'identificateur n'existe pas
					else if (!var.contains(inst[j])) 
						throw new OperationInvalideException("ERREUR: l'identificateur indiqué n'a pas été déclaré\nln." + (i+1));

					else ids.add(inst[j]);
				}

				for (; j < inst.length; j++){
					// operateur
					if (operateur.contains(inst[j])) terme.add(new Character(inst[j].charAt(0)));

					// nombre
					else if (Util.isDigit(inst[j])) terme.add(new Integer(inst[j]));

					// variable existante
					else if (var.contains(inst[j])) terme.add(inst[j]);

					// variable non-existante
					else 
						throw new OperationInvalideException("ERREUR: l'identificateur indiqué n'a pas été déclaré\nln." + (i+1));
				}

				// s'il n'y a plus de mots, il y a une erreur (EX: bla : )
				if (terme.size() == 0) 
					throw new OperationInvalideException("ERREUR: aucune expression pour l'assignation\nln." + (i+1));

				// vérifier l'ordre des termes, si ils sont plus nombreux que 1, sinon, on ne fait que vérifier le dit terme
				if (terme.size() == 1){
					if (terme.get(0).getClass().equals(Character.class))
						throw new OperationInvalideException("ERREUR: operation invalide\nln." + (i+1));
				}
				else {
					// vérification des extrémité: 0 -> valeur; -1 -> operateur
					if (terme.get(0).getClass().equals(Character.class) && !terme.get(terme.size() - 1).getClass().equals(Character.class))
						throw new OperationInvalideException("ERREUR: operation invalide\nln." + (i+1));
					// VRAIMENT vérifier l'ordre des termes (v o v o v o...), commençant par le deuxième
					boolean valeur = !terme.get(1).getClass().equals(Character.class);
					for (int l = 1; i < terme.size(); i++){
						boolean isOp = terme.get(l).getClass().equals(Character.class);
						if (valeur == isOp)
							throw new OperationInvalideException("ERREUR: expression invalide\nln." + (i+1));
						else
							valeur = !valeur;
					}
				}

				Instruction toAdd = new Assigner(ids.toArray(new String[ids.size()]), terme.toArray());
				instructions.add(toAdd);

			}

			if (texte.size() > 0)
				texte.remove(0);
		}

		return new Bloc(instructions.toArray(new Instruction[instructions.size()]));
	}

	/**
	 * Separe les mots d'une instruction et vérifie qu'il n'y a pas de caractère invalide
	 * @param texte : l'instruction à séparer
	 * @param ligne : la ligne à laquelle se trouve l'instruction
	 * @return Un tableau de String correspondant au mot qui construisent l'instruction
	 * @throws CaractereInvalideException
	 */
	public static String[] separe(String texte, int ligne) throws CaractereInvalideException{
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
						throw new CaractereInvalideException("Erreur, caractère " + (i+1) + " invalide à la ligne " + (ligne+1));
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
		for (int i = 0; i < motReserve.length; i++) if (motReserve[i] == null){
			motReserve[i] = mot;
			break;
		}
		return mot;
	}

}
