package easycommuteguide.model.dao;

import easycommuteguide.model.BusRoute;

public interface BusRouteDao {
	
	BusRoute getEveningBusesById (int busId);
	BusRoute getMorningBusesById (int busId);
	
}
