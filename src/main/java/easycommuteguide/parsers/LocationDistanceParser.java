package easycommuteguide.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import easycommuteguide.business.LocationDistance;

public class LocationDistanceParser {

	private static JSONArray jsonArray1, jsonArray2;
	private static JSONObject jsonObject1, jsonObject2, jsonObject3, jsonObject4, jsonObject5;
	private static String distance, duration;
	private static List<LocationDistance> boardingPointsList;
	private static int time;
	private static String[] nbrDistance;
	
	public static List<LocationDistance> parseFeed (String content, List<LocationDistance> localBoardingPointsList) {
		
		LocationDistance locationDistance;
		
		if (content != null && content.length() > 0) {
			
			boardingPointsList = new ArrayList<>();
			
			try {
				
				jsonObject1 = new JSONObject(content);
				
				jsonArray1 = jsonObject1.getJSONArray("rows");
//			System.out.println("The Json rows are: " + jsonArray1);
				
				jsonObject2 = jsonArray1.getJSONObject(0);
//			System.out.println("The Json object elements  are: " + jsonObject2);
				
				jsonArray2 = jsonObject2.getJSONArray("elements");
//			System.out.println("The Json array elements are: " + jsonArray2);
				
//			System.out.println("The JSON Array length is: " + jsonArray2.length());
				System.out.println();
				for (int i=0; i<jsonArray2.length(); i++) {
				
					jsonObject3 = jsonArray2.getJSONObject(i);
					jsonObject4 = jsonObject3.getJSONObject("distance");
					jsonObject5 = jsonObject3.getJSONObject("duration");
					
					distance = jsonObject4.getString("text");
					duration = jsonObject5.getString("text");
					
//				System.out.println("The distance is: " + distance + " and the duration is: " + duration);
					
					nbrDistance = distance.split(" ");
					
					locationDistance = new LocationDistance();
					locationDistance.setBoardingPoint (localBoardingPointsList.get(i).getBoardingPoint());
					locationDistance.setDistance(Double.parseDouble(nbrDistance[0]));
					locationDistance.setTime(duration);
					
					boardingPointsList.add(locationDistance);
				}
				
				return boardingPointsList;
				
			} catch (JSONException e) {
				System.out.println("The JSONException is: " + e.getMessage());
			}

		} else {
			System.out.println("The content feed is empty !");
		}
		return null;
		
	}
	
	public static String parseFeed (String content) {
		
		if (content != null && content.length() > 0) {
			
			try {
				
				jsonObject1 = new JSONObject(content);
				
				jsonArray1 = jsonObject1.getJSONArray("rows");
//			System.out.println("The Json rows are: " + jsonArray1);
				
				jsonObject2 = jsonArray1.getJSONObject(0);
//			System.out.println("The Json object elements  are: " + jsonObject2);
				
				jsonArray2 = jsonObject2.getJSONArray("elements");
//			System.out.println("The Json array elements are: " + jsonArray2);
				
//			System.out.println("The JSON Array length is: " + jsonArray2.length());
				for (int i=0; i<jsonArray2.length(); i++) {
				
					jsonObject3 = jsonArray2.getJSONObject(i);
					jsonObject4 = jsonObject3.getJSONObject("distance");
					jsonObject5 = jsonObject3.getJSONObject("duration");
					
					distance = jsonObject4.getString("text");
					duration = jsonObject5.getString("text");
				
					nbrDistance = distance.split(" ");
					
					time = 0;
					
					if (duration.contains("hour")) {
						
						String[] txtduration = duration.split("hour");
						
						String[] nbrHours = txtduration[0].split(" ");
						String[] nbrMinutes = txtduration[1].split(" ");
						
						int hours = Integer.parseInt( nbrHours[0] );
						int minutes = Integer.parseInt( nbrMinutes[0] );
						
						time = hours * 60  + minutes;
						
					} else {
						
						String[] nbrMinutes = duration.split(" ");
						int minutes = Integer.parseInt( nbrMinutes[0] );
						
						time = minutes;
					}
				}
				
				return nbrDistance[0] + " " + time;
					
			} catch (JSONException e) {
				System.out.println("The JSONException is: " + e.getMessage());
			}
		} else {
			System.out.println("The content feed is empty !");
		}
		return null;
	}
	
}
