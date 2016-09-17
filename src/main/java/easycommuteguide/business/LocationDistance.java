package easycommuteguide.business;

import easycommuteguide.model.BoardingPoint;

public class LocationDistance {
	
	private BoardingPoint boardingPoint;
	private Double distance;
	private String time;

	public BoardingPoint getBoardingPoint() {
		return boardingPoint;
	}
	public void setBoardingPoint(BoardingPoint boardingPoint) {
		this.boardingPoint = boardingPoint;
	}
	
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
