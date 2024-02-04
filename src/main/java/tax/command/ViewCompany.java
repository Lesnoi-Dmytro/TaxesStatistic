package tax.command;

import tax.info.Company;
import tax.info.Person;
import tax.taxes.SalaryTax;
import tax.taxes.Tax;
import tax.util.Util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ViewCompany extends Command {
	public ViewCompany() {
		description = "Показати дані по компанії";
	}

	@Override
	public boolean action(ArrayList<Person> workers, ArrayList<Company> companies) {
		Company company = Util.getCompany(companies);
		Map<Person, Stream<SalaryTax>> taxes = company.getWorkers().stream()
				.collect(Collectors.toMap(p -> p, p -> p.getTaxes(company.getName()).stream()));
		filter(taxes);
		sort(taxes);
		print(taxes);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException ignored) {
		}
		return false;
	}

	private void filter(Map<Person, Stream<SalaryTax>> taxes) {
		ArrayList<String> limitParameters = new ArrayList<>(List.of(
				"Перейти до сортування",
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
					taxes.replaceAll((k, v) -> v.filter(t -> (t.getDate().isAfter(start) || t.getDate().equals(start)) &&
							(t.getDate().isBefore(end) || t.getDate().equals(end))));
				}
				case "Сумою податків" -> {
					System.out.print("Введіть мінімальне значення податку, що сплатила компанія: ");
					int lower = Util.getInt(Integer.MAX_VALUE);
					System.out.print("Введіть максимальне значення податку, що сплатила компанія: ");
					int higher = Util.getInt(Integer.MAX_VALUE);
					taxes.replaceAll((k, v) -> v.filter(t ->
							t.getCompanyTax() >= lower && t.getCompanyTax() <= higher));
				}
				case "Перейти до сортування" -> loop = false;
			}
			limitParameters.remove(ind);
		}
	}

	private void sort(Map<Person, Stream<SalaryTax>> taxes) {
		ArrayList<String> sortParameters = new ArrayList<>(List.of(
				"Перейти до виведення",
				"Датою",
				"Сумою податків"
		));
		System.out.println("Можливе сортування за:");
		Util.printList(sortParameters);
		System.out.print("Сортувати за: ");
		Comparator<Tax> comparator = null;
		switch (sortParameters.get(Util.getInt(sortParameters.size()) - 1)) {
			case "Датою" -> comparator = Comparator.comparing(Tax::getDate);
			case "Сумою податків" -> comparator = Comparator.comparing(Tax::getTaxAmount);
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
			taxes.replaceAll((k, v) -> v.sorted(toLambda));
		}
	}

	private void print(Map<Person, Stream<SalaryTax>> taxes) {
		System.out.println();
		taxes.forEach((key, value) -> {
			List<SalaryTax> tempArr = value.toList();
			if (!tempArr.isEmpty()) {
				System.out.println("\t" + key);
				System.out.printf("\tСумарний податок, сплачений компанією: %.2f%n", tempArr.stream()
						.collect(Collectors.summarizingDouble(SalaryTax::getCompanyTax))
						.getSum());
				tempArr.forEach(t -> {
					System.out.println(t);
					System.out.printf("Податок, сплачений компанією: %.2f%n", t.getCompanyTax());
				});
			}
		});
	}
}