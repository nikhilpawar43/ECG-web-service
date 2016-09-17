package easycommuteguide.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import easycommuteguide.model.BoardingPoint;
import easycommuteguide.model.Employee;
import easycommuteguide.model.dao.BoardingPointDao;

@Repository
public class BoardingPointDaoImpl implements BoardingPointDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	private List<BoardingPoint> boardingPointsList;
	private List<Employee> employeesList;
	private BoardingPoint boardingPoint;
	
	@SuppressWarnings("unchecked")
	@Override
	public BoardingPoint getBoardingPointByEmpId(int employee_id) {
		try {
			 boardingPointsList = entityManager.createQuery("from BoardingPoint").getResultList();
			 
			 for (BoardingPoint boardingPoint : boardingPointsList) {
				
				 employeesList = boardingPoint.getRegisteredEmployees();
				 for (Employee emp : employeesList) {
					if (emp.getId() == employee_id) {
						return boardingPoint;
					}
				}
			}
			 
			 return null;
			 
		} catch (NoResultException e) {
			System.out.println("The NoResultException is: " + e.getMessage());
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<BoardingPoint> getAllBoardingPoints () {
		
		try {
			boardingPointsList = entityManager.createQuery("from BoardingPoint").getResultList();
		} catch (NoResultException e) {
			System.out.println("The NoResultException is: " + e.getMessage());
		}
		return boardingPointsList;
	}

	@Override
	public BoardingPoint getBoardingPointById(int boardingPointId) {
		
		try {
			boardingPoint = (BoardingPoint) entityManager.createQuery("from BoardingPoint where id=:id").setParameter("id", boardingPointId).getSingleResult();
		} catch (NoResultException e) {
			System.out.println("The NoResultException is: " + e.getMessage());
		}
		return boardingPoint;
	}

	@Override
	public BoardingPoint getBoardingPointByName(String boardingPointName) {

		try {
			boardingPoint = (BoardingPoint) entityManager.createQuery("from BoardingPoint where boardingPointName=:boardingPointName").setParameter("boardingPointName", boardingPointName).getSingleResult();
		} catch (NoResultException e) {
			System.out.println("The NoResultException is: " + e.getMessage());
		}
		return boardingPoint;
	}

}
