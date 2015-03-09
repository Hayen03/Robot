package hayen.robot.graphisme;

import java.awt.Dimension;
import java.awt.TextArea;

public class Console extends TextArea {
	public static final int NbLigneDefaut = 10;
	public static final int NbColloneDefaut = 50;
	
	private String _buffer;
	
	public Console(){
		super("", NbLigneDefaut, NbColloneDefaut, TextArea.SCROLLBARS_BOTH);
		this.setEditable(false);
		_buffer = "";
	}
	public Console(int lig, int col){
		super("", lig, col);
		this.setEditable(false);
		_buffer = "";
	}
	
	public void bufferedPrint(int n){
		_buffer += n;
	}
	public void bufferedPrint(char c){
		_buffer += c;
	}
	public void bufferedPrint(String s){
		_buffer += s;
	}
	
	public void print(int n){
		append(Integer.toString(n));
	}
	public void print(char c){
		append(Character.toString(c));
	}
	public void print(String s){
		append(s);
	}
	
	public void flush(){
		append(_buffer);
		_buffer = "";
	}
	
	public void clear(){
		setText("");
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(5*getColumns(), 20*(getRows()+1));
	}
	
}
