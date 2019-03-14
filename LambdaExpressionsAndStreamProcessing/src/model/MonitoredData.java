package model;
/**
 * 
 */
import java.util.Date;

/**
 * Define a class MonitoredData with 3 fields: start time, end time and activity as string.
     Read the data from the file Activity.txt using streams and split each line in 3 parts:
   start_time, end_time and activity label and create a list of objects of type MonitoredData
 * @author Florin
 *
 */
public class MonitoredData {
	private Activity activity;
	private Date startTime;
	private Date endTime;
	
	/** 
	 *  getter for activity field 
	 * @return activities
	 */
	public Activity getActivity() {
		return activity;
	}
	
	/**
	 *   getter for getStartTime field 
	 * @return
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 *  getter for getEndTime field 
	 * @return
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 *  Constructor 
	 * @param activity
	 * @param startTime
	 * @param endTime
	 * 
	 */
	public MonitoredData(Activity activity, Date startTime, Date endTime) {
		super();
		this.activity = activity;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	/**
	 * 
	 * @return
	 */

	public Integer getDay(){
		return startTime.getDay();
	}
	
     /**
      * 
      */
	public String toString(){
		return (startTime.toString() + " \t\t" + endTime.toString() + "\t\t" + activity.toString());
	}
	
	/**
	 * 
	 * @return
	 */
	public Date getDurationDate(){
		return new Date(endTime.getTime() - startTime.getTime());
	}
	
	/**
	 * 
	 * @return
	 */
	public Long getDurationLong(){
		return endTime.getTime() - startTime.getTime();
	}
	
	/**
	 * 
	 * @return
	 */
	public Long getDurationMinsLong(){
		return (long) getDurationDate().getMinutes();
	}
}
