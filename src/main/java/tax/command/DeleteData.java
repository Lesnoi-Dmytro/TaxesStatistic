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

public class DeleteData extends Command {
	public DeleteData() {
		description = "Видалити дані про податки";
	}

	@Override
	public boolean action(ArrayList<Person> workers, ArrayList<Company> companies) {
		worker.getTaxes().forEach(System.out::println);
		boolean hasCompanies = !companies.isEmpty();
		TaxType.typeList(hasCompanies);
		TaxType type = TaxType.getType(Util.getInt(
						hasCompanies ? TaxType.values().length : TaxType.values().length - 2),
				hasCompanies);
		LocalDate date = Util.getDate("Введіть дату податку для видалення: ");
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
						iterator.remove();
						if (worker.getTaxes(company.getName()).isEmpty()) {
							company.removeWorker(worker.getID());
						}
						System.out.println("Дані успашно видалено");
						return true;
					}
				} else {
					iterator.remove();
					System.out.println("Дані успашно видалено");
					return true;
				}
			}
		}
		System.out.println("Дані не знайдено");
		return false;
	}
}