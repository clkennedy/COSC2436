
import customconsole.JavaConsole;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import queueproject.Passenger;
import queueproject.Station;
import queueproject.Train;

/**
 * A class that simulates a train line with passengers.
 *
 * @author Charles Hoot
 * @version 4.0
 */
public class TrainSimulation {

    public static Station[] allStations;
    public static Queue<Passenger> allPassengers;
    public static Queue<Train> allTrains;
    public static int trainCount = 0;
    public static int passengersCreated;
    public static int passengersOnTrains;
    public static int passengersDelivered;
    public static int STATIONS = 10;
    public static int TRAIN_INTERVAL = 15;
    public static int TRAIN_CAPACITY = 10;
    public static int DURATION = 200;

    public static Random generator;

    public static void main(String args[]) {
        try {
            JavaConsole.Show();
        } catch (Exception ex) {
            JavaConsole.Log(ex.getMessage());
        }
        
        allStations = new Station[STATIONS];
        allTrains = new LinkedList<>();
        allPassengers = new LinkedList<>();
        trainCount = 0;

        generator = new Random();
        generateStations();

        for (int clock = 0; clock < DURATION; clock++) {
            report(clock);                 // Provide details
            startNewTrain(clock);
            generatePassengers();
            moveTrains(clock);
        } // end for
        finalReport(DURATION);
        
        JavaConsole.writeLine("Press Any Key to Conintue..");
        JavaConsole.ReadKey();
        
    } // end main

    /**
     * Generates the stations for the track.
     */
    public static void generateStations() {
        for (int i = 0; i < STATIONS; i++) {
            int timeToNext = 10 + generator.nextInt(10);
            allStations[i] = new Station(timeToNext);
            JavaConsole.writeLine("\tCreated station " + i
                    + " time to next is " + timeToNext);
        } // end for
    } // end generateStations

    /**
     * Generates the passengers.
     */
    public static void generatePassengers() {
        int newPassengers = generator.nextInt(10);
        for (int i = 0; i < newPassengers; i++) {
            int startStation = 10;
            int stopStation = 1;
            // Generate stations until we get a start before the stop
            while (startStation >= stopStation) {
                startStation = generator.nextInt(STATIONS);
                stopStation = generator.nextInt(STATIONS);
            }

            Passenger rider = new Passenger(startStation, stopStation);
            allStations[startStation].addPassenger(rider);
            allPassengers.offer(rider);
            passengersCreated++;
        } // end for
    } // end generatePassengers

    /**
     * Moves the trains along the tracks.
     *
     * @param clock The current time.
     */
    public static void moveTrains(int clock) {
        // Move each train
        int trains = trainCount;

        for (int i = 0; i < trains; i++) {
            Train moveMe = allTrains.poll();
            moveMe.move();
            int timeToNext = moveMe.timeToNext();

            if (timeToNext == 0) {
                // Train is at the station
                int stationNo = moveMe.nextStation();
                int gotOff = moveMe.unloadPassengers(stationNo);
                int gotOn = moveMe.loadPassengers(allStations[stationNo], clock);

                passengersOnTrains += gotOn;
                passengersOnTrains -= gotOff;
                passengersDelivered += gotOff;

                // Update for the next station
                moveMe.updateStation(allStations[stationNo].getTimeToNextStation());
            } // end if

            if (moveMe.nextStation() < STATIONS) {
                allTrains.offer(moveMe); // Train is still going
            } else {
                trainCount--;//This train is done
            }
        } // end for
    } // end moveTrains

    /**
     * Reports the current situations of the trains and passengers waiting.
     *
     * @param simulationTime The current time.
     */
    public static void report(int simulationTime) {
        int passengersWaiting
                = passengersCreated - passengersOnTrains - passengersDelivered;

        JavaConsole.writeLine();
        JavaConsole.writeLine("Time Marker " + simulationTime + "\t waiting: "
                + passengersWaiting + "\t on trains: " + passengersOnTrains);
    } // end report

    /**
     * Starts a new train if it is on a quarter-hour mark.
     *
     * @param simulationTime The current time.
     */
    public static void startNewTrain(int simulationTime) {
        if ((simulationTime % TRAIN_INTERVAL) == 0) {
            allTrains.offer(new Train(TRAIN_CAPACITY));
            trainCount++;
        } // end if
    } // end startNewTrain

    /**
     * Reports the final situations of the trains and passengers waiting and
     * some statistics for passengers' wait times.
     *
     * @param clock The time that train operations have ceased.
     */
    public static void finalReport(int clock) {
        JavaConsole.writeLine("Final Report");
        JavaConsole.writeLine("The total number of passengers is "
                + passengersCreated);
        JavaConsole.writeLine("The number of passengers currently on a train "
                + passengersOnTrains);
        JavaConsole.writeLine("The number of passengers delivered is "
                + passengersDelivered);
        int waitBoardedSum = 0;
        int waitNotBoardedSum = 0;

        for (int i = 0; i < passengersCreated; i++) {
            Passenger p = allPassengers.poll();
            if (p.boarded()) {
                waitBoardedSum += p.waitTime(clock);
            } else {
                waitNotBoardedSum += p.waitTime(clock);
            }
        } // end for

        JavaConsole.writeLine("The average wait time for passengers "
                + "that have boarded is");
        JavaConsole.writeLine((double) waitBoardedSum / (passengersOnTrains
                + passengersDelivered));
        JavaConsole.writeLine("The average wait time for passengers "
                + "that have not yet boarded is");
        JavaConsole.writeLine((double) waitNotBoardedSum
                / (passengersCreated - passengersOnTrains - passengersDelivered));
    } // end finalReport
} // end TrainSimulator

