package timeline.story;

import javax.swing.SwingUtilities;

//import infovis.example.Example;
import timeline.window.Window;

public class StoryTimeline {
	private View view = null;
	
	public View getView() {
		if (view == null) generateDiagram();
		return view;
	}
	public void generateDiagram(){
	   view = new View();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Window application = new Window();
				application.setView(new StoryTimeline().getView());
				application.getJFrame().setVisible(true);
			}
		});
	}
}