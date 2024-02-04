package tax.command;

import tax.info.Company;
import tax.info.Person;

import java.util.ArrayList;

public class AddCompany extends Command {
	public AddCompany() {
		description = "Додати компанію";
	}

	@Override
	public boolean action(ArrayList<Person> workers, ArrayList<Company> companies) {
		System.out.print("Введіть назву компанії: ");
		String name = scanner.nextLine();
		for (Company c : companies) {
			if (c.getName().equals(name)) {
				return false;
			}
		}
		companies.add(new Company(name));
		return true;
	}
}