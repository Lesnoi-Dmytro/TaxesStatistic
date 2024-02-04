package tax.command;

import tax.info.Company;
import tax.info.Person;
import tax.util.Util;

import java.util.ArrayList;

public class ChooseWorker extends Command {
	public ChooseWorker() {
		description = "Обрати користувача";
	}

	@Override
	public boolean action(ArrayList<Person> workers, ArrayList<Company> companies) {
		if (workers.isEmpty()) {
			System.out.println("Створіть нового працівника");
			workers.add(Person.newPerson());
			return true;
		} else {
			System.out.print("""
					1 - Створити нового працівника
					2 - Обрати наявного працівника
					Оберіть дію:\s""");
			Person person;
			if (Util.getInt(2) == 1) {
				person = Person.newPerson();
			} else {
				workers.forEach(System.out::println);
				person = Person.toFind();
			}
			for (Person p : workers) {
				if (p.equals(person)) {
					worker = p;
					return false;
				}
			}
			workers.add(person);
			worker = person;
			return true;
		}
	}
}