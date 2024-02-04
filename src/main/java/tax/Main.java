package tax;

import tax.command.Command;
import tax.command.WriteData;
import tax.info.Company;
import tax.info.Person;
import tax.util.Util;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Main {
	private static final ArrayList<Company> companies = new ArrayList<>();
	private static final ArrayList<Person> workers = new ArrayList<>();
	private static final Logger logFile = LogManager.getLogger("file");
	private static final Logger logEmail = LogManager.getLogger("email");

	public static void main(String[] args) {
		try {
			if (true) {
				throw new IOException();
			}
			Command command = Command.getCommand(0);
			command.action(workers, companies);
			logFile.info(command.getClass().getSimpleName() + " was executed");
			boolean changed = false;
			while (true) {
				System.out.println();
				System.out.println("*".repeat(30));
				System.out.println();
				Command.CommandList(workers, companies);
				System.out.print("Ваша дія: ");
				command = Command.getCommand(Util.getInt(0, Command.getCommandCount()));
				if (command instanceof WriteData) {
					if (changed) {
						command.action(workers, companies);
						logFile.info(command.getClass().getSimpleName() + " was executed");
					}
					return;
				}
				boolean action = command.action(workers, companies);
				logFile.info(command.getClass().getSimpleName() +
						" was executed and changed info: " + (action ? "true" : "false"));
				changed = action || changed;
			}
		} catch (Exception e) {
			logEmail.error(e);
			throw new RuntimeException(e);
		}
	}
}