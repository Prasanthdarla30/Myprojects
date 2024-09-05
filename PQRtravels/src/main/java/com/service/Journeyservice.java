package com.service;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.PQRtravels.Journeyplan;
import com.PQRtravels.Order;
import com.PQRtravels.Userdetails;

public class Journeyservice {
	private static SessionFactory sf;
	public Journeyservice(SessionFactory sf) {
        Journeyservice.sf = sf;
    }
	
	

	public void journeydetails() {
		Scanner scanner=new Scanner(System.in);
		System.out.println("Enter your email:");
	    String email = scanner.nextLine();

	    System.out.println("Enter your password:");
	    String password = scanner.nextLine();
	    Userservice userService1 = new Userservice(sf);
	    Userdetails user = Userservice.authenticateUser(email, password);
	    if (user != null) {
	        System.out.println("Login successful. Welcome " + Userdetails.getFirstname() + "!");
	    
	    System.out.println("Enter source location:");
        String source = scanner.nextLine();

        System.out.println("Enter destination location:");
        String destination = scanner.nextLine();

        System.out.println("Enter boarding date (YYYY-MM-DD):");
        String boardingDate = scanner.nextLine();

        System.out.println("Enter the number of passengers:");
        int noOfPassengers = scanner.nextInt();
        scanner.nextLine();
        
        Journeyservice.planJourney(source, destination, boardingDate, noOfPassengers,user);
	    }
	}

	private static void planJourney(String source, String destination, String boardingDate, int noOfPassengers,Userdetails user) {
		Session session = sf.openSession();
		String hql = "FROM Journey J WHERE J.source = :source AND J.destination = :destination AND J.boardingDate = :boardingDate";
        Query<Journeyplan> query = session.createQuery(hql, Journeyplan.class);
        query.setParameter("source", source);
        query.setParameter("destination", destination);
        query.setParameter("boardingDate", boardingDate);
        List<Journeyplan> journeys = query.list();
        if (journeys.isEmpty()) {
            System.out.println("No available routes for the selected criteria.");
        } else {
            System.out.println("Available routes:");
            for (Journeyplan journey : journeys) {
                System.out.println("ID: " + journey.getId() + ", Source: " + journey.getSource() +
                        ", Destination: " + journey.getDestination() + ", Boarding Date: " +
                        journey.getBoardingdate() + ", Seats Available: " + Journeyplan.getSeatsavailable() +
                        ", Price per Seat: " + Journeyplan.getPriceperpassenger());
            }

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the route ID you want to select:");
            Long selectedRouteId = scanner.nextLong();

            // Find the selected route
            Journeyplan selectedJourney = session.get(Journeyplan.class, selectedRouteId);
            if (selectedJourney != null && Journeyplan.getSeatsavailable() >= noOfPassengers) {
                System.out.println("Proceeding to book your journey...");
               order(selectedJourney, noOfPassengers,user);
            } else {
                System.out.println("The selected route does not have enough seats available.");
            }
        }

        session.close();

		
		
	}

	private static  void order(Journeyplan selectedJourney, int noOfPassengers,Userdetails user) {
		// TODO Auto-generated method stub
		Session session = sf.openSession();
        Transaction transaction = session.beginTransaction();
        Long OrderId=(long) (selectedJourney.getId()-10);

        // Calculate the total price
        double totalPrice = selectedJourney.getPriceperpassenger() * noOfPassengers;

        // Update the number of available seats
        Journeyplan.setSeatsavailable(selectedJourney.getSeatsavailable() - noOfPassengers);
        session.update(selectedJourney);
        Order order = new Order();
         order.setOrderId(OrderId);
        order.setSource(selectedJourney.getSource());
        order.setDestination(selectedJourney.getDestination());
        order.setFirstname(user.getFirstname());
        order.setLastname(user.getLastname());
        order.setGender(user.getGender());
        order.setTotalprice(totalPrice);
        order.setEmail(user.getEmail());
        order.setPassword(user.getPassword());
        order.setUser(user);


        transaction.commit();
        session.close();
        System.out.println("Journey booked successfully! Total price: $" + totalPrice);
        System.out.println("Your order id is"+OrderId);
		
	}
	
	public void deleteOrder() {
	    Scanner scanner = new Scanner(System.in);

	    System.out.println("Enter your email:");
	    String email = scanner.nextLine();

	    System.out.println("Enter your password:");
	    String password = scanner.nextLine();
	    Userservice userService = new Userservice(sf);
	    Userdetails user = userService.authenticateUser(email, password);

	    if (user != null) {
	        List<Order> orders = getOrdersByUser(user);

	        if (orders.isEmpty()) {
	            System.out.println("No orders found for this user.");
	            return;
	        }
	        
	

	        // Display orders with Order ID
	        System.out.println("Your Orders:");
	        for (Order order : orders) {
	            System.out.println("Order ID: " + order.getOrderId() +
	                               ", Source: " + order.getSource() +
	                               ", Destination: " + order.getDestination() +
	                               ", Passenger: " + order.getFirstname() + " " + order.getLastname());
	        }

	        // Step 4: Ask for the Order ID to delete
	        System.out.println("Enter the Order ID you want to delete:");
	        Long orderIdToDelete = scanner.nextLong();

	        // Step 5: Delete the order
	        boolean isDeleted = deleteOrderById(orderIdToDelete);

	        if (isDeleted) {
	            System.out.println("Order deleted successfully.");
	        } else {
	            System.out.println("Order deletion failed. Please check the Order ID.");
	        }
	    } else {
	        System.out.println("Invalid email or password.");
	    }
	}
	
	
	public List<Order> getOrdersByUser(Userdetails user) {
	    Session session = sf.openSession();
	    String hql = "FROM Order O WHERE O.email = :email";
	    Query<Order> query = session.createQuery(hql, Order.class);
	    query.setParameter("email", user.getEmail());
	    List<Order> orders = query.list();
	    session.close();
	    return orders;
	}
	
	public boolean deleteOrderById(Long orderId) {
	    Session session = sf.openSession();
	    Transaction transaction = session.beginTransaction();
	    
	    //Fetch the order by ID
	    Order order = session.get(Order.class, orderId);
	    
	    //  Check if the order exists
	    if (order != null) {
	        // Delete the order
	        session.delete(order);
	        transaction.commit();
	        session.close();
	        return true; // Successfully deleted
	    } else {
	        transaction.rollback();
	        session.close();
	        return false; // Order not found
	    }
	}



}