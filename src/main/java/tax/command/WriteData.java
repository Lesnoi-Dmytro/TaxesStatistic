package tax.command;

import tax.info.Company;
import tax.info.Person;
import tax.taxes.Tax;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WriteData extends Command {
	public WriteData() {
		description = "Завершення виконання програми";
	}

	@Override
	public boolean action(ArrayList<Person> workers, ArrayList<Company> companies) {
		try (FileWriter wWorkers = new FileWriter("data/workers.txt");
			 FileWriter wCompanies = new FileWriter("data/companies.txt");
			 FileWriter wTaxes = new FileWriter("data/taxes.txt")) {
			for (Person p : workers) {
				wWorkers.write(p.toFile() + "\n");
				for (Tax t : p.getTaxes()) {
					wTaxes.write(t.toFile(p) + "\n");
				}
			}
			for (Company c : companies) {
				wCompanies.write(c.toString() + "\n");
			}
			System.out.println("Дані успішно збережено");
		} catch (IOException e) {
			System.out.println("Помилка при спробі збереження даних");
		}
		return false;
	}
}