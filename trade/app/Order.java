package com.trade.app;

class Order {
	private int orderNumber;
	private String orderType;
	private String orderName;
	private int variable1;
	private double variable2;

	public Order() {

	}

	public Order(String orderType, String orderName, int variable1, double variable2) {
		super();
		this.orderType = orderType;
		this.orderName = orderName;
		this.variable1 = variable1;
		this.variable2 = variable2;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public int getVariable1() {
		return variable1;
	}

	public void setVariable1(int variable1) {
		this.variable1 = variable1;
	}

	public double getVariable2() {
		return variable2;
	}

	public void setVariable2(double variable2) {
		this.variable2 = variable2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + orderNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (orderNumber != other.orderNumber)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Order [orderType=" + orderType + ", orderName=" + orderName + ", variable1=" + variable1
				+ ", variable2=" + variable2 + "]";
	}
}
