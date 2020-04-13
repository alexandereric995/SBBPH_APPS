/*     */ package lebah.portal;
/*     */ 
/*     */ import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.db.Labels;
import lebah.portal.db.CustomClass;
import lebah.portal.db.UserPage;
import lebah.portal.velocity.VServlet;
import lebah.portal.velocity.VTemplate;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DivControllerServlet extends VServlet {
/*  22 */   static Logger myLogger = Logger.getLogger("DivControllerServlet");
/*     */   
/*     */   public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
/*  27 */     doPost(req, res);
/*     */   }
/*     */   
/*     */   public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
/*  31 */     res.setContentType("text/html");
/*  33 */     PrintWriter out = res.getWriter();
/*  34 */     HttpSession session = req.getSession();

			  if ( session.getAttribute("_portal_islogin") == null || session.getAttribute("_portal_islogin").equals("false")) {
				  System.out.println("NOT LOGIN");
				  res.sendRedirect("../expired.jsp");
			  }

/*  36 */     synchronized (this) {
/*  37 */       this.engine = (VelocityEngine)session.getAttribute("VELOCITY_ENGINE");
/*  38 */       this.context = (VelocityContext)session.getAttribute("VELOCITY_CONTEXT");
/*  40 */       if (this.engine == null || this.context == null) {
/*  41 */         initVelocity(getServletConfig());
/*  42 */         session.setAttribute("VELOCITY_ENGINE", this.engine);
/*  43 */         session.setAttribute("VELOCITY_CONTEXT", this.context);
/*     */       } 
/*     */     } 
/*  46 */     myLogger.debug("DivControllerServlet:User= " + session.getAttribute("_portal_login") + "/ Session Id = " + session.getId());
/*  48 */     String app_path = getServletContext().getRealPath("/");
/*  49 */     app_path = app_path.replace('\\', '/');
/*  51 */     session.setAttribute("_portal_app_path", app_path);
/*  52 */     String uri = req.getRequestURI();
/*  54 */     String s1 = uri.substring(1);
/*  56 */     this.context.put("appname", s1.substring(0, s1.indexOf("/")));
/*  57 */     session.setAttribute("_portal_appname", s1.substring(0, s1.indexOf("/")));
/*  59 */     String pathInfo = req.getPathInfo();
/*  60 */     pathInfo = pathInfo.substring(1);
/*  61 */     int slash = pathInfo.indexOf("/");
/*  62 */     boolean allowed = true;
/*  63 */     if (allowed) {
/*  65 */       Labels label = null;
/*  66 */       String language = req.getParameter("lang");
/*  67 */       if (language != null && !"".equals(language))
/*  68 */         session.setAttribute("_portal_language", language); 
/*  70 */       language = (String)session.getAttribute("_portal_language");
/*  71 */       if (language == null || "".equals(language)) {
/*  72 */         label = Labels.getInstance();
/*  73 */         this.context.put("label", label.getTitles());
/*  74 */         language = Labels.getDefaultLanguage();
/*  75 */         session.setAttribute("_portal_language", language);
/*     */       } else {
/*  77 */         label = Labels.getInstance(language);
/*  78 */         this.context.put("label", label.getTitles());
/*     */       } 
/*  81 */       pathInfo = pathInfo.substring(pathInfo.indexOf("/") + 1);
/*  82 */       String module = (pathInfo != null) ? pathInfo : "";
/*     */       try {
/*  85 */         Object content = null;
/*     */         try {
/*  88 */           String className = "";
/*  90 */           if (module.indexOf(".") > 0) {
/*  91 */             className = module;
/*  93 */             content = ClassLoadManager.load(className);
/*  94 */             ((VTemplate)content).setId(className);
/*     */           } else {
/*  97 */             className = CustomClass.getName(module);
/*  98 */             content = ClassLoadManager.load(className, module, req.getRequestedSessionId());
/*  99 */             ((VTemplate)content).setId(module);
/*     */           } 
/* 102 */           ((VTemplate)content).setEnvironment(this.engine, this.context, req, res);
/* 103 */           ((VTemplate)content).setServletContext(getServletConfig().getServletContext());
/* 104 */           ((VTemplate)content).setServletConfig(getServletConfig());
/* 105 */           ((VTemplate)content).setDiv(true);
/* 107 */           if (content instanceof Attributable) {
/* 108 */             Hashtable h = UserPage.getValuesForAttributable(module);
/* 109 */             if (h != null)
/* 110 */               ((Attributable)content).setValues(h); 
/*     */           } 
/*     */           try {
/* 115 */             if (content != null) {
/* 116 */               ((VTemplate)content).setShowVM(true);
/* 117 */               ((VTemplate)content).print(session);
/*     */             } 
/* 119 */           } catch (Exception ex) {
/* 120 */             out.println(ex.getMessage());
/*     */           } 
/* 123 */         } catch (ClassNotFoundException cnfex) {
/* 125 */           content = new ErrorMsg(this.engine, this.context, req, res);
/* 126 */           ((ErrorMsg)content).setError("ClassNotFoundException : " + cnfex.getMessage());
/* 127 */         } catch (InstantiationException iex) {
/* 128 */           content = new ErrorMsg(this.engine, this.context, req, res);
/* 129 */           ((ErrorMsg)content).setError("InstantiationException : " + iex.getMessage());
/* 130 */         } catch (IllegalAccessException illex) {
/* 131 */           content = new ErrorMsg(this.engine, this.context, req, res);
/* 132 */           ((ErrorMsg)content).setError("IllegalAccessException : " + illex.getMessage());
/* 133 */         } catch (Exception ex) {
/* 134 */           content = new ErrorMsg(this.engine, this.context, req, res);
/* 135 */           ((ErrorMsg)content).setError("Other Exception during class initiation : " + ex.getMessage());
/* 136 */           ex.printStackTrace();
/*     */         } 
/* 139 */       } catch (Exception ex) {
/* 140 */         System.out.println(ex.getMessage());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\EclipseWorkspace\SBBPH\SBBPH_APPS\WebContent\WEB-INF\lib\lebah-xe.jar!\lebah\portal\DivControllerServlet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */