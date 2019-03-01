import java.util.Scanner;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

interface Checkpoint {
	boolean check(String __, String chk);
}

public class Main {

	public static void main(String[] args){

		Checkpoint cp = (String __, String chk) -> {
			return (__.indexOf(chk) != -1);
		};
		
		Helper help = new Helper();
		Parser ps = new Parser();

		Scanner sc = new Scanner(System.in);
		System.out.printf(">> ");
		String query = sc.nextLine().toLowerCase();

		// check last query || query[:-1]
		if (!query.endsWith(";")) ps.error("Missing ;", null);
		
		List<String> tables = new ArrayList<String>();
		List<String> alias = new ArrayList<String>();

		// extract tables || alias
		if (cp.check(query, "join")) {

			if (!cp.check(query, " on")) ps.error("Syntax Error", "Missing [ON] for join query");

			String temp[] = ps.split(query, "from", "join").split(" ");
			tables.add(temp[0]);
			alias.add(temp[1]);

			temp = ps.split(query, "join", " on").split(" ");
			tables.add(temp[0]);
			alias.add(temp[1]);
		} else tables.add(ps.split(query, "from", ";"));

		// load databases
		List<List<String>> datas = new ArrayList<List<String>>();
		for (String t : tables) {
			if (!help.isExists(String.format("datas/%s.csv", t))) {
				help.error(String.format("Dataset %s not found !!", t));
			}
			datas.add(help.read(String.format("datas/%s.csv", t)));
		}
		System.out.printf("Showing datas from: %s\n", String.join(", ", tables));
		
		// mapping <tables:record>
		List<String> flag = new ArrayList<String>();
		String[] columns = ps.split(query, "select", "from").split(",");
		
		int index = 0; // index of tables
		if (cp.check(String.join(",", columns), "*")) { // found [*] then select all
			for (int j = 0; j < datas.get(index).get(0).split(",").length; j++) flag.add(String.format("%s:%s", index, j));
		} else {
			for (String c : columns) {
				c = c.trim();

				if (cp.check(c, ".")) {
					String temp[] = c.split("\\.");
					index = alias.indexOf(temp[0]);
					c = temp[1];
				}

				if (datas.get(index).get(0).contains(c)) 
					flag.add(String.format("%s:%s", index, Arrays.asList(datas.get(index).get(0).split(",")).indexOf(c)));
				else 
					help.error(String.format("Columns %s not found !!", c));
			}
		}

		System.out.printf("Showing columns: %s\n", String.join(", ", columns));

		// join query [found]
		List<String> condition = new ArrayList<String>();
		if (cp.check(query, "on") && cp.check(query, "(") && cp.check(query, ")")) {

			String compare[] = ps.split(query, "on ", ")").split("=");
			for (String c : compare) {
				String temp[] = c.split("\\.");

				index = alias.indexOf(temp[0]);
				if (datas.get(index).get(0).contains(temp[1]))
					condition.add(String.format("%s:%s", index, Arrays.asList(datas.get(index).get(0).split(",")).indexOf(temp[1])));
				else 
					help.error(String.format("Columns %s not found !!", c));
			}
		}

		help.print(datas, flag, condition);
	}
}