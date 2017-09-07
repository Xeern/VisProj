package timeline.story;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
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

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

import timeline.story.Data;

import timeline.window.Window;
import timeline.story.StoryTimeline;

public class View extends JPanel {
	StoryTimeline story = new StoryTimeline();
	Window window = new Window();
	int lastHeight = 0;
	int parts = 4;
	int minScroll = 0;
	int maxScroll = 3;
	int storyRange;
	int lowestStoryKey;
	boolean firstOpen = true;
	boolean overview = true;
	boolean detailView = false;
	boolean overviewRect = false;
	double translateY = 0.0;
	double startPart = (double)(parts-1)/(parts);
	boolean mainView = true;
	boolean charView = false;
	
	double factor = 1.0;
	
	Rectangle2D oRect = null;
	
	List<Entry<Integer, ArrayList<String>>> charlist = new ArrayList<Entry<Integer, ArrayList<String>>>();
	
	boolean alreadyPainted = false;
	
	ArrayList<Point2D> storyPoints = new ArrayList<Point2D>();
	ArrayList<Ellipse2D> storyEllipses = new ArrayList<Ellipse2D>();
	Map<Integer, ArrayList<String>> sorted_events = new TreeMap<Integer, ArrayList<String>>(Data.events);
	Map<Point2D,Integer> segmentAtPoint = new HashMap<Point2D,Integer>();
	Map<Point2D,JPanel> panelAtPoint = new HashMap<Point2D,JPanel>();
	
	static JPanel buttonPanel = new JPanel();
	static JButton newEvent = new JButton("New Event");
	static JButton submit = new JButton("Submit");
	
	public void paint(Graphics g) {
//		System.out.println("____paint");
//		System.out.println("view start: " + alreadyPainted);
		Graphics2D g2D = (Graphics2D)g;
//        g2D.clearRect(0, 0, getWidth(), getHeight());
		if(translateY != 0.0) {
			if(lastHeight != getHeight()) {
				firstOpen = true;
			}
		}
		if(firstOpen) {
			translateY = getHeight()*startPart;
			firstOpen = false;
		}
		

        List<Entry<Integer, ArrayList<String>>> list = new ArrayList<Entry<Integer, ArrayList<String>>>(sorted_events.entrySet());

        int ecount = sorted_events.size();
        
        Entry<Integer, ArrayList<String>> firstInsertedEntry = list.get(0);
        Entry<Integer, ArrayList<String>> lastInsertedEntry = list.get(ecount-1);
        lowestStoryKey = firstInsertedEntry.getKey();
        
        storyRange = lastInsertedEntry.getKey() - firstInsertedEntry.getKey();

        if(overview) {
        	if(mainView) {
        		minScroll = 0;
        		maxScroll = parts-1;
        		drawOverview(g2D, list);
        	} else {
        		drawOverview(g2D, charlist);
        	}
        }

        if(detailView) {
        	if(mainView) {
        		minScroll = 0;
        		maxScroll = parts-1;
        		drawDetailview(g2D, list);
        	} else {
        		drawDetailview(g2D, charlist);
        	}
        }
        lastHeight = getHeight();
//        System.out.println("view end: " + alreadyPainted);
	}
	
	public void getMainview() {
		this.repaint();
		mainView = true;	
	}
	
	public void getScrollRange(List<Entry<Integer, ArrayList<String>>> list) {
		int lowestInt = list.get(0).getKey();
		int highestInt = list.get(list.size()-1).getKey();
		Point2D lowestPoint = null;
		Point2D highestPoint = null;
		for(Point2D p : storyPoints) {
			if(segmentAtPoint.get(p) == lowestInt) {
				lowestPoint = p;
			}
			if(segmentAtPoint.get(p) == highestInt) {
				highestPoint = p;
			}
		}
		int step = 0;
		for(int i = 0; i < parts; i++) {
			if(translateY == (getHeight()/parts)*i) step = i;
		}
		for(int i = parts-1-step; i >= 0-step; i--) {
			Rectangle2D temp = new Rectangle2D.Double(0, (getHeight())*i, getWidth(), getHeight());
			if(temp.contains(lowestPoint)) {
				maxScroll = i+step;
			}
			if(temp.contains(highestPoint)) {
				minScroll = i+step;
			}
		}
	}
	
	public void checkFirstEntry(List<Entry<Integer, ArrayList<String>>> list) {
		int lowestInt = list.get(0).getKey();
		Point2D lowestPoint = null;
		for(Point2D p : storyPoints) {
			if(segmentAtPoint.get(p) == lowestInt) {
				lowestPoint = p;
			}
		}
		for(int i = parts-1; i >= 0; i--) {
			Rectangle2D temp = new Rectangle2D.Double(0, (getHeight()/parts)*i, getWidth(), getHeight()/parts);
			if(temp.contains(lowestPoint)) {
				setTranslateY((getHeight()/parts)*i);
				System.out.println(translateY);
			}
		}
	}
	
	public Shape createCross() {
		GeneralPath p0 = new GeneralPath();
		float x = 20;
		float y = getHeight()-20;
		p0.moveTo(x-10, y+10);
		p0.lineTo(x+10, y-10);
		p0.moveTo(x-10, y-10);
		p0.lineTo(x+10, y+10);
		return p0;
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

	public void drawDetailview(Graphics2D g2D, List<Entry<Integer, ArrayList<String>>> list) {
		int midX = getWidth()/2;
        
        Line2D mainLine = new Line2D.Float(midX, 0, midX, getHeight());
		Shape triDown = createDownTriangle();
		Shape triUp = createUpTriangle();
		Shape cross = createCross();

		g2D.fill(triDown);
		g2D.fill(triUp);
		if(!mainView) {
			g2D.setStroke(new BasicStroke(3));
			g2D.draw(cross);
        	g2D.setStroke(new BasicStroke(1));
		}


        Line2D infLine = new Line2D.Float(midX, 0, midX, (int)translateY+(getHeight()));
        g2D.setStroke(new BasicStroke(3));
        g2D.draw(infLine);
        g2D.setStroke(new BasicStroke(1));
        
        Rectangle2D colorRect = new Rectangle2D.Double(0, translateY*0.2, getWidth()*0.2, getHeight()*0.2/parts+1);
        g2D.setColor(new Color(200,200,200,100));
        g2D.fill(colorRect);
        g2D.setColor(new Color(51,51,51));
        
        oRect = new Rectangle2D.Double(0, 0, getWidth()*0.2, getHeight()*0.2);
        
        if(!alreadyPainted) {
        	createStorypoints(g2D, mainLine, list);
        }
        alreadyPainted = false;
        
        Graphics2D part = g2D;
        part.scale(2*factor, 2*factor);
        part.translate(-getWidth()/4*factor, -translateY*factor);
        g2D.draw(mainLine);
        
		if(this.contains(0, (int)storyPoints.get(storyPoints.size()-1).getY())) {
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
		drawOverview(g2D, list);
		g2D.scale(10, 10);
		g2D.translate(-getWidth()/4, -translateY);

		overviewRect = false;

	}
	
	public void drawOverview(Graphics2D g2D, List<Entry<Integer, ArrayList<String>>> list) {
        int midX = getWidth()/2;

        Line2D mainLine = new Line2D.Float(midX, 0, midX, getHeight());
        Shape cross = createCross();
        g2D.draw(mainLine);
        
		if(!mainView && !overviewRect) {
			g2D.setStroke(new BasicStroke(3));
			g2D.draw(cross);
        	g2D.setStroke(new BasicStroke(1));
		}
        
        if(!alreadyPainted) {
        	createStorypoints(g2D, mainLine, list);
        }
        alreadyPainted = false;
	}
	

	public void drawOverview(String newchar) {
        Graphics2D g2D = (Graphics2D) this.getGraphics();
        g2D.clearRect(0, 0, getWidth(), getHeight());
        Shape cross = createCross();
        charlist.clear();
        
        List<Entry<Integer, ArrayList<String>>> list = new ArrayList<Entry<Integer, ArrayList<String>>>(sorted_events.entrySet());
       
//        List<Entry<Integer, ArrayList<String>>> charlist = new ArrayList<Entry<Integer, ArrayList<String>>>(); 
        
        for(Entry e : list) {
        	ArrayList<String> i = (ArrayList<String>) e.getValue();
        	String chars = i.get(3);
        	String splittoken = Pattern.quote("+");
			String[] splitter = chars.split(splittoken);
			for(String j : splitter) {
				if(j.equalsIgnoreCase(newchar)) {
					charlist.add(e);
//					System.out.println(j);
				}
			}
        }
        
        if(overview) {
			g2D.setStroke(new BasicStroke(3));
        	g2D.draw(cross);
        	g2D.setStroke(new BasicStroke(1));
			drawOverview(g2D, charlist);
        }

        if(detailView) {
			g2D.setStroke(new BasicStroke(3));
        	g2D.draw(cross);
        	g2D.setStroke(new BasicStroke(1));
            drawDetailview(g2D, charlist);
        }
        mainView = false;
	}
	
	
	public void createStorypoints(Graphics2D g, Line2D line, List<Entry<Integer, ArrayList<String>>> list) {
		Point2D start = line.getP2();
		Point2D end = line.getP1();
		
        int range = storyRange;
		
        double storyLength = start.distance(end);
        double freeSpace = storyLength*0.02;
        storyLength = storyLength*0.92;
        double storyStep = storyLength/range;
        int currentSegment = lowestStoryKey;
        ArrayList<String> titles = new ArrayList<String>();
        for(int i = 0; i < list.size(); ++i){
        	String temp = list.get(i).getValue().get(0);
        	titles.add(temp);
        }
//        System.out.println(titles);
        
        int datecounter = 0; // to check if there are duplicate dates
        Point2D DcheckPoint = start; // date check point 
        
        int midX = getWidth()/2;
		
        if(!overviewRect) {
        	storyPoints.clear();
        	storyEllipses.clear();
        }
        
        newEvent.addActionListener(new ButtonListener());
		
        for(int i = 0; i <= range; i++) {
        	boolean breakerino = false;
        	Point2D currentStoryPoint = new Point2D.Double(getWidth()/2,start.getY()-(freeSpace+i*storyStep));
            if (sorted_events.get(currentSegment) != null) {
//            	System.out.println("point: " + i);
            	Ellipse2D storyEllipse = new Ellipse2D.Double((int)currentStoryPoint.getX()-2.5, (int)currentStoryPoint.getY()-2.5,5.0,5.0);
            	ArrayList<String> labels = sorted_events.get(currentSegment);
            	String title = labels.get(0);
              
            	int tempCount = 0;
            	for(String s : titles) {
            		if(!title.equalsIgnoreCase(s)) tempCount += 1;
            		if(tempCount == titles.size()) breakerino = true;
            	}
            	if(breakerino) {
            		currentSegment += 1;
            		continue;
            	}
              
            	if(overviewRect) {
            		currentStoryPoint.setLocation(currentStoryPoint.getX(), (currentStoryPoint.getY()-translateY)*2);
            		g.drawString(title, (int)((currentStoryPoint.getX() + 5)), (int)((currentStoryPoint.getY()*0.5 + translateY)));
                
            		g.draw(storyEllipse);
            		g.fill(storyEllipse);
                    segmentAtPoint.put(currentStoryPoint, currentSegment);
//                    storyPoints.add(currentStoryPoint);
//                    storyEllipses.add(storyEllipse);
                    currentSegment += 1;
            	} else {
            		JPanel viewPanel = new JPanel();
        		if(detailView) {
            		currentStoryPoint.setLocation(currentStoryPoint.getX(), (currentStoryPoint.getY()-translateY)*parts);
            		storyEllipse = new Ellipse2D.Double((int)currentStoryPoint.getX()-5, (int)currentStoryPoint.getY()-2.5,10.0,10.0);
        		}
        		if(overviewRect) {
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
        		
        		buttonPanel.setBounds(30, getHeight()-50, 100, 30);
        		newEvent.setBounds(30, getHeight()-50, 100, 30);
        		buttonPanel.add(newEvent);
        		this.add(buttonPanel);
        		
        		
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
//        if(overviewRect) System.out.println(overviewRect);
//        System.out.println("Story Ellipses: " + storyEllipses.size());
//        System.out.println("Story Points:   " + storyPoints.size());
//        System.out.println("Story Ellipses: " + storyEllipses);
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
	
	public class ButtonListener implements ActionListener {
		  ButtonListener() {
		  }
		  
		  JFrame newEntryFr = new JFrame();  
		  JTextField titlefield = new JTextField("", 15);
	      JTextField datefield = new JTextField("", 12);
	      JTextArea contentfield = new JTextArea(3, 80);
	      JTextField charfield = new JTextField("", 40);
		    
		  public void actionPerformed(ActionEvent e) {
//			  if (e.getActionCommand().equals("New Event")) {
			  if (e.getSource() == newEvent) {
		    	System.out.println("Button has been clicked");
		    	newEntryFr.setSize(500, 300);
		        newEntryFr.setVisible(true);
		        newEntryFr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    	JLabel tif = new JLabel("Title: ");
		    	JLabel daf = new JLabel("Date: ");
		    	JLabel cof = new JLabel("Text: ");
		    	JLabel chf = new JLabel("Characters: ");
		    	contentfield.setOpaque(true);
				contentfield.setLineWrap(true);
				contentfield.setWrapStyleWord(true);
		        JPanel gpanel = new JPanel(new GridBagLayout());
		        GridBagConstraints lcons = new GridBagConstraints();
		        GridBagConstraints tcons = new GridBagConstraints();
		        lcons.gridx = 0;
		        lcons.gridy = 0;
		        gpanel.add(tif, lcons);
		        lcons.gridy = 1;
		        gpanel.add(daf, lcons);
		        lcons.gridy = 2;
		        gpanel.add(cof, lcons);
		        lcons.gridy = 5;
		        gpanel.add(chf, lcons);
		        tcons.fill = GridBagConstraints.HORIZONTAL;
		        tcons.gridwidth = 2;
		        tcons.ipady = 10;
		        tcons.gridx = 1;
		        tcons.gridy = 0;
		        tcons.weightx = 2;
		        gpanel.add(titlefield, tcons);
		        tcons.gridy = 1;
		        gpanel.add(datefield, tcons);
		        tcons.gridy = 2;
		        tcons.gridheight = 3;
		        gpanel.add(contentfield, tcons);
		        tcons.gridy = 5;
		        tcons.gridheight = 1;
		        gpanel.add(charfield, tcons);
		        lcons.gridy = 7;
		        lcons.weightx = 3;
		        lcons.gridwidth = 3;
		        lcons.ipadx = 30;
		        submit.addActionListener(new ButtonListener());
		        gpanel.add(submit, lcons);
		        newEntryFr.add(gpanel);
//		        String titletext = titlefield.getText();
//		        System.out.println(titletext);
			  }
		  
//			  if (e.getActionCommand().equals("New Event")) {
			  if (e.getSource() == submit) {
		    	System.out.println("SUBMIT DAT ASS");
		    	String newtitle = titlefield.getText();
		    	String newdate = datefield.getText();
		    	String newcontent = contentfield.getText();
		    	String newchars = charfield.getText();
		    	if ( newtitle.equals(null) ||  newdate.equals(null) || newcontent.equals(null) || newchars.equals(null)) {
		    		System.out.println("DENIED: Submission not complete.");
		    	} else {
		    		System.out.println("title: " + newtitle);
		    		System.out.println("date: " + newdate);
		    		System.out.println("content: " + newcontent);
		    		System.out.println("chars: " + newchars);
		    		newEntryFr.dispatchEvent(new WindowEvent(newEntryFr, WindowEvent.WINDOW_CLOSING));
		    	}
//		    	JTextField titlefield = new JTextField("", 15);
//		        JTextField datefield = new JTextField("", 12);
//		        JTextArea contentfield = new JTextArea(3, 80);
//		        JTextField charfield = new JTextField("", 40);
//		    	JButton submit = new JButton("Submit");
//		        submit.addActionListener(new ButtonListener());
//		        JPanel gpanel = new JPanel(new GridBagLayout());
//		        GridBagConstraints lcons = new GridBagConstraints();
//		        GridBagConstraints tcons = new GridBagConstraints();
//		        lcons.gridx = 0;
//		        lcons.gridy = 0;
//		        gpanel.add(tif, lcons);
//		        lcons.gridy = 1;
//		        gpanel.add(daf, lcons);
//		        lcons.gridy = 2;
//		        gpanel.add(cof, lcons);
//		        lcons.gridy = 5;
//		        gpanel.add(chf, lcons);
//		        tcons.fill = GridBagConstraints.HORIZONTAL;
//		        tcons.gridwidth = 2;
//		        tcons.ipady = 10;
//		        tcons.gridx = 1;
//		        tcons.gridy = 0;
//		        tcons.weightx = 2;
//		        gpanel.add(titlefield, tcons);
//		        tcons.gridy = 1;
//		        gpanel.add(datefield, tcons);
//		        tcons.gridy = 2;
//		        tcons.gridheight = 3;
//		        gpanel.add(contentfield, tcons);
//		        tcons.gridy = 5;
//		        tcons.gridheight = 1;
//		        gpanel.add(charfield, tcons);
//		        lcons.gridy = 7;
//		        lcons.weightx = 3;
//		        lcons.gridwidth = 3;
//		        lcons.ipadx = 30;
//		        gpanel.add(submit, lcons);
//		        newEntryFr.add(gpanel);
		        
//		        String titletext = titlefield.getText();
//		        System.out.println(titletext);
			  }
		  }
	}
	
//    Timer test = new Timer(2000,scaleIn);
}
