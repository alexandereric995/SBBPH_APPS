<%@ page import="java.io.*,java.util.*,java.security.*,java.text.*" %>
<%@ page import="bph.integrasi.fpx.FPXPkiImplementation"%>

<%			
String fpx_buyerBankBranch=null;
String fpx_buyerBankId=null;
String fpx_buyerIban=null;
String fpx_buyerId=null;
String fpx_buyerName=null;
String fpx_creditAuthCode=null;
String fpx_creditAuthNo=null;
String fpx_debitAuthCode=null;
String fpx_debitAuthNo=null;
String fpx_fpxTxnId=null;
String fpx_fpxTxnTime=null;
String fpx_makerName=null;
String fpx_msgToken=null;
String fpx_msgType=null;
String fpx_sellerExId=null;
String fpx_sellerExOrderNo=null;
String fpx_sellerId=null;
String fpx_sellerOrderNo=null;
String fpx_sellerTxnTime=null;
String fpx_txnAmount=null;
String fpx_txnCurrency=null;
String fpx_checkSum=null;
String fpx_checkSumString=null;
boolean pkivarification=false;
PublicKey  fpxPublicKey=null;

	try {
 // Start receiving response From Fpx
		fpx_buyerBankBranch = request
				.getParameter("fpx_buyerBankBranch");
	fpx_buyerBankId= request.getParameter("fpx_buyerBankId");
	fpx_buyerIban= request.getParameter("fpx_buyerIban");
	fpx_buyerId= request.getParameter("fpx_buyerId");
	fpx_buyerName= request.getParameter("fpx_buyerName");
	fpx_creditAuthCode= request.getParameter("fpx_creditAuthCode");
	fpx_creditAuthNo= request.getParameter("fpx_creditAuthNo");
	fpx_debitAuthCode= request.getParameter("fpx_debitAuthCode");
	fpx_debitAuthNo= request.getParameter("fpx_debitAuthNo");
	fpx_fpxTxnId= request.getParameter("fpx_fpxTxnId");
	fpx_fpxTxnTime= request.getParameter("fpx_fpxTxnTime");
	fpx_makerName= request.getParameter("fpx_makerName");
	fpx_msgToken= request.getParameter("fpx_msgToken");
	fpx_msgType= request.getParameter("fpx_msgType");
	fpx_sellerExId= request.getParameter("fpx_sellerExId");
		fpx_sellerExOrderNo = request
				.getParameter("fpx_sellerExOrderNo");
	fpx_sellerId= request.getParameter("fpx_sellerId");
	fpx_sellerOrderNo= request.getParameter("fpx_sellerOrderNo");
	fpx_sellerTxnTime= request.getParameter("fpx_sellerTxnTime");
	fpx_txnAmount= request.getParameter("fpx_txnAmount");
	fpx_txnCurrency= request.getParameter("fpx_txnCurrency");
	fpx_checkSum= request.getParameter("fpx_checkSum");
	
	// Start verify response From Fpx
		fpx_checkSumString = fpx_buyerBankBranch + "|"
				+ fpx_buyerBankId + "|" + fpx_buyerIban + "|"
				+ fpx_buyerId + "|" + fpx_buyerName + "|"
				+ fpx_creditAuthCode + "|" + fpx_creditAuthNo + "|"
				+ fpx_debitAuthCode + "|" + fpx_debitAuthNo + "|"
				+ fpx_fpxTxnId + "|" + fpx_fpxTxnTime + "|"
				+ fpx_makerName + "|" + fpx_msgToken + "|"
				+ fpx_msgType + "|";
		fpx_checkSumString += fpx_sellerExId + "|"
				+ fpx_sellerExOrderNo + "|" + fpx_sellerId + "|"
				+ fpx_sellerOrderNo + "|" + fpx_sellerTxnTime + "|"
				+ fpx_txnAmount + "|" + fpx_txnCurrency;
		pkivarification = FPXPkiImplementation.verifyData(
				"D:\\SMIExchange\\fpxcert_2015.cer", fpx_checkSumString,
				fpx_checkSum, "SHA1withRSA");
	
	} catch (Exception e) {
		out.println("PARAMETER ERROR");
  }
  
	if (!pkivarification) {
	out.println("CHECKSUM ERROR");
	} else {
		if (fpx_debitAuthCode.equals("00")) {
		//SUCCESSFUL
			out.print("OK");
		} else if (fpx_debitAuthCode.equals("99")) {
		//PENDING AUTHORIZATION
			out.print("PENDING AUTHORIZATION");
		} else {
		//UNSUCCESSFUL
			out.print("UNSUCCESSFUL");
	}							
}	
%> 