package easycommuteguide.model.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import easycommuteguide.model.BoardingPoint;
import easycommuteguide.model.BusRoute;
import easycommuteguide.model.BusRouteBoardingPoint;
import easycommuteguide.model.dao.BusRouteBoardingPointDao;

@Repository
public class BusRouteBoardingPointDaoImpl implements BusRouteBoardingPointDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private List<BusRouteBoardingPoint> busRouteList, tempBusRouteList; 
	private BusRoute busRoute;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BusRouteBoardingPoint> getBusRouteByBoardpointid(BoardingPoint boardingPoint, String ctsFacility) {
		
		try {
//			return entityManager.createQuery("from BusRouteBoardingPoint where boardingPoint=:boardingPoint").setParameter("boardingPoint", boardingPoint).getResultList();
			busRouteList = entityManager.createQuery("from BusRouteBoardingPoint where boardingPoint=:boardingPoint").setParameter("boardingPoint", boardingPoint).getResultList();
			
			tempBusRouteList = new ArrayList<>();
			
			for (BusRouteBoardingPoint busRouteBoardingPoint : busRouteList) {

				busRoute = busRouteBoardingPoint.getBusRoute();
				
				if (busRoute.getDestinationCTSFacility().equals(ctsFacility)) {
					tempBusRouteList.add(busRouteBoardingPoint);
				}
			}
			
			return tempBusRouteList;
			
		} catch (NoResultException e) {
			System.out.println("The NoResultException is: " + e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BusRouteBoardingPoint> getBoardingPointsByBusid(BusRoute busRoute) {
		
		try {
			return entityManager.createQuery("from BusRouteBoardingPoint where busRoute=:busRoute").setParameter("busRoute", busRoute).getResultList();
		} catch (NoResultException e) {
			System.out.println("The NoResultException is: " + e.getMessage());
		}
		return null;
	}

}
