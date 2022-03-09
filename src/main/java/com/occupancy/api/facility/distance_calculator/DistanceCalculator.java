package com.occupancy.api.facility.distance_calculator;

import com.occupancy.api.facility.Facility;


public class DistanceCalculator {

    //returns if a facility is withing given range
    public   boolean inRange(Facility facility,
                             double lat,
                             double lon,
                             char unit,
                             double range){
        if(facility.getLatitude() != null &&
                facility.getLongitude() != null &&
                    distance(facility.getLatitude(), facility.getLongitude(), lat, lon, unit) <= range){
            System.out.println(distance(facility.getLatitude(), facility.getLongitude(), lat, lon, unit));
            return true;
        }else{
            return false;
        }
    }

    //returns the distance between two points using haversine formula
    public double distance(double lat1,
                           double lon1,
                           double lat2,
                           double lon2,
                           char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    //This function converts decimal degrees to radians
    public double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    //This function converts radians to decimal degrees
    public double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
