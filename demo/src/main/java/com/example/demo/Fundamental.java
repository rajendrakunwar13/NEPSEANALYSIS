package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Fundamental {
	@Id
	Long id;
	String fy;
	short quarter;
	String scrip;
	double eps;
	double bookValue;
	double grahamFairValue;
	double ratio;
	long listedShare;
	long marketCap;
	String sector;
	
	
	public long getListedShare() {
		return listedShare;
	}

	public void setListedShare(long listedShare) {
		this.listedShare = listedShare;
	}

	public long getMarketCap() {
		return marketCap;
	}

	public void setMarketCap(long marketCap) {
		this.marketCap = marketCap;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public void setGrahamFairValue(double grahamFairValue) {
		this.grahamFairValue = grahamFairValue;
	}

	public short getQuarter() {
        return quarter;
    }

    public double getBookValue() {
		return bookValue;
	}

	public void setBookValue(double bookValue) {
		this.bookValue = bookValue;
	}

	public double getGrahamFairValue() {
		return grahamFairValue;
	}

	public void calculateGrahamFairValue() {
		this.grahamFairValue = Math.sqrt(bookValue*eps*22.5);
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public void setQuarter(short quarter) {
        if (quarter < 1 || quarter > 4) {
            throw new IllegalArgumentException("Quarter must be either 1, 2, 3, or 4.");
        }
        this.quarter = quarter;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFy() {
		return fy;
	}

	public void setFy(String fy) {
		this.fy = fy;
	}

	public String getScrip() {
		return scrip;
	}

	public void setScrip(String scrip) {
		this.scrip = scrip;
	}

	public double getEps() {
		return eps;
	}

	public void setEps(double eps) {
		this.eps = eps;
	}


    
}
