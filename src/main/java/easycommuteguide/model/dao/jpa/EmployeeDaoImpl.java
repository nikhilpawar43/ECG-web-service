package easycommuteguide.model.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import easycommuteguide.model.Employee;
import easycommuteguide.model.dao.EmployeeDao;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Employee login(int userid, String password) {
		try {
			return entityManager.createQuery("from Employee where id=:id and password=:password", Employee.class).setParameter("id", userid).setParameter("password", password).getSingleResult();
		} catch (NoResultException e) {
			System.out.println("The NoResultException is: " + e.getMessage());
			return null;
		}
	}

	@Override
	public Employee getEmployeeById(int userid) {

		try {
			return entityManager.createQuery("from Employee where id=:id", Employee.class).setParameter("id", userid).getSingleResult();
		} catch (NoResultException e) {
			System.out.println("The NoResultException is: " + e.getMessage());
			return null;
		}
	}
	
}
