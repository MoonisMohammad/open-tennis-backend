package com.occupancy.api.occupancyData.average_calculator;
import com.occupancy.api.occupancyData.OccupancyData;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class AverageCalculator {

    public List<Double> calculateAverage(List<OccupancyData> dataList,
                                         Long interval){
        Double sundayAverage = Double.valueOf(0);
        Double mondayAverage = Double.valueOf(0);
        Double tuesdayAverage = Double.valueOf(0);
        Double wednesdayAverage = Double.valueOf(0);
        Double thursdayAverage = Double.valueOf(0);
        Double fridayAverage = Double.valueOf(0);
        Double saturdayAverage = Double.valueOf(0);
        for(OccupancyData occupancyData : dataList){
            if(occupancyData.getDayOfWeek().equals(DayOfWeek.valueOf("SUNDAY"))){
                sundayAverage = Long.valueOf(occupancyData.getCount()) + sundayAverage;
            }if(occupancyData.getDayOfWeek().equals(DayOfWeek.valueOf("MONDAY"))){
                mondayAverage  = Long.valueOf(occupancyData.getCount()) + mondayAverage ;
            }if(occupancyData.getDayOfWeek().equals(DayOfWeek.valueOf("TUESDAY"))){
                tuesdayAverage = Long.valueOf(occupancyData.getCount()) + tuesdayAverage;
            }if(occupancyData.getDayOfWeek().equals(DayOfWeek.valueOf("WEDNESDAY"))){
                wednesdayAverage = Long.valueOf(occupancyData.getCount()) + wednesdayAverage;
            }if(occupancyData.getDayOfWeek().equals(DayOfWeek.valueOf("THURSDAY"))){
                thursdayAverage = Long.valueOf(occupancyData.getCount()) + thursdayAverage;
            }if(occupancyData.getDayOfWeek().equals(DayOfWeek.valueOf("FRIDAY"))){
                fridayAverage = Long.valueOf(occupancyData.getCount()) + fridayAverage;
            }if(occupancyData.getDayOfWeek().equals(DayOfWeek.valueOf("SATURDAY"))){
                saturdayAverage = Long.valueOf(occupancyData.getCount()) + saturdayAverage;
            }
        }
        ArrayList<Double> weeklyAverage = new ArrayList<Double>();
        weeklyAverage.add(sundayAverage/interval);
        weeklyAverage.add(mondayAverage/interval);
        weeklyAverage.add(tuesdayAverage/interval);
        weeklyAverage.add(wednesdayAverage/interval);
        weeklyAverage.add(thursdayAverage/interval);
        weeklyAverage.add(fridayAverage/interval);
        weeklyAverage.add(saturdayAverage/interval);
        return weeklyAverage ;
    }
}
