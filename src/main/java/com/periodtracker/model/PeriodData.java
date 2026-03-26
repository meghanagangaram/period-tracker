
package com.periodtracker.model;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "period_data") // ⚠️ MUST MATCH DB TABLE
public class PeriodData {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

private int userId;
private LocalDate startDate;
private LocalDate endDate;
private String symptoms;

public int getUserId(){return userId;}
public void setUserId(int userId){this.userId=userId;}

public LocalDate getStartDate(){return startDate;}
public void setStartDate(LocalDate startDate){this.startDate=startDate;}

public LocalDate getEndDate(){return endDate;}
public void setEndDate(LocalDate endDate){this.endDate=endDate;}

public String getSymptoms(){return symptoms;}
public void setSymptoms(String symptoms){this.symptoms=symptoms;}
}
