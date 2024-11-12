package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockFilter {
@Autowired
private StockDataRepository stockDataRepository;

public String decisionByRsiForAScrip(String scrip) {
	String decision="";
	List<Double> lastTwoRsi = stockDataRepository.getLastTwoRsiForAScrip(scrip);
	if (lastTwoRsi.get(0) < 30 && lastTwoRsi.get(1) > 30) {
		decision="Sell Signal by RSI for : " + scrip;
	} else if (lastTwoRsi.get(0)>30 && lastTwoRsi.get(1) < 30)
		decision="Buy Signal by RSI for : " + scrip;
	return decision;
}

public String decisionByRsiNew(String scrip) {
	String decision="";
	List<Double> lastTwoRsi = stockDataRepository.getLastTwoRsiForAScrip(scrip);
	if (lastTwoRsi.get(0) <= 35&&lastTwoRsi.get(0) >=30) {
		decision="buy Signal by RSI for : " + scrip;
	} 
	return decision;
}


public Volume decisionByVolumeForAScrip(String scrip) {
	String decision = "";
	Double shortMovingAverageVolume = stockDataRepository.findMovingAverageVolume(scrip, 3);
	Double longMovingAverageVolume = stockDataRepository.findMovingAverageVolume(scrip, 50);
	//List<Long> lastTwoVolume = stockDataRepository.getLastTwoVolumeForAScrip(scrip);
	if (shortMovingAverageVolume>1.5*longMovingAverageVolume)
		decision = "Exceptional Volume for : " + scrip +" short = "+ shortMovingAverageVolume +" long = "+longMovingAverageVolume;
	return new Volume(longMovingAverageVolume,shortMovingAverageVolume,decision);
}
}
