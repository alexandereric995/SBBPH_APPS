package bph.laporan;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.servlets.IServlet2;

import org.apache.log4j.Logger;

public class FormatLaporan
  implements IServlet2
{
  static Logger myLogger = Logger.getLogger(FormatLaporan.class);

  public void doService(HttpServletRequest request, HttpServletResponse response, ServletContext context)
    throws IOException, ServletException
  {
    HttpSession session = request.getSession();
    PrintWriter out = response.getWriter();
    String contextPath = request.getContextPath();
    String format = request.getParameter("format");
    session.setAttribute("rFormat", format);
    
    String[] report = { "PDF","EXCEL","RTF" };//should get from session
    out.println("<font class='font_welcome'>");
    out.println("Format:");
    for (String s : report)
      if (s.equals(format))
        out.println(s + " |");
      else
        out.println("<a class=\"font_welcome\" href=\"#\" onClick=\"javascript:doChangeReport('" + s + "');\"><u>" + s + "</u></a> |");
    out.println("</font>");
  }
}