package hayen.robot.programme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import hayen.robot.Direction;
import hayen.robot.Robot;
import hayen.robot.graphisme.Console;
import hayen.robot.graphisme.Grille;
import hayen.robot.graphisme.DrawingPanel;
import hayen.robot.programme.instruction.Instruction;

public class Application extends Thread implements ActionListener{
	private Programme _programme;
	private Grille _grille;
	private Robot _robot;
	private Console _console;

	private DrawingPanel _drawingPanel;
	private JFrame _fenetrePrincipale;
	private JPanel _bouttons;

	private JButton _bouttonStart;
	private JButton _bouttonStop;

	private boolean _enPause = false;
	private boolean _interrompu = true;
	private boolean _vivant = true;
	
	private String _fichier;

	public Application(String fichier, int tailleGrille) throws FileNotFoundException, OperationInvalideException, IOException{
		
		_drawingPanel = new DrawingPanel();
		_fenetrePrincipale = new JFrame();
		_bouttons = new JPanel();
		
		ouvrirFichier(fichier);
		_grille = new Grille(tailleGrille, tailleGrille);
		_console = new Console();
		_robot = new Robot(this);

		_drawingPanel.addObject(_grille);
		_drawingPanel.addObject(_robot);
		_drawingPanel.setPreferredSize(_grille.getPreferredSize());

		_programme.setApp(this);
		_grille.setApp(this);
		_robot.setAnimer(true);

		_bouttons.setLayout(new BoxLayout(_bouttons, BoxLayout.X_AXIS));
		_bouttonStart = new JButton("start"){
			@Override
			public String getActionCommand(){
				if (_interrompu)
					return "start";
				else
					return "pause";
			}
			@Override
			public String getText(){
				if (_interrompu)
					return "start";
				else
					return "pause";
			}
		};
		_bouttonStart.addActionListener(this);
		_bouttonStop = new JButton("stop");
		_bouttonStop.setActionCommand("stop");
		_bouttonStop.addActionListener(this);
		_bouttons.add(_bouttonStart);
		_bouttons.add(_bouttonStop);
		
		reserver();

		_fenetrePrincipale.setLayout(new BoxLayout(_fenetrePrincipale.getContentPane(), BoxLayout.Y_AXIS));
		_fenetrePrincipale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_fenetrePrincipale.getContentPane().add(_drawingPanel);
		_fenetrePrincipale.getContentPane().add(_bouttons);
		_fenetrePrincipale.getContentPane().add(_console);
		_fenetrePrincipale.setResizable(false);
		_fenetrePrincipale.pack();
		_fenetrePrincipale.setLocation(10, 10);
		_fenetrePrincipale.setVisible(true);
	}

	public Grille getGrille(){
		return _grille;
	}
	public Robot getRobot(){
		return _robot;
	}
	public Programme getProgramme(){
		return _programme;
	}
	public DrawingPanel getPanel(){
		return _drawingPanel;
	}
	public Console getConsole(){
		return _console;
	}
	public JFrame getFenetrePrincipale(){
		return _fenetrePrincipale;
	}

	public void run(){
		while (_vivant){
			_programme.incremente();
			Instruction i = _programme.getInstructionA(_programme.getLigne());
			if (i == null){
				_enPause = true;
			}
			if (!_enPause && !_interrompu){
				i.executer(this);
				_programme.prochaineLigne();
				i = _programme.getInstructionA(_programme.getLigne());
			} 
			else{
				try {
					sleep(100);
				} catch (InterruptedException e) {}
			}
		}
	}

	public synchronized void setEnPause(boolean p){
		_enPause = p;
	}
	public synchronized boolean getEnPause(){
		return _enPause;
	}

	public synchronized boolean getEstVivant(){
		return _vivant;
	}
	public synchronized void kill(){
		_vivant = false;
	}

	@Override
	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();

		if (cmd.equals("start")){
			_interrompu = false;
		}
		else if (cmd.equals("pause")){
			_interrompu = true;
		}

		else if (cmd.equals("stop")){
			_interrompu = true;
			_programme.reset();
			_robot.reset();
			_grille.reset();
			_drawingPanel.repaint();
			_programme.reserver("posx", 0);
			_programme.reserver("posy", 0);
		}
		else if (cmd.equals("recharger")){
			_interrompu = true;
			_robot.reset();
			_grille.reset();
			_drawingPanel.repaint();
			try {
				ouvrirFichier(_fichier);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (OperationInvalideException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			reserver();
		}
		// TODO charger
	}
	
	private void ouvrirFichier(String fichier) throws OperationInvalideException, FileNotFoundException, IOException{
		BufferedReader br;
		File f = new File(fichier);
		br = new BufferedReader(new FileReader(f));
		_fichier = fichier;
		StringBuilder sb = new StringBuilder();
		String ln = br.readLine();
		while (ln != null){
			sb.append(ln + '\n');
			ln = br.readLine();
		}
		
		br.close();
		_programme = new Programme(Compilateur2.compile(sb.toString()));
		_fenetrePrincipale.setTitle(f.getName());
	}
	
	private void reserver(){
		if (_programme == null)
			return;
		_programme.reserver("largeur", (int)_grille.getTailleGrille().getX());
		_programme.reserver("hauteur", (int)_grille.getTailleGrille().getY());
		_programme.reserver("posx", 0);
		_programme.reserver("posy", 0);
		_programme.reserver("orientation", _robot.getOrientation());
		_programme.reserver("gauche", 1);
		_programme.reserver("droite", -1);
		_programme.reserver("nord", Direction.Nord);
		_programme.reserver("ouest", Direction.Ouest);
		_programme.reserver("sud", Direction.Sud);
		_programme.reserver("est", Direction.Est);
	}
	
}
