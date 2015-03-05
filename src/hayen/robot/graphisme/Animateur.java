package hayen.robot.graphisme;

import hayen.robot.Robot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Animateur implements ActionListener {

	private Animation _anim;
	private Robot _obj;
	private Timer _timer;
	private boolean _running = false;
	
	public Animateur(Robot obj, int refresh){
		_timer = new Timer(refresh, this);
		_obj = obj;
		_timer.start();
	}
	public Animateur(Robot obj, int refresh, int delay){
		_timer = new Timer(refresh, this);
		_obj = obj;
		_timer.setInitialDelay(delay);
	}
	
	public Animateur setAnimation(Animation anim){
		_anim = anim;
		return this;
	}
	
	public Animation getAnimation(){
		return _anim;
	}
	
	public void start(){
		_running = true;
		_timer.start();
	}
	public void pause(){
		_running = false;
		_timer.stop();
	}
	public void stop(){
		_running = false;
		_timer.stop();
		_timer.restart();
		_obj.getApp().setEnPause(false);
	}
	
	public void playAnimation(Animation anim){
		pause();
		_anim = anim;
		start();
		_running = true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (_running){
			if (_anim.run(_obj, _timer.getDelay())){
				_timer.stop();
				_obj.getApp().setEnPause(false);
			}
			_obj.getApp().getPanel().repaint();
		}
	}
	
}
