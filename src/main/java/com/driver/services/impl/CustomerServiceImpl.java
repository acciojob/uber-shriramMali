package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.CabRepository;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Autowired
	CabRepository cabRepository;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
    Customer customer=customerRepository2.findById(customerId).get();
     customerRepository2.delete(customer);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query.

		List<Driver> drivers=driverRepository2.findAll();


		int lowestIdAvilable=Integer.MAX_VALUE;
		for(Driver driver:drivers){

			if(driver.getDriverId()<lowestIdAvilable && driver.getCab().getAvilable())
				lowestIdAvilable=driver.getDriverId();
		}
		if(drivers.size()==0 || lowestIdAvilable==Integer.MAX_VALUE){
			throw new Exception("NO cab Avilable");
		}
			TripBooking tripBooking=new TripBooking(fromLocation,toLocation,distanceInKm,TripStatus.CONFIRMED);

		int driverId=lowestIdAvilable;
		Driver driver=driverRepository2.findById(driverId).get();
		Customer customer=customerRepository2.findById(customerId).get();

		driver.getCab().setAvilable(false);

		tripBooking.setCustomer(customer);
		tripBooking.setDriver(driver);

	    driver.getTripBookings().add(tripBooking);
		customer.getTripBookings().add(tripBooking);

		customerRepository2.save(customer);
		driverRepository2.save(driver);
		tripBookingRepository2.save(tripBooking);

	return tripBooking;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking=tripBookingRepository2.findById(tripId).get();

		Customer customer=tripBooking.getCustomer();
		Driver driver=tripBooking.getDriver();

		customer.getTripBookings().remove(tripBooking);
		driver.getTripBookings().remove(tripBooking);
		driver.getCab().setAvilable(true);

		tripBooking.setCustomer(null);
		tripBooking.setDriver(null);
		tripBooking.setBill(0);
		tripBooking.setStatus(TripStatus.CANCELED);

		tripBookingRepository2.delete(tripBooking);
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking=tripBookingRepository2.findById(tripId).get();
		Customer customer=tripBooking.getCustomer();
		Driver driver=tripBooking.getDriver();
		driver.getCab().setAvilable(true);
		tripBooking.setStatus(TripStatus.COMPLETED);

		driverRepository2.save(driver);
		customerRepository2.save(customer);
		tripBookingRepository2.save(tripBooking);


	}
}
