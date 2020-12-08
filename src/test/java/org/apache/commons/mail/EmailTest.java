package org.apache.commons.mail;

import static org.junit.Assert.*;

import java.util.Date;

import javax.mail.internet.MimeMultipart;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailTest {
	public static String[] TEST_EMAILS = {"abcdef@gmail.com", "124c@b.com", "fff@c.a"};
	
	EmailConcrete email;
	
	@Before
	public void setUpEmailTest() throws Exception{
		//All the common methods needed for more than one test are added here in @Before to clean the code
		email = new EmailConcrete();
		email.addBcc(TEST_EMAILS);
		email.addCc(TEST_EMAILS[0]);
		email.addReplyTo(TEST_EMAILS[1], "SampleName");
		email.addHeader("NameTest", "ValueTest");
		email.setContent(new MimeMultipart());
		email.setContent(new Object(), "SampleType");
		email.setHostName("TestHostName");
	}
	
	@After
	public void tearDownEmailTest() throws Exception{}
	
	//Simple test for adding an array of emails to BCC
	@Test
	public void testAddBcc() throws Exception{
		assertEquals(3, email.getBccAddresses().size());
	}
	
	//Testing adding one email using addCc()
	@Test
	public void testAddCc() throws Exception{
		assertEquals(1, email.getCcAddresses().size());
	}
	
	//Testing addHeader()
	//Since we are testing for a thrown exception we could use the expected attribute of @Test
	@Test
	public void testAddHeader() throws Exception{
		//First we test a successful run of addHeader() with no empty or null strings
		assertEquals(1, email.headers.size());
		//For more coverage we test for empty argument
		boolean exceptionThrown = false;
		//We try to catch the exception thrown and assert
		//This is one way of asserting for exceptions
		//Another way can be using the "expected" attribute with @Test
		try {
			email.addHeader("", "SampleValue");
		} catch (IllegalArgumentException ex){
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		//Now we try the test again for the second argument for 100% coverage
		exceptionThrown = false;
		try {
			email.addHeader("SampleName", "");
		} catch (IllegalArgumentException ex) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
	}
	
	//Testing addReplyTo()
	@Test
	public void testAddReplyTo() throws Exception{
		assertEquals(1, email.replyList.size());
	}
	
	//Testing buildMimeMessage()
	@Test(expected = EmailException.class)
	public void testBuildMimeMessage() throws Exception{
		//First we initialize all other properties buildMimeMessage() needs in order to run successfully
		email.setSubject("TestSubject");
		email.setFrom(TEST_EMAILS[2]);
		email.addTo(TEST_EMAILS[0]);
		email.setPopBeforeSmtp(true, "SampleHost", "SampleUsername", "SamplePassword");
		email.buildMimeMessage();
		assertNotNull(email.getMimeMessage());
	}
	
	//Simple testing for getHostName
	@Test
	public void testGetHostName() throws Exception{
		assertEquals("TestHostName", email.getHostName());
	}
	
	//Testing other branch of the method for more coverage
	@Test
	public void secondTestGetHostName() throws Exception{
		//getMailSession() automatically initializes email.session if it doesn't exist, it is needed for getHostName()
		email.getMailSession();
		assertNotNull(email.getHostName());
	}
	
	//Testing getMailSession()
	@Test
	public void testGetMailSession() throws Exception{
		//Since most coverage is already done when getMailSession() gets called in secondTestGetHostName(),
		//we set SSLOnConnect = true to have more coverage
		email.setSSLOnConnect(true);
		assertNotNull(email.getMailSession());
	}
	
	//Testing getSentDate()
	@Test
	public void testGetSentDate() throws Exception {
		//First we create a date. The default Date() constructor will automatically set the current date as date
		Date testDate = new Date();
		email.setSentDate(testDate);
		//The getDate() method fits our testing needs even though it is deprecated
		//If we need more advanced/up-to-date testing we can use the Calendar class methods instead
		assertEquals(testDate.getDate(), email.getSentDate().getDate());
	}
	
	//Testing getSocketConnectionTimeout()
	@Test
	public void testGetSockedConnectionTimeout() throws Exception{
		//To function properly the method needs a socketConnectionTimeout value initialized
		email.setSocketConnectionTimeout(10);
		assertEquals(10, email.getSocketConnectionTimeout());
	}
	
	//setFrom() is already covered since it was called for testBuildMimeMessage()
	@Test
	public void testSetFrom() throws Exception{
		email.setFrom(TEST_EMAILS[2]);
		assertEquals(TEST_EMAILS[2], email.getFromAddress().getAddress());
	}
}
