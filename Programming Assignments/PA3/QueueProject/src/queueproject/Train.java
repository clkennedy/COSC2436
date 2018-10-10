/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queueproject;

import customconsole.JavaConsole;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author cameron.kennedy
 */
public class Train {

    private static int nextId = 1;
    
    private Queue<Passenger> _riders = new LinkedList<>();
    private int numOfPassengers = 0;
    private int _nextStopId = 0;
    private int _timeToNextStop = 1;
    private int _id;
    private int _cap;
    
    public Train(int capacity) {
        _cap = capacity;
        _id = nextId++;
        
        JavaConsole.writeLine("\tCreated Train " + _id);
    }

    public void move() {
        _timeToNextStop --;
        JavaConsole.writeLine("\t\tTrain " + _id + " will reach next station in " + _timeToNextStop + " ticks");
    }

    public int timeToNext() {
        return _timeToNextStop;
    }

    public int nextStation() {
        return _nextStopId;
    }

    public int unloadPassengers(int stationNo) {
        int count = 0;
        for(int i = 0; i < numOfPassengers; i ++){
            Passenger p = _riders.poll();
            if(p.getDestinationId() != stationNo){
                _riders.offer(p);
            }
            else{
                count++;
                numOfPassengers--;
            }
        }
        JavaConsole.writeLine("\t\t\tUnloaded " + count + " Passengers on train " + _id + " at station " + stationNo);
        return count;
    }

    public int loadPassengers(Station station, int clock) {
        int count = 0;
        
        while(station.arePeopleWaiting()){
            if(_riders.size() < _cap){
                Passenger p = station.getWaitngPassenger();
                p.boardTrain(this);
                _riders.offer(p);
                count ++;
                numOfPassengers++;
            }
            else{
                break;
            }
        }
        JavaConsole.writeLine("\t\t\tLoaded " + count + " Passengers on train " + _id + " at station " + station.getStationId());
        return count;
    }

    public void updateStation(int timeToNextStation) {
        _nextStopId ++;
        _timeToNextStop = timeToNextStation;
    }
    
}
