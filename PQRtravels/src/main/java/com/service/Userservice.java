package com.service;
import com.PQRtravels.Userdetails;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class Userservice {
	private static SessionFactory sf;
	public Userservice(SessionFactory sf) {
        Userservice.sf = sf;
    }
	public boolean registerNewAdmin() {
	    Scanner scanner = new Scanner(System.in);
	    System.out.println("\nNew Admin User Registration");

	    System.out.print("Enter first name: ");
	    String firstName = scanner.nextLine();

	    System.out.print("Enter last name: ");
	    String lastName = scanner.nextLine();

	    System.out.print("Enter mobile number: ");
	    int mobileNumber = scanner.nextInt();

	    System.out.print("Enter gender: ");
	    String gender = scanner.nextLine();

	    System.out.print("Enter email: ");
	    String email = scanner.nextLine();

	    System.out.print("Enter password: ");
	    String password = scanner.nextLine();
	    
	    Userdetails user = new Userdetails();
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setmobileNumber(mobileNumber);
        user.setGender(gender);
        if (isUserExists(user.getEmail())) {
            System.out.println("User already exists with this email!");
            return false;
        }
        saveUser(user);
        return true;
	}
	
        
        private static boolean isUserExists(String email) {
            Session session = sf.openSession();
            String hql = "FROM User U WHERE U.email = :email";
            Query<Userdetails> query = session.createQuery(hql, Userdetails.class);
            query.setParameter("email", email);
            Userdetails existingUser = query.uniqueResult();
            session.close();
            return existingUser != null;
        }
        private static void saveUser(Userdetails user) {
            Session session = sf.openSession();
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            session.close();
            System.out.println("User registered successfully!");
        }
        
        public  Userdetails authenticateUser(String email, String password) {
            Session session = sf.openSession();
            String hql = "FROM User U WHERE U.email = :email AND U.password = :password";
            Query<Userdetails> query = session.createQuery(hql, Userdetails.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            Userdetails user = query.uniqueResult();
            session.close();
            return user;
        }

        


}


	