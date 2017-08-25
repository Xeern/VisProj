package timeline.story;

import timeline.story.Data;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.LayerUI;

//import infovis.example.Example;
import timeline.window.Window;

public class StoryTimeline {
	private View view = null;
	private MouseController mouse = null;
	private MenuController menu = null;
//	private KeyController key = null;
	
	public View getView() {
		if (view == null) generateDiagram();
		return view;
	}
	public void generateDiagram(){
	   view = new View();
	   mouse = new MouseController();
//	   key = new KeyController();
	   menu = MenuController.getMenuController();
	   view.addMouseListener(mouse);
	   view.addMouseMotionListener(mouse);
//	   view.addKeyListener(key);
	   mouse.setView(view);
	   menu.setView(view);
//	   key.setView(view);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Data.importValues();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Window application = new Window();
				application.setView(new StoryTimeline().getView());
				application.getJFrame().setVisible(true);
				application.getView().setOpaque(false);
				application.getView().setLayout(null);
			}
		});
	}
}