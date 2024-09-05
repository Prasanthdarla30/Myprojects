package com.PQRtravels;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Userdetails {
@Id
private String email;
private static String Firstname;
private static String Lastname;
private int mobileNumber;
private static String gender;
private static String password;
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public static String getFirstname() {
	return Firstname;
}
public void setFirstname(String firstname) {
	Firstname = firstname;
}
public static String getLastname() {
	return Lastname;
}
public void setLastname(String lastname) {
	Lastname = lastname;
}
public int getmobileNumber() {
	return mobileNumber;
}
public void setmobileNumber(int mobileNumber) {
	this.mobileNumber = mobileNumber;
}
public static String getGender() {
	return gender;
}
public void setGender(String gender) {
	this.gender = gender;
}
public static String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}



}
