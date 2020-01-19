<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="lebah.db.Db"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.io.*,java.util.*,java.security.*,java.text.*"%>
<%@ page import="bph.integrasi.fpx.MIGSUtil"%>
<%--

/* -----------------------------------------------------------------------------

 Version 3.1

------------------ Disclaimer --------------------------------------------------

Copyright 2004 Dialect Solutions Holdings.  All rights reserved.

This document is provided by Dialect Holdings on the basis that you will treat
it as confidential.

No part of this document may be reproduced or copied in any form by any means
without the written permission of Dialect Holdings.  Unless otherwise expressly
agreed in writing, the information contained in this document is subject to
change without notice and Dialect Holdings assumes no responsibility for any
alteration to, or any error or other deficiency, in this document.

All intellectual property rights in the Document and in all extracts and things
derived from any part of the Document are owned by Dialect and will be assigned
to Dialect on their creation. You will protect all the intellectual property
rights relating to the Document in a manner that is equal to the protection
you provide your own intellectual property.  You will notify Dialect
immediately, and in writing where you become aware of a breach of Dialect's
intellectual property rights in relation to the Document.

The names "Dialect", "QSI Payments" and all similar words are trademarks of
Dialect Holdings and you must not use that name or any similar name.

Dialect may at its sole discretion terminate the rights granted in this
document with immediate effect by notifying you in writing and you will
thereupon return (or destroy and certify that destruction to Dialect) all
copies and extracts of the Document in its possession or control.

Dialect does not warrant the accuracy or completeness of the Document or its
content or its usefulness to you or your merchant customers.   To the extent
permitted by law, all conditions and warranties implied by law (whether as to
fitness for any particular purpose or otherwise) are excluded.  Where the
exclusion is not effective, Dialect limits its liability to $100 or the
resupply of the Document (at Dialect's option).

Data used in examples and sample data files are intended to be fictional and
any resemblance to real persons or companies is entirely coincidental.

Dialect does not indemnify you or any third party in relation to the content or
any use of the content as contemplated in these terms and conditions.

Mention of any product not owned by Dialect does not constitute an endorsement
of that product.

This document is governed by the laws of New South Wales, Australia and is
intended to be legally binding.

-------------------------------------------------------------------------------
 
Following is a copy of the disclaimer / license agreement provided by RSA:

Copyright (C) 1991-2, RSA Data Security, Inc. Created 1991. All rights reserved.

License to copy and use this software is granted provided that it is identified
as the "RSA Data Security, Inc. MD5 Message-Digest Algorithm" in all material 
mentioning or referencing this software or this function.

License is also granted to make and use derivative works provided that such 
works are identified as "derived from the RSA Data Security, Inc. MD5 
Message-Digest Algorithm" in all material mentioning or referencing the 
derived work.

RSA Data Security, Inc. makes no representations concerning either the 
merchantability of this software or the suitability of this software for any 
particular purpose. It is provided "as is" without express or implied warranty 
of any kind.

These notices must be retained in any copies of any part of this documentation 
and/or software.

-------------------------------------------------------------------------------- 

This example assumes that a form has been sent to this example with the
required fields. The example then processes the command and displays the
receipt or error to a HTML page in the users web browser.

*****
NOTE:
*****
 
  For jdk1.2, 1.3
  * Must have jsse.jar, jcert.jar and jnet.jar in your classpath
  * Best approach is to make them installed extensions - 
    i.e. put them in the jre/lib/ext directory.

  For jdk1.4 (jsse is already part of default installation - should run fine)

--------------------------------------------------------------------------------

 @author Dialect Payment Solutions Pty Ltd Group 

------------------------------------------------------------------------------*/

--%>
<%@ page import="java.util.List,
                 java.util.ArrayList,
                 java.util.Collections,
                 java.util.Iterator,
                 java.util.Enumeration,
                 java.security.MessageDigest,
                 java.util.Map,
                 java.net.URLEncoder,
                 java.util.HashMap"%>

<%! // Define Constants
    // ****************
    /* Note:
       ----
       In a proper production environment, only the retrieving of all the input 
       parameters and the HTML output would be in this file. The following 
       constants and all other methods would be contained in a separate helper
       class so that users could not gain access to these values. */
    
    // This is secret for encoding the MD5 hash
    // This secret will vary from merchant to merchant
    // static final String SECURE_SECRET = "your-secure-hash-secret";
    //static final String SECURE_SECRET = "5F0A7CB1DDA880A31651EDBAED51BC2A";
    static final String SECURE_SECRET = "C4342B9D0330CDD09FB7337EAF95FDA8";
%>    
<%!    // This is an array for creating hex chars
    static final char[] HEX_TABLE = new char[] {
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        
//  ----------------------------------------------------------------------------

   /**
    * This method is for sorting the fields and creating an MD5 secure hash.
    *
    * @param fields is a map of all the incoming hey-value pairs from the VPC
    * @return is the hash being returned for comparison to the incoming hash
    */
    String hashAllFields(Map fields) {
        
        // create a list and sort it
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        
        // create a buffer for the md5 input and add the secure secret first
        StringBuffer buf = new StringBuffer();
        buf.append(SECURE_SECRET);
        
        // iterate through the list and add the remaining field values
        Iterator itr = fieldNames.iterator();
        
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                buf.append(fieldValue);
            }
        }
             
        MessageDigest md5 = null;
        byte[] ba = null;
        
        // create the md5 hash and UTF-8 encode it
        try {
            md5 = MessageDigest.getInstance("MD5");
            ba = md5.digest(buf.toString().getBytes("UTF-8"));
         } catch (Exception e) {} // wont happen
       
       return hex(ba);
    
    } // end hashAllFields()
//  ----------------------------------------------------------------------------

    /*
    * This method takes a byte array and returns a string of its contents
    *
    * @param input - byte array containing the input data
    * @return String containing the output String
    */
    static String hex(byte[] input) {
        // create a StringBuffer 2x the size of the hash array
        StringBuffer sb = new StringBuffer(input.length * 2);

        // retrieve the byte array data, convert it to hex
        // and add it to the StringBuffer
        for (int i = 0; i < input.length; i++) {
            sb.append(HEX_TABLE[(input[i] >> 4) & 0xf]);
            sb.append(HEX_TABLE[input[i] & 0xf]);
        }
        return sb.toString();
    }

//  ----------------------------------------------------------------------------

   /*
    * This method takes a data String and returns a predefined value if empty
    * If data Sting is null, returns string "No Value Returned", else returns input
    *
    * @param in String containing the data String
    * @return String containing the output String
    */
    private static String null2unknown(String in) {
        if (in == null || in.length() == 0) {
            return "No Value Returned";
        } else {
            return in;
        }
    } // null2unknown()
    
//  ----------------------------------------------------------------------------
    
   /*
    * This function uses the returned status code retrieved from the Digital
    * Response and returns an appropriate description for the code
    *
    * @param vResponseCode String containing the vpc_TxnResponseCode
    * @return description String containing the appropriate description
    */ 
    String getResponseDescription(String vResponseCode) {

        String result = "";

        // check if a single digit response code
        if (vResponseCode.length() != 1) {
        
            // Java cannot switch on a string so turn everything to a char
            char input = vResponseCode.charAt(0);

            switch (input){
                case '0' : result = "Transaction Successful"; break;
                case '1' : result = "Unknown Error"; break;
                case '2' : result = "Bank Declined Transaction"; break;
                case '3' : result = "No Reply from Bank"; break;
                case '4' : result = "Expired Card"; break;
                case '5' : result = "Insufficient Funds"; break;
                case '6' : result = "Error Communicating with Bank"; break;
                case '7' : result = "Payment Server System Error"; break;
                case '8' : result = "Transaction Type Not Supported"; break;
                case '9' : result = "Bank declined transaction (Do not contact Bank)"; break;
                case 'A' : result = "Transaction Aborted"; break;
                case 'C' : result = "Transaction Cancelled"; break;
                case 'D' : result = "Deferred transaction has been received and is awaiting processing"; break;
                case 'F' : result = "3D Secure Authentication failed"; break;
                case 'I' : result = "Card Security Code verification failed"; break;
                case 'L' : result = "Shopping Transaction Locked (Please try the transaction again later)"; break;
                case 'N' : result = "Cardholder is not enrolled in Authentication Scheme"; break;
                case 'P' : result = "Transaction has been received by the Payment Adaptor and is being processed"; break;
                case 'R' : result = "Transaction was not processed - Reached limit of retry attempts allowed"; break;
                case 'S' : result = "Duplicate SessionID (OrderInfo)"; break;
                case 'T' : result = "Address Verification Failed"; break;
                case 'U' : result = "Card Security Code Failed"; break;
                case 'V' : result = "Address Verification and Card Security Code Failed"; break;
                case '?' : result = "Transaction status is unknown"; break;
                default  : result = "Unable to be determined";
            }
            
            return result;
        } else {
            return "No Value Returned";
        }
    } // getResponseDescription()

//  ----------------------------------------------------------------------------
   
   /**
    * This function uses the QSI AVS Result Code retrieved from the Digital
    * Receipt and returns an appropriate description for this code.
    *
    * @param vAVSResultCode String containing the vpc_AVSResultCode
    * @return description String containing the appropriate description
    */
    private String displayAVSResponse(String vAVSResultCode) {

        String result = "";
        if (vAVSResultCode != null || vAVSResultCode.length() == 0) {

            if (vAVSResultCode.equalsIgnoreCase("Unsupported") || vAVSResultCode.equalsIgnoreCase("No Value Returned")) {
                result = "AVS not supported or there was no AVS data provided";
            } else {
                // Java cannot switch on a string so turn everything to a char
                char input = vAVSResultCode.charAt(0);

                switch (input){
                    case 'X' : result = "Exact match - address and 9 digit ZIP/postal code"; break;
                    case 'Y' : result = "Exact match - address and 5 digit ZIP/postal code"; break;
                    case 'S' : result = "Service not supported or address not verified (international transaction)"; break;
                    case 'G' : result = "Issuer does not participate in AVS (international transaction)"; break;
                    case 'A' : result = "Address match only"; break;
                    case 'W' : result = "9 digit ZIP/postal code matched, Address not Matched"; break;
                    case 'Z' : result = "5 digit ZIP/postal code matched, Address not Matched"; break;
                    case 'R' : result = "Issuer system is unavailable"; break;
                    case 'U' : result = "Address unavailable or not verified"; break;
                    case 'E' : result = "Address and ZIP/postal code not provided"; break;
                    case 'N' : result = "Address and ZIP/postal code not matched"; break;
                    case '0' : result = "AVS not requested"; break;
                    default  : result = "Unable to be determined";
                }
            }
        } else {
            result = "null response";
        }
        return result;
    }

//  ----------------------------------------------------------------------------
   
   /**
    * This function uses the QSI CSC Result Code retrieved from the Digital
    * Receipt and returns an appropriate description for this code.
    *
    * @param vCSCResultCode String containing the vpc_CSCResultCode
    * @return description String containing the appropriate description
    */
    private String displayCSCResponse(String vCSCResultCode) {

        String result = "";
        if (vCSCResultCode != null || vCSCResultCode.length() == 0) {

            if (vCSCResultCode.equalsIgnoreCase("Unsupported")  || vCSCResultCode.equalsIgnoreCase("No Value Returned")) {
                result = "CSC not supported or there was no CSC data provided";
            } else {
                // Java cannot switch on a string so turn everything to a char
                char input = vCSCResultCode.charAt(0);

                switch (input){
                    case 'M' : result = "Exact code match"; break;
                    case 'S' : result = "Merchant has indicated that CSC is not present on the card (MOTO situation)"; break;
                    case 'P' : result = "Code not processed"; break;
                    case 'U' : result = "Card issuer is not registered and/or certified"; break;
                    case 'N' : result = "Code invalid or not matched"; break;
                    default  : result = "Unable to be determined";
                }
            }

        } else {
            result = "null response";
        }
        return result;
    }

//  ----------------------------------------------------------------------------
   
   /**
    * This method uses the 3DS verStatus retrieved from the 
    * Response and returns an appropriate description for this code.
    *
    * @param vpc_VerStatus String containing the status code
    * @return description String containing the appropriate description
    */
    private String getStatusDescription(String vStatus) {

        String result = "";
        if (vStatus != null && !vStatus.equals("")) {

            if (vStatus.equalsIgnoreCase("Unsupported")  || vStatus.equals("No Value Returned")) {
                result = "3DS not supported or there was no 3DS data provided";
            } else {
            
                // Java cannot switch on a string so turn everything to a character
                char input = vStatus.charAt(0);

                switch (input){
                    case 'Y'  : result = "The cardholder was successfully authenticated."; break;
                    case 'E'  : result = "The cardholder is not enrolled."; break;
                    case 'N'  : result = "The cardholder was not verified."; break;
                    case 'U'  : result = "The cardholder's Issuer was unable to authenticate due to some system error at the Issuer."; break;
                    case 'F'  : result = "There was an error in the format of the request from the merchant."; break;
                    case 'A'  : result = "Authentication of your Merchant ID and Password to the ACS Directory Failed."; break;
                    case 'D'  : result = "Error communicating with the Directory Server."; break;
                    case 'C'  : result = "The card type is not supported for authentication."; break;
                    case 'S'  : result = "The signature on the response received from the Issuer could not be validated."; break;
                    case 'P'  : result = "Error parsing input from Issuer."; break;
                    case 'I'  : result = "Internal Payment Server system error."; break;
                    default   : result = "Unable to be determined"; break;
                }
            }
        } else {
            result = "null response";
        }
        return result;
    }

//  ----------------------------------------------------------------------------
%><%

    // *******************************************
    // START OF MAIN PROGRAM
    // *******************************************

    // The Page does a display to a browser

    // retrieve all the incoming parameters into a hash map
   Map fields = new HashMap();
    for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
        String fieldName = (String) e.nextElement();
        String fieldValue = request.getParameter(fieldName);
        if ((fieldValue != null) && (fieldValue.length() > 0)) {
            fields.put(fieldName, fieldValue);
        }
    }
    
/*  If there has been a merchant secret set then sort and loop through all the
    data in the Virtual Payment Client response. while we have the data, we can
    append all the fields that contain values (except the secure hash) so that
    we can create a hash and validate it against the secure hash in the Virtual
    Payment Client response.

    NOTE: If the vpc_TxnResponseCode in not a single character then
    there was a Virtual Payment Client error and we cannot accurately validate
    the incoming data from the secure hash. */

    // remove the vpc_TxnResponseCode code from the response fields as we do not 
    // want to include this field in the hash calculation
    String vpc_Txn_Secure_Hash = null2unknown((String) fields.remove("vpc_SecureHash"));
    String hashValidated = null;

    // defines if error message should be output
    boolean errorExists = false;
    
    if (SECURE_SECRET != null && SECURE_SECRET.length() > 0 && 
        (fields.get("vpc_TxnResponseCode") != null || fields.get("vpc_TxnResponseCode") != "No Value Returned")) {
        
        // create secure hash and append it to the hash map if it was created
        // remember if SECURE_SECRET = "" it wil not be created
        String secureHash = hashAllFields(fields);
    
        // Validate the Secure Hash (remember MD5 hashes are not case sensitive)
        if (vpc_Txn_Secure_Hash.equalsIgnoreCase(secureHash)) {
            // Secure Hash validation succeeded, add a data field to be 
            // displayed later.
            hashValidated = "<font color='#00AA00'><strong>CORRECT</strong></font>";
        } else {
            // Secure Hash validation failed, add a data field to be
            // displayed later.
            errorExists = true;
            hashValidated = "<font color='#FF0066'><strong>INVALID HASH</strong></font>";
        }
    } else {
        // Secure Hash was not validated, 
        hashValidated = "<font color='orange'><strong>Not Calculated - No 'SECURE_SECRET' present.</strong></font>";
    }

    // Extract the available receipt fields from the VPC Response
    // If not present then let the value be equal to 'Unknown'
    // Standard Receipt Data
    String title           = null2unknown((String)fields.get("Title"));
    String againLink       = null2unknown((String)fields.get("AgainLink"));
    String amount          = null2unknown((String)fields.get("vpc_Amount"));
    String locale          = null2unknown((String)fields.get("vpc_Locale"));
    String batchNo         = null2unknown((String)fields.get("vpc_BatchNo"));
    String command         = null2unknown((String)fields.get("vpc_Command"));
    String message         = null2unknown((String)fields.get("vpc_Message"));
    String version         = null2unknown((String)fields.get("vpc_Version"));
    String cardType        = null2unknown((String)fields.get("vpc_Card"));
    String orderInfo       = null2unknown((String)fields.get("vpc_OrderInfo"));
    String receiptNo       = null2unknown((String)fields.get("vpc_ReceiptNo"));
    String merchantID      = null2unknown((String)fields.get("vpc_Merchant"));
    String merchTxnRef     = null2unknown((String)fields.get("vpc_MerchTxnRef"));
    String authorizeID     = null2unknown((String)fields.get("vpc_AuthorizeId"));
    String transactionNo   = null2unknown((String)fields.get("vpc_TransactionNo"));
    String acqResponseCode = null2unknown((String)fields.get("vpc_AcqResponseCode"));
    String txnResponseCode = null2unknown((String)fields.get("vpc_TxnResponseCode"));

    // CSC Receipt Data
    String vCSCResultCode  = null2unknown((String)fields.get("vpc_CSCResultCode"));
    String vCSCRequestCode = null2unknown((String)fields.get("vpc_CSCRequestCode"));
    String vACQCSCRespCode = null2unknown((String)fields.get("vpc_AcqCSCRespCode"));
    
    // AVS Receipt Data
    String vAVS_City       = null2unknown((String)fields.get("vpc_AVS_City"));
    String vAVS_Country    = null2unknown((String)fields.get("vpc_AVS_Country"));
    String vAVS_Street01   = null2unknown((String)fields.get("vpc_AVS_Street01"));
    String vAVS_PostCode   = null2unknown((String)fields.get("vpc_AVS_PostCode"));
    String vAVS_StateProv  = null2unknown((String)fields.get("vpc_AVS_StateProv"));
    String vAVSResultCode  = null2unknown((String)fields.get("vpc_AVSResultCode"));
    String vAVSRequestCode = null2unknown((String)fields.get("vpc_AVSRequestCode"));
    String vACQAVSRespCode = null2unknown((String)fields.get("vpc_AcqAVSRespCode"));

    
    // 3-D Secure Data
    String transType3DS       = null2unknown((String)fields.get("vpc_VerType"));
    String verStatus3DS       = null2unknown((String)fields.get("vpc_VerStatus"));
    String token3DS           = null2unknown((String)fields.get("vpc_VerToken"));
    String secureLevel3DS  = null2unknown((String)fields.get("vpc_VerSecurityLevel"));
    String enrolled3DS       = null2unknown((String)fields.get("vpc_3DSenrolled"));
    String xid3DS           = null2unknown((String)fields.get("vpc_3DSXID"));
    String eci3DS           = null2unknown((String)fields.get("vpc_3DSECI"));
    String status3DS       = null2unknown((String)fields.get("vpc_3DSstatus"));
    
    String error = "";
    String statusTransaksi = "";
    // Show this page as an error page if error condition
    if (txnResponseCode.equals("7") || txnResponseCode.equals("No Value Returned") || errorExists) {
        error = "Error ";
    }

    //FOR TESTING PURPOSE ONLY-----------------------
    //message="Approved";
	//session.setAttribute("sesIdPermohonan","2605794639254020");
	//session.setAttribute("sesModul","IR");
	//-----------------------------------------
	
    // FINISH TRANSACTION - Process the VPC Response Data
    // =====================================================
    // For the purposes of demonstration, we simply display the Result fields on a
    // web page.
%>  <!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
    <html>
    <head><title><%=title%> - VPC Response <%=error%>Page</title>
        <meta http-equiv='Content-Type' content='text/html, charset=iso-8859-1'>
        <style type='text/css'>
            <!--
            h1       { font-family:Arial,sans-serif; font-size:20pt; font-weight:600; margin-bottom:0.1em; color:#08185A;}
            h2       { font-family:Arial,sans-serif; font-size:14pt; font-weight:100; margin-top:0.1em; color:#08185A;}
            h2.co    { font-family:Arial,sans-serif; font-size:24pt; font-weight:100; margin-top:0.1em; margin-bottom:0.1em; color:#08185A}
            h3       { font-family:Arial,sans-serif; font-size:16pt; font-weight:100; margin-top:0.1em; margin-bottom:0.1em; color:#08185A}
            h3.co    { font-family:Arial,sans-serif; font-size:16pt; font-weight:100; margin-top:0.1em; margin-bottom:0.1em; color:#FFFFFF}
            body     { font-family:Verdana,Arial,sans-serif; font-size:10pt; background-color:#FFFFFF; color:#08185A}
            th       { font-family:Verdana,Arial,sans-serif; font-size:8pt; font-weight:bold; background-color:#E1E1E1; padding-top:0.5em; padding-bottom:0.5em;  color:#08185A}
            tr       { height:25px; }
            .shade   { height:25px; background-color:#E1E1E1 }
            .title   { height:25px; background-color:#C1C1C1 }
            td       { font-family:Verdana,Arial,sans-serif; font-size:8pt;  color:#08185A }
            td.red   { font-family:Verdana,Arial,sans-serif; font-size:8pt;  color:#FF0066 }
            td.green { font-family:Verdana,Arial,sans-serif; font-size:8pt;  color:#008800 }
            p        { font-family:Verdana,Arial,sans-serif; font-size:10pt; color:#FFFFFF }
            p.blue   { font-family:Verdana,Arial,sans-serif; font-size:7pt;  color:#08185A }
            p.red    { font-family:Verdana,Arial,sans-serif; font-size:7pt;  color:#FF0066 }
            p.green  { font-family:Verdana,Arial,sans-serif; font-size:7pt;  color:#008800 }
            div.bl   { font-family:Verdana,Arial,sans-serif; font-size:7pt;  color:#C1C1C1 }
            div.red  { font-family:Verdana,Arial,sans-serif; font-size:7pt;  color:#FF0066 }
            li       { font-family:Verdana,Arial,sans-serif; font-size:8pt;  color:#FF0066 }
            input    { font-family:Verdana,Arial,sans-serif; font-size:8pt;  color:#08185A; background-color:#E1E1E1; font-weight:bold }
            select   { font-family:Verdana,Arial,sans-serif; font-size:8pt;  color:#08185A; background-color:#E1E1E1; font-weight:bold; }
            textarea { font-family:Verdana,Arial,sans-serif; font-size:8pt;  color:#08185A; background-color:#E1E1E1; font-weight:normal; scrollbar-arrow-color:#08185A; scrollbar-base-color:#E1E1E1 }
.style1 {
	font-size: 14pt;
	font-weight: bold;
	color: #FFFFFF;
}
            -->
        </style>
    </head>
    <body>
    
    <center>
		<table border="0" cellpadding="0" cellspacing="0" width="722">
		    <tbody>
		      	<tr>
		        	<td colspan="3" align="left">
		        		<table style="color: #ffffff; background:#127BA3;border: 1px solid rgb(18, 123, 163);" cellpadding="0" cellspacing="0" height="111" width="722" >
		            		<tbody>
		              			<tr>	
		                			<td align="center"><span class="style1">SEMAKAN TRANSAKSI KREDIT KAD - SBBPH</span></td>
	              			  </tr>
		            		</tbody>
		          		</table>
				  	</td>
		    	</tr>
				<tr>
        			<td style="padding-left: 1px; padding-right: 1px;" align="left" valign="top" colspan="3">
						<table bgcolor="#FFFFFF" border="0" cellpadding="0" cellspacing="5" width="100%" height="100%">
		  					<tbody>
              					<tr>
                					<td height="25" valign="top">
										<p align="center" class="normal" style="color: #000000;">Terima kasih kerana menggunakan perkhidmatan ePayment kami!</p>
               						  <p>&nbsp;</p>
                  					</td>
                  				</tr>		
	    					</tbody>
	    				</table>
	    			</td>
	    		</tr>
  		        <tr>
		            <td align="left" colspan="3"><div align="center"><strong><b>Butiran Transaksi</b></strong></div></td>
		        </tr>
  		        <tr>
		            <td align="left" colspan="3" height="15px"></td>
		        </tr>
  		        <!-- <tr>
		            <td align="left"><strong><i>Merchant Transaction Reference</i></strong></td>
		            <td>:</td>
		            <td><%=merchTxnRef%></td>
		        </tr>
		        <tr>
		            <td align="left"><strong><i>Merchant ID</i></strong></td>
		            <td>:</td>
		            <td><%=merchantID%></td>
		        </tr> -->	
                <tr>                  
		            <td align="left"><strong><i>Info / Butiran</i></strong></td>
		            <td>:</td>
		            <td><%=orderInfo%></td>
		        </tr>
		        <tr>
		            <td align="left"><strong><i>Amaun Transaksi</i></strong></td>
		            <td>:</td>
		            <% 
		            	String amaun1 = amount.substring(0, amount.length()-2); 
		            	String amaun2 = amount.substring(amount.length()-2);
		            	String realAmaun = "";
		            	if (amount.length() > 2) {
		            		realAmaun = amaun1 + "." + amaun2;	
		            	} else {
		            		realAmaun = "0." + amaun2;
		            	}
		            	
		            	session.setAttribute("amaun", realAmaun);
		            %>
		            <td>RM<%=realAmaun%></td>
		        </tr>
		        <tr>
		            <td align="left"><strong><i>Tarikh Transaksi</i></strong></td>
		            <td>:</td>
		            
		            <td> <%
					  //				SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		            			SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a z");
		            
									sdf.setLenient(false);
									out.print(sdf.format(new Date()));
								%></td>
		        </tr>	
		        <tr>                  
		            <td align="left"><strong><i>Mesej / Status</i></strong></td>
		            <td>:</td>
         			<td><% { 
							if ( message.equals("Approved") )
							{    														
								statusTransaksi = "BERJAYA";
								out.println(statusTransaksi);	
								session.setAttribute("statusTransaksi", statusTransaksi);
							}
							else if ( message.equals("Declined") )
							{
								statusTransaksi = "BELUM DISAHKAN";
								out.println(statusTransaksi);								
								session.setAttribute("statusTransaksi", statusTransaksi);
							}
							else
							{
								statusTransaksi = "TIDAK BERJAYA";
								out.println(statusTransaksi);								
								session.setAttribute("statusTransaksi", statusTransaksi);
							}
							//SIMPAN TO TABLE migs_record
							Db db = null;
							Connection conn = null;
							String sql = "";
							String id_tempahan=(String)session.getAttribute("sesIdPermohonan");
							try {
							    db = new Db();
							    conn = db.getConnection();
							    Statement stmt = db.getStatement();								    
								sql="INSERT INTO migs_records(vpc_Amount,vpc_Locale,vpc_BatchNo,vpc_Command,vpc_Message,vpc_Version,vpc_Card,vpc_OrderInfo,vpc_ReceiptNo,vpc_Merchant,vpc_MerchTxnRef,vpc_AuthorizeId,vpc_TransactionNo,vpc_AcqResponseCode,vpc_TxnResponseCode)";
								sql+="VALUES";
								sql+="('"+amount+"','"+locale+"','"+batchNo+"','"+command+"','"+message+"','"+version+"','"+cardType+"','"+orderInfo+"','"+receiptNo+"','"+merchantID+"','"+merchTxnRef+"','"+authorizeID+"','"+transactionNo+"','"+acqResponseCode+"','"+txnResponseCode+"')";									    
								stmt.executeUpdate(sql);
							} catch ( Exception ex ) {
							   ex.printStackTrace();
							} finally {
								db.close();
							}
						} 
						%>
					</td>
		        </tr>	
		        <tr>
		            <td align="left"><strong><i>Nombor Resit</i></strong></td>
		            <td>:</td>
		            <td><%=receiptNo%></td>
		        </tr>
                <tr>                  
		            <td align="left"><strong><i>Nombor Transaksi</i></strong></td>
		            <td>:</td>
		            <td><%=transactionNo%></td>
		        </tr>
		        <tr>                  
		            <td align="left"><strong><i>Jenis Kad</i></strong></td>
		            <td>:</td>
		            <% if ( cardType.equals("MC") ) { %>
		            	<td>MASTER CARD</td>
		            <% } else if ( cardType.equals("VC") ) { %>
		            	<td>VISA</td>
		            <% } else { %>
		            	<td>OTHERS (<%=cardType%>)</td>            	
		            <% } %>
		        </tr>	
		        <tr>
		        	<td colspan="3">
		        		<hr />
		        	</td>
		        </tr>	        
	    	</tbody>
	    </table>
    </center>
							<%
								String idm=null;
								String flag=null;
								String peranan=null;
								String urlReturn=null;
								
								try{
									idm=(String) session.getAttribute("sesIdPermohonan");
									flag=(String) session.getAttribute("sesModul");
									peranan=(String) session.getAttribute("sesRole");
									urlReturn=""+session.getAttribute("returnlink");
									session.setAttribute("sesReturnFlag",statusTransaksi);
									
									if ("UTIL".equals(flag)) {
										
										if ("Approved".equals(message))
										{	
											MIGSUtil migsUtil=new MIGSUtil(session);
											migsUtil.UTILsimpanMIGS(idm);
										}
									}
									
									if ("IR".equals(flag)) {
										
										if ("Approved".equals(message))
										{	
											MIGSUtil migsUtil=new MIGSUtil(session);
											migsUtil.RPPsimpanMIGS(idm);
										}	
									}
									
									if ("LONDON".equals(flag)) {
										
										if ("Approved".equals(message))
										{	
											MIGSUtil migsUtil=new MIGSUtil(session);
											migsUtil.RPPsimpanMIGS(idm);
										}	
									}
									
								} catch(Exception ex){
								  	out.println("<HR><H3>Error :"+ex);			
								}
							%>			
								<center>
								<p><a href="<%=urlReturn%>" style="text-decoration: none;"><input
									type="button" value="Kembali" /></a>
									<input type="button" style="text-decoration: none;" value="Cetak" onClick="javascript:window.print()"/>
								</p>
								
								</center>
    
    </body>
    </html>
