package hayen.robot.graphisme;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Panel extends JPanel implements ActionListener{

	private Drawable[] _obj;
	private Timer _timer;
	
	public Panel(){
		_obj = new Drawable[0];
	}
	public Panel(Drawable... objects){
		_obj = objects;
	}
	
	public void addObject(Drawable obj){
		Drawable[] newArray = new Drawable[_obj.length + 1];
		for (int i = 0; i < _obj.length; i++)
			newArray[i] = _obj[i];
		newArray[_obj.length] = obj;
		_obj = newArray;
		obj.setPanel(this);
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
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		for (int i = 0; i < _obj.length; i++)
			_obj[i].draw(g2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
	public void startRefresh(int refreshRate){
		if (_timer == null)
			_timer = new Timer(refreshRate, this);
		else
			_timer.setDelay(refreshRate);
		_timer.start();
	}
	public void stopRefresh(){
		if (_timer != null)
			_timer.stop();
	}
	
}
