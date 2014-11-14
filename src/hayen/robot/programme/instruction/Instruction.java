package hayen.robot.programme.instruction;

import hayen.robot.programme.Executable;

import java.io.Serializable;

public abstract class Instruction implements Serializable, Executable {
	
	private static final long serialVersionUID = 2417554043198191652L;
	
	public enum type{
		declarer(0, Declarer.class),
		assigner(1, Assigner.class),
		afficher(2, Afficher.class),
		condition(3, Condition.class),
		fin(4, Fin.class),
		sinon(5, Sinon.class),
		boucle(6, Boucle.class);
		
		public final byte numero;
		@SuppressWarnings("rawtypes")
		public final Class classe;
		
		private type(int numero, @SuppressWarnings("rawtypes") Class classe){
			this.numero = (byte)numero;
			this.classe = classe;
		}
		
	}
	
}
