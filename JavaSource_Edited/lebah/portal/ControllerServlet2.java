/*    */package lebah.portal;

/*    */
/*    */import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.db.UniqueID;
import lebah.portal.velocity.VServlet;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */public class ControllerServlet2
/*    */extends VServlet
/*    */{
	/*    */private static final long serialVersionUID = 7475995265237925214L;
	/* 19 */static Logger myLogger = Logger.getLogger("ControllerServlet2");

	/*    */
	/*    */
	/*    */
	/*    */
	/* 24 */public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

	/*    */
	/*    */
	/*    */
	/*    */
	/*    */public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		/* 30 */res.setContentType("text/html");
		/* 31 */HttpSession session = req.getSession();

		if (session.getAttribute("_portal_islogin") == null
				|| session.getAttribute("_portal_islogin").equals("false")) {
			System.out.println("NOT LOGIN");
			res.sendRedirect("../expired.jsp");
		}

		/* 32 */String className = getClass().getName();
		/*    */
		/* 34 */synchronized (this) {
			/* 35 */this.engine = (VelocityEngine) session
					.getAttribute("VELOCITY_ENGINE");
			/* 36 */this.context = (VelocityContext) session
					.getAttribute("VELOCITY_CONTEXT");
			/*    */
			/* 38 */if (this.engine == null || this.context == null) {
				/* 39 */initVelocity(getServletConfig());
				/* 40 */session.setAttribute("VELOCITY_ENGINE", this.engine);
				/* 41 */session.setAttribute("VELOCITY_CONTEXT", this.context);
				/*    */}
			/*    */}
		/* 44 */myLogger.debug("ControllerServlet2:User= "
				+ session.getAttribute("_portal_login") + "/ Session Id = "
				+ session.getId() + " | className=" + className);
		/*    */
		/*    */
		/*    */
		/*    */
		/*    */
		/*    */
		/* 51 */String securityToken = (String) session
				.getAttribute("securityToken");
		/*    */
		/* 53 */Date now = new Date();
		/*    */
		/* 55 */myLogger
				.debug("[SECURITY TOKEN][" + now + "] " + securityToken);
		/*    */
		/* 57 */if (securityToken == null || "".equals(securityToken)) {
			/*    */
			/* 59 */securityToken = UniqueID.getUID();
			/* 60 */session.setAttribute("securityToken", securityToken);
			/*    */}
		/* 62 */String userAgent = req.getHeader("User-Agent");
		/* 63 */this.context.put("userAgent", userAgent);
		/* 64 */if (userAgent.indexOf("MSIE") > 0) {
			/* 65 */this.context.put("browser", "ie");
			/*    */}
		/* 67 */else if (userAgent.indexOf("Firefox") > 0) {
			/* 68 */this.context.put("browser", "firefox");
			/*    */}
		/* 70 */else if (userAgent.indexOf("Netscape") > 0) {
			/* 71 */this.context.put("browser", "netscape");
			/*    */}
		/* 73 */else if (userAgent.indexOf("Safari") > 0) {
			/* 74 */this.context.put("browser", "safari");
			/*    */}
		/* 76 */else if (userAgent.indexOf("MIDP") > 0) {
			/* 77 */this.context.put("browser", "midp");
			/*    */} else {
			/* 79 */System.out.println("[" + className + "] userAgent = "
					+ userAgent);
			/* 80 */}
		if (userAgent.indexOf("Windows") > 0) {
			/* 81 */PCDeviceController.doService(getServletContext(),
					getServletConfig(), this.engine, this.context, session,
					req, res);
			/*    */}
		/* 83 */else if (userAgent.indexOf("MIDP") > 0) {
			/* 84 */System.out.println("[" + className + "] userAgent = "
					+ userAgent);
			/*    */}
		/* 86 */else if (userAgent.indexOf("MMP") > 0) {
			/* 87 */System.out.println("[" + className + "] userAgent = "
					+ userAgent);
			/*    */} else {
			/* 89 */PCDeviceController.doService(getServletContext(),
					getServletConfig(), this.engine, this.context, session,
					req, res);
			/*    */}
		/*    */}
	/*    */
}

/*
 * Location:
 * D:\EclipseWorkspace\SBBPH\sbbphv2\WebContent\WEB-INF\lib\lebah-xe.jar
 * !\lebah\portal\ControllerServlet2.class Java compiler version: 7 (51.0)
 * JD-Core Version: 1.0.7
 */