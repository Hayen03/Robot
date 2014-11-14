package hayen.robot.programme.instruction;

//import java.io.DataOutputStream;
//import java.io.IOException;

import hayen.robot.programme.Programme;

public class Sinon extends Instruction {
	
	private static final long serialVersionUID = -856650277948777509L;
	
	private int _indentation;
	
	public Sinon(int ind){
		_indentation = ind;
	}
	
	@Override
	public boolean run(Programme p) {
		return p.getIndentation() == _indentation;
	}

	public int getIndentation(){ return _indentation; }
	
	@Override
	public String toString(){
		return "Else: (" + _indentation + ")";
	}

/*	@Override
	public void enregistrer(DataOutputStream fichier) throws IOException {
		fichier.writeByte(Instruction.type.sinon.numero);
		fichier.writeChar('#');
		fichier.writeInt(_indentation);
		fichier.writeChar('&');
	}
	*/
}
