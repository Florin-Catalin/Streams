package controller;
//package controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.Activity;
import model.MonitoredData;

public class TextProcessor {
	private static final String DATAPATH = "Activities.txt";
//	private static final String taks1_PATH = "Activities.txt";
	private static final String CountDays_PATH = "CountDays.txt";
	private static final String ActivityPerDay_PATH = "ActivityPerDay.txt";
	private static final String ActivityTotal_PATH = "ActivityTotal.txt";
	private static final String ActivityFilter_PATH = "task5.txt";
	
	 private TextProcessor () {
			List<MonitoredData> monitoredData = ActivityParser.instance().parseActivityFile(DATAPATH);
			CountDays(monitoredData);
			CountActivities(monitoredData);
			ActivityPerDay(monitoredData);
			ActivityTotal(monitoredData);
			ActivityFilter(monitoredData);
	 }
	private  static TextProcessor instance =  new TextProcessor ();
	   
	 public static TextProcessor getInstance () {
		 return instance ;
	 }
	 
	 /** 1
	  *  This method count the distinct days that appear in the monitoring data 
	  * @param data
	  * As an activity could start in a day and finish in the next one, looking just at the start date of an
activity is not enough. So, firstly all days were concatenated in a stream, then in order to remove
duplicates it was converted to a set and finally the size of the set was the result.
	  */
		public static void CountDays(List<MonitoredData> data){
			int distinctDays = Stream.concat(data.stream().map(s -> s.getStartTime().getDate()), 
											data.stream().map(s -> s.getEndTime().getDate())).
											collect(Collectors.toSet()).size();
			
			System.out.println("The numbers of days of monitores that appears in the log is " + distinctDays + "\n");
		}
		
		/** 2
		 *  maps to each distinct action type the number of occurrences in the log
		 * @param data
		 * In order to map the activities with the corresponding number of occurrences, each the
collector groupingBy was used
		 */
		public static void CountActivities(List<MonitoredData> data){
			Map<Activity, Long> result = data.stream().collect(Collectors.groupingBy(MonitoredData::getActivity, Collectors.counting()));
		
			writeToFile(CountDays_PATH, result);
			System.out.println(result.toString());
		}
		/** 3
		 *   Generates a data structure of type Map<Integer, Map<String, Integer>> that contains the activity count for 
		 *   each day of the log (task number 2 applied for each
	day of the log)and writes the result in a text file
		 * @param data
		 * 
		 * The same strategy as above was used but this time a nested grouping was needed. The first level
of nesting beeing the day of the activity
		 */
		
		public static void ActivityPerDay(List<MonitoredData> data){
			 Map<Integer, Map<Activity, Long>> result = data.stream().collect(Collectors.groupingBy(MonitoredData::getDay, Collectors.groupingBy(MonitoredData::getActivity, Collectors.counting())));
			writeToFile(ActivityPerDay_PATH, result);
			System.out.println("Each activity appeard for each day " +  result.toString()+"\n");
		}


		 /** 4
		  *for each activity the total duration computed over the monitoring period. Filter the
	activities with total duration larger than 10 hours
		  * @param data
		  * In order to solve this task nested stream processing was needed. So, in order to filter out
activities with greater total time than 10 hours the total duration for each activity had to be
computed several times. Further, the remaining activities where grouped and the total time was
re-computed.
		  */
		
		public static void ActivityTotal(List<MonitoredData> data){
			 Map<Activity, Long> result = data.stream()
					 						  .filter(n -> data.stream()
					 								  		   .filter(d -> d.getActivity() == n.getActivity())
					 								  		   .map(a -> a.getDurationMinsLong())
					 								  		   .collect(Collectors.summingLong(Long::longValue)) 
					 								  		    > (long) 600)
					 						  .collect(Collectors.groupingBy(MonitoredData::getActivity, Collectors.summingLong(MonitoredData::getDurationMinsLong)));
			writeToFile(ActivityTotal_PATH, result);
			System.out.println(result.toString());
		}
		
		/**
		 *  Filter the activities that have 90% of the monitoring samples with duration
	less than 5 minutes, collect the results in a List<String> containing only the
	distinct activity names and write the result in a text file
		 * @param data
		 * For the last task also multiple stream processing instructions were needed. The filter
step is the most important. In order to determine the total number of activities of a given type a
simple count() was used, and in order to determine the number of activities with a smaller time
than 5 minutes a filter() was needed.
		 */
		public static void ActivityFilter(List<MonitoredData> data){
			List<Activity> result = data.stream()	
										.map(MonitoredData::getActivity)
										.filter(d ->  data.stream().filter(p -> (p.getActivity() == d) && (p.getDurationMinsLong() < 5)).count() * 100 / data.stream().filter(p -> p.getActivity() == d).count() > 90)
										.distinct()
										.collect(Collectors.toList());
			writeToFile(ActivityFilter_PATH, result);
	
			System.out.println(result.toString());
		}
		
		/**
		 * 
		 * @param path
		 * @param o
		 */
		public static void writeToFile(String path, Object o){
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
				bw.write(o.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	

}
