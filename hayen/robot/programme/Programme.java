package hayen.robot.programme;

import java.util.Hashtable;
import hayen.robot.programme.instruction.*;

import hayen.robot.programme.instruction.Instruction;

public class Programme {
	private Instruction[] _instructions; // la liste d'instruction
	private Hashtable<String, Integer> _variables; // dictionaire des variables
	private int _ligne; // la ligne à laquelle le programme est rendu
	
	public Programme(Instruction[] instructions){
		_instructions = instructions;
		_variables = new Hashtable<String, Integer>();
		_ligne = 0;
	}
	
	public boolean executer(){
		while (_ligne < _instructions.length) {
//			System.out.print("LIGNE " + (_ligne+1) + ">>> ");
			
			// détecteur de condition
			if (_instructions[_ligne].getClass().equals(Condition.class)){ // si c'est une condition et que la condition est fausse
				if (!_instructions[_ligne].run(this)){
					int indentation = 0;
					while (!_instructions[++_ligne].getClass().equals(Fin.class) && !(indentation > 0)){
						if (_instructions[_ligne].getClass().equals(Condition.class)) indentation++;
						else if (_instructions[_ligne].getClass().equals(Fin.class)) indentation--;
					}
				}
			}
			else {
				_instructions[_ligne].run(this);
			}
//			System.out.println();
			_ligne++;
		}
		return true;
	}
	
	public int getVariable(String id){
		return _variables.get(id).intValue();
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
	
}
