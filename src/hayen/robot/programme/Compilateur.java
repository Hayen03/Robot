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
	
	// compile un programme à partir d'un fichier .pr
	public static Instruction[] compileFichier(String adresse) throws IOException, FichierIncorrectException{
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
		return compile(lignes.toArray(new String[lignes.size()]));
	}
	
	public static void compileVersFichier(String adresse, Instruction... instructions) throws IOException{
		
/*		DataOutputStream fichier = new DataOutputStream(new FileOutputStream(adresse));
		for (Instruction i : instructions){
			i.enregistrer(fichier);
		}
*/
		
		ObjectOutputStream fichier = new ObjectOutputStream(new FileOutputStream(adresse));
		fichier.writeObject(version);
		fichier.writeObject(instructions);
		fichier.close();
	}

	public static Instruction[] compile(String... texte){
		Vector<Instruction> inst = new Vector<Instruction>();
		Vector<String> var = new Vector<String>();
		int indentation = 0;
		int decalage = 0;

		for (int i = 0; i < texte.length; i++){
			String[] op = separe(texte[i].toLowerCase());
			if (op.length == 0){
				decalage++;
				continue;
			}

			// l'instruction dépend du premier terme
			String first = op[0];
			if (first.equals(motDeclaration)){ // declaration de variable
				/*
				 * Une variable doit avoir un nom alpha numérique et respecter la syntaxe: var <nom>
				 * (pas d'assignation) + (plusieurs déclaration en même temps)
				 */
				if (op.length >= 2){
					Vector<String> params = new Vector<String>();
					for (int j = 1; j < op.length; j++){
						// envoyer une erreur si le nom est invalide
						if ( !(Character.isAlphabetic(op[j].charAt(0)) && Util.isAlphaNumeric(op[j])) ){
							System.out.println("ERREUR: nom de variable invalide\n" + "ln." + (i+1));
							return null;
						}
						// envoyer une erreur si l'identificateur existe déjà
						else if (var.contains(op[j])){
							//bla bla bla
							System.out.println("ERREUR: la variable <" + op[j] + "> a déjà été déclarée\n" + "ln." + (i+1));
							return null;
						}
						else {
							var.add(op[j]);
							params.add(op[j]);
						}
					}
					Instruction toAdd = new Declarer(params.toArray(new String[params.size()]));
					inst.add(toAdd);
//					System.out.println("" + i + ": new " + toAdd);
				}
				else{
					System.out.println("ERREUR: expression invalide (une déclaration doit être fait sous la forme: var <nom>)\n" + "ln." + (i+1));
					return null;
				}
			}
			else if (first.equals(motAfficher)){ // affichage (afficher ...)
				// la seule chose à vérifier ici, c'est si les variables référencier existe
				String[] param = new String[op.length-1];
				for (int j = 1; j < op.length; j++){
					// une variable serait ce qui n'est pas composé seulement de chiffre, et on ne veut pas afficher de mot réservé
					// et je ne permet malheureusement pas encore les expression lors de l'affichage (print(3 + 5))
					String m = op[j];
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
					param[j-1] = op[j];
				}
				Instruction toAdd = new Afficher(param);
				inst.add(toAdd);
//				System.out.println("" + i + ": new " + toAdd);
			}
			else if (first.equals(motBoucle)){ // tantque
				// syntaxe : tantque <expression>
				if (op.length < 2){
					System.err.println("ERREUR: Expression invalide\n" + "ln." + (i+1));
					return null;
				}
				
				

			}
			else if (first.equals(motCondition)){ // if
				indentation++; // le nombre d'indentation augmente
				// doit avoir plus de 1 parametre
				if (op.length < 2){
					System.out.println("ERREUR: Expression invalide\n" + "ln." + (i+1));
					return null;
				}

				// liste des choses qui vont être passé en paramètre (variable, nombre et opérateur logique/comparaison)(en alternance, débute et se termine par une valeur)
				Vector<Object> params = new Vector<Object>();

				for (int j = 1; j < op.length; j++){
					String terme = op[j];

					if (comparaison.contains(terme)){ // si c'est un opérateur
						params.add(terme.charAt(0));
					}
					else if (Util.isDigit(terme)) { // si c'est un nombre
						params.add(new Integer(terme));
					}
					else { // si c'est un identificateur
						if (!var.contains(terme)){ // si elle est non déclaré
							System.out.println("ERREUR: variable non déclaré\n" + "ln." + (i+1));
							return null;
						}
						params.add(terme);
					}
				}
				Instruction toAdd = new Condition(indentation, params.toArray());
				inst.add(toAdd);
//				System.out.println("" + i + ": new " + toAdd);

			}
			else if (first.equals(motSinon)){ // else
				// n'a pas de parametre et doit être dans un bloc if (indentation >= 1)
				if (op.length > 1 || indentation < 1){
					System.out.println("ERREUR: Expression invalide\n" + "ln." + (i+1));
					return null;
				}
				Instruction toAdd = new Sinon(indentation);
				inst.add(toAdd);
//				System.out.println("" + i + ": new " + toAdd);
			}
			else if (first.equals(motFin)){
				if (indentation < 0){ // si le bloc d'indentation est plus petit que zéro, il y a une erreur
					System.out.println("ERREUR: Symbole fin en trop\n" + "ln." + (i+1));
					return null;
				}
				Instruction toAdd = new Fin(indentation);
				inst.add(toAdd);
//				System.out.println("" + i + ": new " + toAdd);
				indentation--;
			}
			else if (first.equals(motDeplacer)){ // deplacement

			}
			else if (first.equals(motTourner)){ // tourner

			}
			else{ // assignation
				// on peut assigner plusieurs variable en même temps
				// syntaxe: <identificateurs> : <expression>

				Vector<String> ids = new Vector<String>(); // les identificateurs
				Vector<Object> terme = new Vector<Object>(); // les termes de l'expression (String: variable, int: nombre, char: operateur)
				int j = 0;

				// trouver les identificateurs
				for (; j < op.length; j++){
					if (op[j].equals(motAssignation)){
						if (j == 0){ // si le premier terme est le signe d'assignation, il y a un problème
							System.out.println("ERREUR: aucun identificateur trouvé pour l'assignation\nln." + (i+1));
							return null;
						}
						j++;
						break; // sinon, on a terminer de chercher les identificateurs
					}
					else if (!var.contains(op[j])){ // l'identificateur n'existe pas
						System.out.println("ERREUR: l'identificateur indiqué n'a pas été déclaré\nln." + (i+1));
						return null;
					}
					else {
						ids.add(op[j]);
					}
				}

				for (; j < op.length; j++){
					if (/*comparaison.contains(op[j]) || */operateur.contains(op[j])){ // opperateur
						terme.add(new Character(op[j].charAt(0)));
					}
					else if (Util.isDigit(op[j])){ // nombre
						terme.add(new Integer(op[j]));
					}
					else if (var.contains(op[j])){ // variable existante
						terme.add(op[j]);
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

				// vérifier l'ordre des termes (valeur opérateur valeur... ou valeur valeur operateur valeur operateur... (ce mode daoit avoir absolument un minimum de trois termes))
				if (terme.get(0).getClass().equals(Character.class)){ // vérification du premier terme (doit être une valeur)
					System.out.println("ERREUR: premier terme de l'assignation invalide\nln." + (i+1));
					return null;
				}
				if (terme.size() >= 2){ // cas ou il y a plusieurs termes
					if (!terme.get(terme.size()-1).getClass().equals(Character.class)){ // si il y a plus de 1 terme et que le dernier est une valeur, il y a erreur
						System.out.println("ERREUR: expression invalide\nln." + (i+1));
						return null;
					}
					boolean operateur = terme.get(1).getClass().equals(Character.class); // indique si le terme précédent est un operateur
					for (j = 2; j < terme.size(); j++){
						if (terme.get(j).getClass().equals(Character.class) == operateur){
							System.out.println("ERREUR: expression invalide\nln." + (i+1));
							return null;
						}
					}
				}
				Instruction toAdd = new Assigner(ids.toArray(new String[ids.size()]), terme.toArray());
				inst.add(toAdd);
//				System.out.println("" + i + ": new " + toAdd);
			}

		}

		if (indentation > 0){
			System.out.println("ERREUR: bloc d'opération incorrecte");
			return null;
		}
		
		return inst.toArray(new Instruction[inst.size()]);
	}

/*	public static Instruction comileLigne(String ligne){
		
	}
*/
	
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
