package com.example.demo;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock_data")
public class StockData {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String scrip;
    
    private LocalDate date;
    
    private Double closePrice;
    private long volume;
    private double gain;
    private double loss;
    private double rs;
    private double rsi;
    private double low;
    private double high;
    private double averageGain;
    private double averageLoss;

	public StockData(String scrip, LocalDate date, Double closePrice, long volume, 
			 double low, double high) {
		super();
		this.scrip = scrip;
		this.date = date;
		this.closePrice = closePrice;
		this.volume = volume;
		
		this.low = low;
		this.high = high;
		
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getAverageGain() {
		return averageGain;
	}

	public void setAverageGain(double averageGain) {
		this.averageGain = averageGain;
	}

	public double getAverageLoss() {
		return averageLoss;
	}

	public void setAverageLoss(double averageLoss) {
		this.averageLoss = averageLoss;
	}

	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
	    this.gain = Math.round(gain * 10.0) / 10.0;
	}

	public void setLoss(double loss) {
	    this.loss = Math.round(loss * 10.0) / 10.0;
	}


	public double getLoss() {
		return loss;
	}



	public double getRs() {
		return rs;
	}

	public void setRs(double rs) {
		this.rs = rs;
	}

	public double getRsi() {
		return rsi;
	}

	public void setRsi(double rsi) {
		this.rsi = rsi;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getScrip() {
		return scrip;
	}

	public void setScrip(String scrip) {
		this.scrip = scrip;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Double getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(Double closePrice) {
		this.closePrice = closePrice;
	}

	

	public StockData(Long id, String scrip, LocalDate date, Double closePrice, long volume) {
		super();
		this.id = id;
		this.scrip = scrip;
		this.date = date;
		this.closePrice = closePrice;
		this.volume = volume;
	}

	public StockData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StockData(String scrip, LocalDate date, Double closePrice, long volume) {
		super();
		this.scrip = scrip;
		this.date = date;
		this.closePrice = closePrice;
		this.volume = volume;
	}
	


}
