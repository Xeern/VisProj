package timeline.story;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import timeline.story.View;

public class MouseController implements MouseListener,MouseMotionListener {
	
	private View view = null;
	
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		for(int i = 0; i < view.storyEllipses.size(); ++i) {
			Ellipse2D tmp = view.storyEllipses.get(i);
			if(tmp.contains(x, y)) {
				Point2D currentPoint = view.storyPoints.get(i);
				int currentSegment = view.segmentAtPoint.get(currentPoint);
				ArrayList<String> currentEvent = view.sorted_events.get(currentSegment);
				System.out.println(currentEvent);
			} else {

			}
		}
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
		Graphics2D currentGraphic = (Graphics2D)view.getGraphics();
		if(view.overview == true) {
			for(int i = 0; i < view.storyEllipses.size(); ++i) {
				Ellipse2D tmp = view.storyEllipses.get(i);
				if(tmp.contains(x, y)) {
					currentGraphic.setColor(Color.BLUE);
					currentGraphic.draw(tmp);
					currentGraphic.fill(tmp);
				} else {
					currentGraphic.setColor(Color.BLACK);
					currentGraphic.draw(tmp);
					currentGraphic.fill(tmp);
				}
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