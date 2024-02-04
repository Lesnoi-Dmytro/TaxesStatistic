package tax.util;

import tax.info.Company;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Util {
	private static Scanner scanner = new Scanner(System.in);

	public static int getInt(int min, int max) {
		int num;
		while (true) {
			try {
				num = Integer.parseInt(scanner.nextLine());
				if (num >= min && num <= max) {
					break;
				}
				System.out.print("Введіть число від " + min + " до " + max + ": ");
			} catch (NumberFormatException ignore) {
			}
		}
		return num;
	}

	public static int getInt(int max) {
		return getInt(1, max);
	}

	public static double getDouble(double min, double max) {
		double num;
		while (true) {
			try {
				num = Double.parseDouble(scanner.nextLine());
				if (num >= min && num <= max) {
					break;
				}
				System.out.print("Введіть число від " + min + " до " + max + ": ");
			} catch (NumberFormatException ignore) {
			}
		}
		return num;
	}

	public static double getDouble() {
		return getDouble(Double.MIN_NORMAL, Double.MAX_VALUE);
	}

	public static<T> void printList(List<T> list, int offset) {
		for (int i = 0; i < list.size(); i++) {
			System.out.println((i + offset) + ": " + list.get(i));
		}
	}

	public static<T> void printList(List<T> list) {
		printList(list, 1);
	}

	public static LocalDate getDate(String description) {
		LocalDate date = null;
		System.out.print(description);
		do {
			String dateStr = scanner.nextLine();
			if (dateStr.matches("0[1-9][/.-][0-2]\\d{3}") ||
					dateStr.matches("1[0-2][/.-][0-2]\\d{3}")) {
				date = LocalDate.of(Integer.parseInt(dateStr.substring(3)),
						Integer.parseInt(dateStr.substring(0, 2)), 1);
			} else if (dateStr.matches("[0-2]\\d{3}[/.-]0[1-9]") ||
					dateStr.matches("[0-2]\\d{3}[/.-]1[0-2]")) {
				date = LocalDate.of(Integer.parseInt(dateStr.substring(0, 4)),
						Integer.parseInt(dateStr.substring(5)), 1);
			} else {
				System.out.print("Некоректно введена дата, повторіть введення: ");
			}
		} while (date == null || date.isAfter(LocalDate.now()));
		return date;
	}

	public static Company getCompany(ArrayList<Company> companies) {
		System.out.println("Список компаній:");
		printList(companies);
		System.out.print("Компанія: ");
		return companies.get(Util.getInt(companies.size()) - 1);
	}
}