package com.anotherglass.solr;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;

public final class LoadAdminUiServlet extends HttpServlet {
	
	// mchabot: set the value of 'path-prefix' from web.xml to PATH_PREFIX (hack for SOLR-3781)
	private static final String PATH_PREFIX = "/solr";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
		CoreContainer cores = (CoreContainer) request.getAttribute("org.apache.solr.CoreContainer");
		InputStream in = getServletContext().getResourceAsStream(PATH_PREFIX + "/admin.html");
		
		if(in != null && cores != null) {
		  try {
			
			// response.setCharacterEncoding("UTF-8");
		    response.setContentType("text/html");
		    Writer out = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
		
		    String html = IOUtils.toString(in, "UTF-8");
		    Package pack = SolrCore.class.getPackage();
		    
		    String[] search = new String[] { 
		        "${contextPath}", 
		        "${adminPath}",
		        "${version}" 
		    };
		    // 
		    String[] replace = new String[] {
		        StringEscapeUtils.escapeJavaScript(request.getContextPath() + PATH_PREFIX),
		        StringEscapeUtils.escapeJavaScript(cores.getAdminPath()),
		        StringEscapeUtils.escapeJavaScript(pack.getSpecificationVersion())
		    };
		    
		    out.write( StringUtils.replaceEach(html, search, replace) );
		    out.flush();
		  } finally {
		    IOUtils.closeQuietly(in);
		  }
		} else {
		  response.sendError(404);
		}
	}
}