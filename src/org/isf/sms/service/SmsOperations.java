package org.isf.sms.service;

import java.util.ArrayList;

// Generated 31-gen-2014 15.39.04 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.List;

import org.isf.sms.model.Sms;
import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @see org.isf.sms.model.Sms
 * @author Mwithi
 */
@Service
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
public class SmsOperations {

	@Autowired
	private SmsIoOperationRepository repository;
	
	/**
	 * 
	 */
	public SmsOperations() {}
	
	/**
	 * Save or Update a {@link Sms}
	 * @param sms - the {@link Sms} to save or update
	 * @return <code>true</code> if data has been saved, <code>false</code> otherwise. 
	 * @throws OHServiceException 
	 */
	public boolean saveOrUpdate(final Sms sms) throws OHServiceException
	{
		final Sms savedSms = repository.save(sms);
		return (savedSms != null);
	}
	
	/**
	 * Save or Update a list of {@link Sms}s
	 * @param smsList - the list of {@link Sms} to save or update
	 * @return <code>true</code> if data has been saved, <code>false</code> otherwise.
	 */
	public boolean saveOrUpdate(final List<Sms> smsList) {
		final List<Sms> savedSms = repository.save(smsList);
		return (savedSms != null);
	}
	
	/**
	 * Returns a {@link Sms} with specified ID
	 * @param ID - sms ID
	 * @return sms - the sms with specified ID
	 * @throws OHServiceException 
	 */
	public Sms getByID(final int ID) throws OHServiceException
	{
		return repository.findOne(ID);
	}
	
	/**
	 * Returns the list of all {@link Sms}s, sent and not sent, between the two dates
	 * @return smsList - the list of {@link Sms}s
	 * @throws OHServiceException 
	 */
	public List<Sms> getAll(final Date dateFrom, final Date dateTo) throws OHServiceException
	{
		return new ArrayList<Sms>(repository.findAllWhereBetweenDatesByOrderDate(dateFrom, dateTo));
	}
	
	/**
	 * Returns the list of not sent {@link Sms}s between the two dates
	 * @return smsList - the list of {@link Sms}s
	 * @throws OHServiceException 
	 */
	public List<Sms> getList(final Date dateFrom,final Date dateTo) throws OHServiceException
	{
		return new ArrayList<Sms>(repository.findAllWhereSentNotNullBetweenDatesByOrderDate(dateFrom, dateTo));
	}
	
	/**
	 * Returns the list of not sent {@link Sms}s
	 * @return smsList - the list of {@link Sms}s
	 * @throws OHServiceException 
	 */
	public List<Sms> getList() throws OHServiceException 
	{
		return new ArrayList<Sms>(repository.findAllWhereSentNotNullByOrderDate());
	}
	
	/**
	 * Delete the specified {@link Sms}
	 * @param sms - the {@link Sms}s to delete
	 * @throws OHServiceException
	 * @return
	 */
	public void delete(final Sms sms) throws OHServiceException
	{		
		repository.delete(sms);
	}

	/**
	 * Delete the specified list of {@link Sms}
	 * @param smsList - the list of {@link Sms}s to delete
	 * @throws OHServiceException 
	 */
	public void delete(final List<Sms> smsList) throws OHServiceException
	{		
		repository.delete(smsList);
	}

	public int deleteById(final int id) throws OHServiceException{
		return repository.deleteById(String.valueOf(id));
	}

	/**
	 * Delete the specified {@link Sms}s if not already sent
	 * @param module - the module name which generated the {@link Sms}s
	 * @param moduleID - the module ID within its generated {@link Sms}s
	 * @throws OHServiceException 
	 */
	public void deleteByModuleModuleID(final String module, final String moduleID) throws OHServiceException
	{
		repository.deleteWhereModuleAndId(module, moduleID);
	}

	/**
	 * checks if the code is already in use
	 *
	 * @param code - the Sms code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean isCodePresent(final Integer code) throws OHServiceException
	{
		return repository.exists(code);
	}
}
