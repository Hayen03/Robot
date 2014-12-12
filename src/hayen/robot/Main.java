package hayen.robot;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hayen.robot.graphisme.Grille;
import hayen.robot.programme.Bloc;
import hayen.robot.programme.Compilateur;
import hayen.robot.programme.FichierIncorrectException;
import hayen.robot.programme.OperationInvalideException;
import hayen.robot.programme.Programme;
public class Main {

	public static void main(String[] args) {
		if (args.length == 0) test();
		
		else if (args[0].equals("-c") || args[0].equals("compile")){ // si on veut compiler un fichier
			Bloc instructions;
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
			} catch (OperationInvalideException e) {
				e.printStackTrace();
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
			} catch (OperationInvalideException e) {
				e.printStackTrace();
				return;
			}
			p.executer();
		}
	}

	public static void test(){
		
		Grille g = new Grille();
		
		JFrame f = new JFrame();
		f.setTitle("TEST");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(g);
		f.setResizable(false);
		f.pack();
		f.setLocation(10, 10);
		f.setVisible(true);
		
		// oeil droit
		g.setCaseActif(2, 3, true);
		g.setCaseActif(3, 3, true);
		g.setCaseActif(2, 4, true);
		g.setCaseActif(3, 4, true);
		
		// oeil gauche
		g.setCaseActif(6, 3, true);
		g.setCaseActif(7, 3, true);
		g.setCaseActif(6, 4, true);
		g.setCaseActif(7, 4, true);
		
		// bouche
		g.setCaseActif(1, 6, true);
		g.setCaseActif(2, 7, true);
		g.setCaseActif(3, 8, true);
		g.setCaseActif(4, 8, true);
		g.setCaseActif(5, 8, true);
		g.setCaseActif(6, 8, true);
		g.setCaseActif(7, 7, true);
		g.setCaseActif(8, 6, true);
		
	}
	
}
