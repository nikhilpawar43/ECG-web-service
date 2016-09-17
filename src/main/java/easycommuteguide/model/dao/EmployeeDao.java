package easycommuteguide.model.dao;

import easycommuteguide.model.Employee;

public interface EmployeeDao {
	
	Employee login (int userid, String password);
	Employee getEmployeeById (int userid);

}
