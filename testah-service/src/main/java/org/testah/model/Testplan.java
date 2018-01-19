package org.testah.model;

import java.math.BigInteger;

public class Testplan {
	private BigInteger id;
	
	private String name;


	public Testplan() {

	}
	
	/**
	 * @param id
	 * @param name
	 */
	public Testplan(BigInteger id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public BigInteger getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(BigInteger id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
}
