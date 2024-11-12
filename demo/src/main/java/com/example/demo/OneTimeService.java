package com.example.demo;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.openqa.selenium.StaleElementReferenceException;
@Service
public class OneTimeService {

		@Autowired
		private StockDataRepository stockDataRepository;
		
	    public void updateOldDataByDate(String date) {
	        WebDriverManager.chromedriver().setup();
	        WebDriver driver = new ChromeDriver();
	  
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        LocalDate parsedDate = LocalDate.parse(date, formatter);
	        try {
	            driver.get("https://www.sharesansar.com/today-share-price");
	            WebElement datepicker = driver.findElement(By.id("fromdate"));
	            WebElement button = driver.findElement(By.id("btn_todayshareprice_submit"));
	            datepicker.clear();
	            datepicker.sendKeys(date);
	            button.click();
	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='headFixed']/tbody/tr")));
	                try {
	                    List<WebElement> rows = driver.findElements(By.xpath("//table[@id='headFixed']/tbody/tr"));
	                    for (WebElement row : rows) {
	                        List<WebElement> cells = row.findElements(By.xpath(".//td"));
	                        String scrip = cells.get(1).getText();
	                        String close = cells.get(6).getText();
	                        String volume = cells.get(8).getText();
	                        String high = cells.get(4).getText();
	                        String low = cells.get(5).getText();
	                        
	                        System.out.println("Scrip: " + scrip + ", Close: " + close + ", Volume: " + volume);
	                    }
	                   
	                } catch (StaleElementReferenceException e) {
	                	   System.out.println("StaleElementReferenceException caught, retrying... Attempt " );
	                	List<WebElement> rows = driver.findElements(By.xpath("//table[@id='headFixed']/tbody/tr"));
	                    for (WebElement row : rows) {
	                        List<WebElement> cells = row.findElements(By.xpath(".//td"));
	                        String scrip = cells.get(1).getText();
	                        String close = cells.get(6).getText();
	                        String high = cells.get(4).getText();
	                        String low = cells.get(5).getText();
	                        double closePrice=Double.parseDouble(close.replace(",", ""));
	                        
	                        double highPrice=Double.parseDouble(high.replace(",", ""));
	                        double lowPrice=Double.parseDouble(low.replace(",", ""));
	                        String volume = cells.get(8).getText();
	                        String cleanedVolume = volume.split("\\.")[0].replace(",", "");
	                        long vol = Long.parseLong(cleanedVolume);
	                        StockData stockData=new StockData(scrip,parsedDate,closePrice,vol,lowPrice,highPrice);
	                        stockDataRepository.save(stockData);                     
	                       }
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	        	driver.quit();
	        }
	    }
	    
	    


	    //////////////////////////////////////
	    ////////////////////////////////////////
	    /////////////////////////////////////////////
	    public void updateDataOfRsi(String scrip) {
	        List<StockData> allDataForAStock = stockDataRepository.findAllDataForAStock(scrip);
	        if (allDataForAStock.size() < 15) {
	            return; // Not enough data to calculate RSI
	        }

	        double sumGain = 0, sumLoss = 0;
	        double gain, loss;

	        // Calculate initial sum of gains and losses for the first 14 days
	        for (int i = 1; i < 15; i++) {
	            double difference = allDataForAStock.get(i).getClosePrice() - allDataForAStock.get(i - 1).getClosePrice();
	            gain = Math.max(difference, 0);
	            loss = Math.max(-difference, 0);
	            sumGain += gain;
	            sumLoss += loss;
	            allDataForAStock.get(i).setGain(gain);
	            allDataForAStock.get(i).setLoss(loss);
	        }

	        // Calculate and set initial average gain, loss, RS, and RSI for day 15
	        double averageGain = sumGain / 14;
	        double averageLoss = sumLoss / 14;
	        double rs = averageGain / averageLoss;
	        double rsi = 100 - (100 / (1 + rs));

	        allDataForAStock.get(14).setAverageGain(averageGain);
	        allDataForAStock.get(14).setAverageLoss(averageLoss);
	        allDataForAStock.get(14).setRs(rs);
	        allDataForAStock.get(14).setRsi(rsi);

	        // Calculate and update the rest of the data
	        for (int i = 15; i < allDataForAStock.size(); i++) {
	            double difference = allDataForAStock.get(i).getClosePrice() - allDataForAStock.get(i - 1).getClosePrice();
	            gain = Math.max(difference, 0);
	            loss = Math.max(-difference, 0);

	            averageGain = ((allDataForAStock.get(i - 1).getAverageGain() * 13) + gain) / 14;
	            averageLoss = ((allDataForAStock.get(i - 1).getAverageLoss() * 13) + loss) / 14;
	            rs = averageGain / averageLoss;
	            rsi = 100 - (100 / (1 + rs));

	            allDataForAStock.get(i).setAverageGain(averageGain);
	            allDataForAStock.get(i).setAverageLoss(averageLoss);
	            allDataForAStock.get(i).setGain(gain);
	            allDataForAStock.get(i).setLoss(loss);
	            allDataForAStock.get(i).setRs(rs);
	            allDataForAStock.get(i).setRsi(rsi);
	        }

	        // Save all the updated data in one batch operation
	        stockDataRepository.saveAll(allDataForAStock);
	    }
	    public void  updateLastRsi(String scrip) {
	    	List<StockData> lastTwoStockData=stockDataRepository.getLastTwoStockData(scrip);
            double difference = lastTwoStockData.get(0).getClosePrice() - lastTwoStockData.get(1).getClosePrice();
           double  gain = Math.max(difference, 0);
           double loss = Math.max(-difference, 0);
	   
	    	double averageGain =(lastTwoStockData.get(1).getAverageGain()*13+gain)/14;
	    	double averageLoss=(lastTwoStockData.get(1).getAverageLoss()*13+loss)/14;
	
	    	double rs=averageGain/averageLoss;
	    	double rsi =100-100/(1+rs);
	    
	      lastTwoStockData.get(0).setAverageGain(averageGain);
	      lastTwoStockData.get(0).setAverageLoss(averageLoss);
	      lastTwoStockData.get(0).setGain(gain);
	      lastTwoStockData.get(0).setLoss(loss);
	      lastTwoStockData.get(0).setRs(rs);
	      lastTwoStockData.get(0).setRsi(rsi);
	    	
	    	
	    	stockDataRepository.save(lastTwoStockData.get(0));
	    }
	    public void updateLastRsiforAllStocks() {
	    	List<String>listOfScrip=stockDataRepository.findDistinctScrips();
	    	for(int i=0;i<listOfScrip.size();i++) {
	    		try {
	    			this.updateLastRsi(listOfScrip.get(i));
	    		}catch(Exception e) {
	    			System.out.println("error occured in updating RSI of Scrip : "+listOfScrip.get(i));
	    		}
	    		
	    	}
	    }
	    
		public String updateLastStockData() {
			WebDriver driver = new ChromeDriver();
			driver.get("https://www.sharesansar.com/today-share-price");
			WebElement dateElement = driver.findElement(By.xpath("//h5/span[@class='text-org']"));
			String dateText = dateElement.getText();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate parsedDate = LocalDate.parse(dateText, formatter);

			if (stockDataRepository.existsByDate(parsedDate)) {
				return "DATAS ALREADY UP TO DATE";
			} else {
				try {
					List<WebElement> rows = driver.findElements(By.xpath("//table[@id='headFixed']/tbody/tr"));
					for (WebElement row : rows) {
						List<WebElement> cells = row.findElements(By.xpath(".//td"));
						String scrip = cells.get(1).getText();
						String close = cells.get(6).getText();
						double closePrice = Double.parseDouble(close.replace(",", ""));
						String volume = cells.get(8).getText();
						String cleanedVolume = volume.split("\\.")[0].replace(",", "");
						long vol = Long.parseLong(cleanedVolume);
						StockData stockData = new StockData(scrip, parsedDate, closePrice, vol);
						stockDataRepository.save(stockData);
					}

				} catch (StaleElementReferenceException e) {
					return " error occured in updating datas";
				}

			}
			return "Data succesfully updated";
		}
		}
	
	
