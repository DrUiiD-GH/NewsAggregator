package ru.course.aggregator.service.helpers;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.io.StringWriter;


public class YamlParserHelper<T> {

    public T parse(InputStream inputStream, Class<T> classOfMappedObject){
        return new Yaml(new Constructor(classOfMappedObject)).load(inputStream);
    }

    public String getYmlFromObject(T obj){
        Yaml yaml = new Yaml(new Constructor(obj.getClass()));
        StringWriter writer = new StringWriter();
        yaml.dump(obj, writer);

        return writer.toString();
    }
}
