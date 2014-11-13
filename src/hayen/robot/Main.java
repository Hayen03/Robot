package hayen.robot;

import java.io.IOException;

import javax.swing.JOptionPane;

import hayen.robot.programme.Compilateur;
import hayen.robot.programme.FichierIncorrectException;
import hayen.robot.programme.Programme;
import hayen.robot.programme.instruction.Instruction;

public class Main {

	public static void main(String[] args) {
		if (args.length == 0) test();
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
	}
	
	public static void test(){
		Programme p = null;
		String adresse = JOptionPane.showInputDialog("Entrez l'adresse du fichier");
		try{
			p = new Programme(Compilateur.compileFichier(adresse));
		} catch(IOException e){
			System.err.println("Erreur lors de la lecture du fichier");
			e.printStackTrace();
			System.exit(-1);
		} catch (FichierIncorrectException e){
			
		}
		if (p != null) p.executer();
	}

}
