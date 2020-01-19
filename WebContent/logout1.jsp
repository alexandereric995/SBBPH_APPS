<%
if(session!=null) {
	session.invalidate();
}  
String randomNo = lebah.db.UniqueID.getUID();
response.sendRedirect("c/?logoutrndId=" + randomNo); 
%>