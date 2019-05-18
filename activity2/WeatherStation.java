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
* Class for a simple computer based weather station that reports the current
* temperature (in Celsius and Kelvin) every second. The station is attached to a
* sensor that reports the temperature as a 16-bit number (0 to 65535)
* representing the Kelvin temperature to the nearest 1/100th of a degree.
*
* This class is implements Runnable so that it can be embedded in a Thread
* which runs the periodic sensing.
*/

import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;

public class WeatherStation implements Runnable{
    
    // Variables for the plain text output
    public final KelvinTempSensor sensor;  // Temperature sensor.
    public final long PERIOD = 1000;       // 1 sec = 1000 ms.
    
    // Variables for the AWT frame
    public Frame awt = new Frame();
    /**
    * There are two Panels, one each for Kelvin and Celsius, added to the
    * frame. Each Panel is a 2 row by 1 column grid, with the temperature
    * name in the first row and the temperature itself in the second row.
    */
    public Panel AWTPanel1 = new Panel(new GridLayout(2,1));
    public Panel AWTPanel2 = new Panel(new GridLayout(2,1));
    public Label celsiusField ;            // current Celsius reading for awt
    public Label kelvinField ;             // current Kelvin reading for awt
    
    // Variables for the Swing frame
    public JFrame swing = new JFrame();
    /**
    * There are two JPanels, one each for Kelvin and Celsius, added to the
    * frame. Each JPanel is a 2 row by 1 column grid, with the temperature
    * name in the first row and the temperature itself in the second row.
    */
    public JPanel SwingPanel1 = new JPanel(new GridLayout(2,1)) ;
    public JPanel SwingPanel2 = new JPanel(new GridLayout(2,1)) ;
    public JLabel swingCelsiusField;       // current Celsius reading for swing
    public JLabel swingKelvinField;        // current Kelvin reading for swing
      
    /**
    * A Font object contains information on the font to be used to render text.
    */
    private static Font labelFont = new Font(Font.SERIF, Font.PLAIN, 72);

    /**
    * When a WeatherStation object is created, it in turn creates the sensor object it will use, 
    * along with creating the AWT window and creating the Swing window.
    */
    public WeatherStation(){
        sensor = new KelvinTempSensor();
        AWT();
        Swing();
    }

    /**
    * The "run" method called by the enclosing Thread object when started.
    * Repeatedly sleeps a second, acquires the current temperature from
    * its sensor, and reports this as a formatted output string
    * along with updating the AWT and Swing panels.
    */
    public void run(){
        int reading;                // actual sensor reading.
        double celsius;             // sensor reading transformed to Celsius
        final int KTOC = -27315 ;   // Convert raw Kelvin reading to Celsius
        double kelvin;              // sensor reading transformed to Kelvin

        while(true){
           try{
               Thread.sleep(PERIOD);
           }
           catch(Exception e){
               // ignore exceptions
           }

           reading = sensor.reading();
           celsius = (reading + KTOC) / 100.0;
           kelvin = reading / 100.0;
           // Format reading to print to console
           System.out.printf("Reading is %6.2f degrees C and %6.2f degrees K%n", celsius, kelvin);
            
           //////////////////////////////////////////////////////////////////////////////////////////
            
           /**
           * Section for updating the AWT Frame
           */
   
           /**
           * Update Kelvin display.
           */
           AWTPanel1.removeAll();
           setLabel(" Kelvin ", AWTPanel1);
           kelvinField = setLabel("" + kelvin, AWTPanel1) ;
   
           /**
           * Update Celsius display.
           */
           AWTPanel2.removeAll();
           setLabel(" Celsius ", AWTPanel2) ;
           celsiusField = setLabel("" + celsius, AWTPanel2) ;
           
           /**
           * Update the Frame to show changes
           */
           awt.pack();
           awt.repaint();
           
           //////////////////////////////////////////////////////////////////////////////////////////
           
           /**
           * Section for updating the Swing JFrame
           */
           
           /**
           * Update Kelvin display.
           */
           setKelvinJLabel(kelvin);
   
           /**
           * Update Celsius display.
           */
           setCelsiusJLabel(celsius);
           
           /**
           * Update the JFrame to show changes
           */
           swing.pack();
           swing.repaint();
        }
    }
    
    /**
    * Create a Label with the initial value <title>, place it in
    * the specified <panel>, and return a reference to the Label
    * in case the caller wants to remember it.
    * Used for the AWT labels.
    */
    private Label setLabel(String title, Panel panel){
        Label label = new Label(title);

        label.setAlignment(Label.CENTER);
        label.setFont(labelFont);
        panel.add(label);

        return label;
    }
    
    /**
    * Create a Label with the initial value <title>, place it in
    * the specified <panel>, and return a reference to the Label
    * in case the caller wants to remember it.
    * Used for the Swing labels.
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
    * Create the AWT window.
    */
    public void AWT(){
        awt.setTitle("Weather Station, AWT") ;

        /**
        * WeatherStation frame is a grid of 1 row by an indefinite number
        * of columns.
        */
        awt.setLayout(new GridLayout(1,0));

        /**
        * Set up Kelvin display.
        */
        awt.add(AWTPanel1);
        setLabel(" Kelvin ", AWTPanel1);
        kelvinField = setLabel("", AWTPanel1);

        /**
        * Set up Celsius display.
        */
        awt.add(AWTPanel2);
        setLabel(" Celsius ", AWTPanel2);
        celsiusField = setLabel("", AWTPanel2);

        /**
        * Set up the window's default close operation and pack its elements.
        */
        awt.addWindowListener(new WindowAdapter(){
              public void windowClosing(WindowEvent windowEvent){
                  System.exit(0);
              }        
        });

        /**
        * Pack the components in this frame and make the frame visible.
        */
        awt.pack() ;
        awt.setVisible(true) ;
    }
    
    /**
    * Create the Swing window.
    */
    public void Swing(){
        swing.setTitle("Weather Station, Swing");
        // Set location so it doesn't cover the AWT window.
        swing.setLocation(550, 0);

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
    * Initial main method.
    *      Create the WeatherStation (Runnable).
    *      Embed the WeatherStation in a Thread.
    *      Start the Thread.
    */
    public static void main(String[] args){
        WeatherStation ws = new WeatherStation();
        Thread thread = new Thread(ws);
        thread.start();
    }
}