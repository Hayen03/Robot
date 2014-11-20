package hayen.robot.programme;

import hayen.robot.programme.instruction.Instruction;

import java.io.Serializable;
import java.util.Hashtable;

public class Bloc implements Serializable, Executable {
	
	protected final Instruction[] _instructions;
	protected Hashtable<String, Integer> _variables;
	protected int _ligne;
	protected final int _longueur;
	protected Bloc _parent;
	
	public Bloc(Instruction... inst){
		_instructions = inst;
		_variables = new Hashtable<String, Integer>();
		_ligne = 1;
		_longueur = inst.length;
		_parent = null;
	}
	public Bloc(int indentation, Instruction[] inst){
		_instructions = inst;
		_parent = null;
		_variables = new Hashtable<String, Integer>();
		_ligne = 1;
		_longueur = inst.length;
	}
	public Bloc(Bloc parent, Instruction[] inst){
		_instructions = inst;
		_variables = new Hashtable<String, Integer>();
		_parent = parent;
		_ligne = 0;
		_longueur = inst.length;
	}
	public Bloc(Bloc original){
		this._instructions = original._instructions;
		this._ligne = 1;
		this._parent = original._parent;
		this._variables = new Hashtable<String, Integer>();
		this._longueur = this._instructions.length;
	}

	@Override
	public boolean executer(Object... params) {
		for (Instruction i : _instructions){
			i.executer(this);
			_ligne++;
		}
		return true;
	}
	
	public int getVariable(String id){
		int valeur;
		if (!_variables.contains(id) && _parent != null) valeur = _parent.getVariable(id);
		else valeur = _variables.get(id);
		return valeur;
	}
	
	public void assigner(String id, int valeur){
		if (_variables.contains(id) || _parent == null)
			_variables.put(id, valeur);
		else
			_parent.assigner(id, valeur);
	}
	public void assigner(String id, Integer valeur){
		if (_variables.contains(id) || _parent == null)
			_variables.put(id, valeur);
		else 
			_parent.assigner(id, valeur);
	}
	
	public int getLigne() { return _ligne; }
	public void setLigne(int ln){
		_ligne = ln;
	}
	
	public void printInstruction(){
		for (Instruction i : _instructions) System.out.println(i);
	}
	
	public Bloc setParent(Bloc parent){
		_parent = parent;
		return this;
	}
	
}
