package easycommuteguide.model.dao;

import java.util.List;

import easycommuteguide.model.BoardingPoint;
import easycommuteguide.model.BusRoute;
import easycommuteguide.model.BusRouteBoardingPoint;

public interface BusRouteBoardingPointDao {
	
	List<BusRouteBoardingPoint> getBusRouteByBoardpointid (BoardingPoint boardingPoint, String ctsFacility);
	List<BusRouteBoardingPoint> getBoardingPointsByBusid (BusRoute busRoute);

}
