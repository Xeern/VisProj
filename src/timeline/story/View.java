package timeline.story;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class View extends JPanel {
	ArrayList<Point2D> storyPoints = new ArrayList<Point2D>();
	
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D)g;
        int midX = getWidth()/2;
        
        Line2D mainLine = new Line2D.Float(midX, 0, midX, getHeight());
        
        g2D.draw(mainLine);
        Point2D start = mainLine.getP1();
        Point2D end = mainLine.getP2();
        for(double y = start.getY(); y <= end.getY(); y += 50) {
        	Point2D tmp = new Point2D.Double(midX-1,y);
        	storyPoints.add(tmp);
        	g2D.drawOval((int)tmp.getX(), (int)tmp.getY(), 2, 2);
        }
	}
}