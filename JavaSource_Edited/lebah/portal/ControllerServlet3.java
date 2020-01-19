/*     */ package lebah.portal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ import javax.portlet.GenericPortlet;
/*     */ import javax.portlet.PortletConfig;
/*     */ import javax.portlet.PortletMode;
/*     */ import javax.portlet.RenderRequest;
/*     */ import javax.portlet.RenderResponse;
/*     */ import javax.portlet.WindowState;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import lebah.portal.db.CustomClass;
/*     */ import lebah.portal.db.UserPage;
/*     */ import lebah.portal.velocity.VServlet;
/*     */ import lebah.portal.velocity.VTemplate;
/*     */ import lebah.util.Util;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.velocity.VelocityContext;
/*     */ import org.apache.velocity.app.VelocityEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ControllerServlet3
/*     */   extends VServlet
/*     */ {
/*  31 */   static Logger myLogger = Logger.getLogger("ControllerServlet3");
/*     */ 
/*     */ 
/*     */   
/*  35 */   public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { doPost(req, res); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
/*  40 */     res.setContentType("text/html");
/*     */     
/*  42 */     PrintWriter out = res.getWriter();
/*  43 */     HttpSession session = req.getSession();

			if (!"bph.portal.FrmLupaKataLaluan".equals(req.getPathInfo().substring(1))) {
				if ( session.getAttribute("_portal_islogin") == null || session.getAttribute("_portal_islogin").equals("false")) {
					System.out.println("NOT LOGIN");
					res.sendRedirect("../expired.jsp");
				}
			}			
/*     */     
/*  45 */     synchronized (this) {
/*  46 */       this.engine = (VelocityEngine)session.getAttribute("VELOCITY_ENGINE");
/*  47 */       this.context = (VelocityContext)session.getAttribute("VELOCITY_CONTEXT");
/*     */       
/*  49 */       if (this.engine == null || this.context == null) {
/*  50 */         initVelocity(getServletConfig());
/*  51 */         session.setAttribute("VELOCITY_ENGINE", this.engine);
/*  52 */         session.setAttribute("VELOCITY_CONTEXT", this.context);
/*     */       } 
/*     */     } 
/*  55 */     myLogger.debug("ControllerServlet3:User= " + session.getAttribute("_portal_login") + "/ Session Id = " + session.getId());
/*  56 */     this.context.put("util", new Util());
/*     */     
/*  58 */     String prev_token = (session.getAttribute("form_token") != null) ? (String)session.getAttribute("form_token") : "";
/*     */     
/*  60 */     String form_token = (req.getParameter("form_token") != null) ? req.getParameter("form_token") : "empty";
/*     */     
/*  62 */     if (prev_token.equals(form_token)) {
/*  63 */       session.setAttribute("doPost", "true");
/*  64 */       session.setAttribute("isPost", new Boolean(true));
/*     */     }
/*  66 */     else if ("empty".equals(form_token)) {
/*  67 */       session.setAttribute("doPost", "false");
/*  68 */       session.setAttribute("isPost", new Boolean(false));
/*     */     } else {
/*     */       
/*  71 */       session.setAttribute("doPost", "false");
/*  72 */       session.setAttribute("isPost", new Boolean(false));
/*     */     } 
/*     */     
/*  75 */     form_token = Long.toString(System.currentTimeMillis());
/*  76 */     session.setAttribute("form_token", form_token);
/*     */     
/*  78 */     String app_path = getServletContext().getRealPath("/");
/*  79 */     app_path = app_path.replace('\\', '/');
/*  80 */     session.setAttribute("_portal_app_path", app_path);
/*     */     
/*  82 */     String uri = req.getRequestURI();
/*  83 */     String s1 = uri.substring(1);
/*  84 */     this.context.put("appname", s1.substring(0, s1.indexOf("/")));
/*  85 */     session.setAttribute("_portal_appname", s1.substring(0, s1.indexOf("/")));
/*     */     
/*  87 */     String pathInfo = req.getPathInfo();
/*  88 */     pathInfo = pathInfo.substring(1);
/*  89 */     int slash = pathInfo.indexOf("/");
/*  90 */     boolean allowed = true;
/*     */     
/*  92 */     boolean hasSecurityToken = true;
/*  93 */     if (slash == -1) hasSecurityToken = false;
/*     */     
/*  95 */     if (hasSecurityToken) {
/*     */       
/*  97 */       String securityTokenURI = pathInfo.substring(0, pathInfo.indexOf("/"));
/*     */       
/*  99 */       String securityToken = (String)session.getAttribute("securityToken");
/* 100 */       this.context.put("securityToken", securityToken);
/*     */       
/* 102 */       if (!securityTokenURI.equals(securityToken)) {
/* 103 */         securityTokenDenied(this.engine, this.context, req, res);
/* 104 */         allowed = false;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 109 */     if (allowed) {
/*     */       
/* 111 */       pathInfo = pathInfo.substring(pathInfo.indexOf("/") + 1);
/* 112 */       String module = (pathInfo != null) ? pathInfo : "";
/*     */       
/* 114 */       String remoteAddr = req.getRemoteAddr();
/*     */       
/* 116 */       boolean localAccess = false;
/* 117 */       if ("127.0.0.1".equals(remoteAddr)) localAccess = true; 
/* 118 */       this.context.put("session", session);
/* 119 */       String ddir = (req.getPathInfo().lastIndexOf("/") == 0) ? "../" : "../../";
/*     */       
/* 121 */       out.println("<script type=\"text/javascript\" src=\"" + ddir + "scriptaculous/prototype.js\" ></script>");
/* 122 */       out.println("<script type=\"text/javascript\" src=\"" + ddir + "scriptaculous/scriptaculous.js\" ></script>");
/* 123 */       out.println("<script type=\"text/javascript\" src=\"" + ddir + "scriptaculous/fixed.js\" ></script>");
/* 124 */       out.println("<script type=\"text/javascript\" src=\"" + ddir + "scriptaculous/dragdrop.js\" ></script>");
/* 125 */       out.println("<script type=\"text/javascript\" src=\"" + ddir + "scriptaculous/unittest.js\" ></script>");
/* 126 */       out.println("<script type=\"text/javascript\" src=\"" + ddir + "scriptaculous/ajax.js\" ></script>");
/*     */       
/* 128 */       JS_CSS js_css = new JS_CSS(this.engine, this.context, req, res);
/*     */       try {
/* 130 */         js_css.print();
/* 131 */       } catch (Exception ex) {
/* 132 */         ex.printStackTrace();
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 137 */         Object content = null;
/*     */         
/*     */         try {
/* 140 */           String className = "";
/*     */           
/* 142 */           if (module.indexOf(".") > 0) {
/* 143 */             className = module;
/*     */             
/* 145 */             content = ClassLoadManager.load(className);
/* 146 */             ((VTemplate)content).setId(className);
/*     */           } else {
/*     */             
/* 149 */             className = CustomClass.getName(module);
/* 150 */             content = ClassLoadManager.load(className, module, req.getRequestedSessionId());
/* 151 */             ((VTemplate)content).setId(module);
/*     */           } 
/*     */           
/* 154 */           if (content instanceof GenericPortlet) {
/* 155 */             PortletInfo portletInfo = new PortletInfo();
/* 156 */             portletInfo.id = "test_id";
/* 157 */             portletInfo.title = "Test Title";
/* 158 */             Hashtable portletState = getPortletState(getServletConfig(), req, res, out, portletInfo);
/* 159 */             RenderRequest renderRequest = (RenderRequest)portletState.get("renderRequest");
/* 160 */             RenderResponse renderResponse = (RenderResponse)portletState.get("renderResponse");
/* 161 */             PortletConfig config = (PortletConfig)portletState.get("config");
/* 162 */             GenericPortlet portlet = (GenericPortlet)content;
/* 163 */             portlet.init(config);
/* 164 */             portlet.render(renderRequest, renderResponse);
/*     */           } else {
/* 166 */             ((VTemplate)content).setEnvironment(this.engine, this.context, req, res);
/* 167 */             ((VTemplate)content).setServletContext(getServletConfig().getServletContext());
/* 168 */             ((VTemplate)content).setServletConfig(getServletConfig());
/* 169 */             ((VTemplate)content).setDiv(false);
/*     */             
/* 171 */             if (content instanceof Attributable) {
/* 172 */               Hashtable h = UserPage.getValuesForAttributable(module);
/* 173 */               if (h != null) {
/* 174 */                 ((Attributable)content).setValues(h);
/*     */               }
/*     */             } 
/*     */             
/*     */             try {
/* 179 */               if (content != null) {
/* 180 */                 ((VTemplate)content).setShowVM(true);
/* 181 */                 ((VTemplate)content).print(session);
/*     */               } 
/* 183 */             } catch (Exception ex) {
/* 184 */               out.println(ex.getMessage());
/*     */             }
/*     */           
/*     */           } 
/* 188 */         } catch (ClassNotFoundException cnfex) {
/*     */           
/* 190 */           content = new ErrorMsg(this.engine, this.context, req, res);
/* 191 */           ((ErrorMsg)content).setError("ClassNotFoundException : " + cnfex.getMessage());
/* 192 */         } catch (InstantiationException iex) {
/* 193 */           content = new ErrorMsg(this.engine, this.context, req, res);
/* 194 */           ((ErrorMsg)content).setError("InstantiationException : " + iex.getMessage());
/* 195 */         } catch (IllegalAccessException illex) {
/* 196 */           content = new ErrorMsg(this.engine, this.context, req, res);
/* 197 */           ((ErrorMsg)content).setError("IllegalAccessException : " + illex.getMessage());
/* 198 */         } catch (Exception ex) {
/* 199 */           content = new ErrorMsg(this.engine, this.context, req, res);
/* 200 */           ((ErrorMsg)content).setError("Other Exception during class initiation : " + ex.getMessage());
/* 201 */           ex.printStackTrace();
/*     */         }
/*     */       
/* 204 */       } catch (Exception ex) {
/* 205 */         System.out.println(ex.getMessage());
/*     */       } 
/*     */     } 
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Hashtable getPortletState(ServletConfig svtCfg, HttpServletRequest req, HttpServletResponse res, PrintWriter out, PortletInfo portletInfo) throws Exception {
/* 229 */     Hashtable h = new Hashtable();
/*     */     
/* 231 */     MerakContext context = new MerakContext();
/* 232 */     context.httpServletRequest = req;
/*     */     
/* 234 */     MerakConfig config = new MerakConfig();
/* 235 */     config.portletInfo = portletInfo;
/* 236 */     config.portletContext = context;
/*     */     
/* 238 */     MerakResponse renderResponse = new MerakResponse();
/* 239 */     MerakRequest renderRequest = new MerakRequest();
/* 240 */     renderRequest.windowState = WindowState.NORMAL;
/* 241 */     renderRequest.portletMode = PortletMode.VIEW;
/* 242 */     renderResponse.printWriter = out;
/* 243 */     renderRequest.httpServletRequest = req;
/* 244 */     renderResponse.httpServletResponse = res;
/* 245 */     h.put("renderRequest", renderRequest);
/* 246 */     h.put("renderResponse", renderResponse);
/* 247 */     h.put("config", config);
/* 248 */     return h;
/*     */   }
/*     */   
/*     */   private static void securityTokenDenied(VelocityEngine engine, VelocityContext context, HttpServletRequest req, HttpServletResponse res) {
/*     */     try {
/* 253 */       VTemplate content = new SecurityTokenDenied(engine, context, req, res);
/* 254 */       content.print();
/* 255 */     } catch (Exception e) {
/* 256 */       System.out.println(e.getMessage());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\EclipseWorkspace\SBBPH\sbbphv2\WebContent\WEB-INF\lib\lebah-xe.jar!\lebah\portal\ControllerServlet3.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.7
 */