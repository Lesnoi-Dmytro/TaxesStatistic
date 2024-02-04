package tax.info;

import tax.taxes.SalaryTax;
import tax.taxes.Tax;
import tax.taxes.TaxType;
import tax.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Person {
	private static int lastID = 1;
	private static final Scanner scanner = new Scanner(System.in);
	private int id;
	private String firstName;
	private String lastName;
	private final ArrayList<Tax> taxes = new ArrayList<>();

	public Person(String firstName, String lastName) {
		this.id = lastID++;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Person() {
	}

	public static Person newPerson() {
		System.out.print("Ім'я: ");
		String firstName = scanner.nextLine();
		System.out.print("Прізвище: ");
		String lastName = scanner.nextLine();
		return new Person(firstName, lastName);
	}

	public static Person toFind() {
		System.out.print("ID працівника: ");
		Person person = new Person();
		person.id = Util.getInt(Integer.MAX_VALUE);
		return person;
	}

	public static Person toFind(int id) {
		Person person = new Person();
		person.id = id;
		return person;
	}

	public static Person fromFile(String data) {
		Person person = new Person();
		String[] dataArr = data.split("\\|");
		person.id = Integer.parseInt(dataArr[0]);
		person.firstName = dataArr[1];
		person.lastName = dataArr[2];
		if (person.id >= lastID) {
			lastID = person.id + 1;
		}
		return person;
	}

	public int getID() {
		return id;
	}

	public ArrayList<Tax> getTaxes() {
		return taxes;
	}

	public ArrayList<SalaryTax> getTaxes(String name) {
		ArrayList<SalaryTax> companyTax = new ArrayList<>();
		for (Tax t : taxes) {
			if (t instanceof SalaryTax s) {
				if (s.getCompany().equals(name)) {
					companyTax.add(s);
				}
			}
		}
		return companyTax;
	}

	public boolean addTax(Tax tax) {
		for (Tax t : taxes) {
			if (t.getType() == tax.getType() &&
					t.getDate().equals(tax.getDate())) {
				if (t.getType() == TaxType.SECONDARY_SALARY) {
					SalaryTax s = (SalaryTax) t;
					SalaryTax sTax = (SalaryTax) tax;
					if (s.getCompany().equals(sTax.getCompany())) {
						return resolveConflictIn(t, tax);
					}
				} else {
					return resolveConflictIn(t, tax);
				}
			}
		}
		return taxes.add(tax);
	}

	private boolean resolveConflictIn(Tax old, Tax newTax) {
		System.out.println("Даний податок вже зареєстрований");
		System.out.println("Способи вирішення конфлікту");
		List<String> options = List.of(
				"Поєднати",
				"Вставити нові дані",
				"Залишити старі дані"
		);
		Util.printList(options);
		System.out.print("Спосіб вирішення кофлікту: ");
		return resolveConflict(Util.getInt(options.size()), old, newTax);
	}

	private boolean resolveConflict(int option, Tax old, Tax newTax) {
		return switch (option) {
			case 1 -> {
				old.merge(newTax);
				yield true;
			}
			case 2 -> {
				taxes.remove(old);
				taxes.add(newTax);
				yield true;
			}
			default -> false;
		};
	}

	public String toFile() {
		return id + "|" + firstName + "|" + lastName;
	}

	@Override
	public boolean equals(Object obj) {
		return this.id == ((Person) obj).id;
	}

	@Override
	public String toString() {
		return id + ": " + firstName + " " + lastName;
	}
}