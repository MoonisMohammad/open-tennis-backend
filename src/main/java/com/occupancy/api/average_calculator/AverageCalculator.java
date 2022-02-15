package com.occupancy.api.average_calculator;

import com.occupancy.api.data.Data;

import java.util.List;

public class AverageCalculator {

    public Long calculateAverage(List<Data> dataList){
        Long average = Long.valueOf(0);
        for(Data data: dataList){
            average = Long.valueOf(data.getCount()) + average;
        }
        return average / dataList.size();
    }
}
