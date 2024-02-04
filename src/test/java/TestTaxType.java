import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tax.taxes.TaxType;

public class TestTaxType {
	@Test
	@DisplayName("IsSalary test")
	public void isSalary() {
		Assertions.assertTrue(TaxType.SECONDARY_SALARY.isSalary());
		Assertions.assertTrue(TaxType.PRIMARY_SALARY.isSalary());
		Assertions.assertFalse(TaxType.GIFT.isSalary());
	}
}