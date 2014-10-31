package hayen.robot.programme.instruction;

import hayen.robot.programme.Programme;

public class Declarer extends Instruction {
	
	private String[] _nom;
	
	public Declarer(String... nom){
		_nom = nom;
	}

	@Override
	public boolean run(Programme p) {
		for (String n : _nom) p.assigner(n, 0);
		return true;
	}

}
