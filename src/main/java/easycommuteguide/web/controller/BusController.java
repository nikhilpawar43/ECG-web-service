package easycommuteguide.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import easycommuteguide.business.BusSearch;
import easycommuteguide.business.GlobalValues;
import easycommuteguide.business.LocalDistanceComparator;
import easycommuteguide.business.LocationDistance;
import easycommuteguide.business.SearchResults;
import easycommuteguide.model.BoardingPoint;
import easycommuteguide.model.BusRoute;
import easycommuteguide.model.BusRouteBoardingPoint;
import easycommuteguide.model.dao.BoardingPointDao;
import easycommuteguide.model.dao.BusRouteBoardingPointDao;
import easycommuteguide.model.dao.BusRouteDao;
import easycommuteguide.parsers.LocationDistanceParser;
import easycommuteguide.util.HttpManager;
import easycommuteguide.util.RequestPackage;


@Controller
public class BusController {
	
    @Autowired
    private BoardingPointDao boardingPointDao;
    
    @Autowired
    private BusRouteBoardingPointDao busRouteBoardingPointDao;
    
    @Autowired
    private BusRouteDao busRouteDao;
    
    private List<BoardingPoint> boardingPointsList;
    private List<LocationDistance> localBoardingPointsList, googleBoardingPointsList;
    private BoardingPoint tempBoardingPoint;
    
	private String ctsFacility, destinationName, destinationPlaceID;
	private String uri = "https://maps.googleapis.com/maps/api/place/details/json?key=AIzaSyCq6Epz6ti0M-sIlVWQfCZzn_P5r8AqJVE&placeid=";
	private StringBuilder top5LocationSearchURI; 
	private StringBuilder locationDetailsParamaters;
	private double shortestDistance;
	private int shortestDistanceIndex;
	
	private List<BusRouteBoardingPoint> busRouteList, tempbusRouteList;
	private BusRoute busRoute;
	/*private List<BusRoute> searchedBusRoutes;*/
	private List<SearchResults> searchedBusRoutes;
	private SearchResults searchResult;
	
	private double[] latlng;
	int closePosition = 0;
	private HttpSession session;
	
	private double latitude, longitude;
	private String findPlaceById;
	private RequestPackage requestPackage;
	private String content;
	
	@RequestMapping(value="/searchBuses", method = RequestMethod.POST)
	protected @ResponseBody List<SearchResults> searchBus (HttpServletRequest request) {
		
		System.out.println("Requesting the webservice to search bus from CTS.");
		
		ctsFacility = request.getParameter("ctsFacility");
		destinationName = request.getParameter("destinationName");
		destinationPlaceID = request.getParameter("destinationPlaceID");
		
		System.out.println("The CTS facility is: " + ctsFacility + ", destinationName is: " + destinationName + ", destination place id is: " + destinationPlaceID);
		
		session = request.getSession();
		session.setAttribute("ctsFacility", ctsFacility);
		GlobalValues.ctsFacility = ctsFacility;
		
		uri = "https://maps.googleapis.com/maps/api/place/details/json?key=AIzaSyCq6Epz6ti0M-sIlVWQfCZzn_P5r8AqJVE&placeid=";
		uri = uri + destinationPlaceID;
		
		requestPackage = new RequestPackage();
		requestPackage.setMethod("POST");
		requestPackage.setUri(uri);
		requestPackage.setParam("ctsFacility", ctsFacility);
		requestPackage.setParam("destinationName", destinationName);
		requestPackage.setParam("destinationPlaceID", destinationPlaceID);
		
		String content = HttpManager.getData(requestPackage);
//	System.out.println("The location details are: \n\n" + content);
		
		latlng = HttpManager.parseDestinationLocationDetails(content);
		
		System.out.println("The latitude is: " + latlng[0]);
		System.out.println("The longitude is: " + latlng[1]);
		System.out.println();
		
//	Fetching all the boarding points for comparison with the input latitude and longitude
		boardingPointsList = boardingPointDao.getAllBoardingPoints();
		
//	Fetching the TOP 5 nearest boardingPoints using the straight line comparator
		localBoardingPointsList = LocalDistanceComparator.getTopLocalNearestBoardingPoints(boardingPointsList, latlng);
		
//	Displaying the TOP 5 nearest boardingPoints
		locationDetailsParamaters = new StringBuilder("");
		System.out.println("The top 5 local boarding points are: ");
		for (LocationDistance locationDistance : localBoardingPointsList) {
			System.out.println("Location: " + locationDistance.getBoardingPoint().getBoardingPointName() + " having distance: " + locationDistance.getDistance() + " Kms.");
			locationDetailsParamaters.append(locationDistance.getBoardingPoint().getLattitude() + "," + locationDistance.getBoardingPoint().getLongitude() + "|");
		}
		
		int length = locationDetailsParamaters.length();
		locationDetailsParamaters.setCharAt(length-1, '\0');
		
//	Find the distance for top 5 boarding from Google Distance API
		top5LocationSearchURI = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json");
		
		requestPackage = new RequestPackage();
		requestPackage.setUri(top5LocationSearchURI.toString());
		requestPackage.setParam("key", "AIzaSyCq6Epz6ti0M-sIlVWQfCZzn_P5r8AqJVE");
		requestPackage.setParam("origins", latlng[0] + "," + latlng[1]);
		requestPackage.setParam("destinations", locationDetailsParamaters.toString());
		requestPackage.setParam("mode", "driving");
		requestPackage.setParam("language", "en");
		
		content = HttpManager.getData(requestPackage);
		/*System.out.println();
		System.out.println("The distance details are: \n\n" + content);*/
		
//	Google calculated TOP 5 nearest boarding points
		googleBoardingPointsList = LocationDistanceParser.parseFeed (content, localBoardingPointsList);

//	Displaying Google calculated TOP 5 nearest boarding points
		System.out.println("The Google calculated distances are: ");
		for (LocationDistance boardingPoint : googleBoardingPointsList) {
			System.out.println("The boarding point name is: " + boardingPoint.getBoardingPoint().getBoardingPointName() + " having distance: " + boardingPoint.getDistance() + " Kms, approx time needed is: " + boardingPoint.getTime());
		}
		
//	Distance checking for testing purpose
		googleBoardingPointsList = localBoardingPointsList;
		
//	Find the shortest distance boarding point
		shortestDistance = googleBoardingPointsList.get(0).getDistance();
		shortestDistanceIndex = 0;
		for (int i=1; i<googleBoardingPointsList.size(); i++) {
			
			if (shortestDistance > googleBoardingPointsList.get(i).getDistance()) {
				shortestDistance = googleBoardingPointsList.get(i).getDistance();
				shortestDistanceIndex = i;
			}
		}
		
		System.out.println();
		System.out.println("The nearest boarding point is: ");
		System.out.println("The boarding point id: " + googleBoardingPointsList.get(shortestDistanceIndex).getBoardingPoint().getId() + " - " + googleBoardingPointsList.get(shortestDistanceIndex).getBoardingPoint().getBoardingPointName() + " having distance: " + googleBoardingPointsList.get(shortestDistanceIndex).getDistance() + " Kms, needing approx time of: " + googleBoardingPointsList.get(shortestDistanceIndex).getTime());
		
//	Find the evening buses going via the nearest boarding point
		busRouteList = busRouteBoardingPointDao.getBusRouteByBoardpointid(googleBoardingPointsList.get(shortestDistanceIndex).getBoardingPoint(), ctsFacility);
		System.out.println();
		System.out.println("The buses having the nearest boarding point are: ");

		if (busRouteList != null && busRouteList.size() > 0)
			searchedBusRoutes = new ArrayList<>();
		
		int searchflag = 0;
		for (BusRouteBoardingPoint busRouteBoardingPoint : busRouteList) {
			
			busRoute = busRouteDao.getEveningBusesById(busRouteBoardingPoint.getBusRoute().getId());
			if (busRoute != null) {
//				if (busRoute.getDestinationCTSFacility().equals(ctsFacility)) {
					searchResult = new SearchResults();
					searchResult.setBoardingPointName(googleBoardingPointsList.get(shortestDistanceIndex).getBoardingPoint().getBoardingPointName());
					searchResult.setBusRoute(busRoute);
					searchedBusRoutes.add(searchResult);
					System.out.println("The bus id is: " + busRoute.getId() + " - " + busRoute.getRouteName() + ", having departure at: " + busRoute.getStartTime());
					searchflag++;
//				}
			} /*else if (busRoute == null) {
				
				busRouteList = new ArrayList<>();
				closePosition = 0;
				
				while (searchedBusRoutes.size() == 0 && closePosition < 4) {
					closePosition ++;
					searchedBusRoutes = BusSearch.searchAlternativeBusHalt(googleBoardingPointsList, busRouteBoardingPointDao, busRouteDao, searchedBusRoutes, "18:15", closePosition, ctsFacility);
					
					if (searchedBusRoutes.size() > 0) {
						tempBoardingPoint = boardingPointDao.getBoardingPointByName(searchedBusRoutes.get(0).getBoardingPointName());
						tempbusRouteList = busRouteBoardingPointDao.getBusRouteByBoardpointid(tempBoardingPoint, ctsFacility);
						
						searchedBusRoutes = new ArrayList<>();
						for (BusRouteBoardingPoint busRouteBoardingPoint1 : tempbusRouteList) {
							
							busRoute = busRouteBoardingPoint1.getBusRoute();
							if (busRoute.getSession().equals("Evening")) {
								busRouteList.add(busRouteBoardingPoint1);
								searchResult = new SearchResults();
								searchResult.setBoardingPointName(busRouteBoardingPoint1.getBoardingPoint().getBoardingPointName());
								searchResult.setBusRoute(busRoute);
								searchedBusRoutes.add(searchResult);
							}
							
						}
						break;
					}
				}
			}*/
		}
		
		if (searchflag == 0) {
			busRouteList = new ArrayList<>();
			closePosition = 0;
			
			while (searchedBusRoutes.size() == 0 && closePosition < 4) {
				closePosition ++;
				searchedBusRoutes = BusSearch.searchAlternativeBusHalt(googleBoardingPointsList, busRouteBoardingPointDao, busRouteDao, searchedBusRoutes, "18:15", closePosition, ctsFacility);
				
				if (searchedBusRoutes.size() > 0) {
					tempBoardingPoint = boardingPointDao.getBoardingPointByName(searchedBusRoutes.get(0).getBoardingPointName());
					tempbusRouteList = busRouteBoardingPointDao.getBusRouteByBoardpointid(tempBoardingPoint, ctsFacility);
					
					searchedBusRoutes = new ArrayList<>();
					for (BusRouteBoardingPoint busRouteBoardingPoint1 : tempbusRouteList) {
						
						busRoute = busRouteBoardingPoint1.getBusRoute();
						if (busRoute.getSession().equals("Evening")) {
							busRouteList.add(busRouteBoardingPoint1);
							searchResult = new SearchResults();
							searchResult.setBoardingPointName(busRouteBoardingPoint1.getBoardingPoint().getBoardingPointName());
							searchResult.setBusRoute(busRoute);
							searchedBusRoutes.add(searchResult);
						}
						
					}
					break;
				}
			}
		}
		
		
//		Check whether all 3 bus timings are covered
			int timeCounter = 0;
			for (BusRouteBoardingPoint busRouteBoardingPoint : busRouteList) {
				
				if (busRouteBoardingPoint.getBusRoute().getStartTime().equals("18:15")) {
					timeCounter += 1;
				} else if (busRouteBoardingPoint.getBusRoute().getStartTime().equals("19:30")) {
					timeCounter += 2;
				} else if (busRouteBoardingPoint.getBusRoute().getStartTime().equals("21:00")) {
					timeCounter += 4;
				}
			}
			
			if (searchedBusRoutes.size() == 2) {
				
				if (timeCounter == 3) {
					/*searchedBusRoutes = BusSearch.searchAlternativeBusHalt(googleBoardingPointsList, busRouteBoardingPointDao, busRouteDao, searchedBusRoutes, "21:00", 1);*/
					closePosition = 0;
					while (searchedBusRoutes.size() < 3 && closePosition < 4) {
						closePosition ++;
						searchedBusRoutes = BusSearch.searchAlternativeBusHalt(googleBoardingPointsList, busRouteBoardingPointDao, busRouteDao, searchedBusRoutes, "21:00", closePosition, ctsFacility);
					}
				} else if (timeCounter == 5) {
					/*searchedBusRoutes = BusSearch.searchAlternativeBusHalt(googleBoardingPointsList, busRouteBoardingPointDao, busRouteDao, searchedBusRoutes, "19:30", 1);*/
					closePosition = 0;
					while (searchedBusRoutes.size() < 3 && closePosition < 4) {
						closePosition ++;
						searchedBusRoutes = BusSearch.searchAlternativeBusHalt(googleBoardingPointsList, busRouteBoardingPointDao, busRouteDao, searchedBusRoutes, "19:30", closePosition, ctsFacility);
					}
				} else if (timeCounter == 6) {
					/*searchedBusRoutes = BusSearch.searchAlternativeBusHalt(googleBoardingPointsList, busRouteBoardingPointDao, busRouteDao, searchedBusRoutes, "18:15", 1);*/
					closePosition = 0;
					while (searchedBusRoutes.size() < 3 && closePosition < 4) {
						closePosition ++;
						searchedBusRoutes = BusSearch.searchAlternativeBusHalt(googleBoardingPointsList, busRouteBoardingPointDao, busRouteDao, searchedBusRoutes, "18:15", closePosition, ctsFacility);
					}
				}
				
			} else if (searchedBusRoutes.size() == 1) {
				if (timeCounter == 1) {
					
					closePosition = 0;
					while (searchedBusRoutes.size() < 2 && closePosition < 4) {
						closePosition ++;
						searchedBusRoutes = BusSearch.searchAlternativeBusHalt(googleBoardingPointsList, busRouteBoardingPointDao, busRouteDao, searchedBusRoutes, "21:00", closePosition, ctsFacility);
					}
					
					closePosition = 0;
					while (searchedBusRoutes.size() < 3 && closePosition < 4) {
						closePosition ++;
						searchedBusRoutes = BusSearch.searchAlternativeBusHalt(googleBoardingPointsList, busRouteBoardingPointDao, busRouteDao, searchedBusRoutes, "19:30", closePosition, ctsFacility);
					}
					
				} else if (timeCounter == 2) {
					
					closePosition = 0;
					while (searchedBusRoutes.size() < 2 && closePosition < 4) {
						closePosition ++;
						searchedBusRoutes = BusSearch.searchAlternativeBusHalt(googleBoardingPointsList, busRouteBoardingPointDao, busRouteDao, searchedBusRoutes, "21:00", closePosition, ctsFacility);
					}
					
					closePosition = 0;
					while (searchedBusRoutes.size() < 3 && closePosition < 4) {
						closePosition ++;
						searchedBusRoutes = BusSearch.searchAlternativeBusHalt(googleBoardingPointsList, busRouteBoardingPointDao, busRouteDao, searchedBusRoutes, "18:15", closePosition, ctsFacility);
					}
					
				} else if (timeCounter == 4) {
					
					closePosition = 0;
					while (searchedBusRoutes.size() < 2 && closePosition < 4) {
						closePosition ++;
						searchedBusRoutes = BusSearch.searchAlternativeBusHalt(googleBoardingPointsList, busRouteBoardingPointDao, busRouteDao, searchedBusRoutes, "19:30", closePosition, ctsFacility);
					}
					
					closePosition = 0;
					while (searchedBusRoutes.size() < 3 && closePosition < 4) {
						closePosition ++;
						searchedBusRoutes = BusSearch.searchAlternativeBusHalt(googleBoardingPointsList, busRouteBoardingPointDao, busRouteDao, searchedBusRoutes, "18:15", closePosition, ctsFacility);
					}

				}
			}
	
		return searchedBusRoutes;
	}
	
	@RequestMapping(value="/searchBusToCTS", method = RequestMethod.POST)
	protected @ResponseBody List<SearchResults> searchBusToCTS (HttpServletRequest request) {
		
		System.out.println("Requesting the webservice to search bus To CTS.");
		
		String latitude_str = request.getParameter("latitude");
		String longitude_str = request.getParameter("longitude");
		
		if (latitude_str != null && longitude_str != null) {
			
			latitude = Double.parseDouble(latitude_str );
			longitude = Double.parseDouble(longitude_str );
		}
		ctsFacility = request.getParameter("ctsFacility");
		findPlaceById = request.getParameter("placeid");
		GlobalValues.ctsFacility = ctsFacility;
		
		System.out.println("The CTS facility is: " + ctsFacility + ", latitude is: " + latitude + ", longitude is: " + longitude + " and place id is: " + findPlaceById);
		
//	If the user has entered his current location via AutoCompleteTextField
		if (findPlaceById != null && findPlaceById.length() > 0) {
			
			uri = "https://maps.googleapis.com/maps/api/place/details/json?key=AIzaSyCq6Epz6ti0M-sIlVWQfCZzn_P5r8AqJVE&placeid=";
			uri = uri + findPlaceById;
			
			requestPackage = new RequestPackage();
			requestPackage.setMethod("POST");
			requestPackage.setUri(uri);
			
			String content = HttpManager.getData(requestPackage);
//		System.out.println("The location details are: \n\n" + content);
			
			latlng = HttpManager.parseDestinationLocationDetails(content);
			
		} else {
			latlng = new double[2];
			latlng[0] = latitude;
			latlng[1] = longitude;
		}
		
//	Fetching all the boarding points for comparison with the input latitude and longitude
		boardingPointsList = boardingPointDao.getAllBoardingPoints();
		
//	Fetching the TOP 5 nearest boardingPoints using the straight line comparator
		localBoardingPointsList = LocalDistanceComparator.getTopLocalNearestBoardingPoints(boardingPointsList, latlng);
		
//	Displaying the TOP 5 nearest boardingPoints
		locationDetailsParamaters = new StringBuilder("");
		System.out.println("The top 5 local boarding points are: ");
		for (LocationDistance locationDistance : localBoardingPointsList) {
			System.out.println("Location: " + locationDistance.getBoardingPoint().getBoardingPointName() + " having distance: " + locationDistance.getDistance() + " Kms.");
			locationDetailsParamaters.append(locationDistance.getBoardingPoint().getLattitude() + "," + locationDistance.getBoardingPoint().getLongitude() + "|");
		}
		
/*		Preparing the parameters for google distance calculation request (locationDetailsParamaters)
		Removing the last pipe character from the locationDetailsParamaters*/
		int length = locationDetailsParamaters.length();
		locationDetailsParamaters.setCharAt(length-1, '\0');

//	Find the distance for top 5 boarding from Google Distance API
		top5LocationSearchURI = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json");
		
		requestPackage = new RequestPackage();
		requestPackage.setUri(top5LocationSearchURI.toString());
		requestPackage.setParam("key", "AIzaSyCq6Epz6ti0M-sIlVWQfCZzn_P5r8AqJVE");
		requestPackage.setParam("origins", latlng[0] + "," + latlng[1]);
		requestPackage.setParam("destinations", locationDetailsParamaters.toString());
		requestPackage.setParam("mode", "driving");
		requestPackage.setParam("language", "en");
		
		content = HttpManager.getData(requestPackage);
/*		System.out.println();
		System.out.println("The distance details are: \n\n" + content); 		*/

//	Google calculated TOP 5 nearest boarding points
		googleBoardingPointsList = LocationDistanceParser.parseFeed (content, localBoardingPointsList);

//	Displaying Google calculated TOP 5 nearest boarding points
		System.out.println("The Google calculated distances are: ");
		for (LocationDistance boardingPoint : googleBoardingPointsList) {
			System.out.println("The boarding point name is: " + boardingPoint.getBoardingPoint().getBoardingPointName() + " having distance: " + boardingPoint.getDistance() + " Kms, approx time needed is: " + boardingPoint.getTime());
		}
		
//		Distance checking for testing purpose
			googleBoardingPointsList = localBoardingPointsList;

//		Find the shortest distance boarding point
			shortestDistance = googleBoardingPointsList.get(0).getDistance();
			shortestDistanceIndex = 0;
			for (int i=1; i<googleBoardingPointsList.size(); i++) {
				
				if (shortestDistance > googleBoardingPointsList.get(i).getDistance()) {
					shortestDistance = googleBoardingPointsList.get(i).getDistance();
					shortestDistanceIndex = i;
				}
			}
			
			System.out.println();
			System.out.println("The nearest boarding point is: ");
			System.out.println("The boarding point id: " + googleBoardingPointsList.get(shortestDistanceIndex).getBoardingPoint().getId() + " - " + googleBoardingPointsList.get(shortestDistanceIndex).getBoardingPoint().getBoardingPointName() + " having distance: " + googleBoardingPointsList.get(shortestDistanceIndex).getDistance() + " Kms, needing approx time of: " + googleBoardingPointsList.get(shortestDistanceIndex).getTime());

//		Find the morning buses going via the nearest boarding point
			busRouteList = busRouteBoardingPointDao.getBusRouteByBoardpointid(googleBoardingPointsList.get(shortestDistanceIndex).getBoardingPoint(), ctsFacility);
			System.out.println();
			System.out.println("The buses having the nearest boarding point are: ");

			if (busRouteList != null && busRouteList.size()>0) {
				searchedBusRoutes = new ArrayList<>();
			}

			for (BusRouteBoardingPoint busRouteBoardingPoint : busRouteList) {
				
				busRoute = busRouteDao.getMorningBusesById(busRouteBoardingPoint.getBusRoute().getId());
				
				if (busRoute != null) {
						searchResult = new SearchResults();
						searchResult.setBoardingPointName(googleBoardingPointsList.get(shortestDistanceIndex).getBoardingPoint().getBoardingPointName());
						searchResult.setBusRoute(busRoute);
						System.out.println("The halt time is: " + busRouteBoardingPoint.getHaltTime());
						searchResult.setBoardingTime(busRouteBoardingPoint.getHaltTime());
						searchedBusRoutes.add(searchResult);
						System.out.println("The bus id is: " + busRoute.getId() + " - " + busRoute.getRouteName() + ", having departure at: " + busRoute.getStartTime());
						return searchedBusRoutes;
					
				} else if (busRoute == null) {
					
					double[] shortestDistanceArray = new double[6];
					
					for (int i=0; i<googleBoardingPointsList.size(); i++) {
						shortestDistanceArray[i] = googleBoardingPointsList.get(i).getDistance();
					}
					
					Arrays.sort(shortestDistanceArray);
					
					for (int i=2; i<shortestDistanceArray.length-1; i++) {
						for (LocationDistance boardingPoint : googleBoardingPointsList) {
							
							if (shortestDistanceArray[i] == boardingPoint.getDistance()) {
								
								busRouteList = boardingPoint.getBoardingPoint().getBusRouteBoardingPoints();
								for (BusRouteBoardingPoint busRouteBoardingPoint2 : busRouteList) {
									
									busRoute = busRouteDao.getMorningBusesById(busRouteBoardingPoint2.getBusRoute().getId());
									if (busRoute != null) {
										searchResult = new SearchResults();
										searchResult.setBoardingPointName(busRouteBoardingPoint2.getBoardingPoint().getBoardingPointName());
										searchResult.setBusRoute(busRouteBoardingPoint2.getBusRoute());
										searchResult.setBoardingTime(busRouteBoardingPoint2.getHaltTime());
										searchedBusRoutes.add(searchResult);
										System.out.println("The bus id is: " + busRoute.getId() + " - " + busRoute.getRouteName() + ", having departure at: " + busRoute.getStartTime());
										break;
									}
								}
							}
						}
					}
					
				}
			}
			
			return searchedBusRoutes;
	}
	
}
