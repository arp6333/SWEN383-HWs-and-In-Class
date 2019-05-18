/**
* Initial Author
*      Michael J. Lutz
*
* Other Contributers
*      Ellie Parobek
*
* Acknowledgements
*/

/**
* The TextUI class is an observer of the WeatherStation that,
* when it receives an update message, prints readings from
* weather station.
*
* The main method for the text based monitoring application
* is here as well.
*/

import java.util.Observer;
import java.util.Observable;

public class TextUI implements Observer{
    private final WeatherStation station;

    /**
    * Remember the station we're attached to and
    * add ourselves as an observer.
    *
    * @param - WeatherStation station: the observed 
    *          WeatherStation object
    */
    public TextUI(WeatherStation station){
        this.station = station;
        this.station.addObserver(this);
    }

    /**
    * Called when WeatherStation gets another reading.
    * The Observable should be the station; the Object
    * argument is ignored.
    *
    * @param - Observable obs: the observable, Object ignore: ignore
    */
    public void update(Observable obs, Object ignore){
        // Check for spurious updates from unrelated objects.
        if(station != obs){
            return;
        }
        
        // Retrieve and print the temperature readings.
        System.out.printf(
                "Temperature: %6.2f C %6.2f F %6.2f K%n",
                station.getCelsius(), station.getFahrenheit(), station.getKelvin());
     
        // Retrieve and print the pressure readings.
        System.out.printf(
                "Pressure: %6.2f inches %6.2f mbar%n%n",
                station.getPressureInches(), station.getPressureMillibars());
    }

    /**
    * Start the application.
    *
    * @param - String[] args: nothing
    */
    public static void main(String[] args){
        WeatherStation ws = new WeatherStation();
        Thread thread = new Thread(ws);
        TextUI ui = new TextUI(ws);

        thread.start();
    }
}
