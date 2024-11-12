package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
	@Autowired
	private OneTimeService oneTimeService;
	@Autowired
	private StockDataRepository stockDataRepository;
	@Autowired
	private StockFilter stockFilter;
	
@GetMapping("/test")
public String updateBulkData() {
	String[]dateArray=DateList.getDates();
	System.out.println(Arrays.toString(dateArray));
	for(String d:dateArray) {
		oneTimeService.updateOldDataByDate(d);
	}
return "all data succesfully added";
}
@GetMapping("/UpdateRsiOldDate")
public String updateBulkRSI() {
    try {
        List<String> scripList = stockDataRepository.findDistinctScrips();
        for (int i = 0; i < scripList.size(); i++) {
            try {
                oneTimeService.updateDataOfRsi(scripList.get(i));
            } catch (Exception e) {
               
                System.out.println("Error processing scrip: " + scripList.get(i));
                e.printStackTrace();
             
            }
        }
    } catch (Exception e) {
      
        System.out.println("An error occurred while retrieving scrip list or other processing.");
        e.printStackTrace();
    }
    return "RSI update completed.";
}



@GetMapping("/filterStocksByRsi")
public List<String> filterStocksByRsi() {
	List<String>decisionList=new ArrayList<>();
    try {
        List<String> scripList = stockDataRepository.findDistinctScrips();
        for (int i = 0; i < scripList.size(); i++) {
            try {
            	if(!(stockFilter.decisionByRsiForAScrip(scripList.get(i)).equals("")))
                decisionList.add(stockFilter.decisionByRsiNew(scripList.get(i)));
            } catch (Exception e) {
               
                System.out.println("Error processing scrip: " + scripList.get(i));
                e.printStackTrace();
             
            }
        }
    } catch (Exception e) {
      
        System.out.println("An error occurred while retrieving scrip list or other processing.");
        e.printStackTrace();
    }
    return decisionList;
}


//The below methods will filter the list of stocks by volume
//Stock having exceptionally high amount of trading will be filtered
@GetMapping("/filterStocksByVolume")
public List<String> filterStocksByVolume() {
    List<Volume> decisionList = new ArrayList<>();
    
    try {
        // Fetch the list of distinct scrips
        List<String> scripList = stockDataRepository.findDistinctScrips();
        
        for (String scrip : scripList) {
            try {
                // Check if the decision is not empty, then add it to the decisionList
                Volume volumeDecision = stockFilter.decisionByVolumeForAScrip(scrip);
                if (!volumeDecision.getDecision().equals("")) {
                    decisionList.add(volumeDecision);
                }
            } catch (Exception e) {
                System.out.println("Error processing scrip: " + scrip);
                e.printStackTrace();
            }
        }
    } catch (Exception e) {
        System.out.println("An error occurred while retrieving scrip list or other processing.");
        e.printStackTrace();
    }
    
    // Sort decisionList by the ratio of LongAverage to ShortAverage and return the sorted decision list as strings
    return decisionList.stream()
        .sorted((d1, d2) -> {
            double ratio1 = d1.getLongAverage() / d1.getShortAverage();
            double ratio2 = d2.getLongAverage() / d2.getShortAverage();
            return Double.compare(ratio1, ratio2);
        })
        .map(Volume::getDecision)  // Extract the decision string
        .collect(Collectors.toList());  // Collect as a list of strings
}

@GetMapping("/updateLastData")
public String updateLastData() {
	try {
		
		String message=oneTimeService.updateLastStockData();
oneTimeService.updateLastRsiforAllStocks();
		return message;
	}catch(Exception e){
		return"some error occured";
		
	}

}


@GetMapping("/allStocks")
public List<String>getAllStock(){
	return stockDataRepository.findDistinctScrips();
}
}
