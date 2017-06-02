package com.infoservices.lue.adapterdata;

public class TransactionData {
     private String transaction_id;
     private String date;
     private String credit_points;
     private String price;
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCredit_points() {
		return credit_points;
	}
	public void setCredit_points(String credit_points) {
		this.credit_points = credit_points;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
}
