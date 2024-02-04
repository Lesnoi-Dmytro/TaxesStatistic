package tax.taxes;

import tax.info.Person;

public class SalaryTax extends Tax {
	private static final double maxESW = 22110;
	private static final double minESW = 1474;
	private final String company;

	public SalaryTax(Tax tax, String company) {
		super(tax);
		this.company = company;
	}

	public String getCompany() {
		return company;
	}

	public double getCompanyTax() {
		double esw = amount * 0.22;
		if (type == TaxType.PRIMARY_SALARY) {
			esw = Math.max(minESW, esw);
		}
		return Math.min(esw, maxESW);
	}

	@Override
	public String toFile(Person person) {
		return super.toFile(person) + "|" + company;
	}

	@Override
	public String toString() {
		return super.toString() + ", " + company;
	}
}