package timeline.story;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import timeline.story.View;

public class MouseController implements MouseListener,MouseMotionListener {
	
	private View view = null;
	
	JPanel openPanel = null;
	Rectangle2D oldBounds = null;
	
	boolean firstClick = true;
	
	int scrollCount = 0;
	
	Timer down = null;
	Timer up = null;
	

	
	
	/* initiate character view
	 * */
	MouseListener labellistener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            JLabel label = (JLabel)e.getComponent();
            String labelchar = label.getText();
            view.drawOverview(labelchar);
            view.checkFirstEntry(view.charlist);
        }
    };
	
	public void mouseClicked(MouseEvent e) {
		if (firstClick) {
			scrollCount = (int)(view.parts * view.startPart);
			firstClick = false;
		}
		int x = e.getX();
		int y = e.getY();
		
		Shape triDown = view.createDownTriangle();
		Shape triUp = view.createUpTriangle();
		Shape cross = view.createCross();
		
		if(view.detailView) {
			/* navigation via overview inside detailview
			 * */
			Rectangle2D overview = view.oRect;
			double height = overview.getHeight()/view.parts;
			if(!view.mainView) {
				view.getScrollRange(view.charlist);
				System.out.println(view.maxScroll + " " + view.minScroll);
			}
			for (int i = 0; i < view.parts; i++) {
				overview.setFrame(new Rectangle2D.Double(0, height*i, overview.getWidth(), height));
				if(overview.contains(x, y) && i <= view.maxScroll && i >= view.minScroll ) {
					if(down != null) down.stop();
					if(up != null) up.stop();
					scrollCount = i;
					view.setTranslateY((view.getHeight()/view.parts)*i);
					view.repaint();
				}
			}
		}
		
		/* exit characterview
		 * */
		if(cross.getBounds2D().contains(x, y)) {
			view.getMainview();
		}
		
		/* decide if scrollbuttons are clickable or not
		 * */
		if(view.detailView) {
			if(!view.contains(0, (int)view.storyPoints.get(view.storyPoints.size()-1).getY())) {
				if(triUp.contains(x,y)) {
					up = new Timer(25, moveUp);
					up.start();
					scrollCount -= 1;
					if(down != null) down.stop();
				}
			}
			if(!view.contains(0, (int)view.storyPoints.get(0).getY())) {
				if(triDown.contains(x,y)) {
					down = new Timer(25, moveDown);
					down.start();
					scrollCount += 1;
					if(up != null) up.stop();
				}
			}
			
		}
		
		/* get selected point
		 * */
		Point2D currentPoint = null;
		for(int i = view.storyEllipses.size()-1; i >= 0; --i) {
			Ellipse2D tmp = view.storyEllipses.get(i);
			if(tmp.contains(x, y)) {
				currentPoint = view.storyPoints.get(i);
//				int currentSegment = view.segmentAtPoint.get(currentPoint);
//				ArrayList<String> currentEvent = view.sorted_events.get(currentSegment);
//				System.out.println(currentEvent);
			}
		}
		
		/* close currently open storypanel
		 * */
		if(openPanel != null) {
			if(!openPanel.getBounds().contains(x, y)) {
				openPanel.setBounds((Rectangle) oldBounds);
			}
		}
		
		/* open selected storypanel
		 * */
		if(currentPoint != null) {
			int currentSegment = view.segmentAtPoint.get(currentPoint);
			ArrayList<String> currentEvent = view.sorted_events.get(currentSegment);
			String content = currentEvent.get(2);
			String characters = currentEvent.get(3);
			int contentRows = (int)(Math.ceil(content.length()/45.0) + Math.ceil(characters.length()/45.0));
		
			JPanel viewPanel = view.panelAtPoint.get(currentPoint);
			oldBounds = (Rectangle2D) viewPanel.getBounds();
			
			viewPanel.setBounds((int)currentPoint.getX() + 8, (int)currentPoint.getY() - 21, 300, 25 + contentRows*22);

			if((currentPoint.getY() - 21 + (25 + contentRows*25)) >= view.lastHeight) {
				int ueberschuss = (int) (currentPoint.getY() - 21 + (contentRows*25) - view.lastHeight) + 10;			
				viewPanel.setBounds((int)currentPoint.getX() + 8, (int)currentPoint.getY() - ueberschuss, 300, contentRows*22);	
			}

			JTextArea textArea = new JTextArea(content);
			textArea.setBounds((int)currentPoint.getX() + 8, (int)currentPoint.getY() -10, 290, 25 + contentRows*55);
			
			textArea.setFont(new Font("Serif", Font.PLAIN, 14));
			textArea.setOpaque(true);
			textArea.setBackground(null);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			viewPanel.add(textArea);
		
			String splittoken = Pattern.quote("+");
			String[] splitter = characters.split(splittoken);
			for(String j : splitter) {
				JLabel jchar = new JLabel(j); 
			    jchar.addMouseListener(labellistener);
				viewPanel.add(jchar);
			}
			openPanel = viewPanel;
			openPanel.setBackground(Color.WHITE);
			view.setTrue();
		}
	}
	
	/* create a sliding motion
	 * */
	ActionListener moveDown = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			int length = view.getHeight()/view.parts;
			double end = length * scrollCount;
			if(end > view.translateY) {
				if(end-view.translateY > length*11/12) {
					view.setTranslateY((int)view.translateY + view.getHeight()/(48*view.parts));
				}
				if(end-view.translateY <= length*11/12 && end-view.translateY > length*10/12) {
					view.setTranslateY((int)view.translateY + view.getHeight()/(32*view.parts));
				}
				if(end-view.translateY <= length*10/12 && end-view.translateY > length*7/12) {
					view.setTranslateY((int)view.translateY + view.getHeight()/(16*view.parts));
				}
				if(end-view.translateY <= length*7/12 && end-view.translateY > length*4/12) {
					view.setTranslateY((int)view.translateY + view.getHeight()/(32*view.parts));
				}
				if(end-view.translateY <= length*4/12 && end-view.translateY > length*2/12) {
					view.setTranslateY((int)view.translateY + view.getHeight()/(48*view.parts));
				}
				if(end-view.translateY <= length*2/12) {
					view.setTranslateY((int)view.translateY + view.getHeight()/(64*view.parts));
				}
				view.repaint();
			}
		}
	};

	ActionListener moveUp = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			int length = view.getHeight()/view.parts;
			double end = length * scrollCount;
			if(end < view.translateY) {
				if(view.translateY-end > length*11/12) {
					view.setTranslateY((int)view.translateY - view.getHeight()/(48*view.parts));
				}
				if(view.translateY-end <= length*11/12 && view.translateY-end > length*10/12) {
					view.setTranslateY((int)view.translateY - view.getHeight()/(32*view.parts));
				}
				if(view.translateY-end <= length*10/12 && view.translateY-end > length*7/12) {
					view.setTranslateY((int)view.translateY - view.getHeight()/(16*view.parts));
				}
				if(view.translateY-end <= length*7/12 && view.translateY-end > length*4/12) {
					view.setTranslateY((int)view.translateY - view.getHeight()/(32*view.parts));
				}
				if(view.translateY-end <= length*4/12 && view.translateY-end > length*2/12) {
					view.setTranslateY((int)view.translateY - view.getHeight()/(48*view.parts));
				}
				if(view.translateY-end <= length*2/12) {
					view.setTranslateY((int)view.translateY - view.getHeight()/(64*view.parts));
				}
				view.repaint();
			}			
		}
	};
	
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
		Shape triDown = view.createDownTriangle();
		Shape triUp = view.createUpTriangle();
		Shape cross = view.createCross();
		
		/* show if coursor is hovering over close button
		 * */
		if(cross.getBounds2D().contains(x, y) && !view.mainView) {
			 currentGraphic.setColor(Color.RED);
			 currentGraphic.setStroke(new BasicStroke(3));
			 currentGraphic.draw(cross);
		} else {
			if(!view.mainView) {
				currentGraphic.setStroke(new BasicStroke(3));
				currentGraphic.draw(cross);
			}
			
		}
		
		/* show if coursor is hovering over storypoint
		 * */
		for(int i = 0; i < view.storyEllipses.size(); ++i) {
			Ellipse2D tmp = view.storyEllipses.get(i);
			if(tmp.contains(x, y)) {
				currentGraphic.setColor(Color.BLUE);
				currentGraphic.draw(tmp);
				currentGraphic.fill(tmp);
			} else {
				currentGraphic.setColor(Color.DARK_GRAY);
				currentGraphic.draw(tmp);
				currentGraphic.fill(tmp);
			}
		}
			
		/* show if coursor is hovering over scrollbutton
		 * */
		if(view.detailView) {
			if(!view.contains(0, (int)view.storyPoints.get(view.storyPoints.size()-1).getY())) {
				if(triUp.contains(x,y)) {
					currentGraphic.setColor(new Color(100, 100, 100));
					currentGraphic.fill(triUp);
				} else {
					currentGraphic.setColor(new Color(54,54,54));
					currentGraphic.fill(triUp);
				}
			}
			if(!view.contains(0, (int)view.storyPoints.get(0).getY())) {
				if(triDown.contains(x,y)) {
					currentGraphic.setColor(new Color(100, 100, 100));
					currentGraphic.fill(triDown);
				} else {
					currentGraphic.setColor(new Color(54,54,54));
					currentGraphic.fill(triDown);
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