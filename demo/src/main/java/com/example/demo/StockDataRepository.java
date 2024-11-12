package com.example.demo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface StockDataRepository extends JpaRepository<StockData, Long> {
	@Query(value = "SELECT close_price FROM stock_data WHERE scrip = :scrip AND date <=:date ORDER BY date ASC LIMIT 15", nativeQuery = true)
	List<Double> findLast14DaysByScrip(@Param("scrip") String scrip, @Param("date") LocalDate date);

	@Query(value="SELECT rsi FROM stock_data  WHERE scrip = :scrip ORDER BY date DESC LIMIT 2",
			nativeQuery=true)
	List<Double> getLastTwoRsiForAScrip(@Param("scrip") String scrip);
	
	@Query(value="SELECT * FROM stock_data  WHERE scrip = :scrip ORDER BY date DESC LIMIT 2",
			nativeQuery=true)
	List<StockData> getLastTwoStockData(@Param("scrip") String scrip);
	

	@Query(value="SELECT volume FROM stock_data  WHERE scrip = :scrip ORDER BY date DESC LIMIT 2",
			nativeQuery=true)
	List<Long> getLastTwoVolumeForAScrip(@Param("scrip") String scrip);
	
	@Query("SELECT DISTINCT s.scrip FROM StockData s")
	List<String> findDistinctScrips();

	Optional<StockData> findByScripAndDate(String scrip, LocalDate date);

	@Query(value = "SELECT AVG(volume) FROM "
			+ "(SELECT volume FROM stock_data WHERE scrip = :scrip ORDER BY date DESC LIMIT :days) AS subquery", nativeQuery = true)
	Double findMovingAverageVolume(@Param("scrip") String scrip,@Param("days")int days);


	@Query("SELECT DISTINCT s.scrip FROM StockData s")
	List<String> findAllScrips();

	@Query("SELECT MAX(s.date) FROM StockData s")
	LocalDate findLastDateInStockData();
	
	@Query(value="select s from StockData s where s.scrip=:scrip order by date ASC")
	public List<StockData>findAllDataForAStock(@Param(value = "scrip") String scrip);

	boolean existsByDate(LocalDate parsedDate);
@Query(value="select low from stock_data where scrip=:scrip order by date desc" ,nativeQuery=true)
	List<Double> findAllPrice(@Param("scrip")String scrip);

@Query(value="select high from stock_data where scrip=:scrip order by date desc" ,nativeQuery=true)
List<Double> findAllHighPrice(@Param("scrip")String scrip);




}
