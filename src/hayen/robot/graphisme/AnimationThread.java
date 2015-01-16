package hayen.robot.graphisme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class AnimationThread extends Thread implements ActionListener {

	private Animation _anim;
	private Timer _timer;
	
	public AnimationThread(Animation anim, int refresh){
		_anim = anim;
		_timer = new Timer(refresh, this);
		this.setDaemon(true);
	}
	
	@Override
	public void run(){
		_timer.start();
//		System.out.println("open");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (_anim.run()){
			_timer.stop();
			_timer = null;
//			System.out.println("\nclose");
		}
	}
	
}
