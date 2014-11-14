package hayen.robot.programme.instruction;

//import java.io.DataOutputStream;
//import java.io.IOException;

import hayen.robot.programme.Programme;

public class Declarer extends Instruction {
	
	private static final long serialVersionUID = -3147632128806040991L;
	
	private String[] _nom;
	
	public Declarer(String... nom){
		_nom = nom;
	}

	@Override
	public boolean run(Programme p) {
		for (String n : _nom) p.assigner(n, 0);
		return true;
	}
	
	@Override
	public String toString(){
		String n = "";
		for (String s : _nom) n += s + ", ";
		return "Declaration: " + n;
	}

/*	@Override
	public void enregistrer(DataOutputStream fichier) throws IOException {
		fichier.writeByte(Instruction.type.declarer.numero);
		fichier.writeChar('#');
		fichier.writeInt(_nom.length);
		for (String str : _nom) {
			fichier.writeChar('$');
			fichier.writeUTF(str);
		}
		fichier.writeChar('&');
	}
*/
}
