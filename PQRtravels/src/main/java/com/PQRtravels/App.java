package com.PQRtravels;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.Scanner;

import com.service.Journeyservice;
import com.service.Orderservice;
import com.service.Userservice;
public class App 
{
    public static void main( String[] args )
    
    {
    	Configuration cfg= new Configuration();
    	cfg.configure("hibernate.cfg.xml");
        cfg.addAnnotatedClass(Userdetails.class).addAnnotatedClass(Journeyplan.class);
        SessionFactory sf= cfg.buildSessionFactory();
        
    	Scanner scanner= new Scanner(System.in);
        int choice;
        boolean running = true;

        while (running) {
            System.out.println("\nMenu Options:");
            System.out.println("1. New Admin User Registration");
            System.out.println("2. Plan journey");
            System.out.println("3. Show all your orders");
            System.out.println("4. Cancel ticket/order");

            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
            case 1:
            	Userservice userService = new Userservice(sf);
                userService.registerNewAdmin();
                break;
            case 2:
            	Journeyservice js=new Journeyservice(sf);
            	js.journeydetails();
            	break;
            	
            case 3:
            	System.out.println("Enter your email:");
                String email = scanner.nextLine();
                System.out.println("Enter your password:");
                String password = scanner.nextLine();
                scanner.close();
            	Orderservice orderService = new Orderservice(sf);
            	orderService.displayUserOrdersByEmailPassword(email, password);
            	break;
            case 4:
            	Journeyservice js2= new Journeyservice(sf);
            	js2.deleteOrder();
            	break;

            	
            	
            }
            
    	
       
    }
}
}
