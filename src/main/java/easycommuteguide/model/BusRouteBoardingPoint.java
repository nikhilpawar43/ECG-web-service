package easycommuteguide.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "BusRoute_BoardingPoint")
public class BusRouteBoardingPoint {
	
	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne (cascade = CascadeType.ALL)
	@JoinColumn (name = "Bus_ID")
	private BusRoute busRoute;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn (name = "BoardingPoint_ID")
	private BoardingPoint boardingPoint;
	
	private String haltTime;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public BusRoute getBusRoute() {
		return busRoute;
	}
	public void setBusRoute(BusRoute busRoute) {
		this.busRoute = busRoute;
	}

	public BoardingPoint getBoardingPoint() {
		return boardingPoint;
	}
	public void setBoardingPoint(BoardingPoint boardingPoint) {
		this.boardingPoint = boardingPoint;
	}

	public String getHaltTime() {
		return haltTime;
	}
	public void setHaltTime(String haltTime) {
		this.haltTime = haltTime;
	}
	
}
