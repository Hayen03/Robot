package hayen.robot.programme.instruction;

import hayen.robot.programme.Application;

public abstract class Instruction {
	public static final int codeAfficher = 0;
	public static final int codeAssigner = 0;
	public static final int codeAvancer = 0;
	public static final int codeCondition = 0;
	public static final int codeFin = 0;
	public static final int codePlacer = 0;
	public static final int codetourner = 0;
	
	public abstract void executer(Application app);
	
}
