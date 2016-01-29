package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Zaver R
 * Date: January 28, 2016
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(1050, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 300, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 300, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println(f.getProperties());
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	// PointFeatures also have a getLocation method
	    }

	    for (PointFeature feature : earthquakes) {
			markers.add(createMarker(feature));
		}
		map.addMarkers(markers);
	}
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	private SimplePointMarker createMarker(PointFeature feature)
	{
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		float magnitude = Float.parseFloat(feature.getProperty("magnitude").toString());
		setColorAndSize(marker, magnitude);
		return marker;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}

	private void setColorAndSize(SimplePointMarker marker, float magnitude) {
		if(magnitude < 4.0) {
			marker.setColor(getColor(ColorCode.BLUE));
			marker.setRadius(3.33f);
		} else if(magnitude >= 4.0 && magnitude <= 4.9) {
			marker.setColor(getColor(ColorCode.YELLOW));
			marker.setRadius(6.66f);
		} else {
			marker.setColor(getColor(ColorCode.RED));
			marker.setRadius(10.0f);
		}
	}

	private int getColor(ColorCode code) {
		switch (code) {
			case RED:
				return color(255, 0, 0);
			case GREEN:
				return color(0, 255, 0);
			case BLUE:
				return color(0, 0, 255);
			case YELLOW:
				return color(255, 255, 0);
		}
		return 0;
	}

	// helper method to draw key in GUI
	private void addKey()
	{
		//Set rectangle background color
		fill(205, 255, 255);
		rect(50, 50, 160, 200, 5);

		textSize(14);
		fill(0);
		text("Earthquake key", 85, 75);

		fill(getColor(ColorCode.RED));
		ellipse(75, 100, 10, 10);
		textSize(12);
		fill(0);
		text("5.0+ Magnitude", 95, 105);

		fill(getColor(ColorCode.YELLOW));
		ellipse(75, 130, 6.66f, 6.66f);
		textSize(12);
		fill(0);
		text("4.0+ Magnitude", 95, 135);

		fill(getColor(ColorCode.BLUE));
		ellipse(75, 160, 3.33f, 3.33f);
		textSize(12);
		fill(0);
		text("Below 4.0", 95, 165);
	
	}

	private enum ColorCode {
		RED,
		GREEN,
		BLUE,
		YELLOW
	}
}
