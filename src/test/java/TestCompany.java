import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tax.info.Company;
import tax.info.Person;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TestCompany {
	@Test
	@DisplayName("Creation test")
	public void create() {
		Company company = new Company("1");
		Assertions.assertEquals("1", company.getName());
		Assertions.assertEquals("1", company.toString());
	}

	@Test
	@DisplayName("Add and remove workers test")
	@ExtendWith(MockitoExtension.class)
	public void addWorker() {
		Company company = new Company("1");
		Person person1 = mock(Person.class);
		Person person2 = mock(Person.class);
		Person person3 = mock(Person.class);

		when(person1.getID()).thenReturn(1);
		when(person2.getID()).thenReturn(2);
		when(person3.getID()).thenReturn(1);

		Assertions.assertTrue(company.addWorker(person1));
		Assertions.assertTrue(company.addWorker(person2));
		Assertions.assertFalse(company.addWorker(person3));
		Assertions.assertTrue(company.getWorkers().contains(person1));
		Assertions.assertTrue(company.getWorkers().contains(person2));
		Assertions.assertFalse(company.getWorkers().contains(person3));

		Assertions.assertTrue(company.removeWorker(1));
		Assertions.assertFalse(company.removeWorker(3));
	}
}