package com.electronicshop.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class BdContants {

	public static final String BD = "bd";
	
	public static final Map<String, String> mapOfStates = new HashMap<String, String>(){
		{
			put("CO", "Comilla");
			put("DH", "Dhaka");
			put("CH", "Chattogram");
            put("CA", "Chadpur");
            put("NH", "Nohakhali");
            put("RJ", "Rajshashi");
            put("RA", "Rangpur");
            put("MY", "Maymansing");
            put("NR", "Norshindhi");
       
		}
	};
	
	
	
	
	
	public static List<String> listofStatesCode= new ArrayList<String>(mapOfStates.keySet());
	public static List<String> listOfStateName = new ArrayList<String>(mapOfStates.values());
	
	
}
