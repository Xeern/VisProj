package timeline.story;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import timeline.story.View;

public class MouseController implements MouseListener,MouseMotionListener {
	
	private View view = null;
	
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		for(int i = 0; i < view.storyEllipses.size(); ++i) {
			if(view.overview == true) {
				Ellipse2D tmp = view.storyEllipses.get(i);
				if(tmp.contains(x, y)) {
					Point2D currentPoint = view.storyPoints.get(i);
					int currentSegment = view.segmentAtPoint.get(currentPoint);
					ArrayList<String> currentEvent = view.sorted_events.get(currentSegment);
//					System.out.println(currentEvent);
				}
			}
			if(view.detailView == true) {
				double tmpX = view.storyEllipses.get(i).getMinX()-3;
				double tmpY = (view.storyEllipses.get(i).getMinY()-view.getHeight()*2/4)*2;
				Ellipse2D tmp = new Ellipse2D.Double(tmpX, tmpY, 10, 10);
				if(tmp.contains(x,y)) {
					Point2D currentPoint = view.storyPoints.get(i);
					int currentSegment = view.segmentAtPoint.get(currentPoint);
					ArrayList<String> currentEvent = view.sorted_events.get(currentSegment);
//					System.out.println(currentEvent);

//					String date = currentEvent.get(1);
//					JInternalFrame intframe = new JInternalFrame(date, false, true);
//					intframe.setBounds((int)currentPoint.getX() + 5, (int)currentPoint.getY() - 21, 300, 500);
					JPanel viewPanel = view.panelAtPoint.get(currentPoint);
	        		viewPanel.setBounds((int)currentPoint.getX() + 5, (int)currentPoint.getY() - 21, 300, 500);
	        		viewPanel.setLayout((LayoutManager) new FlowLayout(FlowLayout.LEFT));
	        		
//	        		JLabel titel = (JLabel) viewPanel.getComponent(0);
//	        		titel.setFont(new Font("Courier New", 0, 14));
	        		
					String content = currentEvent.get(2);
					JTextArea textArea = new JTextArea(content);
					textArea.setLayout((LayoutManager) new FlowLayout(FlowLayout.RIGHT));
            		textArea.setBounds((int)currentPoint.getX() + 5, (int)currentPoint.getY() -10, 290, 300);
            		
					textArea.setFont(new Font("Serif", Font.PLAIN, 14));
					textArea.setBackground(null);
					textArea.setLineWrap(true);
					textArea.setWrapStyleWord(true);
	        		viewPanel.add(textArea);
	        		
	        		String characters = currentEvent.get(3);
	        		System.out.println(characters);
	        		String splittoken = Pattern.quote("+");
					String[] splitter = characters.split(splittoken);
					for(String j : splitter) {
						JLabel jchar = new JLabel(j); 
						viewPanel.add(jchar);
					}

//	        		viewPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//					view.add(intframe);
//	        		intframe.updateUI();
//					viewPanel.repaint();
					
				}
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
		for(int i = 0; i < view.storyEllipses.size(); ++i) {
			if(view.overview == true) {
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
			if(view.detailView == true) {
				double tmpX = view.storyEllipses.get(i).getMinX()-3;
				double tmpY = (view.storyEllipses.get(i).getMinY()-view.getHeight()*2/4)*2;
				Ellipse2D tmp = new Ellipse2D.Double(tmpX, tmpY, 10, 10);
				if(tmp.contains(x,y)) {
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