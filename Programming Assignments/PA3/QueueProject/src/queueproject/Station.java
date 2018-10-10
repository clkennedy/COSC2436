/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queueproject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author cameron.kennedy
 */
public class Station {

    private static int nextId = 0;
    
    private final Queue<Passenger> _passengers = new LinkedList<>();
    private int _clicksToNextStation;
    private int _id;
    
    public Station(int timeToNext) {
        _clicksToNextStation = timeToNext;
        _id = nextId++;
    }

    public void addPassenger(Passenger rider) {
        _passengers.offer(rider);
    }
    
    public int getTimeToNextStation(){
        return _clicksToNextStation;
    }
    
    public boolean arePeopleWaiting(){
        return !_passengers.isEmpty();
    }
    
    public Passenger getWaitngPassenger(){
        return _passengers.poll();
    }
    
    public int getStationId(){
        return _id;
    }
    
}
