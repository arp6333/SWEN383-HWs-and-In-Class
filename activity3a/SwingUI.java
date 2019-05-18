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
* when it receives an update message, displays the temperature
* in Celsius and Kelvin.
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
    /**
    * There are two JPanels, one each for Kelvin and Celsius, added to the
    * frame. Each JPanel is a 2 row by 1 column grid, with the temperature
    * name in the first row and the temperature itself in the second row.
    */
    public JPanel SwingPanel1 = new JPanel(new GridLayout(2,1));
    public JPanel SwingPanel2 = new JPanel(new GridLayout(2,1));
    public JLabel swingCelsiusField;       // current Celsius reading
    public JLabel swingKelvinField;        // current Kelvin reading
        
    /**
    * A Font object contains information on the font to be used to render text.
    */
    private static Font labelFont = new Font(Font.SERIF, Font.PLAIN, 72);

    /**
    * Remember the station we're attached to and
    * add ourselves as an observer.
    * Call Swing() to create the UI.
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

        /**
        * WeatherStation frame is a grid of 1 row by an indefinite number of columns.
        */
        swing.setLayout(new GridLayout(1,0));

        /**
        * Set up Kelvin display.
        */
        swing.add(SwingPanel1);
        createLabel(" Kelvin ", SwingPanel1);
        swingKelvinField = createLabel("", SwingPanel1);

        /**
        * Set up Celsius display.
        */
        swing.add(SwingPanel2);
        createLabel(" Celsius ", SwingPanel2);
        swingCelsiusField = createLabel("", SwingPanel2);

        /**
        * Set up the frame's default close operation pack its elements,
        * and make the frame visible.
        */
        swing.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        swing.pack();
        swing.setVisible(true);
    }

    /**
    * Called when WeatherStation gets another reading.
    * The Observable should be the station; the Object
    * argument is ignored.
    */
    public void update(Observable obs, Object ignore){
        /**
        * Check for spurious updates from unrelated objects.
        */
        if(station != obs){
            return;
        }
        
        /**
        * Update Kelvin display.
        */
        setKelvinJLabel(station.getKelvin());

        /**
        * Update Celsius display.
        */
        setCelsiusJLabel(station.getCelsius());
        
        /**
        * Update the JFrame to show changes
        */
        swing.pack();
        swing.repaint();
    }
    
    /**
    * Create a Label with the initial value <title>, place it in
    * the specified <panel>, and return a reference to the Label
    * in case the caller wants to remember it.
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
    * Set the label holding the Kelvin temperature.
    */
    public void setKelvinJLabel(double temperature){
        swingKelvinField.setText(String.format("%6.2f", temperature));
    }

    /**
    * Set the label holding the Celsius temperature.
    */
    public void setCelsiusJLabel(double temperature){
        swingCelsiusField.setText(String.format("%6.2f", temperature));
    }

    /**
    * Start the application.
    */
    public static void main(String[] args){
        WeatherStation ws = new WeatherStation();
        Thread thread = new Thread(ws);
        SwingUI ui = new SwingUI(ws);

        thread.start();
    }
}
