package com.occupancy.api.config;

import com.occupancy.api.facility.Facility;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReadSampleData {

    @Autowired
    public ReadSampleData(){}

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
}
