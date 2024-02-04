package tax.info;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Company {
	private final String name;
	private final Set<Person> workers;

	public Company(String name) {
		this.name = name;
		workers = new HashSet<>();
	}

	public String getName() {
		return name;
	}

	public Set<Person> getWorkers() {
		return new HashSet<>(workers);
	}

	public Person getWorker(int id) {
		for (Person w : workers) {
			if (w.getID() == id) {
				return w;
			}
		}
		return null;
	}

	public boolean addWorker(Person worker) {
		if (getWorker(worker.getID()) != null) {
			return false;
		}
		return workers.add(worker);
	}

	public boolean removeWorker(int id) {
		Iterator<Person> iterator = workers.iterator();
		while (iterator.hasNext()) {
			Person person = iterator.next();
			if (person.getID() == id) {
				iterator.remove();
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return name;
	}
}