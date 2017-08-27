package timeline.story;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
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
	Map<Point2D,JPanel> panelAtPoint = new HashMap<Point2D,JPanel>();
	
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D)g;
//        g2D.clearRect(0, 0, getWidth(), getHeight());


        if(overview == true) {
			drawOverview(g2D);
        }

        if(detailView == true) {
            int midX = getWidth()/2;
            
            Line2D mainLine = new Line2D.Float(midX, 0, midX, getHeight());
            
            Point2D storyStart = storyPoints.get(1);
            
            Graphics2D part = g2D;
            part.scale(2, 2);
            part.translate(-getWidth()/4, -getHeight()*2/4);
            g2D.draw(mainLine);
            
            int ecount = sorted_events.size();
              
            List<Entry<Integer, ArrayList<String>>> list = new ArrayList<Entry<Integer, ArrayList<String>>>(sorted_events.entrySet());
    		
            createStorypoints(g2D, mainLine, ecount, list);
        }
        
//        System.out.println(firstInsertedEntry);
//        System.out.println(lastInsertedEntry);
//        System.out.println(sorted_events);		       
	}
	
	public void drawOverview(Graphics2D g2D) {
        int midX = getWidth()/2;
        
        Line2D mainLine = new Line2D.Float(midX, 0, midX, getHeight());
        g2D.draw(mainLine);
        
        int ecount = sorted_events.size();
        
        List<Entry<Integer, ArrayList<String>>> list = new ArrayList<Entry<Integer, ArrayList<String>>>(sorted_events.entrySet());
        
        createStorypoints(g2D, mainLine, ecount, list);
	}
	
	
	public void createStorypoints(Graphics2D g, Line2D line,int ecount , List<Entry<Integer, ArrayList<String>>> list) {
		Point2D start = line.getP2();
		Point2D end = line.getP1();
		
        Entry<Integer, ArrayList<String>> firstInsertedEntry = list.get(0);
        Entry<Integer, ArrayList<String>> lastInsertedEntry = list.get(ecount-1);
		
        int range = lastInsertedEntry.getKey() - firstInsertedEntry.getKey();
		
        double storyLength = start.distance(end);
        double freeSpace = storyLength*0.02;
        storyLength = storyLength*0.92;
        double storyStep = storyLength/range;
        int currentSegment = firstInsertedEntry.getKey();
        
        int datecounter = 0; // to check if there are duplicate dates
        Point2D DcheckPoint = start; // date check point 
        
        int midX = getWidth()/2;
		
		storyPoints.clear();
		storyEllipses.clear();
        for(int i = 0; i <= range; i++) {
        	Point2D currentStoryPoint = new Point2D.Double(getWidth()/2,start.getY()-(freeSpace+i*storyStep));
        	if (sorted_events.get(currentSegment) != null) {
        		Ellipse2D storyEllipse = new Ellipse2D.Double((int)currentStoryPoint.getX()-2.5, (int)currentStoryPoint.getY()-2.5,5.0,5.0);
     
        		ArrayList<String> labels = sorted_events.get(currentSegment);
        		String title = labels.get(0);
        		
        		JPanel viewPanel = new JPanel();
        		if(detailView == true) {
            		currentStoryPoint.setLocation(currentStoryPoint.getX(), (currentStoryPoint.getY()-getHeight()*2/4)*2);
        		}
        		viewPanel.setLayout((LayoutManager) new FlowLayout(FlowLayout.LEFT));
        		viewPanel.setBounds((int)currentStoryPoint.getX() + 5, (int)currentStoryPoint.getY() - 21, 200, 20);
        		
        		JLabel jtitle = new JLabel(title);
        		viewPanel.add(jtitle);
        		this.add(viewPanel);
        		viewPanel.updateUI();
        		 
        		String date = labels.get(1);
        		
        		for(int j = 1; j<=31; j++) { // check for the next month if the date occurs more than once
        			if(sorted_events.get(currentSegment+j) != null) {                		
        				ArrayList<String> testlabels = sorted_events.get(currentSegment+j);
        				String testdate = testlabels.get(1);
        				System.out.println(date + testdate);
        				if (date.equalsIgnoreCase(testdate)) { // if so raise the counter
        					datecounter += 1;
        					DcheckPoint = new Point2D.Double(midX,start.getY()-(freeSpace+i*storyStep));
        					System.out.println("found duplicate");
        				}
        			}
        		}
        		
        		System.out.println(datecounter);
        		
        		if (datecounter == 1) { // that should mean that you can draw the label vertical from first duplicate to last
        			int counterstart = i-datecounter; // get range
        			DcheckPoint = new Point2D.Double(midX,start.getY()-(freeSpace+counterstart*storyStep));
        			int yrange = (int)currentStoryPoint.getY() - (int)DcheckPoint.getY();
        			JPanel datePanel = new JPanel();
            		datePanel.setLayout((LayoutManager) new FlowLayout(FlowLayout.RIGHT));
            		datePanel.setBounds((int)currentStoryPoint.getX() - 105, (int)currentStoryPoint.getY() -18, 100, yrange);
            		JLabel jdate = new JLabel(date);
        			jdate.setFont(new Font("Courier New",Font.ITALIC,8));
        			jdate.setText(verticalText(date));
        			datePanel.add(jdate);
        			this.add(datePanel);
        			datePanel.updateUI();
        			datecounter = 0;
        		} else if (datecounter > 1) {
        			datecounter = 1;
        		} else {
        			JPanel datePanel = new JPanel();
            		datePanel.setLayout((LayoutManager) new FlowLayout(FlowLayout.RIGHT));
            		datePanel.setBounds((int)currentStoryPoint.getX() - 105, (int)currentStoryPoint.getY() -18, 100, 20);
        			JLabel jdate = new JLabel(date);
        			jdate.setFont(new Font("Courier New",Font.ITALIC,10));
        			datePanel.add(jdate);
        			this.add(datePanel);
        			datePanel.updateUI();
        		}
        		
//        		String content = labels.get(2);
//        		String characters = labels.get(3);
        		
        		
        		g.draw(storyEllipse);
        		g.fill(storyEllipse);
                segmentAtPoint.put(currentStoryPoint, currentSegment);
                panelAtPoint.put(currentStoryPoint, viewPanel);
        		storyPoints.add(currentStoryPoint);
        		storyEllipses.add(storyEllipse);
        		currentSegment += 1;
        	} else {
        		currentSegment += 1;
        	}
        }
	}

	public static String verticalText(String horizontal) { // fancy vertical label
		String temp = "<html>";
		String newline = "<br>";
		String [] splitter = horizontal.split("");
		for (String letter : splitter) {
			temp += letter + newline;
		}
		temp += "</html>";
		return temp;
	}
}
