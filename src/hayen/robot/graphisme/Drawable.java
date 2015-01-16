package hayen.robot.graphisme;

import java.awt.Graphics2D;

public interface Drawable {
	public void draw(Graphics2D g2);
	public void setPanel(Panel p);
	public Panel getPanel();
}
