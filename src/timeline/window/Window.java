package timeline.window;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Event;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;


import timeline.story.StoryTimeline;
import timeline.story.MenuController;

public class Window {
	
	private JFrame jFrame = null;
	
	private JPanel jContentPane = null;

	private JMenuBar jJMenuBar = null;
	
	private JMenu fileMenu = null;

	private JMenu editMenu = null;

	private JMenu helpMenu = null;

	private JMenuItem exitMenuItem = null;

	private JMenuItem aboutMenuItem = null;

	private JMenuItem cutMenuItem = null;

	private JMenuItem copyMenuItem = null;

	private JMenuItem pasteMenuItem = null;

	private JMenuItem saveMenuItem = null;
	
	private JDialog aboutDialog = null;

	private JPanel aboutContentPane = null;

	private JLabel aboutVersionLabel = null;
	
	private JToolBar jJToolBarBar = null;

	private JToggleButton overviewToggleButton = null;
	
	private JToggleButton detailToggleButton = null;

	private JPanel view = null;	
	
	public JFrame getJFrame() {
		if (jFrame == null) {
			jFrame = new JFrame();
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			jFrame.setJMenuBar(getJJMenuBar());
			jFrame.setSize(800, 600);
			jFrame.setContentPane(getJContentPane());
			jFrame.setTitle("StoryLine");
		}
		return jFrame;
	}
		
		private JPanel getJContentPane() {
			if (jContentPane == null) {
				jContentPane = new JPanel();
				jContentPane.setLayout(new BorderLayout());
				jContentPane.add(getJJToolBarBar(), BorderLayout.NORTH);
				jContentPane.add(getView(), BorderLayout.CENTER);
			}
			return jContentPane;
		}
		
		private JMenuBar getJJMenuBar() {
			if (jJMenuBar == null) {
				jJMenuBar = new JMenuBar();
				jJMenuBar.add(getFileMenu());
				jJMenuBar.add(getEditMenu());
				jJMenuBar.add(getHelpMenu());
			}
			return jJMenuBar;
		}
		
		private JMenu getFileMenu() {
			if (fileMenu == null) {
				fileMenu = new JMenu();
				fileMenu.setText("File");
				fileMenu.add(getSaveMenuItem());
				fileMenu.add(getExitMenuItem());
			}
			return fileMenu;
		}
		
		private JMenu getEditMenu() {
			if (editMenu == null) {
				editMenu = new JMenu();
				editMenu.setText("Edit");
				editMenu.add(getCutMenuItem());
				editMenu.add(getCopyMenuItem());
				editMenu.add(getPasteMenuItem());
			}
			return editMenu;
		}
		
		private JMenu getHelpMenu() {
			if (helpMenu == null) {
				helpMenu = new JMenu();
				helpMenu.setText("Help");
				helpMenu.add(getAboutMenuItem());
			}
			return helpMenu;
		}
		
		private JMenuItem getExitMenuItem() {
			if (exitMenuItem == null) {
				exitMenuItem = new JMenuItem();
				exitMenuItem.setText("Exit");
				exitMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
			}
			return exitMenuItem;
		}
		
		private JMenuItem getAboutMenuItem() {
			if (aboutMenuItem == null) {
				aboutMenuItem = new JMenuItem();
				aboutMenuItem.setText("About");
				aboutMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JDialog aboutDialog = getAboutDialog();
						aboutDialog.pack();
						Point loc = getJFrame().getLocation();
						loc.translate(20, 20);
						aboutDialog.setLocation(loc);
						aboutDialog.setVisible(true);
					}
				});
			}
			return aboutMenuItem;
		}
		
		private JDialog getAboutDialog() {
			if (aboutDialog == null) {
				aboutDialog = new JDialog(getJFrame(), true);
				aboutDialog.setTitle("About");
				aboutDialog.setContentPane(getAboutContentPane());
			}
			return aboutDialog;
		}
		
		private JPanel getAboutContentPane() {
			if (aboutContentPane == null) {
				aboutContentPane = new JPanel();
				aboutContentPane.setLayout(new BorderLayout());
				aboutContentPane.add(getAboutVersionLabel(), BorderLayout.CENTER);
			}
			return aboutContentPane;
		}
		
		private JLabel getAboutVersionLabel() {
			if (aboutVersionLabel == null) {
				aboutVersionLabel = new JLabel();
				aboutVersionLabel.setText("Version 1.0");
				aboutVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
			}
			return aboutVersionLabel;
		}
		
		private JMenuItem getCutMenuItem() {
			if (cutMenuItem == null) {
				cutMenuItem = new JMenuItem();
				cutMenuItem.setText("Cut");
				cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
						Event.CTRL_MASK, true));
			}
			return cutMenuItem;
		}
		
		private JMenuItem getCopyMenuItem() {
			if (copyMenuItem == null) {
				copyMenuItem = new JMenuItem();
				copyMenuItem.setText("Copy");
				copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
						Event.CTRL_MASK, true));
			}
			return copyMenuItem;
		}
		
		private JMenuItem getPasteMenuItem() {
			if (pasteMenuItem == null) {
				pasteMenuItem = new JMenuItem();
				pasteMenuItem.setText("Paste");
				pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
						Event.CTRL_MASK, true));
			}
			return pasteMenuItem;
		}
		
		private JMenuItem getSaveMenuItem() {
			if (saveMenuItem == null) {
				saveMenuItem = new JMenuItem();
				saveMenuItem.setText("Save");
				saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
						Event.CTRL_MASK, true));
			}
			return saveMenuItem;
		}
		
		private JToolBar getJJToolBarBar() {
			if (jJToolBarBar == null) {
				jJToolBarBar = new JToolBar();
				jJToolBarBar.add(getOverviewToggleButton());
				jJToolBarBar.add(getDetailToggleButton());
			}
			return jJToolBarBar;
		}
		
		private JToggleButton getOverviewToggleButton() {
			if (overviewToggleButton == null) {
				overviewToggleButton = new JToggleButton();
				overviewToggleButton.setText("Overview");
				overviewToggleButton.setSelected(true);
				overviewToggleButton.addItemListener(new java.awt.event.ItemListener() {
					public void itemStateChanged(java.awt.event.ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED){
							MenuController.getInstance().toggleOverview(true);
							detailToggleButton.setSelected(false);
						}else if (e.getStateChange() == ItemEvent.DESELECTED){
							MenuController.getInstance().toggleOverview(false);
							detailToggleButton.setSelected(true);
						}
					}
				});
			}
			return overviewToggleButton;
		}
		
		private JToggleButton getDetailToggleButton() {
			if (detailToggleButton == null) {
				detailToggleButton = new JToggleButton();
				detailToggleButton.setText("Detail View");
				detailToggleButton.addItemListener(new java.awt.event.ItemListener() {
					public void itemStateChanged(java.awt.event.ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED){
							MenuController.getInstance().toggleOverview(false);
							overviewToggleButton.setSelected(false);
						}else if (e.getStateChange() == ItemEvent.DESELECTED){
							MenuController.getInstance().toggleOverview(true);
							overviewToggleButton.setSelected(true);
						}
					}
				});
			}
			return detailToggleButton;
		}

		public JPanel getView() {
			if (view == null) {
				view = new StoryTimeline().getView();
			}
			return view;
		}
		
		public void setView(JPanel view){
			this.view = view;
		}
}