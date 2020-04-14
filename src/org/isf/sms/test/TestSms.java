package org.isf.sms.test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.isf.sms.model.Sms;
import org.isf.utils.exception.OHException;


public class TestSms 
{
	private Date smsDate = new GregorianCalendar(2010, Calendar.OCTOBER, 8).getTime();
	private Date smsDateSched = new GregorianCalendar(2011, Calendar.OCTOBER, 8).getTime();
	private String smsNumber = "TestNumber";	
	private String smsText = "TestText";
	private Date smsDateSent = null;
	private String smsUser = "TestUser";
	private String module = "TestModule";
	private String moduleID = "TestModId";
	 
			
	public Sms setup(boolean usingSet) throws OHException
	{
		Sms sms;
		if (usingSet)
		{
			sms = new Sms();
			_setParameters(sms);
		}
		else
		{
			// Create Sms with all parameters 
			int smsId = 0;
			sms = new Sms(smsId, smsDate, smsDateSched, smsNumber, smsText,
							smsDateSent, smsUser, module, moduleID);
		}
				    	
		return sms;
	}
	
	public void _setParameters(final Sms sms)
	{	
		sms.setModule(module);
		sms.setModuleID(moduleID);
		sms.setSmsDate(smsDate);
		sms.setSmsDateSched(smsDateSched);
		sms.setSmsDateSent(smsDateSent);
		sms.setSmsNumber(smsNumber);
		sms.setSmsText(smsText);
		sms.setSmsUser(smsUser);
	}
	
	public void check(Sms sms)
	{		
    	assertEquals(module, sms.getModule());
    	assertEquals(moduleID, sms.getModuleID());
    	assertEquals(smsDate, sms.getSmsDate());
    	assertEquals(smsDateSched, sms.getSmsDateSched());
    	assertEquals(smsDateSent, sms.getSmsDateSent());
    	assertEquals(smsNumber, sms.getSmsNumber());
    	assertEquals(smsText, sms.getSmsText());
    	assertEquals(smsUser, sms.getSmsUser());
	}
}
