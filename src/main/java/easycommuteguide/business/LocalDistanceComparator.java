package easycommuteguide.business;

import java.util.ArrayList;
import java.util.List;

import easycommuteguide.model.BoardingPoint;

public class LocalDistanceComparator {
	
	public static List<LocationDistance> getTopLocalNearestBoardingPoints (List<BoardingPoint> boardingPointsList, double[] latlng) {
		
		List<LocationDistance> locationDistanceList = new ArrayList<>();
		LocationDistance locationDistance;
	    double distance, greatestDistance;
		
		for (BoardingPoint boardingPoint : boardingPointsList) {

			distance = StraightLineDistanceCalculator.distance(latlng[0], latlng[1], boardingPoint.getLattitude(), boardingPoint.getLongitude(), "k");
			
			if (locationDistanceList.size() < 5 ) {
				
				locationDistance = new LocationDistance();
				locationDistance.setBoardingPoint(boardingPoint);
				locationDistance.setDistance(distance);
				
				locationDistanceList.add(locationDistance);
								
			} else {
				
				greatestDistance = findGreatestDistance (locationDistanceList); 
				if (distance < greatestDistance) {
					
					locationDistance = new LocationDistance();
					locationDistance.setBoardingPoint(boardingPoint);
					locationDistance.setDistance(distance);
					
					replaceGreatestDistance(locationDistanceList, locationDistance, greatestDistance);
				}
			}
		}
		
		return locationDistanceList;
		
	}
	
	public static double findGreatestDistance (List<LocationDistance> locationDistanceList) {
		
		double distance = locationDistanceList.get(0).getDistance();
		for (LocationDistance locationDistance : locationDistanceList) {
			if (locationDistance.getDistance() > distance) {
				distance = locationDistance.getDistance();
			}
		}
		
		return distance;
	}
	
	public static void replaceGreatestDistance (List<LocationDistance> locationDistanceList, LocationDistance newLocationDistance, double greatestDistance) {
		
		LocationDistance originalLocationDistance;
		int i=0;
		
		for (i=0; i<locationDistanceList.size(); i++) {
			
			originalLocationDistance = locationDistanceList.get(i);
			if (originalLocationDistance.getDistance() == greatestDistance) {
				break;
			}
		}
		
		locationDistanceList.set(i, newLocationDistance);
	}

}
