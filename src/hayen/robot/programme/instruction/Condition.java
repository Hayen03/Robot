package hayen.robot.programme.instruction;

import java.io.DataOutputStream;
import java.io.IOException;

import hayen.robot.programme.Programme;

public class Condition extends Instruction {
	
	private Object[] _expression;
	private int _indentation;
	
	public Condition(int ind, Object... params){
		_expression = params;
		_indentation = ind;
	}

	@Override
	public boolean run(Programme p) {
		boolean retour = true;
		int tmp1 = 0;
		
		Object t1;
		int i = 0;
		while (i < _expression.length){
			t1 = _expression[i];
			if (!t1.getClass().equals(Character.class)){ // si c'est une valeur
				if (t1.getClass().equals(String.class)) tmp1 = p.getVariable((String)t1);
				else tmp1 = (Integer)t1;
			}
			else { // si c'est un opérateur
//				System.out.print("OPERATEUR ");
				int tmp2 = 0;
				Object t2 = _expression[i+1];
				if (t2.getClass().equals(String.class)) tmp1 = p.getVariable((String)t2);
				else tmp2 = (Integer)t2;
				
				char op = (Character)t1;
				switch (op){
				case '<':
//					System.out.print("Evaluation... " + retour + "&" + (tmp1 < tmp2));
					retour = retour && tmp1 < tmp2;
//					System.out.print("; " + retour + ": "); 
					break;
				case '>':
//					System.out.print("Evaluation... " + retour + "&" + (tmp1 > tmp2));
					retour = retour && tmp1 > tmp2;
//					System.out.print("; " + retour + ": ");
					break;
				case '=':
//					System.out.print("Evaluation... " + retour + "&" + (tmp1 == tmp2));
					retour = retour && tmp1 == tmp2;
//					System.out.print("; " + retour + ": ");
					break;
				}
				
			}
			i++;
		}
		
		return retour;
	}
	
	public int getIndentation(){
		return _indentation;
	}
	
	public String toString(){
		return "Condition: (" + _indentation + ")"; 
	}

	@Override
	public void enregistrer(DataOutputStream fichier) throws IOException {
		fichier.writeByte(Instruction.type.condition.numero);
		fichier.writeChar('#');
		fichier.writeInt(_indentation);
		fichier.writeChar('#');
		fichier.writeInt(_expression.length);
		for (Object obj : _expression){
			if (obj.getClass().equals(String.class)){
				fichier.writeChar('$');
				fichier.writeUTF((String)obj);
			}
			else if (obj.getClass().equals(Character.class)){
				fichier.writeChar('?');
				fichier.writeChar((Character)obj);
			}
			else {
				fichier.writeChar('#');
				fichier.writeInt((Integer)obj);
			}
		}
		fichier.writeChar('&');
		
	}

}
