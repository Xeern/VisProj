package timeline.story;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JPanel;
import timeline.story.Data;

public class View extends JPanel {
	ArrayList<Point2D> storyPoints = new ArrayList<Point2D>();
	ArrayList<Ellipse2D> storyEllipses = new ArrayList<Ellipse2D>();
	Map<Integer, ArrayList<String>> sorted_events = new TreeMap<Integer, ArrayList<String>>(Data.events);
	Map<Point2D,Integer> segmentAtPoint = new HashMap<Point2D,Integer>();
	Color pointColor = Color.BLACK;
	
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D)g;
        int midX = getWidth()/2;

        Line2D mainLine = new Line2D.Float(midX, 0, midX, getHeight());

        g2D.draw(mainLine);
        Point2D start = mainLine.getP2();
        Point2D end = mainLine.getP1();
        
//        Map<Integer, ArrayList<String>> sorted_events = new TreeMap<Integer, ArrayList<String>>(Data.events);
        int ecount = sorted_events.size();
        
        List<Entry<Integer, ArrayList<String>>> list = new ArrayList<Entry<Integer, ArrayList<String>>>(sorted_events.entrySet());
        Entry<Integer, ArrayList<String>> firstInsertedEntry = list.get(0);
        Entry<Integer, ArrayList<String>> lastInsertedEntry = list.get(ecount-1);
        
        int range = lastInsertedEntry.getKey() - firstInsertedEntry.getKey();
        
//        System.out.println(firstInsertedEntry);
//        System.out.println(lastInsertedEntry);
//        System.out.println(sorted_events);
        
        double storyLength = start.distance(end);
        double freeSpace = storyLength*0.02;
        storyLength = storyLength*0.92;
        double storyStep = storyLength/range;
        int currentSegment = firstInsertedEntry.getKey();
        
        for(int i = 0; i <= range; i++) {
        	Point2D currentStoryPoint = new Point2D.Double(midX,start.getY()-(freeSpace+i*storyStep));
        	if (sorted_events.get(currentSegment) != null) {
        		Ellipse2D storyEllipse = new Ellipse2D.Double((int)currentStoryPoint.getX()-2.5, (int)currentStoryPoint.getY()-2.5,5.0,5.0);
     
        		ArrayList<String> label = sorted_events.get(currentSegment);
        		String titel = label.get(0);
        		String date = label.get(1);
        		String content = label.get(2);
        		String characters = label.get(3);
        		
                g2D.setColor(pointColor);
        		g2D.draw(storyEllipse);
        		g2D.fill(storyEllipse);
                g2D.drawString(titel, (int)currentStoryPoint.getX() + 5, (int)currentStoryPoint.getY() - 1);
                segmentAtPoint.put(currentStoryPoint, currentSegment);
        		storyPoints.add(currentStoryPoint);
        		storyEllipses.add(storyEllipse);
        		currentSegment += 1;
        	} else {
        		currentSegment += 1;
        	}
        }
        System.out.println(storyPoints);
	}
}
