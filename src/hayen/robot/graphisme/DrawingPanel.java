package hayen.robot.graphisme;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class DrawingPanel extends Component{

	private static final long serialVersionUID = 1331855797238651386L;
	
	private Drawable[] _obj;
	
	public DrawingPanel(){
		_obj = new Drawable[0];
	}
	public DrawingPanel(Drawable... objects){
		_obj = objects;
	}
	
	public void addObject(Drawable obj){
		Drawable[] newArray = new Drawable[_obj.length + 1];
		for (int i = 0; i < _obj.length; i++)
			newArray[i] = _obj[i];
		newArray[_obj.length] = obj;
		_obj = newArray;
	}
	
	public Drawable removeObject(int i){
		Drawable[] newArray = new Drawable[_obj.length - 1];
		int n = 0, m = 0;
		while (n < _obj.length){
			if (n != i)
				newArray[m++] = _obj[n];
			n++;	
		}
		Drawable buffer = _obj[i];
		_obj = newArray;
		return buffer;
	}
	public void removeObject(Drawable obj){
		Drawable[] newArray = new Drawable[_obj.length - 1];
		int n = 0, m = 0;
		while (n < _obj.length){
			if (_obj[n] != obj)
				newArray[m++] = _obj[n];
			n++;	
		}
		_obj = newArray;
	}
	
	public int nbOfObject(){
		return _obj.length;
	}
	
	@Override
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		for (int i = 0; i < _obj.length; i++)
			_obj[i].draw(g2);
	}
	
}
