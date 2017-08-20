package timeline.story;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JPanel;
import timeline.story.Data;

public class View extends JPanel {
	ArrayList<Point2D> storyPoints = new ArrayList<Point2D>();

	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D)g;
        int midX = getWidth()/2;

        Line2D mainLine = new Line2D.Float(midX, 0, midX, getHeight());

        g2D.draw(mainLine);
        Point2D start = mainLine.getP2();
        Point2D end = mainLine.getP1();
        
        Map<Integer, ArrayList<String>> sorted_events = new TreeMap<Integer, ArrayList<String>>(Data.events);
        int ecount = sorted_events.size();
        
        List<Entry<Integer, ArrayList<String>>> list = new ArrayList<Entry<Integer, ArrayList<String>>>(sorted_events.entrySet());
        Entry<Integer, ArrayList<String>> firstInsertedEntry = list.get(0);
        Entry<Integer, ArrayList<String>> lastInsertedEntry = list.get(ecount-1);
        
        int range = lastInsertedEntry.getKey() - firstInsertedEntry.getKey();
        
//        System.out.println(firstInsertedEntry);
//        System.out.println(lastInsertedEntry);
//        System.out.println(sorted_events);
        
        double storyLength = start.distance(end);
        double freeSpace = storyLength*0.01;
        storyLength = storyLength*0.98;
        double storyStep = storyLength/range;
        int currentSegment = firstInsertedEntry.getKey();
        
        for(int i = 0; i <= range; i++) {
        	Point2D currentStoryPoint = new Point2D.Double(midX-3,start.getY()-(freeSpace+i*storyStep));
        	if (sorted_events.get(currentSegment) != null) {
        		ArrayList<String> label = sorted_events.get(currentSegment);
        		g2D.drawOval((int)currentStoryPoint.getX(), (int)currentStoryPoint.getY(), 6, 1);
        		currentSegment += 1;
        		storyPoints.add(currentStoryPoint);
        			} else {
        		currentSegment += 1;
        		}
        }
        System.out.println(storyPoints);
	}
}
