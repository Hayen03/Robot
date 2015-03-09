package hayen.robot.programme;

import java.util.Hashtable;
import java.util.Vector;

import hayen.robot.programme.instruction.Instruction;

public class Programme {
	
	private Application _app;
	
	private final Instruction[] _instructions;
	private Vector<Hashtable<String, Integer>> _variables;
	private int _ligne;
	private final int _longueur;
	
	public Programme(Instruction[] instructions){
		
		_instructions = instructions;
		_variables = new Vector<Hashtable<String, Integer>>();
		_variables.add(new Hashtable<String, Integer>());
		_ligne = 0;
		_longueur = _instructions.length;
	}
	
	public Application getApp(){
		return _app;
	}
	public void setApp(Application app){
		_app = app;
	}
	
	public int longueur(){
		return _longueur;
	}
	
	public synchronized Instruction getInstructionA(int ln){
		if (ln >= _instructions.length)
			return null;
		else
			return _instructions[ln];
	}
	public synchronized int getLigne(){
		return _ligne;
	}
	public synchronized void setLigne(int ln){
		_ligne = ln;
	}
	public synchronized void prochaineLigne(){
		if (_ligne < _instructions.length)
			_ligne++;
	}
	
	public synchronized void assigner(String id, int n){
		int i = 0;
		while (i < _variables.size()-1){
			if (_variables.get(i).containsKey(id)){
				_variables.get(i).put(id, n);
				return;
			}
			else
				i++;
		}
		_variables.get(i).put(id, n);
	}
	public synchronized int getVariable(String id){
		int i = 0;
		while (i < _variables.size()){
			if (_variables.get(i).containsKey(id)){
				return _variables.get(i).get(id);
			}
			else
				i++;
		}
		return 0;
	}
	
	public Programme reset(){
		_ligne = 0;
		Hashtable<String, Integer> buffer = _variables.get(0);
		_variables = new Vector<Hashtable<String, Integer>>();
		_variables.add(buffer);
		return this;
	}
	
	public synchronized void incremente(){
		_variables.add(new Hashtable<String, Integer>());
	}
	public synchronized void decremente(){
		if (_variables.size() > 1)
			_variables.remove(_variables.size()-1);
	}
	
	public synchronized void reserver(String id, int v){
		_variables.get(0).put(id, v);
	}
	
}
