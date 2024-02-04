package tax.command;

import tax.info.Company;
import tax.info.Person;
import tax.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Command {
	protected static Person worker = null;
	private final static ArrayList<Command> commands = new ArrayList<>(List.of(new LoadData()));
	protected static Scanner scanner = new Scanner(System.in);
	protected String description;

	public abstract boolean action(ArrayList<Person> workers, ArrayList<Company> companies);

	public static void CommandList(ArrayList<Person> workers, ArrayList<Company> companies) {
		commands.clear();
		commands.addAll(List.of(
				new WriteData(),
				new ChooseWorker(),
				new AddCompany()));
		if (!workers.isEmpty() && worker != null) {
			commands.addAll(List.of(
					new AddData(),
					new EditData(),
					new DeleteData(),
					new ViewPerson()
			));
		}
		if (!companies.isEmpty()) {
			commands.add(new ViewCompany());
		}
		System.out.println("Доступні дії:");
		Util.printList(commands, 0);
	}

	public static int getCommandCount() {
		return commands.size();
	}

	public static Command getCommand(int ind) {
		return (ind > -1 && ind < commands.size()) ? commands.get(ind) : null;
	}

	@Override
	public String toString() {
		return description;
	}
}