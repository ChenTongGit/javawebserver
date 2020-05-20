package com.ct.server.core;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class WebApp {
//    private static List<Entity> entities;
//    private static List<Mapping> mappings;
    private static WebContext webContext;
    static {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = null;
            parser = factory.newSAXParser();
            WebHandler handler = new WebHandler();
            parser.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream("web.xml"),handler);
            webContext = new WebContext(handler.getEntities(),handler.getMappings());
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }

    }

    public static Servlet getServletFromURL(String url)  {
        String clazzName = webContext.getClz("/"+url);

        try {
            Class clazz = Class.forName(clazzName);
            Servlet servlet = (Servlet) clazz.getConstructor().newInstance();
            return servlet;
        } catch (Exception e) {
            return null;
        }


    }
}
