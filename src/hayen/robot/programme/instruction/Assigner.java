package hayen.robot.programme.instruction;

import hayen.robot.programme.Programme;
import hayen.robot.util.Util;

public class Assigner extends Instruction {

	public static final char opAdd = '+';
	public static final char opMin = '-';
	public static final char opMul = '*';
	public static final char opDiv = '/';
	public static final char opMod = '%';

	private String[] _ids;
	private Object[] _termes;

	public Assigner(String[] identificateurs, Object[] termes){
		_ids = identificateurs;
		_termes = termes;
	}

	@Override
	public boolean executer(Object... params){
		Programme p =(Programme)params[0];

		if (_termes == null){ // s'il n'y a aucun terme, c'est une déclaration de variable
			for (String id : _ids)
				p.assigner(id, 0);
			return true;
		}
		else { // autrement, on évalue l'expression, puis on assigne les variables
			int v = Util.evaluer(_termes, p);
			for (String id : _ids)
				p.assigner(id, v);
			return true;
		}
	}

	@Override
	public String toString(){
		String s = "Assign: ";
		for (String n : _ids) s += n + ", ";
		return s;
	}

}
