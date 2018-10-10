/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queueproject;

import customconsole.JavaConsole;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

/**
 *
 * @author cameron.kennedy
 */
public class Passenger {

    private int _stationFrom;
    private int _stationTo;
    private boolean _boarded = false;
    private Train _boardedTrain = null;
    private LocalDateTime _personCreated;
    private Duration _timeWaited;
    
    public Passenger(int startStation, int stopStation) {
        _stationFrom = startStation;
        _stationTo = stopStation;
        _personCreated = LocalDateTime.now();
        JavaConsole.writeLine("\tCreated Passenger at Station " + _stationFrom + " going to Station " + _stationTo);
    }

    public boolean boarded() {
        return _boarded;
    }
    
    public int getDestinationId(){
        return _stationTo;
    }
    
    public long getTimeWaited(){
        if(_boarded){
            return _timeWaited.toMinutes();
        }
        Duration diff = Duration.between(_personCreated, LocalDateTime.now());
        return diff.toMinutes();
    }
    
    public void boardTrain(Train train){
        _timeWaited = Duration.between(_personCreated, LocalDateTime.now());
        _boarded = true;
        _boardedTrain = train;
    }
    
    public int waitTime(int time){
        return 0;
    }
    
}
