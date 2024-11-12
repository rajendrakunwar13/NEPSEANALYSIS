package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class UtilityClass {
	@Autowired
	private StockDataRepository stockDataRepository;


	public List<Double> supportResistanceIdentifier(String scrip) {
	    List<Double> priceList = stockDataRepository.findAllPrice(scrip);
	    List<Double> highpriceList = stockDataRepository.findAllHighPrice(scrip);
	    List<Double> supportList = new ArrayList<>();
	    List<Double> resistanceList = new ArrayList<>();


	    // Identify support points
	    for (int i = 3; i < priceList.size() - 3; i++) {
	        if (priceList.get(i) < priceList.get(i - 1) && priceList.get(i) < priceList.get(i + 1)) {
	            if (priceList.get(i) < priceList.get(i - 2) && priceList.get(i) < priceList.get(i + 2)) {
	            	 if (priceList.get(i) < priceList.get(i - 3) && priceList.get(i) < priceList.get(i + 3)) {
	   	              
	            	supportList.add(priceList.get(i));
	            	 }
	            }
	        }
	        
	
	    }
	    for (int i = 3; i < highpriceList.size() - 3; i++) {
	        if (highpriceList.get(i) < highpriceList.get(i - 1) && highpriceList.get(i) < highpriceList.get(i + 1)) {
	            if (highpriceList.get(i) < highpriceList.get(i - 2) && highpriceList.get(i) < highpriceList.get(i + 2)) {
	            	 if (highpriceList.get(i) < highpriceList.get(i - 3) && highpriceList.get(i) < highpriceList.get(i + 3)) {
	   	              
	            	supportList.add(highpriceList.get(i));
	            	 }
	            }
	        }
	        
	
	    }

	    supportList = supportList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
	    List<Double> filteredSupportList = new ArrayList<>();
	    for (int i = 0; i < supportList.size(); i++) {
	        boolean isTooClose = false;
	        for (int j = 0; j < filteredSupportList.size(); j++) {
	            if (Math.abs(supportList.get(i) - filteredSupportList.get(j)) <= filteredSupportList.get(j) * 0.03) {
	                isTooClose = true;
	                break;
	            }
	        }
	        if (!isTooClose) {
	            filteredSupportList.add(supportList.get(i));
	        }
	    }

	    return filteredSupportList;
	}

}
