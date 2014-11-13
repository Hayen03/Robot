package hayen.robot.programme.instruction;

import java.io.DataOutputStream;
import java.io.IOException;

import hayen.robot.programme.Programme;

public class Fin extends Instruction {
	
	private int _indentation;
	
	public Fin(int ind){
		_indentation = ind;
	}
	
	@Override
	public boolean run(Programme p){
		return p.getIndentation() == _indentation;
	}
	
	@Override
	public String toString(){
		return "End: (" + _indentation + ")";
	}

/*	@Override
	public void enregistrer(DataOutputStream fichier) throws IOException {
		fichier.writeByte(Instruction.type.fin.numero);
		fichier.writeChar('#');
		fichier.writeInt(_indentation);
		fichier.writeChar('&');
	}
	*/
}
