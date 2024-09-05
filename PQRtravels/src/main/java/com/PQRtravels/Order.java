package com.PQRtravels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	@Column(name = "order_id")
    private Long orderId; // Custom Order ID (e.g., subtracting 10)

    @Column(name = "source")
    private String Source;
    @Column(name = "Firstname")
    private String Firstname;
    @Column(name = "Lastname")
    private String Lastname;
    @Column(name = "destination")
    private String destination;
    @Column(name = "gender")
    private String gender;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name="Totalprice")
    private int Totalprice;
    public int getTotalprice() {
		return Totalprice;
	}
	public void setTotalprice(double totalPrice2) {
		Totalprice = (int) totalPrice2;
	}
	@ManyToOne
    @JoinColumn(name = "user_id")
    private Userdetails user;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getSource() {
		return Source;
	}
	public String getFirstname() {
		return Firstname;
	}
	public void setFirstname(String firstname) {
		Firstname = firstname;
	}
	public String getLastname() {
		return Lastname;
	}
	public void setLastname(String lastname) {
		Lastname = lastname;
	}
	public void setTotalprice(int totalprice) {
		Totalprice = totalprice;
	}
	public void setSource(String source) {
		this.Source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Userdetails getUser() {
		return user;
	}
	public void setUser(Userdetails user) {
		this.user = user;
	}
	

}
