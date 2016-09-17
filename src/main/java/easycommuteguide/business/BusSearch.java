package easycommuteguide.business;

import java.util.Arrays;
import java.util.List;

import easycommuteguide.model.BusRoute;
import easycommuteguide.model.BusRouteBoardingPoint;
import easycommuteguide.model.dao.BusRouteBoardingPointDao;
import easycommuteguide.model.dao.BusRouteDao;

public class BusSearch {
	
	private static double shortestDistance;
	private static int shortestDistanceIndex;
	private static double[] distancePoints = new double[5];
	private static List<BusRouteBoardingPoint> busRouteList;
	private static BusRoute busRoute;
	/*private static SearchResults searchResult = new SearchResults();*/
	
	public static List<SearchResults> searchAlternativeBusHalt (List<LocationDistance> googleBoardingPointsList, BusRouteBoardingPointDao busRouteBoardingPointDao, BusRouteDao busRouteDao, List<SearchResults> searchedBusRoutes, String startTime, int closePosition, String ctsFacility) {
		
		SearchResults searchResult;
		
		for (int i=0; i<googleBoardingPointsList.size(); i++) {
			distancePoints [i] = googleBoardingPointsList.get(i).getDistance();
		}
		
		Arrays.sort(distancePoints);
		shortestDistance = distancePoints[closePosition];
		System.out.println("The alternate closest distance is: " + shortestDistance);
		
		shortestDistanceIndex = 0;
		for (int i=0; i<googleBoardingPointsList.size(); i++) {
			/*System.out.println(googleBoardingPointsList.get(i).getDistance());*/
			if (shortestDistance == googleBoardingPointsList.get(i).getDistance()) {
				shortestDistanceIndex = i;
			}
		}
	
		searchResult = new SearchResults();
		
		System.out.println("The nearest boarding point is: ");
		System.out.println("The alternate boarding point id: " + googleBoardingPointsList.get(shortestDistanceIndex).getBoardingPoint().getId() + " - " + googleBoardingPointsList.get(shortestDistanceIndex).getBoardingPoint().getBoardingPointName() + " having distance: " + googleBoardingPointsList.get(shortestDistanceIndex).getDistance() + " Kms, needing approx time of: " + googleBoardingPointsList.get(shortestDistanceIndex).getTime());
		searchResult.setBoardingPointName(googleBoardingPointsList.get(shortestDistanceIndex).getBoardingPoint().getBoardingPointName());
		
		busRouteList = busRouteBoardingPointDao.getBusRouteByBoardpointid(googleBoardingPointsList.get(shortestDistanceIndex).getBoardingPoint(), ctsFacility);
		for (BusRouteBoardingPoint busRouteBoardingPoint : busRouteList) {
			
			busRoute = busRouteDao.getEveningBusesById(busRouteBoardingPoint.getBusRoute().getId());
			if (busRoute != null && busRoute.getStartTime().equals(startTime)) {
				searchResult.setBusRoute(busRoute);
				searchedBusRoutes.add(searchResult);
				System.out.println("The " + startTime + " bus is: ");
				System.out.println("The bus id is: " + busRoute.getId() + " - " + busRoute.getRouteName() + ", having departure at: " + busRoute.getStartTime());
			}
		}
		
		return searchedBusRoutes;
	}

}
