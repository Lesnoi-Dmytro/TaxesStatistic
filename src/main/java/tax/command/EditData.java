package tax.command;

import tax.info.Company;
import tax.info.Person;
import tax.taxes.SalaryTax;
import tax.taxes.Tax;
import tax.taxes.TaxType;
import tax.util.Util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

public class EditData extends Command {
	public EditData() {
		description = "Змінити дані про податки";
	}

	@Override
	public boolean action(ArrayList<Person> workers, ArrayList<Company> companies) {
		worker.getTaxes().forEach(System.out::println);
		boolean hasCompanies = !companies.isEmpty();
		TaxType.typeList(hasCompanies);
		TaxType type = TaxType.getType(Util.getInt(
						hasCompanies ? TaxType.values().length : TaxType.values().length - 2),
				hasCompanies);
		LocalDate date = Util.getDate("Введіть дату податку для зміни: ");
		Company company = null;
		if (type.isSalary()) {
			company = Util.getCompany(companies);
		}
		Iterator<Tax> iterator = worker.getTaxes().iterator();
		while (iterator.hasNext()) {
			Tax t = iterator.next();
			if (t.getDate().equals(date) && t.getType() == type) {
				if (t instanceof SalaryTax s) {
					if (s.getCompany().equals(company.getName())) {
						changeTax(t, companies);
						return true;
					}
				} else {
					changeTax(t, companies);
					return true;
				}
			}
		}
		System.out.println("Дані не знайдено");
		return false;
	}

	private static void changeTax(Tax tax, ArrayList<Company> companies) {
		System.out.println(tax);
		tax.change();
		System.out.println("Дані успашно змінено");
	}
}