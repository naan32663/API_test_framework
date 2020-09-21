package com.yeepay.base.koala.util;

import com.yeepay.base.koala.annotation.ChildrenConfig;
import com.yeepay.base.koala.annotation.ConfigElement;
import com.yeepay.base.koala.annotation.Property;
import com.yeepay.base.koala.command.CommandFactory;
import com.yeepay.base.koala.config.StepConfig;
import com.yeepay.base.koala.model.KeyValueStore;
import com.yeepay.koala.koala.dsl.DSLCommandConfig;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.lang.reflect.Field;
import java.util.*;

import static com.yeepay.base.koala.util.PropertyUtils.replaceConfigValue;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public class ConfigUtils {
    public static <T> T init(Class<? extends T> clazz, Element element) {
        T target = ReflectionUtils.newInstance(clazz);
        setCommandName(target, element);
        setDefaultProperty(target, clazz, element);
        List<Field> fields = ReflectionUtils.getAllFields(clazz);
        for (Field field : fields) {
            if (getDefaultProperty(clazz).equals(field.getName())) {
                continue;
            }
            if (ReflectionUtils.isConstant(field)) {
                continue;
            }
            if (target instanceof DSLCommandConfig) {
                if (field.isAnnotationPresent(com.yeepay.base.koala.annotation.Element.class)) {
                    List<KeyValueStore> params = getAllParams(element);
                    Iterator iterator = element.attributeIterator();
                    while (iterator.hasNext()) {
                        Attribute attribute = (Attribute) iterator.next();
                        params.add(new KeyValueStore(attribute.getName(), attribute.getValue()));
                    }
                    ReflectionUtils.setFieldValue(target, field, params);
                }
            } else {
                initProperty(target, element, field);
                initElementProperty(target, element, field);
                initConfigChildren(target, element, field);
            }
        }
        return target;
    }

    private static <T> void setCommandName(T target, Element element) {
        if (target instanceof StepConfig) {
            ReflectionUtils.setFieldValue(target, "commandName", element.getName());
        }
    }

    private static void setDefaultProperty(Object target, Class clazz, Element element) {
        if (clazz.isAnnotationPresent(ConfigElement.class)) {
            ConfigElement annotation = (ConfigElement) clazz.getAnnotation(ConfigElement.class);
            String propertyName = annotation.defaultProperty();
            if (isNotBlank(propertyName) && isNotBlank(element.getTextTrim())) {
                Field field = ReflectionUtils.getField(clazz, propertyName);
                ReflectionUtils.setFieldValue(target, field, getValue(element));
            }
        }
    }

    private static String getDefaultProperty(Class clazz) {
        if (clazz.isAnnotationPresent(ConfigElement.class)) {
            ConfigElement annotation = (ConfigElement) clazz.getAnnotation(ConfigElement.class);
            return annotation.defaultProperty();
        }
        return StringUtils.EMPTY;
    }

    private static <T> void initProperty(T target, Element element, Field field) {
        if (field.isAnnotationPresent(Property.class)) {
            String value = getValueByName(element, field);
            Property property = field.getAnnotation(Property.class);
            if (property.required() && StringUtils.isBlank(value)) {
                throw new RuntimeException(String.format("必须为%s的%s字段提供值", target.getClass().getName(), field.getName()));
            }
            ReflectionUtils.setFieldValue(target, field, value);
        }
    }

    private static <T> void initElementProperty(T target, Element element, Field field) {
        if (field.isAnnotationPresent(com.yeepay.base.koala.annotation.Element.class)) {
            List<KeyValueStore> params = getAllParams(element);
            ReflectionUtils.setFieldValue(target, field, params);
        }
    }

    private static List<KeyValueStore> getAllParams(Element element) {
        Iterator iterator = element.elementIterator();
        List<KeyValueStore> params = new ArrayList<KeyValueStore>();
        while (iterator.hasNext()) {
            params.addAll(getParams((Element) iterator.next()));
        }
        return params;
    }

    private static List<KeyValueStore> getParams(Element element) {

        if (isListElement(element)) {
            ArrayList<KeyValueStore> list = new ArrayList<KeyValueStore>();
            list.add(new KeyValueStore(element.getName(), getList(element)));
            return list;
        }
        if (hasAttribute(element)) {
            return getAllAttribute(element);
        }
        if (hasChildren(element)) {
            return Arrays.asList(new KeyValueStore(element.getName(), getAllChildrenMap(element)));
        }
        if (element.isTextOnly()) {
            return getElementBody(element);
        }
        return Collections.emptyList();
    }

    private static boolean isListElement(Element element) {
        return element.getName().equals("list")
                && element.attributeCount() == 0;
    }

    private static Map<String, Object> getAllChildrenMap(Element element) {
        List<KeyValueStore> result = getList(element);
        return convertKeyValueStoreToMap(result);
    }

    private static List<KeyValueStore> getList(Element element) {
        Iterator iterator = element.elementIterator();
        List<KeyValueStore> result = new ArrayList<KeyValueStore>();
        while (iterator.hasNext()) {
            Element child = (Element) iterator.next();
            List<KeyValueStore> params = getParams(child);
            result.addAll(params);
        }
        return result;
    }

    protected static Map<String, Object> convertKeyValueStoreToMap(List<KeyValueStore> params) {
        Map<String, Object> result = new HashMap<String, Object>();
        for (KeyValueStore kvs : params) {
            result.put(kvs.getName(), kvs.getValue());
        }
        return result;
    }

    private static <T> void initConfigChildren(T target, Element element, Field field) {
        if (field.isAnnotationPresent(ChildrenConfig.class)) {
            ReflectionUtils.setFieldValue(target, field, getChildrenConfig(element));
        }
    }

    private static List<StepConfig> getChildrenConfig(Element element) {
        Iterator iterator = element.elementIterator();
        List<StepConfig> result = new ArrayList<StepConfig>();
        while (iterator.hasNext()) {
            Element configElement = (Element) iterator.next();
            Class<? extends StepConfig> configClass = CommandFactory.getInstance().getConfig(configElement.getName());
            if (configClass != null) {
                StepConfig config = ConfigUtils.init(configClass, configElement);
                result.add(config);
            }
        }
        return result;
    }

    protected static List<KeyValueStore> getElementBody(Element element) {
        String name = element.getName();
        String value = getValue(element);
        return Arrays.asList(new KeyValueStore(name, value));
    }

    private static List<KeyValueStore> getAllAttribute(Element element) {
        List<KeyValueStore> result = new ArrayList<KeyValueStore>();
        Iterator iterator = element.attributeIterator();
        while (iterator.hasNext()) {
            Attribute attribute = (Attribute) iterator.next();
            //TODO fix me only attribute name
            if (isNameValuePair(element, attribute)) {
                result.add(new KeyValueStore(attribute.getValue(), getValue(element)));
            } else if (isSingleValue(element, attribute)) {
                result.add(new KeyValueStore(element.getName(), getValue(attribute)));
            } else {
                result.add(new KeyValueStore(attribute.getName(), getValue(attribute)));
            }
        }
        return result;
    }

    private static boolean isSingleValue(Element element, Attribute attribute) {
        return element.attributeCount() == 1 && "value".equals(attribute.getName());
    }

    private static boolean isNameValuePair(Element element, Attribute attribute) {
        return element.attributeCount() == 1 && "name".equals(attribute.getName());
    }

    protected static boolean hasChildren(Element element) {
        List elements = element.elements();
        return elements != null && elements.size() > 0;
    }

    protected static boolean hasAttribute(Element element) {
        return element.attributeCount() > 0;
    }

    private static String getValueByName(Element element, Field field) {
        Property annotation = field.getAnnotation(Property.class);
        String name = getName(field, annotation);
        if (!hasAttribute(element, name)) {
            String value = getChildrenValue(element, name);
            return value == null ? replaceConfigValue(annotation.defaultValue()) : value;
        }
        return getAttribute(element, name);
    }

    private static String getChildrenValue(Element element, String name) {
        Element e = element.element(name);
        if (e == null) {
            return null;
        }
        return getValue(e);
    }

    private static String getAttribute(Element node, String name) {
        Attribute attribute = node.attribute(name);
        if (attribute != null) {
            return getValue(attribute);
        }
        return null;
    }

    private static Boolean hasAttribute(Element element, String name) {
        return getAttribute(element, name) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    private static String getName(Field field, Property annotation) {
        if (isNotBlank(annotation.value())) {
            return annotation.value();
        }
        return field.getName();
    }

    private static String getValue(Attribute attribute) {
        return replaceConfigValue(attribute.getValue());
    }

    private static String getValue(Element element) {
        return replaceConfigValue(StringUtils.trim(element.getText()));
    }
}
