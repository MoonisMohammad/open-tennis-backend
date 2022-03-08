package com.occupancy.api.config;

import com.occupancy.api.occupancyData.OccupancyData;
import com.occupancy.api.facility.Facility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.io.*;
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
        InputStream in = new ClassPathResource("Tennis_Courts.txt").getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String st;
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

    public void readDataForCourts() throws IOException {
        LocalDateTime curr = java.time.LocalDateTime.now();
        LocalDateTime max = getDateTime("2021-10-31 13:00",Long.valueOf(0));
        long daysBetween = ChronoUnit.DAYS.between(max,curr);
        InputStream in = new ClassPathResource("Count_data_lyndwood.txt").getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
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

    public List<OccupancyData> getOccupancyData(Long deviceId,Long facilityId) throws IOException {
        List<OccupancyData> simulatedData = new ArrayList<>();
        readDataForCourts();
        List<OccupancyData> simulatedData3 = simulateData(
                                                            court1.get(0).start,
                                                            court3.get(court3.size()-1).end,
                                                    2,
                                                            deviceId,
                                                            facilityId,
                                                            court3);
        List<OccupancyData> simulatedData1 = simulateData(
                                                            court1.get(0).start,
                                                            court1.get(court1.size()-1).end,
                                                    0,
                                                            deviceId,
                                                            facilityId,
                                                            court1);
       List<OccupancyData> simulatedData2 = simulateData(
                                                            court1.get(0).start,
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

    public List<OccupancyData> getRandomOccupancyData(Long deviceId,Long facilityId) throws IOException {
        List<OccupancyData> randomData = new ArrayList<>();
        readDataForCourts();
        List<OccupancyData> randomData1 = generateRandomData(
                                                        0,
                                                                deviceId,
                                                                facilityId);
        List<OccupancyData> randomData2 = generateRandomData(
                                                        1,
                                                                deviceId,
                                                                facilityId);
        List<OccupancyData> randomData3 = generateRandomData(
                                                        2,
                                                                deviceId,
                                                                facilityId);
        randomData.addAll(randomData1);
        randomData.addAll(randomData2);
        randomData.addAll(randomData3);
        return randomData;
    }

    public List<OccupancyData> generateRandomData(int reference,
                                                  Long deviceId,
                                                  Long facilityId) {
        LocalDateTime currDateTime =  LocalDateTime.now();
        LocalDateTime startDateTime = currDateTime.minusDays(35);
        List<OccupancyData> dataList = new ArrayList<>();
        while (startDateTime.isBefore(currDateTime)) {
            int count = 0;
            if (startDateTime.getDayOfWeek().getValue() == DayOfWeek.SUNDAY.getValue()) {
                count = getRandomNumber(2,5);
            }
            if (startDateTime.getDayOfWeek().getValue() == DayOfWeek.MONDAY.getValue()) {
                count = getRandomNumber(1,2);
            }
            if (startDateTime.getDayOfWeek().getValue() == DayOfWeek.TUESDAY.getValue()) {
                count = getRandomNumber(1,3);
            }
            if (startDateTime.getDayOfWeek().getValue() == DayOfWeek.WEDNESDAY.getValue()) {
                count = getRandomNumber(1,3);

            }
            if (startDateTime.getDayOfWeek().getValue() == DayOfWeek.THURSDAY.getValue()) {
                count = getRandomNumber(1,4);
            }
            if (startDateTime.getDayOfWeek().getValue() == DayOfWeek.FRIDAY.getValue()) {
                count = getRandomNumber(2,4);
            }
            if (startDateTime.getDayOfWeek().getValue() == DayOfWeek.SATURDAY.getValue()) {
                count = getRandomNumber(2,5);
            }
            if(count != 0){
                System.out.println();
                OccupancyData simData = new OccupancyData(facilityId,
                                                          deviceId,
                                                          count,
                                                          reference,
                                                          startDateTime.getDayOfWeek(),
                                                          startDateTime);
                System.out.println(simData.getTimeStamp()+" "+simData.getDayOfWeek());
                dataList.add(simData);
            }
            startDateTime = startDateTime.plusMinutes(10);
        }
        return  dataList;
    }


    //Simulates data with the added gaps for which we don't have data
    public List<OccupancyData> simulateData(LocalDateTime start,
                                            LocalDateTime finish,
                                            int reference,
                                            Long deviceId,
                                            Long facilityId,
                                            ArrayList<DataPoint> courtData){
        int i = 0;
        start = start.minusDays(1);
        finish = finish.plusDays(1);
        List<OccupancyData> dataList = new ArrayList<>();
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
                OccupancyData simData = new OccupancyData(facilityId,
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

    //Get texts to correct format un date and time
    public LocalDateTime getDateTime(String d,Long extra){
        String time = d.split("\\s+")[1];
        if(time.length() == 4) time = "0" + time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(d.split("\\s+")[0]+" "+time,formatter).plusDays(extra);
    }

    //Get random number between a and  b - 1
    public int getRandomNumber(int a, int b) {
        return (int) ((Math.random() * (b - a)) + a);
    }
}
