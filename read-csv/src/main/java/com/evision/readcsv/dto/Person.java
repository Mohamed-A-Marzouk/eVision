package com.evision.readcsv.dto;

public class Person {

	private String name;
	private String nationalId;
	private int amount;
	
	public Person() {
		super();
	}
	public Person(String nationalId , String name, int amount) {
		super();
		this.name = name;
		this.nationalId = nationalId;
		this.amount = amount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNationalId() {
		return nationalId;
	}
	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "Person [name=" + name + ", nationalId=" + nationalId + ", amount=" + amount + "]";
	}
	
	public String print() {
		return  name + "," + nationalId + ", " + amount ;
	}
	
	
	
	
}
