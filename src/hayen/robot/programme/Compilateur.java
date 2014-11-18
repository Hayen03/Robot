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

	public static final String alpha = "abcdefghijklmnopqrstuvwxyz";
	public static final String numerique = "1234567890";
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

	// compile un programme à partir d'un fichier .pr
	public static Bloc compileFichier(String adresse) throws IOException, FichierIncorrectException{
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
		ArrayList<String> tmp = new ArrayList(lignes);
		return compile(blocMain, null, tmp, tmp.size());
	}

	public static void compileVersFichier(String adresse, Bloc instructions) throws IOException{
		ObjectOutputStream fichier = new ObjectOutputStream(new FileOutputStream(adresse));
		fichier.writeObject(version);
		fichier.writeObject(instructions);
		fichier.close();
	}

	private static Bloc compile(int typeBloc, Vector<String> preVar, ArrayList<String> texte, int taille){
		Vector<Instruction> instructions = new Vector<Instruction>();
		Vector<String> var = preVar != null ? (Vector<String>)preVar.clone() : new Vector<String>();
		Bloc retour;

		int i;
		while (texte.size() > 0){
			i = taille - texte.size();
			String[] inst = separe(texte.get(0));
			if (inst.length == 0) continue; // on ignore la ligne si aucune instruction n'est donnée

			String premierMot = inst[0];

			if (premierMot.equals(motAfficher)){
				String[] param = (String[])Util.subArray(inst, 1, inst.length);
				for (int j = 1; j < inst.length; j++){
					// une variable serait ce qui n'est pas composé seulement de chiffre, et on ne veut pas afficher de mot réservé
					// et je ne permet malheureusement pas encore les expression lors de l'affichage (print(3 + 5))
					String m = inst[j];
					if (!Util.isDigit(m)){
						if (isMotReserve(m)){
							System.out.println("ERREUR: Il est impossible d'afficher un mot reservé\n" + "ln." + (i+1));
							return null;
						}
						else if (!(Util.isAlphaNumeric(m) || motCaractere.equals(m))){
							System.out.println("ERREUR: Caractère invalide\n" + "ln." + (i+1));
							return null;
						}
						else if (!(var.contains(m) || motCaractere.equals(m))){
							System.out.println("ERREUR: variable non déclaré\n" + "ln." + (i+1));
							return null;
						}
						// si toutes ces étapes sont passé, le terme est correcte
					}
					param[j-1] = inst[j];
				}
				Instruction toAdd = new Afficher(param);
				instructions.add(toAdd);
			}

			else if (premierMot.equals(motDeclaration)){
				/*
				 * Une variable doit avoir un nom alpha numérique et respecter la syntaxe: var <nom>
				 * (pas d'assignation) + (plusieurs déclaration en même temps)
				 */
				if (inst.length >= 2){
					Vector<String> params = new Vector<String>();
					for (int j = 1; j < inst.length; j++){
						// envoyer une erreur si le nom est invalide
						if ( !(Character.isAlphabetic(inst[j].charAt(0)) && Util.isAlphaNumeric(inst[j])) ){
							System.out.println("ERREUR: nom de variable invalide\n" + "ln." + (i+1));
							return null;
						}
						// envoyer une erreur si l'identificateur existe déjà
						else if (var.contains(inst[j])){
							System.out.println("ERREUR: la variable <" + inst[j] + "> a déjà été déclarée\n" + "ln." + (i+1));
							return null;
						}
						else {
							var.add(inst[j]);
							params.add(inst[j]);
						}
					}
					Instruction toAdd = new Declarer(params.toArray(new String[params.size()]));
					instructions.add(toAdd);
				}
				else{
					System.out.println("ERREUR: expression invalide (une déclaration doit être fait sous la forme: var <nom>)\n" + "ln." + (i+1));
					return null;
				}
			}

			else if (premierMot.equals(motBoucle)){
				
			}

			else if (premierMot.equals(motCondition)){
				if (inst.length < 2){
					System.out.println("ERREUR: Expression invalide\n" + "ln." + (i+1));
					return null;
				}

				// 1. Vérifier si la condition est correcte
				Vector<Object> termes = new Vector<Object>(); // termes de la condition
				for (int j = 1; j < inst.length; j++){
					String terme = inst[j];

					if (comparaison.contains(terme)){ // si c'est un opérateur
						termes.add(terme.charAt(0));
					}
					else if (Util.isDigit(terme)) { // si c'est un nombre
						termes.add(new Integer(terme));
					}
					else { // si c'est un identificateur
						if (!var.contains(terme)){ // si elle est non déclaré
							System.out.println("ERREUR: variable non déclaré\n" + "ln." + (i+1));
							return null;
						}
						termes.add(terme);
					}
				}

				// 2. Trouver le bloc à executer si la condition est vrai
				texte.remove(0);
				Bloc blocSi = compile(Compilateur.blocSi, var, texte, taille);
				if (texte.size() < 1){ // si il n'y a rien après le bloc-si, c'est une erreur
					System.out.println("ERREUR: 'fin' manquant à la fin de la condition\n" + "ln." + (i+1));
					return null;
				}

				// 3. Trouver si il y a un bloc à executer si la condition est fausse
				String[] l = separe(texte.get(0));
				if (l[0].equals(motSinon) && l.length == 1){
					
					// 3.1 compiler le bloc à executer si la condition est fausse
					Bloc blocSinon = compile(Compilateur.blocSinon, var, texte, taille);
					
					// 3.2 Vérifier qu'un 'fin' est à la fin du bloc
					if (texte.size() < 1){
						System.out.println("ERREUR: 'fin' manquant à la fin de la condition\n" + "ln." + (i+1));
						return null;
					}
					String[] l2 = separe(texte.get(0));
					if (l2[0].equals(motFin) && l2.length == 1){
						instructions.add(new Condition(blocSi, blocSinon, termes));
					}
					else { // autre chose que juste 'fin' = erreur
						System.out.println("ERREUR: Instruction invalide\n" + "ln." + (i+1));
						return null;
					}
				}
				// 4. Est-ce un 'fin'
				else if (l[0].equals(motFin) && l.length == 1){
					instructions.add(new Condition(blocSi, termes));
				}
				// 5. les autres cas sont des erreurs
				else { 
					System.out.println("ERREUR: Instruction invalide\n" + "ln." + (i+1));
					return null;
				}

			}

			else if (premierMot.equals(motSinon)){
				if (typeBloc != Compilateur.blocSi){
					System.out.println("ERREUR: Instruction invalide\n" + "ln." + (i+1));
					return null;
				}
				retour = new Bloc(instructions.toArray(new Instruction[instructions.size()]));
				return retour;
			}

			else if (premierMot.equals(motFin)){
				if (inst.length != 1){
					System.out.println("ERREUR: Instruction invalide\n" + "ln." + (i+1));
					return null;
				}
				retour = new Bloc(instructions.toArray(new Instruction[instructions.size()]));
				return retour;
			}

			else { // assignation
				// on peut assigner plusieurs variable en même temps
				// syntaxe: <identificateurs> : <expression>

				Vector<String> ids = new Vector<String>(); // les identificateurs
				Vector<Object> terme = new Vector<Object>(); // les termes de l'expression (String: variable, int: nombre, char: operateur)
				int j = 0;

				// trouver les identificateurs
				for (; j < inst.length; j++){
					if (inst[j].equals(motAssignation)){
						if (j == 0){ // si le premier terme est le signe d'assignation, il y a un problème
							System.out.println("ERREUR: aucun identificateur trouvé pour l'assignation\nln." + (i+1));
							return null;
						}
						j++;
						break; // sinon, on a terminer de chercher les identificateurs
					}
					else if (!Util.isAlphaNumeric(inst[j])){
						System.out.println("ERREUR: identificateur invalide\nln." + (i+1));
						return null;
					}
					else if (!var.contains(inst[j])){ // l'identificateur n'existe pas
						System.out.println("ERREUR: l'identificateur indiqué n'a pas été déclaré\nln." + (i+1));
						return null;
					}

					else {
						ids.add(inst[j]);
					}
				}

				for (; j < inst.length; j++){
					if (operateur.contains(inst[j])){ // operateur
						terme.add(new Character(inst[j].charAt(0)));
					}
					else if (Util.isDigit(inst[j])){ // nombre
						terme.add(new Integer(inst[j]));
					}
					else if (var.contains(inst[j])){ // variable existante
						terme.add(inst[j]);
					}
					else{ // variable non-existante
						System.out.println("ERREUR: l'identificateur indiqué n'a pas été déclaré\nln." + (i+1));
						return null;
					}
				}

				// s'il n'y a plus de mots, il y a une erreur (EX: bla : )
				if (terme.size() == 0){
					System.out.println("ERREUR: aucune expression pour l'assignation\nln." + (i+1));
					return null;
				}

				// vérifier l'ordre des termes (valeur opérateur valeur... ou valeur valeur insterateur valeur insterateur... (ce mode daoit avoir absolument un minimum de trois termes))
				if (terme.get(0).getClass().equals(Character.class)){ // vérification du premier terme (doit être une valeur)
					System.out.println("ERREUR: premier terme de l'assignation invalide\nln." + (i+1));
					return null;
				}
				if (terme.size() >= 2){ // cas ou il y a plusieurs termes
					if (!terme.get(terme.size()-1).getClass().equals(Character.class)){ // si il y a plus de 1 terme et que le dernier est une valeur, il y a erreur
						System.out.println("ERREUR: expression invalide\nln." + (i+1));
						return null;
					}
					boolean insterateur = terme.get(1).getClass().equals(Character.class); // indique si le terme précédent est un insterateur
					for (j = 2; j < terme.size(); j++){
						if (terme.get(j).getClass().equals(Character.class) == insterateur){
							System.out.println("ERREUR: expression invalide\nln." + (i+1));
							return null;
						}
					}
				}
				Instruction toAdd = new Assigner(ids.toArray(new String[ids.size()]), terme.toArray());
				instructions.add(toAdd);

			}
			texte.remove(0);
		}

		return new Bloc(instructions.toArray(new Instruction[instructions.size()]));
	}

	public static String[] separe(String texte){
		Vector<String> mots = new Vector<String>();
		TypeMot type = TypeMot.Inconnu;
		String mot = "";

		for (int i = 0; i < texte.length(); i++){
			String l = "" + texte.charAt(i);
			// si la lettre est le commencement d'un commentaire, on arrête la boucle
			if (l.equals(motCommentaire)) break;

			// cas ou le type du mot est un identificateur
			if (type == TypeMot.Identificateur){
				// marche uniquement si c'est un alphanumérique
				if (alpha.contains(l) || numerique.contains(l)){
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
				if (numerique.contains(l)){
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
				if (alpha.contains(l)){
					type = TypeMot.Identificateur; // si le mot commence par une lettre, c'est un identificateur
					mot += l;
				}
				else if (numerique.contains(l)){
					type = TypeMot.Nombre; // si c'est par un chiffre, c'est un nombre
					mot += l;
				}
				else{ // autre cas spéciaux

					// si c'est un operateur, ou un mot de comparaison, ou le mot d'assignation, on pousse le mot
					if (operateur.contains(l) || comparaison.contains(l) || motAssignation.equals(l) || motCaractere.equals(l)){
						mots.add(l);
					}

					// si c'est un espace ou une tabulation, on avance sans rien faire
					else if (l.equals(" ") || l.equals("\t")){
					}

					// si c'est autre chose, c'est une erreur, et on l'affiche
					else{
						//ERREUR
						/*
						 * juste me contenter de ne rien renvoyer pour le moment
						 */
						System.out.println("Erreur, caractère " + (i+1) + " invalide!!!");
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
		for (int i = 0; i < motReserve.length; i++) if (motReserve[i] == null){
			motReserve[i] = mot;
			break;
		}
		return mot;
	}

}
