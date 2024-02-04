package tax.command;

import tax.info.Company;
import tax.info.Person;
import tax.taxes.Tax;
import tax.taxes.TaxType;
import tax.util.Util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ViewPerson extends Command {
	private Map<Integer, List<Tax>> yearGrouped = null;
	private Map<LocalDate, List<Tax>> monthGrouped = null;
	private Map<TaxType, List<Tax>> typeGrouped = null;

	public ViewPerson() {
		description = "Показати дані по працівнику";
	}

	@Override
	public boolean action(ArrayList<Person> workers, ArrayList<Company> companies) {
		Stream<Tax> taxes = worker.getTaxes().stream();
		taxes = filter(taxes);
		group(taxes);
		if (yearGrouped != null) {
			sort(yearGrouped);
			print(yearGrouped, "рік");
		} else if (monthGrouped != null) {
			sort(monthGrouped);
			print(monthGrouped, "місяць");
		} else if (typeGrouped != null) {
			sort(typeGrouped);
			print(typeGrouped, "тип");
		} else {
			taxes = sort(taxes);
			System.out.println();
			taxes.forEach(System.out::println);
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException ignored) {
		}
		return false;
	}

	private Stream<Tax> filter(Stream<Tax> taxes) {
		ArrayList<String> limitParameters = new ArrayList<>(List.of(
				"Перейти до групування",
				"Датою",
				"Сумою податків"
		));
		boolean loop = true;
		while (loop && limitParameters.size() > 1) {
			System.out.println("Можливе обмеження за:");
			Util.printList(limitParameters);
			System.out.print("Обмежити за: ");
			int ind = Util.getInt(limitParameters.size()) - 1;
			switch (limitParameters.get(ind)) {
				case "Датою" -> {
					LocalDate start = Util.getDate("Введіть початкову дату: ");
					LocalDate end = Util.getDate("Введіть кінцеву дату: ");
					taxes = taxes.filter(t -> (t.getDate().isAfter(start) || t.getDate().equals(start)) &&
							(t.getDate().isBefore(end) || t.getDate().equals(end)));
				}
				case "Сумою податків" -> {
					System.out.print("Введіть мінімальне значення податку: ");
					int lower = Util.getInt(Integer.MAX_VALUE);
					System.out.print("Введіть максимальне значення податку: ");
					int higher = Util.getInt(Integer.MAX_VALUE);
					taxes = taxes.filter(t ->
							t.getTaxAmount() >= lower && t.getTaxAmount() <= higher);
				}
				default -> loop = false;
			}
			limitParameters.remove(ind);
		}
		return taxes;
	}

	private void group(Stream<Tax> taxes) {
		ArrayList<String> limitParameters = new ArrayList<>(List.of(
				"Не групувати",
				"Роком",
				"Місяцем",
				"Типом"
		));
		System.out.println("Групувати можливе за:");
		Util.printList(limitParameters);
		System.out.print("Групувати за: ");
		switch (Util.getInt(limitParameters.size()) - 1) {
			case 1 -> yearGrouped = taxes.collect(Collectors.groupingBy(Tax::getYear, Collectors.toList()));
			case 2 -> monthGrouped = taxes.collect(Collectors.groupingBy(Tax::getDate, Collectors.toList()));
			case 3 -> typeGrouped = taxes.collect(Collectors.groupingBy(Tax::getType, Collectors.toList()));
		}
	}

	private Stream<Tax> sort(Stream<Tax> taxes) {
		ArrayList<String> sortParameters = new ArrayList<String>(List.of(
				"Перейти до виведення",
				"Датою",
				"Сумою податків",
				"Типом"
		));
		boolean loop = true;
		Comparator<Tax> comparator = Comparator.comparing(t -> 0);
		while (loop && sortParameters.size() > 1) {
			System.out.println("Можливе сортування за:");
			Util.printList(sortParameters);
			System.out.print("Сортувати за: ");
			int ind = Util.getInt(sortParameters.size()) - 1;
			Comparator<Tax> second = null;
			switch (sortParameters.get(ind)) {
				case "Датою" -> second = Comparator.comparing(Tax::getDate);
				case "Сумою податків" -> second = Comparator.comparing(Tax::getTaxAmount);
				case "Типом" -> second = Comparator.comparing(Tax::getType);
				default -> loop = false;
			}
			if (second != null) {
				System.out.print("""
						1: За зростанням
						2: За спаданням
						Введить тип сортування:\s""");
				if (Util.getInt(2) == 2) {
					second = second.reversed();
				}
				comparator = comparator.thenComparing(second);
			}
			sortParameters.remove(ind);
		}
		Comparator<Tax> toLambda = comparator;
		return taxes.sorted(toLambda);
	}

	private <T> void sort(Map<T, List<Tax>> taxes) {
		ArrayList<String> sortParameters = new ArrayList<>(List.of(
				"Перейти до виведення",
				"Сумою податків"
		));
		if (monthGrouped == null) {
			sortParameters.add("Місяцем");
		}
		if (typeGrouped == null) {
			sortParameters.add("Типом");
		}
		System.out.println("Можливе сортування за:");
		Util.printList(sortParameters);
		System.out.print("Сортувати за: ");
		Comparator<Tax> comparator = null;
		switch (sortParameters.get(Util.getInt(sortParameters.size()) - 1)) {
			case "Сумою податків" -> comparator = Comparator.comparing(Tax::getTaxAmount);
			case "Місяцем" -> comparator = Comparator.comparing(Tax::getDate);
			case "Типом" -> comparator = Comparator.comparing(Tax::getType);
		}
		if (comparator != null) {
			System.out.print("""
					1: За зростанням
					2: За спаданням
					Введить тип сортування:\s""");
			if (Util.getInt(2) == 2) {
				comparator = comparator.reversed();
			}
			Comparator<Tax> toLambda = comparator;
			taxes.values().forEach(v -> v.sort(toLambda));
		}
	}

	private <T> void print(Map<T, List<Tax>> taxes, String groupParam) {
		System.out.println();
		taxes.forEach((key, value) -> {
			if (!value.isEmpty()) {
				if (groupParam.equals("місяць")) {
					if (key instanceof LocalDate d) {
						System.out.println("\t" + d.getMonthValue() + "-" + d.getYear());
					}
				} else {
					System.out.println("\t" + key);
				}
				System.out.printf("\tСумарний податок, сплачений за " + groupParam + ": %.2f%n", value.stream()
						.collect(Collectors.summarizingDouble(Tax::getTaxAmount))
						.getSum());
				value.forEach(System.out::println);
			}
		});
	}
}