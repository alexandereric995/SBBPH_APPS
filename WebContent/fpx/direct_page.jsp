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

try
{	
 // Start receiving response From Fpx
	fpx_buyerBankBranch= request.getParameter("fpx_buyerBankBranch");
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
	fpx_sellerExOrderNo= request.getParameter("fpx_sellerExOrderNo");
	fpx_sellerId= request.getParameter("fpx_sellerId");
	fpx_sellerOrderNo= request.getParameter("fpx_sellerOrderNo");
	fpx_sellerTxnTime= request.getParameter("fpx_sellerTxnTime");
	fpx_txnAmount= request.getParameter("fpx_txnAmount");
	fpx_txnCurrency= request.getParameter("fpx_txnCurrency");
	fpx_checkSum= request.getParameter("fpx_checkSum");
	
	// Start verify response From Fpx
	fpx_checkSumString=fpx_buyerBankBranch+"|"+fpx_buyerBankId+"|"+fpx_buyerIban+"|"+fpx_buyerId+"|"+fpx_buyerName+"|"+fpx_creditAuthCode+"|"+fpx_creditAuthNo+"|"+fpx_debitAuthCode+"|"+fpx_debitAuthNo+"|"+fpx_fpxTxnId+"|"+fpx_fpxTxnTime+"|"+fpx_makerName+"|"+fpx_msgToken+"|"+fpx_msgType+"|";
	fpx_checkSumString+=fpx_sellerExId+"|"+fpx_sellerExOrderNo+"|"+fpx_sellerId+"|"+fpx_sellerOrderNo+"|"+fpx_sellerTxnTime+"|"+fpx_txnAmount+"|"+fpx_txnCurrency;
	//fpx_checkSumString="7DC574A1D32F3F1165260F08A226FE35F98448147B5AE029DDC51F7CEDEF9BDB34C632F5A34511748A872658E1060CD7723897E2C9CFCCAC8C101FDB4413F5FBA0C12985A68B8B5949D74CB2FB870ED58BEA705419D076465A23036B2C0C46FA1367C66ACFA0E6765F65A5D9795EBE6FA754A02A0F1AF2C6B9A955B4A28D19865055BD9248C16FE40922EBC282C29DA1385B5A7D8ADDE496212EF27703C28EB8B431EA1A29EAEEAE23B3739FA52D382C2D02A26C51326AE2BB3A27432D9712AB9662AA8674B8A0DAF6F1F0CDFB0B64CA0CAC7757D7BE68FEC23366F92BE67020DED5747CDDA78F1B7B37625BBDAEC2CD5290D10C3D7CEB8144C5F62A390F1C86";
	
	pkivarification=FPXPkiImplementation.verifyData("D:\\SMIExchange\\fpxcert_2015.cer",fpx_checkSumString,fpx_checkSum,"SHA1withRSA");	
	
  }
  catch(Exception e)
  {
  	out.println("OTHER ERROR");
  }
  
if(!pkivarification)
{
	out.println("CHECKSUM ERROR");
}
else
{
	if (fpx_debitAuthCode.equals("00"))
	{
		//SUCCESSFUL
		
	}
	else if (fpx_debitAuthCode.equals("99"))
	{
		//PENDING AUTHORIZATION
	}
	else
	{
		//UNSUCCESSFUL
	}							
	out.print("OK");	
}	
%> 