package easycommuteguide.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
	
	private String password;
	
	private String employeeName;
	
/*	@ManyToOne
	@JoinColumn(name = "BoardingPoint")
	private BoardingPoint registeredBoardingPoint;*/
	
	private Date registeredDate;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

/*	public BoardingPoint getRegisteredBoardingPoint() {
		return registeredBoardingPoint;
	}
	public void setRegisteredBoardingPoint(BoardingPoint registeredBoardingPoint) {
		this.registeredBoardingPoint = registeredBoardingPoint;
	}*/

	public Date getRegisteredDate() {
		return registeredDate;
	}
	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}
	
}
