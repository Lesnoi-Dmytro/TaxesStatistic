import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import tax.info.Person;
import tax.taxes.SalaryTax;
import tax.taxes.Tax;
import tax.taxes.TaxType;
import tax.util.Util;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class PersonTest {
	@Test
	@DisplayName("Get company tax test")
	@ExtendWith(MockitoExtension.class)
	public void getTaxes() {
		Person person = new Person();
		try {
			Tax tax1 = mock(Tax.class);
			SalaryTax tax2 = mock(SalaryTax.class);
			SalaryTax tax3 = mock(SalaryTax.class);
			SalaryTax tax4 = mock(SalaryTax.class);
			when(tax2.getCompany()).thenReturn("2");
			when(tax3.getCompany()).thenReturn("1");
			when(tax4.getCompany()).thenReturn("1");
			Field taxes = Person.class.getDeclaredField("taxes");
			taxes.setAccessible(true);
			taxes.set(person, new ArrayList<>(
					List.of(tax1, tax2, tax3, tax4)));
			Assertions.assertEquals(List.of(tax3, tax4), person.getTaxes("1"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	@DisplayName("Add tax test")
	@ExtendWith(MockitoExtension.class)
	public void resolveConflict() {
		Person person = new Person();

		try (MockedStatic<Util> mocked = mockStatic(Util.class)) {
			Tax tax1 = mock(Tax.class);
			when(tax1.getType()).thenReturn(TaxType.BENEFIT);
			when(tax1.getDate()).thenReturn(LocalDate.of(2022, 10, 1));
			Assertions.assertTrue(person.addTax(tax1));
			Assertions.assertTrue(person.getTaxes().contains(tax1));

			Tax tax2 = mock(Tax.class);
			when(tax2.getType()).thenReturn(TaxType.BENEFIT);
			when(tax2.getDate()).thenReturn(LocalDate.of(2022, 11, 1));
			Assertions.assertTrue(person.addTax(tax2));
			Assertions.assertTrue(person.getTaxes().contains(tax2));

			tax2 = mock(Tax.class);
			when(tax2.getType()).thenReturn(TaxType.GIFT);
			Assertions.assertTrue(person.addTax(tax2));
			Assertions.assertTrue(person.getTaxes().contains(tax1));

			tax2 = mock(Tax.class);
			when(tax2.getType()).thenReturn(TaxType.BENEFIT);
			when(tax2.getDate()).thenReturn(LocalDate.of(2022, 10, 1));
			when(Util.getInt(3)).thenReturn(1);
			Assertions.assertTrue(person.addTax(tax2));
			Assertions.assertTrue(person.getTaxes().contains(tax1));
			Assertions.assertFalse(person.getTaxes().contains(tax2));

			tax2 = mock(Tax.class);
			when(tax2.getType()).thenReturn(TaxType.BENEFIT);
			when(tax2.getDate()).thenReturn(LocalDate.of(2022, 10, 1));
			when(Util.getInt(3)).thenReturn(2);
			Assertions.assertTrue(person.addTax(tax2));
			Assertions.assertFalse(person.getTaxes().contains(tax1));
			Assertions.assertTrue(person.getTaxes().contains(tax2));

			SalaryTax salaryTax1 = mock(SalaryTax.class);
			when(salaryTax1.getType()).thenReturn(TaxType.SECONDARY_SALARY);
			when(salaryTax1.getDate()).thenReturn(LocalDate.of(2022, 10, 1));
			when(salaryTax1.getCompany()).thenReturn("1");
			Assertions.assertTrue(person.addTax(salaryTax1));
			Assertions.assertTrue(person.getTaxes().contains(salaryTax1));

			SalaryTax salaryTax2 = mock(SalaryTax.class);
			when(salaryTax2.getType()).thenReturn(TaxType.SECONDARY_SALARY);
			when(salaryTax2.getDate()).thenReturn(LocalDate.of(2022, 10, 1));
			when(salaryTax2.getCompany()).thenReturn("2");
			Assertions.assertTrue(person.addTax(salaryTax2));
			Assertions.assertTrue(person.getTaxes().contains(salaryTax2));

			salaryTax2 = mock(SalaryTax.class);
			when(salaryTax2.getType()).thenReturn(TaxType.SECONDARY_SALARY);
			when(salaryTax2.getDate()).thenReturn(LocalDate.of(2022, 10, 1));
			when(salaryTax2.getCompany()).thenReturn("1");
			when(Util.getInt(3)).thenReturn(3);
			Assertions.assertFalse(person.addTax(salaryTax2));
			Assertions.assertTrue(person.getTaxes().contains(salaryTax1));
			Assertions.assertFalse(person.getTaxes().contains(salaryTax2));
		}
	}

	@Test
	@DisplayName("Creation test")
	@ExtendWith(MockitoExtension.class)
	public void fromFile() {
		Person person1 = Person.fromFile("1|A|A");
		Person person2 = Person.fromFile("4|B|A");
		Person person3 = Person.fromFile("1|B|A");
		Person person4 = new Person("C", "D");

		Assertions.assertEquals(1, person1.getID());
		Assertions.assertEquals(4, person2.getID());
		Assertions.assertEquals(1, person3.getID());
		Assertions.assertEquals(5, person4.getID());
		Assertions.assertEquals(person1, person3);
		Assertions.assertNotEquals(person1, person4);
	}
}