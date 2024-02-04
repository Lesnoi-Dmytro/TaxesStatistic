package tax.command;

import tax.info.Company;
import tax.info.Person;
import tax.taxes.Tax;

import java.util.ArrayList;

public class AddData extends Command {
	public AddData() {
		description = "Додати дані про податки";
	}

	@Override
	public boolean action(ArrayList<Person> workers, ArrayList<Company> companies) {
		Tax.createTax(worker, companies);
		return true;
	}
}