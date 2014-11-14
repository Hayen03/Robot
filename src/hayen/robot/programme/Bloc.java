package hayen.robot.programme;

import hayen.robot.programme.instruction.Instruction;

import java.io.Serializable;
import java.util.Hashtable;

public class Bloc implements Serializable, Executable {
	
	protected final Instruction[] _instructions;
	protected Hashtable<String, Integer> _variables;
	protected int _ligne;
	protected final int _depart;
	protected final int _longueur;
	protected final int _indentation;
	protected final Bloc _parent;
	
	public Bloc(Instruction... inst){
		_instructions = inst;
		_variables = new Hashtable<String, Integer>();
		_ligne = 1;
		_depart = 1;
		_longueur = inst.length;
		_indentation = 0;
		_parent = null;
	}
	public Bloc(Bloc parent, Instruction... inst){
		_instructions = inst;
		_variables = new Hashtable<String, Integer>();
		_parent = parent;
		_ligne = parent._depart + parent._longueur;
		_depart = _ligne;
		_longueur = inst.length;
		_indentation = parent._indentation + 1;
	}

	@Override
	public boolean executer(Object... params) {
		for (Instruction i : _instructions) i.executer(this);
		return true;
	}
	
	public int getVariable(String id){
		int valeur;
		if (!_variables.contains(id) && _parent != null) valeur = _parent.getVariable(id);
		else valeur = _variables.get(id);
		return valeur;
	}
	
	public void assigner(String id, int valeur){
		_variables.put(id, valeur);
	}
	public void assigner(String id, Integer valeur){
		_variables.put(id, valeur);
	}
	
	public int getLigne() { return _ligne; }
	public void setLigne(int ln){
		_ligne = ln;
	}
	
	public int getIndentation(){
		return _indentation;
	}
	
	public void printInstruction(){
		for (Instruction i : _instructions) System.out.println(i);
	}

}
