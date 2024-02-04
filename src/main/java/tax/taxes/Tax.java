package tax.taxes;

import tax.info.Company;
import tax.info.Person;
import tax.util.Util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Tax {
	protected TaxType type;
	protected LocalDate date;
	protected double amount;

	public static Tax createTax(Person person, ArrayList<Company> companies) {
		Tax tax = new Tax();
		boolean hasCompanies = !companies.isEmpty();
		TaxType.typeList(hasCompanies);
		tax.type = TaxType.getType(Util.getInt(
						hasCompanies ? TaxType.values().length : TaxType.values().length - 2),
				hasCompanies);
		tax.date = Util.getDate("Введіть дату: ");
		System.out.print("Введіть суму для оподаткування: ");
		tax.amount = Util.getDouble();
		tax = tax.setCompany(person, companies);
		person.addTax(tax);
		return tax;
	}

	public static Tax createTax(String data, List<Person> persons, List<Company> companies) {
		String[] dataArr = data.split("\\|");
		Tax tax = new Tax(TaxType.valueOf(dataArr[1]),
				LocalDate.of(Integer.parseInt(dataArr[2]),
						Integer.parseInt(dataArr[3]), 1),
				Double.parseDouble(dataArr[4]));
		int personID = Integer.parseInt(dataArr[0]);
		Person person = null;
		for (Person p : persons) {
			if (p.getID() == personID) {
				person = p;
				break;
			}
		}
		tax = tax.setCompany(dataArr, person, companies);
		person.addTax(tax);
		return tax;
	}

	private Tax setCompany(Person person, ArrayList<Company> companies) {
		if (type.isSalary()) {
			Company company = Util.getCompany(companies);
			company.addWorker(person);
			return new SalaryTax(this, company.getName());
		}
		return this;
	}

	private Tax setCompany(String[] dataArr, Person person, List<Company> companies) {
		if (type.isSalary()) {
			String name = dataArr[5];
			for (Company c : companies) {
				if (c.getName().equals(name)) {
					c.addWorker(person);
					return new SalaryTax(this, name);
				}
			}
		}
		return this;
	}

	public void change() {
		List<String> dataToChange = List.of(
				"Дата",
				"Сума для оподаткування"
		);
		System.out.println("Дані для зміни:");
		Util.printList(dataToChange);
		switch (Util.getInt(dataToChange.size())) {
			case 1 -> date = Util.getDate("Введіть дату: ");
			case 2 -> amount = Util.getDouble();
		}
	}

	private Tax() {
		this.type = null;
		this.date = null;
		this.amount = 0;
	}

	public Tax(TaxType type, LocalDate date, double amount) {
		this.type = type;
		this.date = date;
		this.amount = amount;
	}

	protected Tax(Tax tax) {
		this.type = tax.type;
		this.date = tax.date;
		this.amount = tax.amount;
	}

	public TaxType getType() {
		return type;
	}

	public LocalDate getDate() {
		return date;
	}

	public double getTaxAmount() {
		return amount * 0.195;
	}

	public int getYear() {
		return date.getYear();
	}

	public void merge(Tax toMerge) {
		amount += toMerge.amount;
	}

	public String toFile(Person person) {
		return person.getID() + "|" + type + "|" + date.getYear() + "|" + date.getMonthValue() + "|" + amount;
	}

	@Override
	public String toString() {
		return date.getYear() + "-" + date.getMonthValue() + ": " + type + ", " + "%.2f".formatted(getTaxAmount());
	}
}