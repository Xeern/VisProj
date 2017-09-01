package timeline.story;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import timeline.story.Data;

import timeline.window.Window;
import timeline.story.StoryTimeline;

public class View extends JPanel {
	StoryTimeline story = new StoryTimeline();
	Window window = new Window();
	int lastHeight = 0;
	boolean firstOpen = true;
	boolean overview = true;
	boolean detailView = false;
	boolean overviewRect = false;
	double translateY = 0.0;
	
	boolean alreadyPainted = false;
	
	ArrayList<Point2D> storyPoints = new ArrayList<Point2D>();
	ArrayList<Ellipse2D> storyEllipses = new ArrayList<Ellipse2D>();
	Map<Integer, ArrayList<String>> sorted_events = new TreeMap<Integer, ArrayList<String>>(Data.events);
	Map<Point2D,Integer> segmentAtPoint = new HashMap<Point2D,Integer>();
	Map<Point2D,JPanel> panelAtPoint = new HashMap<Point2D,JPanel>();
	
	public void paint(Graphics g) {
		System.out.println("____paint");
		System.out.println("view start: " + alreadyPainted);
		Graphics2D g2D = (Graphics2D)g;
//        g2D.clearRect(0, 0, getWidth(), getHeight());
		
		if(translateY != 0.0) {
			if(lastHeight != getHeight()) {
				firstOpen = true;
			}
		}
		if(firstOpen) {
			translateY = getHeight()/2;
			firstOpen = false;
		}

        if(overview == true) {
			drawOverview(g2D);
        }

        if(detailView == true) {
            int midX = getWidth()/2;
            
            Line2D mainLine = new Line2D.Float(midX, 0, midX, getHeight());
    		Shape triDown = createDownTriangle();
    		Shape triUp = createUpTriangle();

			g2D.fill(triDown);
			g2D.fill(triUp);
//          Point2D storyStart = storyPoints.get(1);
            
            Rectangle2D colorRect = new Rectangle2D.Double(0, translateY*0.2, getWidth()*0.2, getHeight()*0.1+1);
            g2D.setColor(new Color(200,200,200,100));
            g2D.fill(colorRect);
            g2D.setColor(new Color(51,51,51));
            
            Graphics2D part = g2D;
            part.scale(2, 2);
            part.translate(-getWidth()/4, -translateY);
            g2D.draw(mainLine);

            
            int ecount = sorted_events.size();
              
            List<Entry<Integer, ArrayList<String>>> list = new ArrayList<Entry<Integer, ArrayList<String>>>(sorted_events.entrySet());
    		
            if(!alreadyPainted) {
            	createStorypoints(g2D, mainLine, ecount, list);
            }
            alreadyPainted = false;
            
			if(this.contains(0, (int)storyPoints.get(ecount-1).getY())) {
				g2D.translate(getWidth()/4, translateY);
				g2D.scale(0.5, 0.5);
				g2D.setColor(new Color(238, 238, 238));
				g2D.fill(triUp);
				g2D.setColor(new Color(51,51,51));
	            g2D.scale(2, 2);
	            g2D.translate(-getWidth()/4, -translateY);
			}
			if(this.contains(0, (int)storyPoints.get(0).getY())) {
				g2D.translate(getWidth()/4, translateY);
				g2D.scale(0.5, 0.5);
				g2D.setColor(new Color(238, 238, 238));
				g2D.fill(triDown);
				g2D.setColor(new Color(51,51,51));
	            g2D.scale(2, 2);
	            g2D.translate(-getWidth()/4, -translateY);
			}
            
            overviewRect = true;
            part.translate(getWidth()/4, translateY);
    		g2D.scale(0.1, 0.1);
    		drawOverview(g2D);
    		overviewRect = false;
        }
        
        
//        System.out.println(firstInsertedEntry);
//        System.out.println(lastInsertedEntry);
//        System.out.println(sorted_events);	
        lastHeight = getHeight();
        System.out.println("view end: " + alreadyPainted);
	}
	
	public Shape createDownTriangle() {
	      final GeneralPath p0 = new GeneralPath();
	      p0.moveTo(getWidth()-80.0f, getHeight()-40);
	      p0.lineTo(getWidth()-40.0f, getHeight()-40);
	      p0.lineTo(getWidth()-60.0f, getHeight()-20);
	      p0.closePath();
	      return p0;
	}
	
	public Shape createUpTriangle() {
	      final GeneralPath p0 = new GeneralPath();
	      p0.moveTo(getWidth()-80.0f, 40);
	      p0.lineTo(getWidth()-40.0f, 40);
	      p0.lineTo(getWidth()-60.0f, 20);
	      p0.closePath();
	      return p0;
	}
	
	public void setTranslateY(int translate) {
		translateY = translate;
	}
	
	public void setTrue() {
		alreadyPainted = true;
	}
	
	public void setFalse() {
		alreadyPainted = false;
	}

	
	public void drawOverview(Graphics2D g2D) {
        int midX = getWidth()/2;
        
        Line2D mainLine = new Line2D.Float(midX, 0, midX, getHeight());
        g2D.draw(mainLine);
        
        int ecount = sorted_events.size();
        
        List<Entry<Integer, ArrayList<String>>> list = new ArrayList<Entry<Integer, ArrayList<String>>>(sorted_events.entrySet());
        
        if(!alreadyPainted) {
        	createStorypoints(g2D, mainLine, ecount, list);
        }
        alreadyPainted = false;
	}
	

	public void drawOverview(String newchar) {
        Graphics2D g2D = (Graphics2D) this.getGraphics();
        
        int midX = getWidth()/2;
        Line2D mainLine = new Line2D.Float(midX, 0, midX, getHeight());
        g2D.draw(mainLine);
        
        List<Entry<Integer, ArrayList<String>>> list = new ArrayList<Entry<Integer, ArrayList<String>>>(sorted_events.entrySet());
       
        List<Entry<Integer, ArrayList<String>>> charlist = new ArrayList<Entry<Integer, ArrayList<String>>>(); 
        
        for(Entry e : list) {
        	ArrayList<String> i = (ArrayList<String>) e.getValue();
        	String chars = i.get(3);
        	String splittoken = Pattern.quote("+");
			String[] splitter = chars.split(splittoken);
			for(String j : splitter) {
				if(j.equalsIgnoreCase(newchar)) {
					charlist.add(e);
					System.out.println(j);
				}
			}
        }
        
        int ecount = charlist.size();
        System.out.println(ecount);
        System.out.println(charlist);
        System.out.println(list);
        
        if(!alreadyPainted) {
        	createStorypoints(g2D, mainLine, ecount, charlist);
        }
        alreadyPainted = false;
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
        ArrayList<String> titles = new ArrayList<String>();
        for(int i = 0; i < list.size(); ++i){
          String temp = list.get(i).getValue().get(0);
          titles.add(temp);
        }
        
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
              
            	for(String s : titles) {
            		int tempCount = 0;
            		if(!title.equalsIgnoreCase(s)) {
            			tempCount +=1;
            		}
            		if(tempCount == titles.size()) {
            			break;
            		}
            	}
              
            	if(overviewRect == true) {
            		currentStoryPoint.setLocation(currentStoryPoint.getX(), (currentStoryPoint.getY()-translateY)*2);
            		g.drawString(title, (int)((currentStoryPoint.getX() + 5)), (int)((currentStoryPoint.getY()*0.5 + translateY)));
                
            		g.draw(storyEllipse);
            		g.fill(storyEllipse);
                    segmentAtPoint.put(currentStoryPoint, currentSegment);
                    storyPoints.add(currentStoryPoint);
                    storyEllipses.add(storyEllipse);
                    currentSegment += 1;
            	} else {
            		JPanel viewPanel = new JPanel();
        		if(detailView == true) {
            		currentStoryPoint.setLocation(currentStoryPoint.getX(), (currentStoryPoint.getY()-translateY)*2);
        		}
        		if(overviewRect == true) {
        			g.drawString(title, (int)((currentStoryPoint.getX() + 5)), (int)((currentStoryPoint.getY()*0.5 + translateY)));
        		}
        		viewPanel.setLayout((LayoutManager) new FlowLayout(FlowLayout.LEFT));
        		viewPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        		viewPanel.setBounds((int)currentStoryPoint.getX() + 10, (int)currentStoryPoint.getY() - 21, 200, 20);
        		
        		JLabel jtitle = new JLabel(title);
        		viewPanel.add(jtitle);
        		this.add(viewPanel);
        		viewPanel.updateUI();
        		 
        		String date = labels.get(1);
        		
        		for(int j = 1; j<=31; j++) { // check for the next month if the date occurs more than once
        			if(sorted_events.get(currentSegment+j) != null) {                		
        				ArrayList<String> testlabels = sorted_events.get(currentSegment+j);
        				String testdate = testlabels.get(1);
        				if (date.equalsIgnoreCase(testdate)) { // if so raise the counter
        					datecounter += 1;
        					DcheckPoint = new Point2D.Double(midX,start.getY()-(freeSpace+i*storyStep));
        				}
        			}
        		}
        		
//        		System.out.println(datecounter);
        		
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
        		}
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
