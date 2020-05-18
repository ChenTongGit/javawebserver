package com.ct.server.servlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//上下文

/**
 * URL to ClassPath
 */
public class WebContext {
    private List<Entity> entities = null;
    private List<Mapping> mappings = null;

    //servlet-name,servlet-class
    private Map<String,String> entitiesMap = new HashMap<>();
    //url-pattern,servlet-name
    private Map<String,String> mappingsMap = new HashMap<>();


    public WebContext(List<Entity> entities,List<Mapping> mappings){
        this.entities = entities;
        this.mappings = mappings;
        build();
    }

    /**
     *
     */
    private void build(){
        for (Entity entity:entities){
            entitiesMap.put(entity.getName(),entity.getClz());
        }

        for(Mapping mapping:mappings){
            for (String patten:mapping.getPatterns()){
                mappingsMap.put(patten,mapping.getName());
            }
        }
    }

    /**
     * get the classpath by url (web.xml)
     * @param pattern url
     * @return
     */
    public String getClz(String pattern){
        String name = mappingsMap.get(pattern);
        return entitiesMap.get(name);
    }
}
