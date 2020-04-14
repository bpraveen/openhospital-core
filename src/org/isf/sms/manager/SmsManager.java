package org.isf.sms.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.sms.model.Sms;
import org.isf.sms.service.SmsOperations;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.OHDataValidationException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SmsManager {

	public final static int MAX_LENGTH = 160;
	private final String NUMBER_REGEX = "^\\+?\\d+$"; //$NON-NLS-1$
	
	@Autowired
	private SmsOperations smsOperations;
	
	public SmsManager(){}
	
	/**
	 * Verify if the object is valid for CRUD and return a list of errors, if any
	 * @param sms
	 * @throws OHDataValidationException 
	 */
	protected void validateSms(Sms sms) throws OHDataValidationException {
		List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
		String number = sms.getSmsNumber();
		String text = sms.getSmsText();
		
		if (!number.matches(NUMBER_REGEX)) {
			errors.add(new OHExceptionMessage("numberError", 
	        		MessageBundle.getMessage("angal.sms.pleaseinsertavalidtelephonenumber"), 
	        		OHSeverityLevel.ERROR));
		}
		if (text.isEmpty()) {
			errors.add(new OHExceptionMessage("emptyTextError", 
	        		MessageBundle.getMessage("angal.sms.pleaseinsertatext"), 
	        		OHSeverityLevel.ERROR));
		}
		if(!errors.isEmpty()){
	        throw new OHDataValidationException(errors);
	    }
	}

	public List<Sms> getAll(Date from, Date to) throws OHServiceException {
		return smsOperations.getAll(from, to);
	}

	/**
	 * Save or Update a {@link Sms}. If the sms's text lenght is greater than 
	 * {@code MAX_LENGHT} it will throw a {@code testMaxLenghtError} error if
	 * {@code split} parameter is set to {@code false}
	 * @param smsToSend - the {@link Sms} to save or update
	 * @param split - specify if to split sms's text longer than {@code MAX_LENGHT}
	 * @throws OHServiceException
	 * @return
	 */
	public boolean saveOrUpdate(Sms smsToSend, boolean split) throws OHServiceException  {
		validateSms(smsToSend);
		
		List<Sms> smsList = new ArrayList<Sms>();
		final String text = smsToSend.getSmsText();
		int textLength = text.length();
		if (textLength > MAX_LENGTH && !split) {
			final String message = MessageBundle.getMessage("angal.sms.themessageislongerthen") +
					" " +
					MAX_LENGTH +
					" " +
					MessageBundle.getMessage("angal.sms.chars");
			throw new OHDataValidationException(new OHExceptionMessage("testMaxLenghtError",
					message,
					OHSeverityLevel.ERROR));
			
		} else if (textLength > MAX_LENGTH && split) {
			String[] parts = split(text);
			String number = smsToSend.getSmsNumber();
			Date schedDate = smsToSend.getSmsDateSched();
			
			for (String part : parts) {
				Sms sms = new Sms();
				sms.setSmsNumber(number);
				sms.setSmsDateSched(schedDate);
				sms.setSmsUser(UserBrowsingManager.getCurrentUser());
				sms.setSmsText(part);
				sms.setModule("smsmanager");
				sms.setModuleID(null);
				
				smsList.add(sms);
			}
			
		} else {
			smsList.add(smsToSend);
		}
		return smsOperations.saveOrUpdate(smsList);
	}

	public boolean delete(Sms smsToDelete) throws OHServiceException{
		final int deleteCount = smsOperations.deleteById(smsToDelete.getSmsId());
		return deleteCount == 1;
	}

	public void delete(List<Sms> smsToDelete) throws OHServiceException {
		smsOperations.delete(smsToDelete);
	}

	private String[] split(String text) {
		int len = text.length();
		if (len <= MAX_LENGTH) {
			return new String[]{text};
		}
		
		// Number of parts
	    int nParts = (len + MAX_LENGTH - 1) / MAX_LENGTH;
	    String[] parts = new String[nParts];

	    // Break into parts
	    int offset= 0;
	    int i = 0;
	    while (i < nParts)
	    {
	        parts[i] = text.substring(offset, Math.min(offset + MAX_LENGTH, len));
	        offset += MAX_LENGTH;
	        i++;
	    }
		return parts;
	}
}
