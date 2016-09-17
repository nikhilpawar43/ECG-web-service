package easycommuteguide.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import easycommuteguide.business.BusRegistrationDetails;
import easycommuteguide.model.BoardingPoint;
import easycommuteguide.model.BusRoute;
import easycommuteguide.model.BusRouteBoardingPoint;
import easycommuteguide.model.Employee;
import easycommuteguide.model.dao.BoardingPointDao;
import easycommuteguide.model.dao.BusRouteBoardingPointDao;
import easycommuteguide.model.dao.EmployeeDao;

@Controller
public class UserController {

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private BoardingPointDao boardingPointDao;
    @Autowired
    private BusRouteBoardingPointDao busRouteBoardingPointDao;

    private List<Employee> employees = null;
    private Employee user;
    private BoardingPoint boardingPoint;
    private List<BusRouteBoardingPoint> busRouteBoardingPointList;
    private BusRegistrationDetails busRegistrationDetails;
    private BusRoute busRoute;
    
	@RequestMapping(value="/login", method = RequestMethod.GET)
	protected @ResponseBody List<Employee> showHomePage(HttpServletRequest request) {
		System.out.println("Perform android user login.");
		
		employees = new ArrayList<>();
		String userid = request.getParameter("userid");
		int nbr_userid = Integer.parseInt(userid);
		String password = request.getParameter("password");
		System.out.println("The userid is: " + nbr_userid + " and password is: " + password);
		
		user = employeeDao.login(nbr_userid, password);
		
		if (user != null) {
			System.out.println("The user: " + user.getEmployeeName() + " logged in.");
			boardingPoint = boardingPointDao.getBoardingPointByEmpId(user.getId());
			System.out.println("The user: " + user.getId() + " has boarding point: " + boardingPoint.getBoardingPointName());
			
			busRouteBoardingPointList = busRouteBoardingPointDao.getBusRouteByBoardpointid(boardingPoint, "DLF Quadron Phase-2");
			System.out.println();
			for (BusRouteBoardingPoint busRouteBoardingPoint : busRouteBoardingPointList) {
				System.out.println("The bus id is: " + busRouteBoardingPoint.getBusRoute().getId() + " having bus route name as: " + busRouteBoardingPoint.getBusRoute().getRouteName());
			}
			
			employees.add(user);
			return employees;
		} else {
			System.out.println("Login attempt with invalid credentials");
			System.out.println("The userid is: " + nbr_userid + " and password is: " + password);
			return null;
		}
	}
	
	@RequestMapping(value="/viewRegisteredDetails", method = RequestMethod.GET)
	protected @ResponseBody BusRegistrationDetails getRegisteredDetails (HttpServletRequest request) {
	
		System.out.println("Perform search for registered details.");
		
		String userid = request.getParameter("userid");
		int nbr_userid = Integer.parseInt(userid);
		System.out.println("The userid is: " + nbr_userid);
		
		user = employeeDao.getEmployeeById(nbr_userid);
		if (user != null) {
			
			boardingPoint = boardingPointDao.getBoardingPointByEmpId(user.getId());
			busRouteBoardingPointList = busRouteBoardingPointDao.getBusRouteByBoardpointid(boardingPoint, "DLF Quadron Phase-2");
			
			busRegistrationDetails = new BusRegistrationDetails();
			busRegistrationDetails.setBoardingPointName(boardingPoint.getBoardingPointName());
			
			for (BusRouteBoardingPoint busRouteBoardingPoint : busRouteBoardingPointList) {
				System.out.println("The bus id is: " + busRouteBoardingPoint.getBusRoute().getId() + " having bus route name as: " + busRouteBoardingPoint.getBusRoute().getRouteName());
				
				busRoute = busRouteBoardingPoint.getBusRoute();
				if (busRoute.getEndTime().equals("09:00")) {
					
					busRegistrationDetails.setMorningBusRouteno(busRoute.getRouteNumber());
					busRegistrationDetails.setMorningBusRoutename(busRoute.getRouteName());
					
				} else if (busRoute.getStartTime().equals("18:15")) {
					
					busRegistrationDetails.setEveningBusRouteno(busRoute.getRouteNumber());
					busRegistrationDetails.setEveningBusRoutename(busRoute.getRouteName());
					
				}
			}
			
			return busRegistrationDetails;
			
		} else {
			System.out.println("The userid: " + userid + " is not present in database !");
			return null;
		}
	}
}	
