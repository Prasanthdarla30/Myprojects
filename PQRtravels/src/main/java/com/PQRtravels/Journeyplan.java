package com.PQRtravels;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Journeyplan {
@Id
int id;
private String boardingdate;
private String destination;
private String source;
private int passengers;
private static int seatsavailable;
private static double priceperpassenger;

public static int getSeatsavailable() {
	return seatsavailable;
}
public static  void setSeatsavailable(int seatsavailable) {
	Journeyplan.seatsavailable = seatsavailable;
}
public static double getPriceperpassenger() {
	return priceperpassenger;
}
public void setPriceperpassenger(double priceperpassenger) {
	Journeyplan.priceperpassenger = priceperpassenger;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getBoardingdate() {
	return boardingdate;
}
public void setBoardingdate(String boardingdate) {
	this.boardingdate = boardingdate;
}
public String getDestination() {
	return destination;
}
public void setDestination(String destination) {
	this.destination = destination;
}
public String getSource() {
	return source;
}
public void setSource(String source) {
	this.source = source;
}
public int getPassengers() {
	return passengers;
}
public void setPassengers(int passengers) {
	this.passengers = passengers;
}



}
