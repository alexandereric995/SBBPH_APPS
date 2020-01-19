<!-- 

# These sample pages are provided for information purposes only. It does not imply any recommendation or endorsement by anyone.
  These sample pages are provided for FREE, and no additional support will be provided for these sample pages. 
  There is no warranty and no additional document. USE AT YOUR OWN RISK.
-->
<%@ page import="java.io.*,java.util.*,java.security.*,java.text.*"%>
<%@ page import="bph.integrasi.fpx.FPXPkiImplementation"%>
<%@ page import="lebah.db.Db"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="bph.integrasi.fpx.FPXUtil"%>
<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<html>
<head>
<title>Semakan Transaksi FPX -SBBPH</title>
<meta http-equiv='Content-Type' content='text/html, charset=iso-8859-1'>
<style type='text/css'>
<!--
h1 {
	font-family:Arial, sans-serif;
	font-size:20pt;
	font-weight:600;
	margin-bottom:0.1em;
	color:#08185A;
}
h2 {
	font-family:Arial, sans-serif;
	font-size:14pt;
	font-weight:100;
	margin-top:0.1em;
	color:#08185A;
}
h2.co {
	font-family:Arial, sans-serif;
	font-size:24pt;
	font-weight:100;
	margin-top:0.1em;
	margin-bottom:0.1em;
	color:#08185A
}
h3 {
	font-family:Arial, sans-serif;
	font-size:16pt;
	font-weight:100;
	margin-top:0.1em;
	margin-bottom:0.1em;
	color:#08185A
}
h3.co {
	font-family:Arial, sans-serif;
	font-size:16pt;
	font-weight:100;
	margin-top:0.1em;
	margin-bottom:0.1em;
	color:#FFFFFF
}
body {
	font-family:Verdana, Arial, sans-serif;
	font-size:10pt;
	background-color:#FFFFFF;
	color:#08185A
}
th {
	font-family:Verdana, Arial, sans-serif;
	font-size:8pt;
	font-weight:bold;
	background-color:#E1E1E1;
	padding-top:0.5em;
	padding-bottom:0.5em;
	color:#08185A
}
tr {
	height:25px;
}
.shade {
	height:25px;
	background-color:#E1E1E1
}
.title {
	height:25px;
	background-color:#C1C1C1
}
td {
	font-family:Verdana, Arial, sans-serif;
	font-size:8pt;
	color:#08185A
}
td.red {
	font-family:Verdana, Arial, sans-serif;
	font-size:8pt;
	color:#FF0066
}
td.green {
	font-family:Verdana, Arial, sans-serif;
	font-size:8pt;
	color:#008800
}
p {
	font-family:Verdana, Arial, sans-serif;
	font-size:10pt;
	color:#FFFFFF
}
p.blue {
	font-family:Verdana, Arial, sans-serif;
	font-size:7pt;
	color:#08185A
}
p.red {
	font-family:Verdana, Arial, sans-serif;
	font-size:7pt;
	color:#FF0066
}
p.green {
	font-family:Verdana, Arial, sans-serif;
	font-size:7pt;
	color:#008800
}
div.bl {
	font-family:Verdana, Arial, sans-serif;
	font-size:7pt;
	color:#C1C1C1
}
div.red {
	font-family:Verdana, Arial, sans-serif;
	font-size:7pt;
	color:#FF0066
}
li {
	font-family:Verdana, Arial, sans-serif;
	font-size:8pt;
	color:#FF0066
}
input {
	font-family:Verdana, Arial, sans-serif;
	font-size:8pt;
	color:#08185A;
	background-color:#E1E1E1;
	font-weight:bold
}
select {
	font-family:Verdana, Arial, sans-serif;
	font-size:8pt;
	color:#08185A;
	background-color:#E1E1E1;
	font-weight:bold;
}
textarea {
	font-family:Verdana, Arial, sans-serif;
	font-size:8pt;
	color:#08185A;
	background-color:#E1E1E1;
	font-weight:normal;
	scrollbar-arrow-color:#08185A;
	scrollbar-base-color:#E1E1E1
}
.style1 {
	color: #FFFFFF;
	font-weight: bold;
	font-size: 14pt;
}
.style3 {
	font-size: 9px
}
-->
</style>
</head>
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
	pkivarification=FPXPkiImplementation.verifyData("D:\\SMIExchange\\fpxcert_2015.cer",fpx_checkSumString,fpx_checkSum,"SHA1withRSA");

 //FOR TESTING PURPOSE ONLY------------------
    /* pkivarification=true;
 	fpx_debitAuthCode="00";
 	fpx_txnAmount="150";
 	session.setAttribute("sesIdPermohonan","232824407968689");
 	session.setAttribute("sesModul","IR");
 	fpx_fpxTxnId="1703061029570929";
 	fpx_sellerOrderNo="194090142031321";
 	fpx_sellerExOrderNo="20170306102903"; */
//----------------------------------------------------------- 
  }
  catch(Exception e)
  {
  	out.println("<HR><H3>Error :"+e);			
  }
%>
<body>
<center>
  <table border="0" cellpadding="0" cellspacing="0" width="722">
    <tbody>
      <%
		if(!pkivarification) {
	  %>
      <tr>
        <td class="normal">Check Sum Verification</td>
        <td class="normal"><div align="center">:</div></td>
        <td class="normal"><%=pkivarification %></td>
      </tr>
    Check sum String[<%=fpx_checkSumString %>]
    <%} else {
	%>
    <tr>
      <td colspan="3" align="left"><table style="color: #ffffff; background:#127BA3;border: 1px solid rgb(18, 123, 163);" cellpadding="0" cellspacing="0" height="111" width="722" >
          <tbody>
            <tr>
              <td align="center"><span class="style1">SEMAKAN TRANSAKSI FPX - SBBPH</span></td>
            </tr>
          </tbody>
        </table></td>
    </tr>
    <tr>
      <td style="padding-left: 1px; padding-right: 1px;" align="left" valign="top" colspan="3"><table bgcolor="#FFFFFF" border="0" cellpadding="0" cellspacing="5" width="100%" height="100%">
          <tbody>
            <tr>
              <td height="25" valign="top"><p align="center" class="normal" style="color: #000000;">Terima kasih kerana menggunakan perkhidmatan ePayment kami!</p></td>
            </tr>
          </tbody>
        </table></td>
    </tr>
    <tr>
      <td align="left" colspan="3"><div align="center"><strong><b>Butiran Transaksi</b></strong></div></td>
    </tr>
    <tr>
      <td align="left" colspan="3" height="15px"></td>
    </tr>
    <tr>
      <td align="left"><div align="left"><strong><i>Status Transaksi</i></strong></div></td>
      <td>:</td>
      <td><div align="left" class="style3">
          <!-- Comparing Debit Auth Code and Credit Auth Code to cater SUCCESSFUL and UNSUCCESSFUL result -->
          <% 
							if (fpx_debitAuthCode.equals("00"))
							{	
								String statusTransaksi="BERJAYA";
								out.println(statusTransaksi);	
								request.getSession().setAttribute("statusTransaksi", statusTransaksi);	
							}
							else if (fpx_debitAuthCode.equals("09"))
							{
								String statusTransaksi="MENUNGGU PENGESAHAN";
								out.println(statusTransaksi);	
								request.getSession().setAttribute("statusTransaksi", statusTransaksi);
							}
							else
							{
								String statusTransaksi="TIDAK BERJAYA";
								out.println(statusTransaksi);	
								request.getSession().setAttribute("statusTransaksi", statusTransaksi);
							}
															
								//SIMPAN TO TABLE fpx_records
								//INSERT FPX TRANSAKSI DLM TABLE FPX_RECORDS
								Db db = null;
								Connection conn = null;
								String sql = "";
								String flag_modul = (String) session.getAttribute("sesModul");
								try {
								    db = new Db();
								    conn = db.getConnection();
								    Statement stmt = db.getStatement();								    
									sql= "INSERT INTO fpx_records(fpxTxnId, buyerBankBranch, buyerBankId, buyerIban, buyerId, buyerName," 
											+ " creditAuthCode, creditAuthNo, debitAuthCode, debitAuthNo,"
											+ " fpxTxnTime, makerName, msgToken, msgType, sellerExId, sellerExOrderNo, sellerId, sellerOrderNo,"
											+ " sellerTxnTime, txnAmount, txnCurrency, flagModul, flagManagePayment)";
									sql+= " VALUES ";
									sql+= "('" + fpx_fpxTxnId + "','" + fpx_buyerBankBranch + "','" + fpx_buyerBankId + "','" + fpx_buyerIban + "','" + fpx_buyerId + "','" + fpx_buyerName 
											+  "','" + fpx_creditAuthCode + "','" + fpx_creditAuthNo + "','" + fpx_debitAuthCode + "','" + fpx_debitAuthNo
											+ "','" + fpx_fpxTxnTime + "','" + fpx_makerName + "','" + fpx_msgToken + "','" + fpx_msgType + "','" + fpx_sellerExId + "','" + fpx_sellerExOrderNo + "','" + fpx_sellerId + "','" + fpx_sellerOrderNo
											+ "','" + fpx_sellerTxnTime + "','" + fpx_txnAmount + "','" + fpx_txnCurrency + "','" + flag_modul + "','T')";								    		
								    stmt.executeUpdate(sql);
								    
								    // ADD BY PEJE - UPDATE FPXREQUEST RECORDS.
								    sql = "UPDATE fpx_records_request SET fpxTxnId = '" + fpx_fpxTxnId + "', respondDate = NOW() WHERE sellerOrderNo = '" + fpx_sellerOrderNo + "' AND sellerExOrderNo = '" + fpx_sellerExOrderNo + "';";
								    stmt.executeUpdate(sql);
								} catch ( Exception ex ) {
								   ex.printStackTrace();
								} finally {
									if (db!=null) db.close();
								}
	%>
        </div></td>
    </tr>
    <tr>
      <td align="left"><div align="left"><strong><i>Tarikh Transaksi</i></strong></div></td>
      <td>:</td>
      <td><div align="left" class="style3">
		<%
	                SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a z");
		            
					sdf.setLenient(false);
					out.print(sdf.format(new Date()));
		%>
        </div></td>
    </tr>
    <tr>
      <td align="left"><div align="left"><strong><i>Id Transaksi FPX</i></strong></div></td>
      <td>:</td>
      <td><div align="left" class="style3"><%=fpx_fpxTxnId%></div></td>
    </tr>
    <tr>
      <td align="left"><div align="left"><strong><i>Nombor Rujukan</i></strong></div></td>
      <td>:</td>
      <td><div align="left" class="style3"><%=fpx_sellerOrderNo%></div></td>
    </tr>
    <tr>
      <td align="left"><div align="left"><strong><i>Jumlah Transaksi</i></strong></div></td>
      <td>:</td>
      <td><div align="left" class="style3">RM<%=fpx_txnAmount%></div></td>
    </tr>
    <tr>
      <td colspan="3"><hr />
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
								
								idm=(String) session.getAttribute("sesIdPermohonan");
								flag=(String) session.getAttribute("sesModul");
								peranan=(String) session.getAttribute("sesRole");
								urlReturn=(String)session.getAttribute("returnlink");
								
                                //AZAM ADD
								if (urlReturn == null || urlReturn == "null" || urlReturn == "") {
									urlReturn = "../c/";
								}
								
								FPXUtil fpxUtil = null;
								String statusBayaran;
								try{
									fpxUtil = new FPXUtil(session);
									
									fpxUtil.doAddFPXLog(fpx_fpxTxnId,fpx_sellerOrderNo,"INSERT INTO FPX_RECORDS..DONE | Amount="+fpx_txnAmount+" | DebitAuthCode="+fpx_debitAuthCode+"| Flag="+flag);
									
									if ("UTIL".equals(flag)) {
										
										if (fpx_debitAuthCode.equals("00"))
										{												
											fpxUtil.UTILsimpanFPX(fpx_fpxTxnId, fpx_sellerOrderNo);
											fpxUtil.doAddFPXLog(fpx_fpxTxnId,fpx_sellerOrderNo,"UTILsimpanFPX DONE");
										}
									} else if ("IR".equals(flag)) {
										
										if (fpx_debitAuthCode.equals("00"))
										{	
											try {
												fpxUtil.RPPsimpanFPX(fpx_fpxTxnId, fpx_sellerOrderNo);
											} catch (Exception e) {
												fpxUtil.doAddFPXLog(fpx_fpxTxnId,fpx_sellerOrderNo,"ERROR RPPsimpanFPX:"+e.getMessage());
											}
											statusBayaran = fpxUtil.getStatusBayaranRPP(fpx_sellerOrderNo);
											fpxUtil.doAddFPXLog(fpx_fpxTxnId,fpx_sellerOrderNo,"RPPsimpanFPX Completed. Status Bayaran RPP Permohonan="+statusBayaran);
										}	
									} else if ("LONDON".equals(flag)) {
										
										if (fpx_debitAuthCode.equals("00"))
										{	
											try {
												fpxUtil.LondonSimpanFPX(fpx_fpxTxnId, fpx_sellerOrderNo);
											} catch (Exception e) {
												fpxUtil.doAddFPXLog(fpx_fpxTxnId,fpx_sellerOrderNo,"ERROR RPPsimpanFPX LONDON :"+e.getMessage());
											}
											statusBayaran = fpxUtil.getStatusBayaranRPPLondon(fpx_sellerOrderNo);
											fpxUtil.doAddFPXLog(fpx_fpxTxnId,fpx_sellerOrderNo,"RPPsimpanFPX LONDON Completed. Status Bayaran RPP Permohonan="+statusBayaran);

										}	
									} else {
										if (fpx_debitAuthCode.equals("00")) {
											try {
												fpxUtil = new FPXUtil();
												fpxUtil.defineModulFPXPayment(fpx_fpxTxnId, fpx_sellerOrderNo);
											} catch (Exception e) {
												fpxUtil.doAddFPXLog(fpx_fpxTxnId,fpx_sellerOrderNo,"ERROR DEFINE MODUL :" + e.getMessage());
											}
											fpxUtil.doAddFPXLog(fpx_fpxTxnId,fpx_sellerOrderNo,"DEFINE MODUL Finish.");
										}
									}
									
								} catch(Exception ex){
									fpxUtil.doAddFPXLog(fpx_fpxTxnId,fpx_sellerOrderNo,ex.getMessage());
								  	out.println("<HR><H3>Error :"+ex);			
								}
%>
<center>
  <p> <a href="<%=urlReturn%>" style="text-decoration: none;">
    <input type="button" value="Kembali" />
    </a>
    <input type="button" style="text-decoration: none;" value="Cetak" onClick="javascript:window.print()"/>
  </p>
</center>
</body>
</html>
<%
}
%>
