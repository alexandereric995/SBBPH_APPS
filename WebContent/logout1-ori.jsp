<%
String visitor = session.getAttribute("_portal_visitor") != null ? 
			     (String) session.getAttribute("_portal_visitor") : "anon";
session.invalidate();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Log Keluar...</title>
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
<link href="tools/style.css" rel="stylesheet" type="text/css" />
<link href="tools/960.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-2.1.4.min.js"></script> 
<script type="text/javascript" src="js/cufon-yui.js"></script>
<script type="text/javascript" src="js/Adobe_Caslon_Pro_600.font.js"></script>
<script type="text/javascript">
	Cufon.replace('h1', {fontFamily: 'Adobe Caslon Pro', hover:true})
</script>
</head>
<body>
<div class="container_12" id="content">
	<p><img src="images/logo-bph-baru.png" width="275px" alt="" /></p>
	<h1>LOG KELUAR</h1>
	<div class="main">
		<div class="mcontent">
			<div class="box">
				<h3>Sila tunggu...</h3>
			</div>
		</div>
	</div>
</div>
</body>
</html>

<script>
document.location = "logout2.jsp?visitor=<%=visitor%>";
</script>
