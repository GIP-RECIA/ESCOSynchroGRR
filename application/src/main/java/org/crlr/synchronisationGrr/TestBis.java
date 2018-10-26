package org.crlr.synchronisationGrr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class TestBis {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		
		String extraction = "((.*):)*%UAI(:(.*))*";
		String pattern = extraction.replace("%UAI", "(.*)");
		String chaine = "azert:hello:uiop";
		System.out.println(pattern);
		System.out.println(chaine);
		
		//Trouver le numero du groupe
		Pattern patternC = Pattern.compile(pattern);
		Matcher matcher1 = patternC.matcher(extraction);
		Boolean matchFound = matcher1.find();

		System.out.println();
		
		if (matchFound) {
		    // Get all groups for this match
		    for (int i=0; i<=matcher1.groupCount(); i++) {
		        String groupStr = matcher1.group(i);
		        System.out.println(groupStr);
		    }
		}
		
		System.out.println();
		
		Matcher matcher2 = patternC.matcher(chaine);
		matchFound = matcher2.find();

		if (matchFound) {
		    // Get all groups for this match
		    for (int i=0; i<=matcher2.groupCount(); i++) {
		        String groupStr = matcher2.group(i);
		        System.out.println(groupStr);
		    }
		}

	}

}
