package hayen.robot;

import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hayen.robot.graphisme.Console;
import hayen.robot.graphisme.Grille;
import hayen.robot.graphisme.Panel;
import hayen.robot.programme.FichierIncorrectException;
import hayen.robot.programme.OperationInvalideException;
import hayen.robot.programme.Programme;
import hayen.robot.programme.instruction.Assigner;
public class Main {

	// FileNotFoundException, ClassNotFoundException, IOException, FichierIncorrectException, OperationInvalideException
	public static void main(String[] args) throws InterruptedException, FileNotFoundException, ClassNotFoundException, IOException, FichierIncorrectException, OperationInvalideException {
//		defaut2();
		
		Object[] op = {'(', 1, '+', "vachon", ')', '*', '(', 4, '-', '(',  5, '/', 3, ')', ')'};
		Hashtable<String, Integer> v = new Hashtable<String, Integer>();
		v.put("vachon", 2);
		Assigner.evaluer(op, v);
	}
	
	public static void defaut1() throws InterruptedException, HeadlessException, FileNotFoundException, ClassNotFoundException, IOException, FichierIncorrectException, OperationInvalideException{
		Grille g = new Grille();
		Programme po = new Programme("/Users/Hayen/Documents/dev/java/Robot/source/exemple.pr");
		JFrame f = new JFrame();
		Console c = new Console(10, 45);
		Panel pa = new Panel();
		Robot r = g.getRobot();
		
		pa.addObject(g);
		pa.addObject(r);
		pa.setPreferredSize(g.getPreferredSize());
		f.setTitle("TEST");
		f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(pa);
		f.getContentPane().add(c);
		f.setResizable(false);
		f.pack();
		f.setLocation(10, 10);
		f.setVisible(true);
		po.setGrille(g);
		po.setConsole(c);
		
		po.executer();
	}

	public static void defaut2() throws InterruptedException, HeadlessException, FileNotFoundException, ClassNotFoundException, IOException, FichierIncorrectException, OperationInvalideException{
		Grille g = new Grille();
		Programme po = new Programme("/Users/Hayen/Documents/dev/java/Robot/source/exempleRobot2.pr");
		JFrame f = new JFrame();
		Console c = new Console(10, 45);
		Panel pa = new Panel();
		Robot r = g.getRobot();
		
		pa.addObject(g);
		pa.addObject(r);
		pa.setPreferredSize(g.getPreferredSize());
		pa.startRefresh(10);
		
		r.setAnimer(true);
		f.setTitle("TEST");
		f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(pa);
		f.getContentPane().add(c);
		f.setResizable(false);
		f.pack();
		f.setLocation(10, 10);
		f.setVisible(true);
		po.setGrille(g);
		po.setConsole(c);
		
		po.start();
	}
	
	public static void test() throws FileNotFoundException, ClassNotFoundException, IOException, FichierIncorrectException, OperationInvalideException{
		Grille g = new Grille();
		Programme po = new Programme(JOptionPane.showInputDialog("Entrez l'adresse"));
		JFrame f = new JFrame();
		Console c = new Console(10, 45);
		Panel pa = new Panel();
		Robot r = g.getRobot();
		
		pa.addObject(g);
		pa.addObject(r);
		pa.setPreferredSize(g.getPreferredSize());
		f.setTitle("TEST");
		f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(pa);
		f.getContentPane().add(c);
		f.setResizable(false);
		f.pack();
		f.setLocation(10, 10);
		f.setVisible(true);
		po.setGrille(g);
		po.setConsole(c);
		
		po.executer();
	}
	
}
