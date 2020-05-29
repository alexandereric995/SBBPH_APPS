package db.persistence;

import java.util.Date;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * This class will create singleton PersistenceManager specifically for
 * Transaction
 * 
 * @author tab
 */
public class PersistenceManager {

	public static final boolean DEBUG = true;
	private static final PersistenceManager singleton = new PersistenceManager();
	protected EntityManagerFactory emf;

	public static PersistenceManager getInstance() {
		return singleton;
	}

	private PersistenceManager() {
	}

	public EntityManagerFactory getEntityManagerFactory() {
		if (emf == null)
			createEntityManagerFactory();
		return emf;
	}

	public void closeEntityManagerFactory() {
		if (emf != null) {
			emf.close();
			emf = null;
			if (DEBUG)
				System.out.println("Persistence closed at " + new Date());
		}
	}

	public void createEntityManagerFactory() {
		this.emf = Persistence.createEntityManagerFactory("persistence");
		if (DEBUG)
			System.out.println("Persistence created at " + new Date());
	}
}
