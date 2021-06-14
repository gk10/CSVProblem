import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Main {

	/*
	 * I'd normally look for a library to handle reading in the CSV file but I'll be
	 * using what can be found in the java standard
	 */
	public static void main(String[] args) {
		// I'm assuming the the data columns are in order of: ID, first name, last name, version, and company
		String file = "Path\\To\\.csv";
		try {
			sortCustomers(cleanData(csvData(file)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Invalid File");
		}
	}

	public static ArrayList<String> csvData(String file) throws IOException {
		ArrayList<String> data = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;

		while ((line = br.readLine()) != null) {
			System.out.println(line);
			data.add(line);
		}
		System.out.println(data.size());
		return data;

	}

	public static ArrayList<Customers> cleanData(ArrayList<String> data) {
		// List<String> cats = Arrays.asList(data.get(0).split(","));
		ArrayList<Customers> customers = new ArrayList<Customers>();
		// for (String temp : cats) {
		// System.out.println(temp);
		// }
		for (int i = 1; i < data.size(); i++) {
			List<String> temp = Arrays.asList(data.get(i).split(","));
			Customers current = new Customers();
			current.setID(temp.get(0));
			current.setFname(temp.get(1));
			current.setlName(temp.get(2));
			current.setVersion(Integer.parseInt(temp.get(3)));
			current.setID(temp.get(4));
			customers.add(current);
		}
		return customers;
	}

	public static void sortCustomers(ArrayList<Customers> data) {
		HashMap<String, ArrayList<Customers>> map = new HashMap<>();
		// map customers to their correct company
		for (int i = 0; i < data.size(); i++) {
			map.putIfAbsent(data.get(i).getCompany(), new ArrayList<Customers>());
			map.get(data.get(i).getCompany()).add(data.get(i));
		}

		for (String key : map.keySet()) {
			ArrayList<Customers> curCompany = map.get(key);
			// Loop for finding duplicate IDs and removing the ones with smaller versions
			for (int i = 0; i < curCompany.size(); i++) {
				Customers one = curCompany.get(i);
				for (int j = i + 1; j < curCompany.size(); j++) {
					Customers two = curCompany.get(j);
					if (one.getID().equals(two.getID())) {
						if (one.getVersion() > two.getVersion()) {
							curCompany.remove(j);
						} else {
							curCompany.remove(i);
						}
					}
				}				
			}
			
			
			
			// overwrite old unsorted arraylist with new sorted arraylist
			Collections.sort(curCompany, ascOrder);
			map.put(key, curCompany);			
		}
	}
	public static Comparator<Customers> ascOrder = new Comparator<Customers>() {
	public int compare(Customers a, Customers b) {
		return a.getLname().compareTo(b.getLname());
	}
	};
}
