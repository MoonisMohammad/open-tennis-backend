package com.occupancy.api.average_calculator;

import com.occupancy.api.data.Data;

import java.util.List;

public class AverageCalculator {

    public Long calculateAverage(List<Data> dataList,Long interval){
        Long average = Long.valueOf(0);
        for(Data data: dataList){
            average = Long.valueOf(data.getCount()) + average;
        }
        if(dataList.size() == 0)return Long.valueOf(0);
        else return average / interval;
    }
}
