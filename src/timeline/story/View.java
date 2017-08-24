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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import timeline.story.Data;

import timeline.window.Window;
import timeline.story.StoryTimeline;

public class View extends JPanel {
	StoryTimeline story = new StoryTimeline();
	Window window = new Window();
	boolean overview = true;
	boolean detailView = false;
	
	ArrayList<Point2D> storyPoints = new ArrayList<Point2D>();
	ArrayList<Ellipse2D> storyEllipses = new ArrayList<Ellipse2D>();
	Map<Integer, ArrayList<String>> sorted_events = new TreeMap<Integer, ArrayList<String>>(Data.events);
	Map<Point2D,Integer> segmentAtPoint = new HashMap<Point2D,Integer>();
	
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D)g;
//        g2D.clearRect(0, 0, getWidth(), getHeight());


        if(overview == true) {
			drawOverview(g2D);
        }

        if(detailView == true) {
            int midX = getWidth()/2;
            
            Line2D mainLine = new Line2D.Float(midX, 0, midX, getHeight());
            Point2D start = mainLine.getP2();
            Point2D end = mainLine.getP1();
            
            Point2D storyStart = storyPoints.get(1);
            
            Graphics2D part = g2D;
            part.scale(2, 2);
            part.translate(-getWidth()/4, -getHeight()*2/4);
            g2D.draw(mainLine);
            
            int ecount = sorted_events.size();
            
            List<Entry<Integer, ArrayList<String>>> list = new ArrayList<Entry<Integer, ArrayList<String>>>(sorted_events.entrySet());
            Entry<Integer, ArrayList<String>> firstInsertedEntry = list.get(0);
            Entry<Integer, ArrayList<String>> lastInsertedEntry = list.get(ecount-1);
    		
            int range = lastInsertedEntry.getKey() - firstInsertedEntry.getKey();
    		
            double storyLength = start.distance(end);
            double freeSpace = storyLength*0.02;
            storyLength = storyLength*0.92;
            double storyStep = storyLength/range;
            int currentSegment = firstInsertedEntry.getKey();
            
            storyPoints.clear();
    		storyEllipses.clear();
            for(int i = 0; i <= range; i++) {
            	Point2D currentStoryPoint = new Point2D.Double(midX,start.getY()-(freeSpace+i*storyStep));
            	if (sorted_events.get(currentSegment) != null) {
            		Ellipse2D storyEllipse = new Ellipse2D.Double((int)currentStoryPoint.getX()-2.5, (int)currentStoryPoint.getY()-2.5,5.0,5.0);
         
            		ArrayList<String> label = sorted_events.get(currentSegment);
            		String titel = label.get(0);
            		String date = label.get(1);
            		String content = label.get(2);
            		String characters = label.get(3);
            		
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
        }
        
//        System.out.println(firstInsertedEntry);
//        System.out.println(lastInsertedEntry);
//        System.out.println(sorted_events);		       
	}
	
	public void drawOverview(Graphics2D g2D) {
        int midX = getWidth()/2;
        
        Line2D mainLine = new Line2D.Float(midX, 0, midX, getHeight());
        g2D.draw(mainLine);
        Point2D start = mainLine.getP2();
        Point2D end = mainLine.getP1();
        
        int ecount = sorted_events.size();
        
        List<Entry<Integer, ArrayList<String>>> list = new ArrayList<Entry<Integer, ArrayList<String>>>(sorted_events.entrySet());
        Entry<Integer, ArrayList<String>> firstInsertedEntry = list.get(0);
        Entry<Integer, ArrayList<String>> lastInsertedEntry = list.get(ecount-1);
		
        int range = lastInsertedEntry.getKey() - firstInsertedEntry.getKey();
		
        double storyLength = start.distance(end);
        double freeSpace = storyLength*0.02;
        storyLength = storyLength*0.92;
        double storyStep = storyLength/range;
        int currentSegment = firstInsertedEntry.getKey();
		
		storyPoints.clear();
		storyEllipses.clear();
        for(int i = 0; i <= range; i++) {
        	Point2D currentStoryPoint = new Point2D.Double(midX,start.getY()-(freeSpace+i*storyStep));
        	if (sorted_events.get(currentSegment) != null) {
        		Ellipse2D storyEllipse = new Ellipse2D.Double((int)currentStoryPoint.getX()-2.5, (int)currentStoryPoint.getY()-2.5,5.0,5.0);
     
        		JPanel temp = new JPanel();
        		temp.setBounds((int)currentStoryPoint.getX() + 5, (int)currentStoryPoint.getY() - 21, 200, 20);
        		ArrayList<String> label = sorted_events.get(currentSegment);
        		String title = label.get(0);
        		JLabel jtitle = new JLabel(title);
        		String date = label.get(1);
        		String content = label.get(2);
        		String characters = label.get(3);
        		temp.add(jtitle);
        		this.add(temp);
        		temp.updateUI();
        		
        		g2D.draw(storyEllipse);
        		g2D.fill(storyEllipse);
//                g2D.drawString(title, (int)currentStoryPoint.getX() + 5, (int)currentStoryPoint.getY() - 1);
                segmentAtPoint.put(currentStoryPoint, currentSegment);
        		storyPoints.add(currentStoryPoint);
        		storyEllipses.add(storyEllipse);
        		currentSegment += 1;
        	} else {
        		currentSegment += 1;
        	}
        }
	}
}
