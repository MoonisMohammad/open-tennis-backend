package com.occupancy.api.config;

import com.occupancy.api.data.Data;
import com.occupancy.api.facility.Facility;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReadSampleData {
    ArrayList<DataPoint> court1 ;
    ArrayList<DataPoint> court2 ;
    ArrayList<DataPoint> court3 ;

    public class DataPoint{
        public LocalDateTime start;
        public LocalDateTime end;
        public int count;
        private DayOfWeek dayOfWeek;
        public DataPoint(LocalDateTime start,
                         LocalDateTime end,
                         int count){
            this.start = start;
            this.end = end;
            this.dayOfWeek =start.getDayOfWeek();
            this.count =count;
        }

        public void print(){
            System.out.println(this.start+"|"+ this.end+"|"+ this.dayOfWeek+"|"+ this.count);
        }
    }

    @Autowired
    public ReadSampleData(){
        this.court1 = new ArrayList<DataPoint>();
        this.court2 = new ArrayList<DataPoint>();
        this.court3 = new ArrayList<DataPoint>();
    }

    public List<Facility> readFacilities(Long ownerId) throws IOException {
        List<Facility> facilities = new ArrayList<Facility>();
        // File path is passed as parameter
        URL path = ClassLoader.getSystemResource("./src/main/java/com/occupancy/api/config/Tennis_Courts.txt");
        if(path==null) {
            //The file was not found, insert error handling here
        }
        File file = new File("./src/main/java/com/occupancy/api/config/Tennis_Courts.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        // Declaring a string variable
        String st;
        // Condition holds true till
        // there is character in a string
        while ((st = br.readLine()) != null) {
            String[] sArray = st.split("#");
            Double latitude = Double.valueOf(sArray[1].stripLeading());
            Double longitude = Double.valueOf(sArray[2].stripLeading());
            String name = sArray[3].stripLeading();
            Facility facility = new Facility(ownerId,name,"OTTAWA",latitude,longitude);
            facilities.add(facility);
        }
        return facilities;
    }

    public List<Data> getOccupancyData(Long deviceId,
                                       Long facilityId) throws IOException {
        List<Data> simulatedData = new ArrayList<>();
        readDataForCourts();
        List<Data> simulatedData3 = simulateData(   court1.get(0).start,
                                                    court3.get(court3.size()-1).end,
                                                    2,
                                                    deviceId,
                                                    facilityId,
                                                    court3);
        List<Data> simulatedData1 = simulateData(court1.get(0).start,
                                                court1.get(court1.size()-1).end,
                                        0,
                                                deviceId,
                                                facilityId,
                                                court1);
       List<Data> simulatedData2 = simulateData(court1.get(0).start,
                                                court2.get(court2.size()-1).end,
                                        1,
                                                deviceId,
                                                facilityId,
                                                court2);
       simulatedData.addAll(simulatedData1);
       simulatedData.addAll(simulatedData2);
       simulatedData.addAll(simulatedData3);
       return simulatedData;
    }
    public List<Data> simulateData(LocalDateTime start,
                                   LocalDateTime finish,
                                   int reference,
                                   Long deviceId,
                                   Long facilityId,
                                   ArrayList<DataPoint> courtData){
        int i = 0;
        start = start.minusDays(1);
        finish = finish.plusDays(1);
        List<Data> dataList = new ArrayList<>();
        while(start.isBefore(finish)){
            if(i>=courtData.size()) break;
            DataPoint d = courtData.get(i);
            LocalDateTime entry = d.start;
            LocalDateTime exit = d.end;
            while(start.isBefore(entry)) {
                start = start.plusMinutes(10);
                if (start.toLocalTime().compareTo(LocalTime.parse("22:00:00")) > -1) {
                    start.plusDays(1);
                    start.minusHours(15);
                    break;
                }
            }
            while (start.isBefore(exit)) {
                Data simData = new Data(facilityId,
                        deviceId,
                        d.count,
                        reference,
                        d.dayOfWeek,
                        start);
                dataList.add(simData);
                start = start.plusMinutes(10);
                if (start.toLocalTime().compareTo(LocalTime.parse("22:00:00")) > -1) {
                    start.plusDays(1);
                    start.minusHours(15);
                    break;
                }
            }
            i = i +1;
        }
        return  dataList;
    }


    public void readDataForCourts() throws IOException {
        LocalDateTime curr = java.time.LocalDateTime.now();
        LocalDateTime max = getDateTime("2021-10-31 13:00",Long.valueOf(0));
        long daysBetween = ChronoUnit.DAYS.between(max,curr);
        URL path = ClassLoader.getSystemResource("./src/main/java/com/occupancy/api/config/Count_data_lyndwood.txt");
        if(path==null) {
            //The file was not found, insert error handling here
        }
        File file = new File("./src/main/java/com/occupancy/api/config/Count_data_lyndwood.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null) {
            String[] sArray = st.split("#");
            String startString = sArray[0].stripTrailing().stripLeading();
            String endString = sArray[1].stripTrailing().stripLeading();
            String courtNames = sArray[2].stripTrailing().stripLeading();
            LocalDateTime startDateTime = getDateTime(startString,daysBetween);
            LocalDateTime endDateTime = getDateTime(endString,daysBetween);
            int count = getRandomNumber(1,5);
            DataPoint dataPoint = new DataPoint(startDateTime,endDateTime,count);
            dataPoint.print();
            if(courtNames.contains("Court 1")){
                this.court1.add(dataPoint);
            }
            if(courtNames.contains("Court 2")){
                this.court2.add(dataPoint);
            }
            if(courtNames.contains("Court 3")){
                this.court3.add(dataPoint);
            }
        }
    }

    public LocalDateTime getDateTime(String d,Long extra){
        String time = d.split("\\s+")[1];
        if(time.length() == 4) time = "0" + time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(d.split("\\s+")[0]+" "+time,formatter).plusDays(extra);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
