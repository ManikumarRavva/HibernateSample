package com.journaldev.hibernate.main;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.stat.Statistics;

import com.journaldev.hibernate.model.Employee;
import com.journaldev.hibernate.model.Employee1;
import com.journaldev.hibernate.model.Person;

public class HibernateMain {
	public static void main(String[] args) throws InterruptedException {

		SessionFactory sessionFactory = buildSessionAnnotationFactory();
		Session session = sessionFactory.openSession();

		/*
		 * Employee1 emp = new Employee1();
		 * 
		 * emp.setName("Psandkaj"); emp.setRole("CEO2d"); emp.setInsertTime(new
		 * Date());
		 * 
		 * 
		 * System.out.println("transaction started");
		 * session.beginTransaction();
		 * 
		 * //session.save(emp); System.out.println("after save");
		 * emp.setRole("asd_new"); System.out.println("after save1");
		 * emp.setName("asdd_new"); session.save(emp);
		 * 
		 * System.out.println("after save2 "+emp.getId());
		 * 
		 * //session.clear(); emp.setName("asdf_new"); //session.flush();
		 * System.out.println("before commit");
		 * session.getTransaction().commit(); emp.setName("abc");
		 * System.out.println("emp.getId()"+emp.getId()); session.update(emp);
		 * System.out.println("emp.getId()"+emp.getId());
		 * //session.persist(emp); System.out.println("Saved successfully");
		 * //session.flush(); //List<Employee1> emps = session.createQuery(
		 * "from Employee1").list();
		 */

		/*
		 * 
		 * Employee1 emp = (Employee1) session.load(Employee1.class,
		 * 1);session.evict(emp); System.out.println(session.contains(emp));
		 * 
		 * Thread th = new Thread(); th.sleep(10000);
		 */

		/*Employee1 emp = (Employee1) session.load(Employee1.class, 1);
		System.out.println("hit first time");
		System.out.println(emp.getName());

		System.out.println("fetch second time");

		System.out.println(session.contains(emp));

		Employee1 emp1 = (Employee1) session.load(Employee1.class, 1);

		System.out.println(emp1.getName());

		System.out.println("fetch third time");
		Employee1 emp2 = (Employee1) session.load(Employee1.class, 1);
		System.out.println(emp2.getName());
		session.clear();

		System.out.println("fetch forth time");
		Employee1 emp3 = (Employee1) session.load(Employee1.class, 1);
		System.out.println(emp3.getName());
*/
		
System.out.println("Temp Dir:"+System.getProperty("java.io.tmpdir"));
		
		//Initialize Sessions
		//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Statistics stats = sessionFactory.getStatistics();
		System.out.println("Stats enabled="+stats.isStatisticsEnabled());
		stats.setStatisticsEnabled(true);
		System.out.println("Stats enabled="+stats.isStatisticsEnabled());
		
		//Session session = sessionFactory.openSession();
		Session otherSession = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Transaction otherTransaction = otherSession.beginTransaction();
		
		printStats(stats, 0);
		
		Employee1 emp = (Employee1) session.load(Employee1.class, 1);
		printData(emp, stats, 1);
		
		emp = (Employee1) session.load(Employee1.class, 1);
		printData(emp, stats, 2);
		
		//clear first level cache, so that second level cache is used
		session.evict(emp);
		emp = (Employee1) session.load(Employee1.class, 1);
		printData(emp, stats, 3);
		
		emp = (Employee1) session.load(Employee1.class, 2);
		printData(emp, stats, 4);
		
		emp = (Employee1) otherSession.load(Employee1.class, 1);
		printData(emp, stats, 5);
		
		//Release resources
		transaction.commit();
		otherTransaction.commit();
		sessionFactory.close();

	}

	private static SessionFactory buildSessionAnnotationFactory() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			Configuration configuration = new Configuration();
			configuration.configure("hibernate-annotation.cfg.xml");
			System.out.println("Hibernate Annotation Configuration loaded");

			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
			System.out.println("Hibernate Annotation serviceRegistry created");

			SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

			return sessionFactory;
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	private static void printStats(Statistics stats, int i) {
		System.out.println("***** " + i + " *****");
		System.out.println("Fetch Count=" + stats.getEntityFetchCount());
		System.out.println("Second Level Hit Count=" + stats.getSecondLevelCacheHitCount());
		System.out.println("Second Level Miss Count=" + stats.getSecondLevelCacheMissCount());
		System.out.println("Second Level Put Count=" + stats.getSecondLevelCachePutCount());
	}

	private static void printData(Employee1 emp, Statistics stats, int count) {
		System.out.println(count + ":: Name=" + emp.getName() + ", Zipcode=" + emp.getRole());
		printStats(stats, count);
	}
}
