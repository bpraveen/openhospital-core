package org.isf.sms.test;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.sms.model.Sms;
import org.isf.sms.service.SmsOperations;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class Tests {
	private static DbJpaUtil jpa;
	private static TestSms testSms;
	private static TestSmsContext testSmsContext;

	@Autowired
	private SmsOperations smsIoOperation;

	@BeforeClass
	public static void setUpClass() {
		jpa = new DbJpaUtil();
		testSms = new TestSms();
		testSmsContext = new TestSmsContext();
	}

	@Before
	public void setUp() throws OHException {
		jpa.open();
		_saveContext();
	}

	@After
	public void tearDown() throws Exception {
		_restoreContext();
		jpa.flush();
		jpa.close();
	}

	@AfterClass
	public static void tearDownClass() {
	}


	@Test
	public void testSmsGets() {
		try {
			int code = _setupTestSms(false);
			_checksmsIntoDb(code);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testSmsSets() throws OHException {
		int code = _setupTestSms(true);
		_checksmsIntoDb(code);
	}

	@Test
	public void testSmsSaveOrUpdate() throws OHServiceException, OHException {
		Sms sms = testSms.setup(true);
		boolean result = smsIoOperation.saveOrUpdate(sms);
		assertTrue(result);
		_checksmsIntoDb(sms.getSmsId());
	}

	@Test
	public void testSmsGetByID() throws OHException, OHServiceException {
		int code = _setupTestSms(false);
		Sms foundSms = smsIoOperation.getByID(code);
		_checksmsIntoDb(foundSms.getSmsId());
	}

	@Test
	public void testSmsGetAll() throws OHException, OHServiceException {
		Date smsDateStart = new GregorianCalendar(2011, Calendar.OCTOBER, 6).getTime();
		Date smsDateEnd = new GregorianCalendar(2011, Calendar.OCTOBER, 9).getTime();
		int code = _setupTestSms(false);
		Sms foundSms = (Sms) jpa.find(Sms.class, code);
		List<Sms> sms = smsIoOperation.getAll(smsDateStart, smsDateEnd);
		assertEquals(foundSms.getSmsText(), sms.get(0).getSmsText());
	}

	@Test
	public void testSmsGetList() throws OHException, OHServiceException {
		int code = _setupTestSms(false);
		Sms foundSms = (Sms) jpa.find(Sms.class, code);
		List<Sms> sms = smsIoOperation.getList();
		assertEquals(foundSms.getSmsText(), sms.get(0).getSmsText());
	}

	@Test
	public void testIoDeleteSms() throws OHException, OHServiceException {
		int code = _setupTestSms(false);
		Sms foundSms = (Sms) jpa.find(Sms.class, code);
		smsIoOperation.delete(foundSms);

		boolean result = smsIoOperation.isCodePresent(code);
		assertFalse(result);
	}


	@Test
	public void testIoDeleteSmsById() throws OHException, OHServiceException {
		int code = _setupTestSms(false);
		smsIoOperation.deleteById(code);
		boolean result = smsIoOperation.isCodePresent(code);
		assertFalse(result);

	}

	@Test
	public void testIoDeleteSmssById() throws OHServiceException {
		List<Sms> sms = smsIoOperation.getList();
	}


	@Test
	public void testIoDeleteByModuleModuleID() throws OHException, OHServiceException {
		int code = _setupTestSms(false);
		Sms foundSms = (Sms) jpa.find(Sms.class, code);
		smsIoOperation.deleteByModuleModuleID(foundSms.getModule(), foundSms.getModuleID());
		boolean result = smsIoOperation.isCodePresent(code);
		assertFalse(result);
	}


	private void _saveContext() throws OHException {
		testSmsContext.saveAll(jpa);
	}

	private void _restoreContext() throws OHException {
		testSmsContext.deleteNews(jpa);
	}

	private int _setupTestSms(boolean usingSet) throws OHException {
		jpa.beginTransaction();
		Sms sms = testSms.setup(usingSet);
		jpa.persist(sms);
		jpa.commitTransaction();

		return sms.getSmsId();
	}

	private void _checksmsIntoDb(int code) throws OHException {
		Sms foundSms = (Sms) jpa.find(Sms.class, code);
		testSms.check(foundSms);
	}
}