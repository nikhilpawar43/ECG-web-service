package easycommuteguide.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import easycommuteguide.business.BoardingPointMarker;
import easycommuteguide.business.GlobalValues;
import easycommuteguide.business.GoogleMapsRoutePlotData;
import easycommuteguide.model.BoardingPoint;
import easycommuteguide.model.BusRoute;
import easycommuteguide.model.BusRouteBoardingPoint;
import easycommuteguide.model.dao.BusRouteBoardingPointDao;
import easycommuteguide.model.dao.BusRouteDao;
import easycommuteguide.parsers.LocationDistanceParser;
import easycommuteguide.util.HttpManager;
import easycommuteguide.util.RequestPackage;

@Controller
public class RouteDisplayController {

	@Autowired
	private BusRouteDao busRouteDao;
	
	@Autowired
	private BusRouteBoardingPointDao busRouteBoardingPointDao;
	
	private BusRoute busRoute;
	private List<BusRouteBoardingPoint> busBoardingPoints;
	private BoardingPoint boardingPoint, previousBoardingPoint, startBoardingPoint;
	private List<BoardingPointMarker> boardingPointsList;
	
	private String ctsFacility;
	private int busid;
	private String boardingPointName;
	private double ctsFacilityLatitude, ctsFacilityLongitude;
	private String waypointsURLValues, destinationURLValues, waypointsURLValues2, origin2, waypointsURLValues3, origin3;
	private String uri;
	private StringBuilder distanceCalculationURL;
	private String content, content2, content3;
	
	private GoogleMapsRoutePlotData routePlotData;
	private BoardingPointMarker boardingPointMarker;
	
	@RequestMapping(value="/getGoogleMapRoute", method = RequestMethod.GET)
	protected @ResponseBody GoogleMapsRoutePlotData getGoogleMapRoute (HttpServletRequest request) {
		
		System.out.println("Requesting the webservice to get the display route on google maps.");
		
		content = "";
		content2 = "";
		content3 = "";
		
		/*ctsFacility = (String) session.getAttribute("ctsFacility"); */
		ctsFacility = GlobalValues.ctsFacility;
		
		if (ctsFacility != null && ctsFacility.length() > 0) {
			if ( ctsFacility.equals("DLF Quadron Phase-2")) {
				ctsFacilityLatitude = 18.585637;
				ctsFacilityLongitude = 73.696155;
			} else if ( ctsFacility.equals("CTS Hinjewadi Phase-1")) {
				ctsFacilityLatitude = 18.583008;
				ctsFacilityLongitude = 73.734361;
			}
		}
		
		boardingPointsList = new ArrayList<>();				
		
		/*ctsFacilityLatitude = 18.585637;
		ctsFacilityLongitude = 73.696155;*/
		
		busid = Integer.parseInt( (String) request.getParameter("busid") );
		boardingPointName = request.getParameter("boardingPointName");
		
//		nearestBoardingPoint = boardingPointDao.getBoardingPointByName(boardingPointName);
		
		busRoute = busRouteDao.getEveningBusesById(busid);	
		busBoardingPoints = busRouteBoardingPointDao.getBoardingPointsByBusid(busRoute);
		
		waypointsURLValues = "";
		destinationURLValues = "";
		waypointsURLValues2 = "";
		origin2 = "";
		
		previousBoardingPoint = null;
		
		for (BusRouteBoardingPoint busRouteBoardingPoint : busBoardingPoints) {
			
			boardingPoint = busRouteBoardingPoint.getBoardingPoint();
			
			distanceCalculationURL = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json");
			
			if (previousBoardingPoint == null) {
				previousBoardingPoint = new BoardingPoint();
				previousBoardingPoint.setLattitude(ctsFacilityLatitude);
				previousBoardingPoint.setLongitude(ctsFacilityLongitude);
			}
			
			RequestPackage requestPackage = new RequestPackage();
			requestPackage.setUri(distanceCalculationURL.toString());
			requestPackage.setParam("key", "AIzaSyCq6Epz6ti0M-sIlVWQfCZzn_P5r8AqJVE");
			requestPackage.setParam("origins",previousBoardingPoint.getLattitude() + "," + previousBoardingPoint.getLongitude());
			requestPackage.setParam("destinations", boardingPoint.getLattitude() + "," + boardingPoint.getLongitude());
			requestPackage.setParam("mode", "driving");
			requestPackage.setParam("language", "en");
			
			content = HttpManager.getData(requestPackage);
			String distanceAndTime = LocationDistanceParser.parseFeed(content);
			String[] data = distanceAndTime.split(" ");
			
			previousBoardingPoint = boardingPoint;
			
			boardingPointMarker = new BoardingPointMarker();
			boardingPointMarker.setBoardingPointName(boardingPoint.getBoardingPointName());
			boardingPointMarker.setLatitude(boardingPoint.getLattitude());
			boardingPointMarker.setLongitude(boardingPoint.getLongitude());
			boardingPointMarker.setDistance( Double.parseDouble(data[0]) );
			boardingPointMarker.setTime( Double.parseDouble(data[1]) );
			
			if (boardingPoint.getBoardingPointName().equals(boardingPointName)) {
				
				destinationURLValues = boardingPoint.getLattitude() + "," + boardingPoint.getLongitude();
				boardingPointsList.add(boardingPointMarker);
				break;
			} else {
				
				if (waypointsURLValues.equals(""))
					waypointsURLValues = waypointsURLValues + boardingPoint.getLattitude() + "," + boardingPoint.getLongitude();
				else
					waypointsURLValues = waypointsURLValues + "|" + boardingPoint.getLattitude() + "," + boardingPoint.getLongitude();
			}
			
			boardingPointsList.add(boardingPointMarker);
		}
		
		if (boardingPointsList.size() > 8 && boardingPointsList.size() <=16) {
			
			waypointsURLValues = "";
			waypointsURLValues = waypointsURLValues + boardingPointsList.get(0).getLatitude() + "," + boardingPointsList.get(0).getLongitude();
			
			for (int i=1; i<7; i++) {
				
				boardingPointMarker = boardingPointsList.get(i);
				waypointsURLValues = waypointsURLValues + "|" + boardingPointMarker.getLatitude() + "," + boardingPointMarker.getLongitude();
			}
			
			waypointsURLValues = waypointsURLValues + "|" + boardingPointsList.get(7).getLatitude() + "," + boardingPointsList.get(7).getLongitude();
		
			origin2 = boardingPointsList.get(7).getLatitude() + "," + boardingPointsList.get(7).getLongitude();
			
			for (int i=8; i<boardingPointsList.size()-1; i++) {
				
				boardingPointMarker = boardingPointsList.get(i);
				if (waypointsURLValues2.equals(""))
					waypointsURLValues2 = waypointsURLValues2 + boardingPointMarker.getLatitude() + "," + boardingPointMarker.getLongitude();
				else
					waypointsURLValues2 = waypointsURLValues2 + "|" + boardingPointMarker.getLatitude() + "," + boardingPointMarker.getLongitude();
			}
			
		}
		
		if (boardingPointsList.size() > 16) {
			
			waypointsURLValues = "";
			waypointsURLValues2 = "";
			waypointsURLValues3 = "";
					
			waypointsURLValues = waypointsURLValues + boardingPointsList.get(0).getLatitude() + "," + boardingPointsList.get(0).getLongitude();
			
			for (int i=1; i<7; i++) {
				
				boardingPointMarker = boardingPointsList.get(i);
				waypointsURLValues = waypointsURLValues + "|" + boardingPointMarker.getLatitude() + "," + boardingPointMarker.getLongitude();
			}
			
			waypointsURLValues = waypointsURLValues + "|" + boardingPointsList.get(7).getLatitude() + "," + boardingPointsList.get(7).getLongitude();
			origin2 = boardingPointsList.get(7).getLatitude() + "," + boardingPointsList.get(7).getLongitude();
			
			for (int i=8; i<15; i++) {
				
				boardingPointMarker = boardingPointsList.get(i);
				if (waypointsURLValues2.equals(""))
					waypointsURLValues2 = waypointsURLValues2 + boardingPointMarker.getLatitude() + "," + boardingPointMarker.getLongitude();
				else
					waypointsURLValues2 = waypointsURLValues2 + "|" + boardingPointMarker.getLatitude() + "," + boardingPointMarker.getLongitude();
			}
			
			origin3 = boardingPointsList.get(16).getLatitude() + "," + boardingPointsList.get(16).getLongitude();

			for (int i=17; i<boardingPointsList.size()-1; i++) {
				
				boardingPointMarker = boardingPointsList.get(i);
				if (waypointsURLValues3.equals(""))
					waypointsURLValues3 = waypointsURLValues3 + boardingPointMarker.getLatitude() + "," + boardingPointMarker.getLongitude();
				else
					waypointsURLValues3 = waypointsURLValues3 + "|" + boardingPointMarker.getLatitude() + "," + boardingPointMarker.getLongitude();
			}
			
		}
		
		uri = "https://maps.googleapis.com/maps/api/directions/json";
		
		RequestPackage requestPackage = new RequestPackage();
		requestPackage.setMethod("GET");
		requestPackage.setUri(uri);
		requestPackage.setParam("origin", ctsFacilityLatitude + "," + ctsFacilityLongitude);
		requestPackage.setParam("destination", destinationURLValues);
		requestPackage.setParam("waypoints", waypointsURLValues);
		requestPackage.setParam("mode", "driving");
		requestPackage.setParam("key", "AIzaSyCq6Epz6ti0M-sIlVWQfCZzn_P5r8AqJVE");
		
		content = HttpManager.getData(requestPackage);
		
		if (origin2 != null && origin2.length() > 0) {
			
			requestPackage = new RequestPackage();
			requestPackage.setMethod("GET");
			requestPackage.setUri(uri);
			requestPackage.setParam("origin", origin2);
			requestPackage.setParam("destination", destinationURLValues);
			if (waypointsURLValues2 != null && waypointsURLValues2.length()>0)
				requestPackage.setParam("waypoints", waypointsURLValues2);
			requestPackage.setParam("mode", "driving");
			requestPackage.setParam("key", "AIzaSyCq6Epz6ti0M-sIlVWQfCZzn_P5r8AqJVE");
			
			content2 = HttpManager.getData(requestPackage);
		}
		
		if (origin3 != null && origin3.length() > 0) {
			
			requestPackage = new RequestPackage();
			requestPackage.setMethod("GET");
			requestPackage.setUri(uri);
			requestPackage.setParam("origin", origin3);
			requestPackage.setParam("destination", destinationURLValues);
			if (waypointsURLValues3 != null && waypointsURLValues3.length()>0)
				requestPackage.setParam("waypoints", waypointsURLValues3);
			requestPackage.setParam("mode", "driving");
			requestPackage.setParam("key", "AIzaSyCq6Epz6ti0M-sIlVWQfCZzn_P5r8AqJVE");
			
			content3 = HttpManager.getData(requestPackage);
		}
		
		routePlotData = new GoogleMapsRoutePlotData();
		
		if (content.length() > 0) {
			
			/*System.out.println();
			System.out.println(content);*/
			
			routePlotData.setGoogleMapRouteData(content);
			routePlotData.setGoogleMapRouteData2(content2);
			routePlotData.setGoogleMapRouteData3(content3);
			routePlotData.setBoardingPointsMarkerList(boardingPointsList);
			
			return routePlotData;
		} else {
			return null;
		}
	}
	
	@RequestMapping(value="/getGoogleMapRouteToCTS", method = RequestMethod.GET)
	protected @ResponseBody GoogleMapsRoutePlotData getGoogleMapRouteToCTS (HttpServletRequest request) {
		
		System.out.println("Requesting the webservice to get the display route on google maps.");
		
		content = "";
		content2 = "";
		content3 = "";
		
		/*ctsFacilityLatitude = 18.585637;
		ctsFacilityLongitude = 73.696155;*/
		
		/*ctsFacility = (String) session.getAttribute("ctsFacility"); */
		ctsFacility = GlobalValues.ctsFacility;
		
		if (ctsFacility != null && ctsFacility.length() > 0) {
			if ( ctsFacility.equals("DLF Quadron Phase-2")) {
				ctsFacilityLatitude = 18.585637;
				ctsFacilityLongitude = 73.696155;
			} else if ( ctsFacility.equals("CTS Hinjewadi Phase-1")) {
				ctsFacilityLatitude = 18.583008;
				ctsFacilityLongitude = 73.734361;
			}
		}
		
		boardingPointsList = new ArrayList<>();				
		
		busid = Integer.parseInt( (String) request.getParameter("busid") );
		boardingPointName = request.getParameter("boardingPointName");
		
//	nearestBoardingPoint = boardingPointDao.getBoardingPointByName(boardingPointName);
		
		busRoute = busRouteDao.getMorningBusesById(busid);
		busBoardingPoints = busRouteBoardingPointDao.getBoardingPointsByBusid(busRoute);
		
		waypointsURLValues = "";
		destinationURLValues = "";
		waypointsURLValues2 = "";
		origin2 = "";
		
		previousBoardingPoint = null;
		int traverseFlag = 0;
		
		for (BusRouteBoardingPoint busRouteBoardingPoint : busBoardingPoints) {

			boardingPoint = busRouteBoardingPoint.getBoardingPoint();
			
			if (boardingPoint.getBoardingPointName().equals(boardingPointName) && traverseFlag == 0 ) {
				traverseFlag = 1;
				startBoardingPoint = boardingPoint;
				/*destinationURLValues = boardingPoint.getLattitude() + "," + boardingPoint.getLongitude();*/
			}
			
			if ( traverseFlag == 1) {
				
				distanceCalculationURL = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json");
				
				if (previousBoardingPoint == null) {
					previousBoardingPoint = new BoardingPoint();
					/*previousBoardingPoint.setLattitude(ctsFacilityLatitude);
					previousBoardingPoint.setLongitude(ctsFacilityLongitude);*/
					
					previousBoardingPoint.setLattitude(boardingPoint.getLattitude());
					previousBoardingPoint.setLongitude(boardingPoint.getLongitude());
					
					boardingPointMarker = new BoardingPointMarker();
					boardingPointMarker.setBoardingPointName(boardingPoint.getBoardingPointName());
					boardingPointMarker.setLatitude(boardingPoint.getLattitude());
					boardingPointMarker.setLongitude(boardingPoint.getLongitude());
					boardingPointMarker.setDistance( 0 );
					boardingPointMarker.setTime( 0 );

					boardingPointsList.add(boardingPointMarker);
					
				} else {
					
					RequestPackage requestPackage = new RequestPackage();
					requestPackage.setUri(distanceCalculationURL.toString());
					requestPackage.setParam("key", "AIzaSyCq6Epz6ti0M-sIlVWQfCZzn_P5r8AqJVE");
					requestPackage.setParam("origins",previousBoardingPoint.getLattitude() + "," + previousBoardingPoint.getLongitude());
					requestPackage.setParam("destinations", boardingPoint.getLattitude() + "," + boardingPoint.getLongitude());
					requestPackage.setParam("mode", "driving");
					requestPackage.setParam("language", "en");
					
					content = HttpManager.getData(requestPackage);
					String distanceAndTime = LocationDistanceParser.parseFeed(content);
					String[] data = distanceAndTime.split(" ");
					
					previousBoardingPoint = boardingPoint;
					
					boardingPointMarker = new BoardingPointMarker();
					boardingPointMarker.setBoardingPointName(boardingPoint.getBoardingPointName());
					boardingPointMarker.setLatitude(boardingPoint.getLattitude());
					boardingPointMarker.setLongitude(boardingPoint.getLongitude());
					boardingPointMarker.setDistance( Double.parseDouble(data[0]) );
					boardingPointMarker.setTime( Double.parseDouble(data[1]) );

					if (boardingPoint.getBoardingPointName().equals(boardingPointName)) {
						
						destinationURLValues = boardingPoint.getLattitude() + "," + boardingPoint.getLongitude();
						boardingPointsList.add(boardingPointMarker);
						break;
					} else {
						
						if (waypointsURLValues.equals(""))
							waypointsURLValues = waypointsURLValues + boardingPoint.getLattitude() + "," + boardingPoint.getLongitude();
						else
							waypointsURLValues = waypointsURLValues + "|" + boardingPoint.getLattitude() + "," + boardingPoint.getLongitude();
					}
					
					boardingPointsList.add(boardingPointMarker);
				}
			}
		}
		
		if (boardingPointsList.size() > 8 && boardingPointsList.size() <=16) {
			
			waypointsURLValues = "";
			waypointsURLValues = waypointsURLValues + boardingPointsList.get(0).getLatitude() + "," + boardingPointsList.get(0).getLongitude();
			
			for (int i=1; i<7; i++) {
				
				boardingPointMarker = boardingPointsList.get(i);
				waypointsURLValues = waypointsURLValues + "|" + boardingPointMarker.getLatitude() + "," + boardingPointMarker.getLongitude();
			}
			
			waypointsURLValues = waypointsURLValues + "|" + boardingPointsList.get(7).getLatitude() + "," + boardingPointsList.get(7).getLongitude();
		
			origin2 = boardingPointsList.get(7).getLatitude() + "," + boardingPointsList.get(7).getLongitude();
			
			for (int i=8; i<boardingPointsList.size()-1; i++) {
				
				boardingPointMarker = boardingPointsList.get(i);
				if (waypointsURLValues2.equals(""))
					waypointsURLValues2 = waypointsURLValues2 + boardingPointMarker.getLatitude() + "," + boardingPointMarker.getLongitude();
				else
					waypointsURLValues2 = waypointsURLValues2 + "|" + boardingPointMarker.getLatitude() + "," + boardingPointMarker.getLongitude();
			}
			
		}
		
		if (boardingPointsList.size() > 16) {
			
			waypointsURLValues = "";
			waypointsURLValues2 = "";
			waypointsURLValues3 = "";
					
			waypointsURLValues = waypointsURLValues + boardingPointsList.get(0).getLatitude() + "," + boardingPointsList.get(0).getLongitude();
			
			for (int i=1; i<7; i++) {
				
				boardingPointMarker = boardingPointsList.get(i);
				waypointsURLValues = waypointsURLValues + "|" + boardingPointMarker.getLatitude() + "," + boardingPointMarker.getLongitude();
			}
			
			waypointsURLValues = waypointsURLValues + "|" + boardingPointsList.get(7).getLatitude() + "," + boardingPointsList.get(7).getLongitude();
			origin2 = boardingPointsList.get(7).getLatitude() + "," + boardingPointsList.get(7).getLongitude();
			
			for (int i=8; i<15; i++) {
				
				boardingPointMarker = boardingPointsList.get(i);
				if (waypointsURLValues2.equals(""))
					waypointsURLValues2 = waypointsURLValues2 + boardingPointMarker.getLatitude() + "," + boardingPointMarker.getLongitude();
				else
					waypointsURLValues2 = waypointsURLValues2 + "|" + boardingPointMarker.getLatitude() + "," + boardingPointMarker.getLongitude();
			}
			
			origin3 = boardingPointsList.get(16).getLatitude() + "," + boardingPointsList.get(16).getLongitude();

			for (int i=17; i<boardingPointsList.size()-1; i++) {
				
				boardingPointMarker = boardingPointsList.get(i);
				if (waypointsURLValues3.equals(""))
					waypointsURLValues3 = waypointsURLValues3 + boardingPointMarker.getLatitude() + "," + boardingPointMarker.getLongitude();
				else
					waypointsURLValues3 = waypointsURLValues3 + "|" + boardingPointMarker.getLatitude() + "," + boardingPointMarker.getLongitude();
			}
			
		}
		
		uri = "https://maps.googleapis.com/maps/api/directions/json";
		
		RequestPackage requestPackage = new RequestPackage();
		requestPackage.setMethod("GET");
		requestPackage.setUri(uri);
		requestPackage.setParam("origin", startBoardingPoint.getLattitude() + "," + startBoardingPoint.getLongitude());
		requestPackage.setParam("destination", ctsFacilityLatitude + "," + ctsFacilityLongitude);
		requestPackage.setParam("waypoints", waypointsURLValues);
		requestPackage.setParam("mode", "driving");
		requestPackage.setParam("key", "AIzaSyCq6Epz6ti0M-sIlVWQfCZzn_P5r8AqJVE");
		
		content = HttpManager.getData(requestPackage);
		
		if (origin2 != null && origin2.length() > 0) {
			
			requestPackage = new RequestPackage();
			requestPackage.setMethod("GET");
			requestPackage.setUri(uri);
			requestPackage.setParam("origin", origin2);
			requestPackage.setParam("destination", ctsFacilityLatitude + "," + ctsFacilityLongitude);
			if (waypointsURLValues2 != null && waypointsURLValues2.length()>0)
				requestPackage.setParam("waypoints", waypointsURLValues2);
			requestPackage.setParam("mode", "driving");
			requestPackage.setParam("key", "AIzaSyCq6Epz6ti0M-sIlVWQfCZzn_P5r8AqJVE");
			
			content2 = HttpManager.getData(requestPackage);
		}
		
		if (origin3 != null && origin3.length() > 0) {
			
			requestPackage = new RequestPackage();
			requestPackage.setMethod("GET");
			requestPackage.setUri(uri);
			requestPackage.setParam("origin", origin3);
			requestPackage.setParam("destination", ctsFacilityLatitude + "," + ctsFacilityLongitude);
			if (waypointsURLValues3 != null && waypointsURLValues3.length()>0)
				requestPackage.setParam("waypoints", waypointsURLValues3);
			requestPackage.setParam("mode", "driving");
			requestPackage.setParam("key", "AIzaSyCq6Epz6ti0M-sIlVWQfCZzn_P5r8AqJVE");
			
			content3 = HttpManager.getData(requestPackage);
		}
		
		routePlotData = new GoogleMapsRoutePlotData();
		
		if (content.length() > 0) {
			
			routePlotData.setGoogleMapRouteData(content);;
			routePlotData.setGoogleMapRouteData2(content2);
			routePlotData.setGoogleMapRouteData3(content3);
			routePlotData.setBoardingPointsMarkerList(boardingPointsList);
			
			return routePlotData;
		} else {
			return null;
		}
	}

}
