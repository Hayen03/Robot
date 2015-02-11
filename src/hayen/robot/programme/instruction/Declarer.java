package hayen.robot.programme.instruction;

import hayen.robot.programme.Programme;

public class Declarer extends Instruction {
	
	private static final long serialVersionUID = -3147632128806040991L;
	
	private String[] _nom;
	
	public Declarer(String... nom){
		_nom = nom;
	}

	@Override
	public boolean executer(Object... params){
		Programme p = (Programme)params[0];
		for (String n : _nom) p.assigner(n, 0);
		return true;
	}
	
	@Override
	public String toString(){
		String n = "";
		for (String s : _nom) n += s + ", ";
		return "Declaration: " + n;
	}

}
