package com.ct.server.basic;

import com.ct.server.servlet.Entity;
import com.ct.server.servlet.Mapping;
import com.ct.server.servlet.Servlet;
import com.ct.server.servlet.WebContext;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class XmlTest01 {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //SAX解析
        //1获取解析工厂
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //2从解析工厂获取解析器
        SAXParser parser = factory.newSAXParser();

        WebHandler handler = new WebHandler();
        parser.parse(Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("web.xml")
                ,handler
        );

        //数据注入从上下文
        WebContext webContext = new WebContext(handler.getEntities(),handler.getMappings());

        String name = webContext.getClz("/reg");//不存在时需要处理404
        Class clazz = Class.forName(name);
        Servlet servlet = (Servlet) clazz.getConstructor().newInstance();
        System.out.println(servlet);
//        servlet.service();
//        List<Entity> entities = handler.getEntities();
//        for (Entity entity:entities){
//            System.out.println(entity.getName() + "  " +entity.getClz());
//        }
//        System.out.println(entities.size());

//        List<Mapping> mappings = handler.getMappings();
//        System.out.println(mappings.size());


    }


}
class WebHandler extends DefaultHandler {
    //存储数据 容器
    private List<Entity> entities = new ArrayList<Entity>();;
    private List<Mapping> mappings = new ArrayList<Mapping>();
    private Entity entity;
    private Mapping mapping;
    //存储temp标签名
    private String tag;
    private boolean isMapping = false;


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        System.out.println(qName+" 解析开始");
        if (null!=qName){
            tag = qName;

            if(tag.equals("servlet")){
                //初始化Person 对象
                entity = new Entity();
                isMapping = false;
            }else if(tag.equals("servlet-mapping")){
                mapping = new Mapping();
                isMapping = true;//告知正在操作mapping
            }
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.println(qName+" 解析结束");
        if (qName.equals("servlet")) {
            //存储
            entities.add(entity);
        }else if(qName.equals("servlet-mapping")){
            mappings.add(mapping);
        }
        tag = null;

    }

    //处理内容
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String contents = new String(ch,start,length).trim();
        if (null!= tag){
            if (!isMapping){//操作mapping
                if (tag.equals("servlet-name")){
                    entity.setName(contents);
                }else if(tag.equals("servlet-class")){
                    entity.setClz(contents);
                }
            }else {
                if (tag.equals("servlet-name")){
                    mapping.setName(contents);
                }else if(tag.equals("url-pattern")){
                    mapping.addPattern(contents);
                }
            }

        }

    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public List<Mapping> getMappings() {
        return mappings;
    }


    public void setMapping(boolean mapping) {
        isMapping = mapping;
    }
}
