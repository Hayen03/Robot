package hayen.robot.programme.instruction;

//import java.io.DataOutputStream;
//import java.io.IOException;

import hayen.robot.programme.Programme;

public class Boucle extends Instruction {

	private static final long serialVersionUID = -2859171750583912735L;
	
	private int _ln;
	
	public Boucle(int ln){
		_ln = ln-1;
	}
	
	@Override
	public boolean run(Programme p) {
		p.setLigne(_ln);
		return true;
	}
	
	@Override
	public String toString(){
		return "Boucle: go to " + _ln;
	}

/*	@Override
	public void enregistrer(DataOutputStream fichier) throws IOException {
		fichier.writeByte(Instruction.type.boucle.numero);
		fichier.writeChar('#');
		fichier.writeInt(_ln);
		fichier.writeChar('&');
	}
*/
}
