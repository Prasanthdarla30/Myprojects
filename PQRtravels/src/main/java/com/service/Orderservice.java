package com.service;
import java.util.List;
import java.util.Scanner;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.PQRtravels.Order;


public class Orderservice {

	private SessionFactory sf;

	public Orderservice(SessionFactory sf) {
		this.sf=sf;
	}
	Scanner scanner=new Scanner(System.in);
	public void displayUserOrdersByEmailPassword(String email, String password) {
	    Session session = sf.openSession();
	    String hql = "FROM Order O WHERE O.email = :email AND O.password = :password";
	    Query query = session.createQuery(hql, Order.class);
	    query.setParameter("email", email);
	    query.setParameter("password", password);
	    @SuppressWarnings("unchecked")
		List<Order> orders = ((org.hibernate.query.Query<Order>) query).list();
	    session.close();

	    if (orders.isEmpty()) {
	        System.out.println("No orders found for the provided email and password.");
	    } else {
	        System.out.println("Your Orders:");
	        for (Order order : orders) {
	            System.out.println("Order ID: " + order.getOrderId() +
	                    ", Source: " + order.getSource() +
	                    ", Destination: " + order.getDestination() +
	                    ", Passenger: " + order.getFirstname() + " " + order.getLastname());
	        }
	    }
	}


}
