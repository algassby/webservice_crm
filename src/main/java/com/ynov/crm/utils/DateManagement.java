/**
 * 
 */
package com.ynov.crm.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author algas
 *
 */
@Service
@Slf4j
public class DateManagement {

	public boolean dateCompare(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);  
		cal2.setTime(date2); 
	
		long diff = date1.getTime() - date2.getTime();//as given

		long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
		
		log.info(String.valueOf(minutes));
		return (minutes >=1) ? true : false;
		
		
	}
}
