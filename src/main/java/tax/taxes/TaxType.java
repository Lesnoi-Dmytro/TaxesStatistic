package tax.taxes;

public enum TaxType {
	ROYALTY, PROPERTY_SALE, GIFT, ABROAD_TRANSFER, BENEFIT, PRIMARY_SALARY, SECONDARY_SALARY;

	public static void typeList(boolean hasCompanies) {
		System.out.println("""
				Види податків:
				1 - Авторські винагороди
				2 - Продаж майна
				3 - Подарунок
				4 - Переказ з-за кордону
				5 - Пільги""");
		if (hasCompanies) {
			System.out.println("""
					6 - Основне місце роботи
					7 - Додаткове місце роботи""");
		}
		System.out.print("Вид реєструємого податку: ");
	}

	public static TaxType getType(int ind, boolean hasCompanies) {
		if (hasCompanies) {
			return switch (ind) {
				case 1 -> ROYALTY;
				case 2 -> PROPERTY_SALE;
				case 3 -> GIFT;
				case 4 -> ABROAD_TRANSFER;
				case 5 -> BENEFIT;
				case 6 -> PRIMARY_SALARY;
				case 7 -> SECONDARY_SALARY;
				default -> null;
			};
		} else {
			return switch (ind) {
				case 1 -> ROYALTY;
				case 2 -> PROPERTY_SALE;
				case 3 -> GIFT;
				case 4 -> ABROAD_TRANSFER;
				case 5 -> BENEFIT;
				default -> null;
			};
		}
	}

	public boolean isSalary() {
		return this == PRIMARY_SALARY || this == SECONDARY_SALARY;
	}
}