package hayen.robot;

import java.awt.HeadlessException;
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

	public static void main(String[] args) throws InterruptedException, FileNotFoundException, ClassNotFoundException, IOException, FichierIncorrectException, OperationInvalideException {
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
	
	public static void defaut1() throws InterruptedException, HeadlessException, FileNotFoundException, ClassNotFoundException, IOException, FichierIncorrectException, OperationInvalideException{
		Grille g = new Grille();
		Programme p = new Programme("/Users/Hayen/Documents/dev/java/Robot/source/exemple.pr");
		JFrame f = new JFrame();
		
		f.setTitle("TEST");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(g);
		f.setResizable(false);
		f.pack();
		f.setLocation(10, 10);
		f.setVisible(true);
		p.setGrille(g);
		
		p.executer();
	}

	public static void defaut2() throws InterruptedException, HeadlessException, FileNotFoundException, ClassNotFoundException, IOException, FichierIncorrectException, OperationInvalideException{
		Grille g = new Grille();
		Programme p = new Programme("/Users/Hayen/Documents/dev/java/Robot/source/exempleRobot.pr");
		JFrame f = new JFrame();
		
		f.setTitle("TEST");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(g);
		f.setResizable(false);
		f.pack();
		f.setLocation(10, 10);
		f.setVisible(true);
		p.setGrille(g);
		
		p.executer();
	}
	
	public static void test() throws FileNotFoundException, ClassNotFoundException, IOException, FichierIncorrectException, OperationInvalideException{
		Grille g = new Grille();
		Programme p = new Programme(JOptionPane.showInputDialog("Entrez l'adresse"));
		JFrame f = new JFrame();
		
		f.setTitle("TEST");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(g);
		f.setResizable(false);
		f.pack();
		f.setLocation(10, 10);
		f.setVisible(true);
		p.setGrille(g);
		
		p.executer();
	}
	
}
