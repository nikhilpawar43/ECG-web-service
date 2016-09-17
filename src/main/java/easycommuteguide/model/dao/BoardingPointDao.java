package easycommuteguide.model.dao;

import java.util.List;

import easycommuteguide.model.BoardingPoint;

public interface BoardingPointDao {
	
	BoardingPoint getBoardingPointByEmpId (int employee_id);
	List<BoardingPoint> getAllBoardingPoints ();
	BoardingPoint getBoardingPointById (int boardingPointId);
	BoardingPoint getBoardingPointByName (String boardingPointName);
	
}
