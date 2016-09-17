package easycommuteguide.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "boardingpoints")
public class BoardingPoint implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
	
	private String boardingPointName;
	
	private Double lattitude;
	
	private Double longitude;
	
	private Double cost;
	
	private int nbrRegisteredUsers;
	
	/*@OneToMany (mappedBy = "registeredBoardingPoint")
	private List<Employee> registeredEmployees = new ArrayList<>();*/
	
	@OneToMany
	private List<Employee> registeredEmployees = new ArrayList<>();
	
	/*@ManyToMany
	private List<BusRoute> busRoutes = new ArrayList<>();	*/
	
	@OneToMany (mappedBy = "boardingPoint")
	private List<BusRouteBoardingPoint> busRouteBoardingPoints = new ArrayList<>();

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getBoardingPointName() {
		return boardingPointName;
	}
	public void setBoardingPointName(String boardingPointName) {
		this.boardingPointName = boardingPointName;
	}

	public Double getLattitude() {
		return lattitude;
	}
	public void setLattitude(Double lattitude) {
		this.lattitude = lattitude;
	}

	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}

	public int getNbrRegisteredUsers() {
		return nbrRegisteredUsers;
	}
	public void setNbrRegisteredUsers(int nbrRegisteredUsers) {
		this.nbrRegisteredUsers = nbrRegisteredUsers;
	}

	public List<Employee> getRegisteredEmployees() {
		return registeredEmployees;
	}
	public void setRegisteredEmployees(List<Employee> registeredEmployees) {
		this.registeredEmployees = registeredEmployees;
	}
	
	public List<BusRouteBoardingPoint> getBusRouteBoardingPoints() {
		return busRouteBoardingPoints;
	}
	public void setBusRouteBoardingPoints(List<BusRouteBoardingPoint> busRouteBoardingPoints) {
		this.busRouteBoardingPoints = busRouteBoardingPoints;
	}
	
}
