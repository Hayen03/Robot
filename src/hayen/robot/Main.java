package hayen.robot;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import hayen.robot.programme.Compilateur;
import hayen.robot.programme.FichierIncorrectException;
import hayen.robot.programme.Programme;
import hayen.robot.programme.instruction.Instruction;

public class Main {

	public static void main(String[] args) {
		if (args.length == 0) test2();
		else if (args[0].equals("-c") || args[0].equals("compile")){ // si on veut compiler un fichier
			Instruction[] instructions;
			try {
				instructions = Compilateur.compileFichier(args[1]);
			} catch (IOException e) {
				System.err.println("Erreur lors de la lecture du fichier");
				e.printStackTrace();
				return;
			} catch (ArrayIndexOutOfBoundsException e){
				System.err.println("L'adresse du fichier de depart est absente");
				return;
			} catch (FichierIncorrectException e){
				System.err.println("Extension du premier fichier incorecte (requiert .pr)");
				return;
			}

			try {
				Compilateur.compileVersFichier(args[2], instructions);
			} catch (IOException e) {
				System.err.println("Erreur lors de l'écriture du fichier");
				e.printStackTrace();
				return;
			} catch (ArrayIndexOutOfBoundsException e){
				System.err.println("L'adresse du fichier final est absente");
				return;
			}

			System.out.println("compilation terminée");

		}
		else if (args[0].equals("run")){
			Programme p;
			try {
				p = new Programme(args[1]);
			} catch (IOException e) {
				System.err.println("Erreur lors de la lecture du fichier");
				e.printStackTrace();
				return;
			} catch (ArrayIndexOutOfBoundsException e){
				System.err.println("L'adresse du fichier est absente");
				return;
			} catch (FichierIncorrectException e){
				System.err.println("Extension du fichier incorecte (requiert .pr/.prc)");
				return;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return;
			}
			p.executer();
		}
	}

	public static void test(){
		String adresseEntree = JOptionPane.showInputDialog("Entrez l'adresse d'entrée");
		String adresseSortie = JOptionPane.showInputDialog("Entrez l'adress de sortie");
		Instruction[] insts;

		try {
			insts = Compilateur.compileFichier(adresseEntree);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (FichierIncorrectException e) {
			System.err.println("fichier invalide");
			return;
		}

		try {
			Compilateur.compileVersFichier(adresseSortie, insts);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}

	public static void test2(){
		String adresse = JOptionPane.showInputDialog("Entrez l'adresse"); // /Users/Hayen/Desktop/premier.prc
		Programme p;


		try {
			p = new Programme(adresse);
		} catch (FileNotFoundException e) {
			System.err.println("Ficher non-trouvé");
			return;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (FichierIncorrectException e) {
			System.err.println("fichier invalde");
			return;
		}

		p.printInstruction();
		p.executer();

	}

	public static void test3(){
		String adresse = JOptionPane.showInputDialog("Entrez l'adresse"); // /Users/Hayen/Desktop/premier.pr
		Programme p;

		try {
			p = new Programme(Compilateur.compileFichier(adresse));
		} catch (FileNotFoundException e) {
			System.err.println("Ficher non-trouvé");
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (FichierIncorrectException e) {
			System.err.println("fichier invalde");
			return;
		}

		p.executer();
		p.printInstruction();

	}

}
