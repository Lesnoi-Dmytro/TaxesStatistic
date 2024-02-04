package tax.command;

import tax.info.Company;
import tax.info.Person;
import tax.taxes.Tax;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LoadData extends Command {
	@Override
	public boolean action(ArrayList<Person> workers, ArrayList<Company> companies) {
		File fWorkers = new File("data/workers.txt");
		File fCompanies = new File("data/companies.txt");
		File fTaxes = new File("data/taxes.txt");
		if (!createFile(fWorkers)) {
			readWorkers(workers, fWorkers);
		}
		if (!createFile(fCompanies)) {
			readCompanies(companies, fCompanies);
		}
		if (!createFile(fTaxes)) {
			readTaxes(workers, companies, fTaxes);
		}
		return false;
	}

	private static boolean createFile(File file) {
		if (!file.exists()) {
			try {
				file.createNewFile();
				System.out.println("Успішно створено файл " + file);
				return true;
			} catch (IOException e) {
				System.out.println("Помилка при створені файлу " + file);
				throw new RuntimeException(e);
			}
		}
		return false;
	}

	private static void readWorkers(ArrayList<Person> workers, File read) {
		try {
			Scanner scanner = new Scanner(read);
			if (!scanner.hasNext()) {
				return;
			}
			while (scanner.hasNext()) {
				workers.add(Person.fromFile(scanner.nextLine()));
			}
			System.out.println("Дані про працівників успішно зчитано");
		} catch (FileNotFoundException e) {
			System.out.println("Файл " + read + " не знайдено, зчитування неможливе");
			throw new RuntimeException(e);
		}
	}

	private static void readCompanies(ArrayList<Company> companies, File read) {
		try {
			Scanner scanner = new Scanner(read);
			if (!scanner.hasNext()) {
				return;
			}
			while (scanner.hasNext()) {
				companies.add(new Company(scanner.nextLine()));
			}
			System.out.println("Дані про компанії успішно зчитано");
		} catch (FileNotFoundException e) {
			System.out.println("Файл " + read + " не знайдено, зчитування неможливе");
			throw new RuntimeException(e);
		}
	}

	private static void readTaxes(ArrayList<Person> workers, ArrayList<Company> companies, File read) {
		try {
			Scanner scanner = new Scanner(read);
			if (!scanner.hasNext()) {
				return;
			}
			while (scanner.hasNext()) {
				Tax.createTax(scanner.nextLine(), workers, companies);
			}
			System.out.println("Дані про податки успішно зчитано");
		} catch (FileNotFoundException e) {
			System.out.println("Файл " + read + " не знайдено, зчитування неможливе");
			throw new RuntimeException(e);
		}
	}
}