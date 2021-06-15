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
		// I'm assuming the the data columns are in order of: ID, first name, last name,
		// version, and company
		String file = "Path\\To\\.csv";
		try {
			sortCustomers(cleanData(csvData(file)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Invalid File");
		}
	}

	/**
	 * Read in file and return an ArrayList of csv rows
	 * @param file path to .csv file
	 * @return arraylist of .csv file rows
	 * @throws IOException
	 */
	public static ArrayList<String> csvData(String file) throws IOException {
		ArrayList<String> data = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;

		while ((line = br.readLine()) != null) {
			data.add(line);
		}
		return data;

	}

	/**
	 * 
	 * Convert ArrayList of comma separated strings into ArrayList of customers
	 * 
	 * @param data ArrayList of comma separated strings
	 * @return ArrayList now of type Customer; removes header row
	 */
	public static ArrayList<Customers> cleanData(ArrayList<String> data) {
		ArrayList<Customers> customers = new ArrayList<Customers>();
		for (int i = 1; i < data.size(); i++) {
			List<String> temp = Arrays.asList(data.get(i).split(","));
			Customers current = new Customers();
			current.setID(temp.get(0));
			current.setFname(temp.get(1));
			current.setlName(temp.get(2));
			current.setVersion(Integer.parseInt(temp.get(3)));
			current.setCompany(temp.get(4));
			customers.add(current);
		}
		return customers;
	}

	/**
	 * Separates customers by their company; 
	 * Sorts by last and first name asc
	 * Remove duplicate IDs within company and retain one with highest version
	 * @param data ArrayList of customers
	 */
	public static HashMap<String, ArrayList<Customers>> sortCustomers(ArrayList<Customers> data) {
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
						i = 0;
						j = 0;
					}
				}
			}

			// overwrite old unsorted arraylist with new sorted arraylist
			Collections.sort(curCompany, ascOrder);
			map.put(key, curCompany);

			// debug loop
//			for (int i = 0; i < curCompany.size(); i++) {
//				System.out.println(curCompany.get(i).getID() + ", " + curCompany.get(i).getFname() + ", "
//						+ curCompany.get(i).getLname() + ", " + curCompany.get(i).getCompany() + ", "
//						+ curCompany.get(i).getVersion());
//			}
//			System.out.println("");
		}
		return map;

	}

	public static Comparator<Customers> ascOrder = new Comparator<Customers>() {
		public int compare(Customers a, Customers b) {
			if (a.getLname().compareTo(b.getLname()) == 0) {
				return a.getFname().compareTo(b.getFname());
			} else {
				return a.getLname().compareTo(b.getLname());
			}
		}
	};

}
