import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

public class Helper {
	
	public void error(String err) {
		System.out.printf("[ERR] %s\n", err);
		System.exit(0);
	}

	public boolean isExists(String locate) {
		File f = new File(locate);
		return f.exists() && !f.isDirectory();
	}
	
	public List<String> read(String locate) {
		File f = new File(locate);
		List<String> temp = new ArrayList<String>();

		try {
			Scanner sc = new Scanner(f);
			while (sc.hasNext()) temp.add(sc.next());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return temp;
	}

	public void print(List<List<String>> datas, List<String> flag, List<String> cond) {

		HashMap<Integer, String> result = new HashMap<Integer, String>(); 

		for (int i = 0;	i < datas.size(); i++) {

			List<Integer> candidate = new ArrayList<Integer>();
			for (String f: flag) {
				String temp[] = f.split(":");
				
				if (Integer.valueOf(temp[0]) == i) 
					candidate.add(Integer.valueOf(temp[1]));
			}

			datas.get(i).remove(0); // remove attribute
			for (int j = 0; j < datas.get(i).size(); j++) {

				List<String> record = new ArrayList(Arrays.asList(datas.get(i).get(j).split(",")));

				int found = -1; // harusnya pake hashmap, tapi karna gampangan, jadi manual sadja ea gan..
				if (datas.size() > i + 1 && cond.size() > 0) {
					found = 0;
					for (int k = 1; k < datas.get(i + 1).size() - 1; k++) {
						String prev[] = cond.get(i).split(":");
						String next[] = cond.get(i + 1).split(":");
		
						if (record.get(Integer.valueOf(prev[1])).contains(datas.get(i + 1).get(k).split(",")[Integer.valueOf(next[1])]))
							found++;
					}
				} if (found == 0) continue;

				for (int k = record.size() - 1; k >= 0; k--) 
					if (!candidate.contains(k)) record.remove(k);

				if (result.containsKey(j)) 
					result.put(j, result.get(j) + " " + String.join(" ", record));
				else 
					result.put(j, String.join(" ", record));
			}
		}

		if (cond.size() > 0) 
			System.out.printf("-%s\n", result.values().stream().map(Object::toString).reduce(" ", String::concat));
		else 
			for (String r : result.values()) System.out.printf("- %s\n", r);
	}
}