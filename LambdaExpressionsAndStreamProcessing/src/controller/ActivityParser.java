package controller;
//package controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Activity;
import model.MonitoredData;
/**
 * 
 * @author Florin
 *
 */
public class ActivityParser {
	private static ActivityParser instance;
	private static final String dateFormat = "yyyy-mm-dd hh:mm:ss";
	public static ActivityParser instance(){
		if (instance == null){
			instance = new ActivityParser();
		}
		return (instance);
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	public List<MonitoredData> parseActivityFile(String path){
		List<MonitoredData> monitoredData = new ArrayList<MonitoredData>();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				monitoredData.add(parseActivityLine(sCurrentLine));
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();	
		}
		return (monitoredData);
	}
	/**
	 * 
	 * @param sCurrentLine
	 * @return
	 * @throws ParseException
	 */

	private MonitoredData parseActivityLine(String sCurrentLine) throws ParseException {
		Date start = (new SimpleDateFormat(dateFormat)).parse(sCurrentLine.substring(0, dateFormat.length()));
		Date end = (new SimpleDateFormat(dateFormat)).parse(sCurrentLine.substring(dateFormat.length() + 2, 2 + 2 * dateFormat.length()));
		Activity activity = parseActivity(sCurrentLine.substring(dateFormat.length() * 2 + 4));
		return (new MonitoredData(activity, start, end));
	}
   /**
    * 
    * @param activity
    * @return
    */
	private Activity parseActivity(String activity) {
		activity = activity.toUpperCase().replaceAll("[\t\n ]", "");
		for (Activity act : Activity.values()){
			if (activity.equals(act.toString())){
				return (act);
			}
		}
		if (activity.equals("SPARE_TIME/TV")){
			return (Activity.SPARE_TIME_TV);
		}
		return (Activity.UNKNOWN);
	}
	
	
}
