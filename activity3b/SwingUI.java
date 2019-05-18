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
* The SwingUI class is an observer of the WeatherStation that,
* when it receives an update message, displays the readings
* from WeatherStation.
*
* The main method for the Swing based monitoring application
* is here as well.
*/

import java.awt.Font;
import java.util.Observer;
import java.util.Observable;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SwingUI implements Observer{
    private final WeatherStation station;
    
    public JFrame swing = new JFrame();
   
    public JLabel swingKelvinField;        // current Kelvin reading.
    public JLabel swingCelsiusField;       // current Celsius reading.
    public JLabel swingFahrenheitField;    // current Fahrenheit reading.
    public JLabel swingInchesField;        // current pressure in inches.
    public JLabel swingMillibarsField;     // current pressure in millibars.
        
    /**
    * A Font object contains information on the font to be used to render text.
    */
    private static Font labelFont = new Font(Font.SERIF, Font.PLAIN, 56);

    /**
    * Remember the station we're attached to and
    * add ourselves as an observer.
    * Call Swing() to create the UI.
    *
    * @param - WeatherStation station: the observed 
    *          WeatherStation object
    */
    public SwingUI(WeatherStation station){
        this.station = station;
        this.station.addObserver(this);
        Swing();
    }
    
    /**
    * Create the Swing window.
    */
    public void Swing(){
        swing.setTitle("Weather Station, Swing");

        // WeatherStation frame is a grid of 1 row by an indefinite number of columns.
        swing.setLayout(new GridLayout(1,0));

        // Set up temperature displays.
        swingKelvinField = createDisplay("Kelvin", swingKelvinField);
        swingCelsiusField = createDisplay("Celsius", swingCelsiusField);
        swingFahrenheitField = createDisplay("Fahrenheit", swingFahrenheitField);
        
        //Set up pressure displays.
        swingInchesField = createDisplay("Inches", swingInchesField);
        swingMillibarsField = createDisplay("Millibars", swingMillibarsField);

        /**
        * Set up the frame's default close operation pack its elements,
        * and make the frame visible.
        */
        swing.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        swing.pack();
        swing.setVisible(true);
    }
    
    /**
    * Create the JPanels for the readings. Each JPanel is a 
    * 2 row by 1 column grid, with the name in the first row 
    * and the reading itself in the second row.
    *
    * @ param - String label: the title of the panel,
    *           JLabel field: the label to be created
    * @ return - JLabel field: the created JLabel
    */
    public JLabel createDisplay(String label, JLabel field){
        JPanel panel = new JPanel(new GridLayout(2,1));
        swing.add(panel);
        createLabel(" " + label + " ", panel);
        field = createLabel("", panel);
        return field;
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
        
        // Update temperature displays.
        setJLabel(station.getKelvin(), swingKelvinField);
        setJLabel(station.getCelsius(), swingCelsiusField);
        setJLabel(station.getFahrenheit(), swingFahrenheitField);

        // Update pressure displays.
        setJLabel(station.getPressureInches(), swingInchesField);
        setJLabel(station.getPressureMillibars(), swingMillibarsField);
        
        // Update the JFrame to show changes
        swing.pack();
        swing.repaint();
    }
    
    /**
    * Create a Label with the initial value <title>, place it in
    * the specified <panel>, and return a reference to the Label
    * in case the caller wants to remember it.
    *
    * @param - String title: the value to be set, JPanel panel: the panel to be used
    * @return - Jlabel label: the new value
    */
    private JLabel createLabel(String title, JPanel panel){
        JLabel label = new JLabel(title);

        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.TOP);
        label.setFont(labelFont);
        panel.add(label);

        return label;
    }
    
    /**
    * Update the label holding the changed reading.
    *
    * @param - double reading: the new reading, JLabel label: the label to be changed
    */
    public void setJLabel(double reading, JLabel label){
        label.setText(String.format("%6.2f", reading));
    }

    /**
    * Start the application.
    *
    * @param - String[] args: nothing
    */
    public static void main(String[] args){
        WeatherStation ws = new WeatherStation();
        Thread thread = new Thread(ws);
        SwingUI ui = new SwingUI(ws);

        thread.start();
    }
}
