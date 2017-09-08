package timeline.story;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import java.util.ArrayList;
import java.util.List;

public class Data{
	public static HashMap<Integer, ArrayList<String>> events = new HashMap<Integer, ArrayList<String>>(); 
	
	public static void importValues() throws IOException {
		try {
//			File file = new File("datasets/z_data01.txt");
			File file = new File("datasets/z_data02.txt");
		
			List<String> lines = new ArrayList<String>();
		
			LineIterator it = FileUtils.lineIterator(file, "UTF-8"); {
				try {
					while (it.hasNext()) {
						String line = it.nextLine();
						if (lines.size() >= 1) {
							String dup_check = lines.get(lines.size() -1);
							if (line.equalsIgnoreCase(dup_check)) {
								continue;
							}
						}
						lines.add(line);
					}
				} finally {
					it.close();
				}
			}
		
			for (String line : lines) {
				String[] splitter = line.split(";", 5);
				
				int itemp = Integer.parseInt(splitter[0]);
				ArrayList<String> temp = new ArrayList<String>(); 
				for(int i = 1; i < 5; i++) {
					temp.add(splitter[i]);
				}
				events.put(itemp, temp);
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}