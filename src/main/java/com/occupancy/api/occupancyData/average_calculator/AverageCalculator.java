package com.occupancy.api.occupancyData.average_calculator;

import com.occupancy.api.occupancyData.OccupancyData;

import java.time.DayOfWeek;
import java.util.List;

public class AverageCalculator {

    public Double calculateAverage(List<OccupancyData> dataList,
                                   DayOfWeek dayOfWeek,
                                   Long interval){
        Double average = Double.valueOf(0);
        for(OccupancyData occupancyData : dataList){
           if(occupancyData.getDayOfWeek().equals(dayOfWeek))
               average = Long.valueOf(occupancyData.getCount()) + average;
        }
        if(dataList.size() == 0) return Double.valueOf(0);
        else return average / Double.valueOf(interval);
    }
}
