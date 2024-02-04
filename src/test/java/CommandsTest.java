import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tax.command.AddCompany;
import tax.command.Command;
import tax.info.Company;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandsTest {
	@Test
	@DisplayName("Add company test")
	@ExtendWith(MockitoExtension.class)
	public void addCompany() {
		Company company1 = mock(Company.class);
		when(company1.getName()).thenReturn("1");

		ArrayList<Company> companies = new ArrayList<>(List.of(company1));
		try {
			Scanner s = mock(Scanner.class);
			Field scanner = Command.class.getDeclaredField("scanner");
			scanner.setAccessible(true);
			scanner.set(scanner, s);
			when(s.nextLine()).thenReturn("2");
			Assertions.assertTrue(new AddCompany().action(null, companies));
			when(s.nextLine()).thenReturn("1");
			Assertions.assertFalse(new AddCompany().action(null, companies));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}