package com.asindem.gestiondocumental.configuration;

import com.lowagie.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
@Service
public class PdfGenarator {

	private static final Logger logger = LoggerFactory.getLogger(PdfGenarator.class);

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private ApplicationContext context;

	@Autowired
	ServletContext servletContext;

	String urlBase = "http://localhost:8082";

	public ByteArrayOutputStream createPdf(final String templateName, final Map map, final HttpServletRequest request, final HttpServletResponse response)
			throws DocumentException {

		logger.debug("Generando informe pdf");

		Assert.notNull(templateName, "The templateName can not be null");
 		IWebContext ctx = new WebContext(request,response,request.getServletContext(),request.getLocale(),map);

		String processedHtml = templateEngine.process(templateName, ctx);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {

			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(processedHtml, urlBase);

			renderer.layout();
			renderer.createPDF(bos, false);
			renderer.finishPDF();
			logger.info("Archivo pdf creado correctamente");

		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					logger.error("Error creando pdf", e);
				}
			}
		}
		return bos;
	}
}
