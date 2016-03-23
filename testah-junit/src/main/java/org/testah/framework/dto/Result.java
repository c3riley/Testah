package org.testah.framework.dto;

public class Result {

	private int total;
	private int pass;
	private int fail;
	private int na;
	private int manual;
	private int pending;
	private int retire;
	private int knownFail;
	private int knownPass;
	private int knownNa;
	private int knownTotal;

	public int getTotal() {
		return total;
	}

	public void setTotal(final int total) {
		this.total = total;
	}

	public int getPass() {
		return pass;
	}

	public void setPass(final int pass) {
		this.pass = pass;
	}

	public int getFail() {
		return fail;
	}

	public void setFail(final int fail) {
		this.fail = fail;
	}

	public int getNa() {
		return na;
	}

	public void setNa(final int na) {
		this.na = na;
	}

	public int getManual() {
		return manual;
	}

	public void setManual(final int manual) {
		this.manual = manual;
	}

	public int getPending() {
		return pending;
	}

	public void setPending(final int pending) {
		this.pending = pending;
	}

	public int getRetire() {
		return retire;
	}

	public void setRetire(final int retire) {
		this.retire = retire;
	}

	public int getKnownFail() {
		return knownFail;
	}

	public void setKnownFail(final int knownFail) {
		this.knownFail = knownFail;
	}

	public int getKnownPass() {
		return knownPass;
	}

	public void setKnownPass(final int knownPass) {
		this.knownPass = knownPass;
	}

	public int getKnownNa() {
		return knownNa;
	}

	public void setKnownNa(final int knownNa) {
		this.knownNa = knownNa;
	}

	public int getKnownTotal() {
		return knownTotal;
	}

	public void setKnownTotal(final int knownTotal) {
		this.knownTotal = knownTotal;
	}

}
