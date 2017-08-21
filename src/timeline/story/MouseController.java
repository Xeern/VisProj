package timeline.story;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class MouseController implements MouseListener,MouseMotionListener {
	
	private View view = null;
	
	public void mouseClicked(MouseEvent e) {
		
	}
	
	public void mouseEntered(MouseEvent arg0) {
		
	}

	public void mouseExited(MouseEvent arg0) {
		
	}
	
	public void mousePressed(MouseEvent e) {
		
	}
	
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	public void mouseDragged(MouseEvent e) {
		
	}
	
	public void mouseMoved(MouseEvent e){
		int x = e.getX();
		int y = e.getY();
		for(int i = 0; i < view.storyEllipses.size(); ++i) {
			Ellipse2D tmp = view.storyEllipses.get(i);
			if(tmp.contains(x, y)) {
				System.out.println((tmp.getCenterY()));
			}
		}
	}
	
	public View getView() {
		return view;
	}
	
	public void setView(View view) {
		this.view = view;
	}
}