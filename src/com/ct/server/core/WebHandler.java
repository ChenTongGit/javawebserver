package com.ct.server.core;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

class WebHandler extends DefaultHandler {
    //存储数据 容器
    private List<Entity> entities ;
    private List<Mapping> mappings ;
    private Entity entity;
    private Mapping mapping;
    //存储temp标签名
    private String tag;
    private String class_tag;
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        System.out.println("----解析文档开始(web.xml)---");
        this.entities = new ArrayList<>();
        this.mappings = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        System.out.println("---"+qName+"--->解析开始");
        if (null != qName){
            tag = qName;
            switch (tag){
                case "servlet" :
                    entity = new Entity();
                    class_tag = qName;
                    break;
                case "servlet-mapping":
                    mapping = new Mapping();
                    class_tag = qName;
                    break;
                default:
            }
        }


    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content = new String(ch,start,length).trim();

        if (null != class_tag && null != tag){
            System.out.println("属性封装");
            switch (class_tag){
                case "servlet" :
                    switch (tag){
                        case "servlet-name":
                            this.entity.setName(content);
                            break;
                        case "servlet-class":
                            this.entity.setClz(content);
                            break;
                        default:
                    }
                    break;
                case "servlet-mapping" :
                    switch (tag){
                        case "servlet-name":
                            this.mapping.setName(content);
                            break;
                        case "url-pattern":
                            this.mapping.getPatterns().add(content);
                            break;
                        default:
                    }
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.println("---"+qName+"--->解析结束");
        tag = null;
        switch (qName){
            case "servlet":
                entities.add(entity);
                class_tag = null;
                break;
            case "servlet-mapping":
                mappings.add(mapping);
                class_tag = null;
                break;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        System.out.println("---解析文档结束---");
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Mapping> getMappings() {
        return mappings;
    }

}
