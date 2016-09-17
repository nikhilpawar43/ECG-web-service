package easycommuteguide.model.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import easycommuteguide.model.BusRoute;
import easycommuteguide.model.dao.BusRouteDao;

@Repository
public class BusRouteDaoImpl implements BusRouteDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public BusRoute getEveningBusesById(int busId) {
		
		try {
			return (BusRoute) entityManager.createQuery("from BusRoute where id=:id and session = 'Evening'").setParameter("id", busId).getSingleResult();
		} catch (NoResultException e) {
			System.out.println("The NoResultException is: " + e.getMessage());
		}
		return null;
	}

	@Override
	public BusRoute getMorningBusesById(int busId) {

		try {
			return (BusRoute) entityManager.createQuery("from BusRoute where id=:id and session = 'Morning'").setParameter("id", busId).getSingleResult();
		} catch (NoResultException e) {
			System.out.println("The NoResultException is: " + e.getMessage());
		}
		return null;
	}
	
}
