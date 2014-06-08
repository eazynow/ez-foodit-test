package com.foodit.test.model.Response;

public class CountStatistic {

	String name;
	int count;

	public String getName() {
		return name;
	}
	public void setCategory(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public CountStatistic() {}
	
	public CountStatistic(String name, int count) {
		this.name = name;
		this.count = count;
	}
}
