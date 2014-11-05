package hayen.robot;

import java.io.IOException;

import javax.swing.JOptionPane;

import hayen.robot.programme.Compilateur;
import hayen.robot.programme.Programme;

public class Main {

	public static void main(String[] args) {
		Programme p = null;
		String adresse = JOptionPane.showInputDialog("Entrez l'adresse du fichier");
		try{
			p = new Programme(Compilateur.compileFichier(adresse));
		} catch(IOException e){
			System.err.println("Erreur lors de la lecture du fichier");
			e.printStackTrace();
			System.exit(-1);
		}
		if (p != null) p.executer();
	}

}
