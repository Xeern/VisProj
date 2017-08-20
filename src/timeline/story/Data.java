package timeline.story;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Data{
	public static HashMap<Integer, ArrayList<String>> events = new HashMap<Integer, ArrayList<String>>(); 
	
	public static void importValues() throws IOException {
		try {
			File file = new File("datasets/z_data01.txt");
		
			List<String> lines = new ArrayList<String>();
		
			LineIterator it = FileUtils.lineIterator(file, "UTF-8"); {
				try {
					while (it.hasNext()) {
						String line = it.nextLine();
						lines.add(line);
					}
				} finally {
					it.close();
				}
			}
		
			//for (int i = 0; i < lines.size(); i++) {
			for (String line : lines) {
				//System.out.println(line);
				String[] splitter = line.split(";", 5);
				
				int itemp = Integer.parseInt(splitter[0]);
				ArrayList<String> temp = new ArrayList<String>(); 
				for(int i = 1; i < 5; i++) {
					temp.add(splitter[i]);
				}
//				System.out.println(temp);
				
				events.put(itemp, temp);
			}
			System.out.println(events);
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}