import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import tax.info.Company;
import tax.info.Person;
import tax.taxes.Tax;
import tax.taxes.TaxType;
import tax.util.Util;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

public class TaxTest {
	@Test
	@DisplayName("Add tax test")
	@ExtendWith(MockitoExtension.class)
	public void resolveConflict() {
		Tax tax = new Tax(TaxType.BENEFIT, LocalDate.of(2022, 10, 1), 100);

		try (MockedStatic<Util> mocked = mockStatic(Util.class)) {
			when(Util.getInt(2)).thenReturn(1);
			when(Util.getDate("Введіть дату: ")).thenReturn(LocalDate.of(2021, 10, 1));
			tax.change();
			Assertions.assertEquals(LocalDate.of(2021, 10, 1), tax.getDate());

			when(Util.getInt(2)).thenReturn(2);
			when(Util.getDouble()).thenReturn(200.0);
			tax.change();
			Assertions.assertEquals(200 * 0.195, tax.getTaxAmount());
		}
	}

	@Test
	@DisplayName("Merge test")
	@ExtendWith(MockitoExtension.class)
	public void merge() {
		double amount1 = 100;
		double amount2 = 200;
		Tax oldTax = new Tax(TaxType.BENEFIT, LocalDate.of(2022, 10, 1), amount1);
		Tax newTax = new Tax(TaxType.BENEFIT, LocalDate.of(2022, 10, 1), amount2);
		oldTax.merge(newTax);
		Assertions.assertEquals((amount1 + amount2) * 0.195, oldTax.getTaxAmount());
	}

	@Test
	@DisplayName("Create from string test")
	public void createTax() {
		Person person1 = Person.toFind(1);
		Person person2 = Person.toFind(2);
		Company company1 = new Company("1");
		Company company2 = new Company("2");
		List<Person> persons = List.of(person1, person2);
		List<Company> companies = List.of(company1, company2);
		Tax tax1 = Tax.createTax("2|PRIMARY_SALARY|2023|11|12000.0|2", persons, companies);
		Tax tax2 = Tax.createTax("2|ROYALTY|2022|10|3000.0", persons, companies);
		Assertions.assertEquals(TaxType.PRIMARY_SALARY, tax1.getType());
		Assertions.assertEquals(LocalDate.of(2023, 11, 1), tax1.getDate());
		Assertions.assertEquals(12000 * 0.195, tax1.getTaxAmount());
		Assertions.assertTrue(person2.getTaxes().contains(tax1));
		Assertions.assertTrue(company2.getWorkers().contains(person2));

		Assertions.assertEquals(TaxType.ROYALTY, tax2.getType());
		Assertions.assertEquals(LocalDate.of(2022, 10, 1), tax2.getDate());
		Assertions.assertEquals(3000 * 0.195, tax2.getTaxAmount());
		Assertions.assertTrue(person2.getTaxes().contains(tax2));
	}
}