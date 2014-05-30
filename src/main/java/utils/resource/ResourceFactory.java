package utils.resource;

import utils.resource.parser.ResourceXMLParser;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrey
 * 24.04.14.
 */

public class ResourceFactory {
    private static ResourceFactory resourceFactory = null;
    private Map<String, Resource> resourceMap = new HashMap<>();


    public static synchronized ResourceFactory getInstance(){
        if(resourceFactory != null){
            return resourceFactory;
        }
        return resourceFactory = new ResourceFactory();
    }

    public void add(String name, String absPath){
        resourceMap.put(name, (Resource) ResourceXMLParser.readXML(absPath));
    }

    public Resource get(String path){
        Resource resource = resourceMap.get(path);
        if(resource != null){
            return resource;
        }
        resource = (Resource)ResourceXMLParser.readXML(path);
        resourceMap.put(path, resource);
        return resource;
    }

    private ResourceFactory(){}
}