package db.persistence;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;

public class MyPersistence implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EntityManager em;

	public MyPersistence() {
		this.em = PersistenceManager.getInstance().emf.createEntityManager();
	}

	public void clear() {
		em.clear();
	}

	public void begin() {
		em.getTransaction().begin();
	}

	@SuppressWarnings("rawtypes")
	public List list(String q) {
		List list = null;
		try {
			list = em.createQuery(q).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List list(String q, Hashtable h) {
		Query query = em.createQuery(q);
		for (Enumeration e = h.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			Object value = h.get(key);
			query.setParameter(key, value);
		}
		List list = query.getResultList();
		return list;
	}

	// AZAM ADD
	@SuppressWarnings("rawtypes")
	public List list(EntityManager em, String q) {
		List list = null;
		try {
			list = em.createQuery(q).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public Object get(EntityManager em, String q) {
		List l = list(em, q);
		return ((l.size() > 0) ? l.get(0) : null);
	}

	@SuppressWarnings("rawtypes")
	public Object get(String q) {
		List l = list(q);
		return ((l.size() > 0) ? l.get(0) : null);
	}

	public void persist(Object object) {
		em.persist(object);
	}

	public Object merge(Object object) {
		return em.merge(object);
	}

	public void remove(Object object) {
		em.remove(object);
	}

	public void executeUpdate(String ql) {
		em.createQuery(ql).executeUpdate();
	}

	public void isActive() {
		if (em.getTransaction().isActive())
			em.getTransaction().rollback();
	}

	public void commit() throws Exception {
		try {
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			isActive();
			throw e;
		}
	}

	public void close() {
		isActive();
		em.close();
	}

	public Object find(Class paramClass, Object paramObject) {
		return em.find(paramClass, paramObject);
	}

	public void pesismisticLock(Object seq) {
		em.lock(seq, LockModeType.PESSIMISTIC_WRITE);
	}

	public void flush() {
		em.flush();
	}

	public void refresh(Object o) {
		em.refresh(o);
	}

	public int getRecordMax(String q, int limit) {
		Query query = em.createQuery(q);
		query.setMaxResults(limit);
		return 0;
	}
}
