/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.video.utils;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.swing.SwingUtilities;
import net.landora.video.ui.ContextProducer;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bdickie
 */
public class UIUtils {
    
    public static final Comparator LEXICAL_SORTER = new LexicalSorter();
    public static final Comparator COMPARABLE_SORTER = new ComparableSorter();
    
    public static Object unwrap(Object obj) {
        if (obj instanceof Representation) {
            Representation rep  = (Representation)obj;
            return unwrap(rep.getValue());
        } else
            return obj;
    }
    
    private static class LexicalSorter implements Comparator<Object> {

        public int compare(Object o1, Object o2) {
            if (o1 == o2)
                return 0;
            else if (o1 == null)
                return -1;
            else if (o2 == null)
                return 1;
            else
                return o1.toString().compareToIgnoreCase(o2.toString());
        }
        
    }
    
    private static class ComparableSorter<T extends Comparable> implements Comparator<T> {

        public int compare(T o1, T o2) {
            if (o1 == o2)
                return 0;
            else if (o1 == null)
                return -1;
            else if (o2 == null)
                return 1;
            else
                return o1.compareTo(o2);
        }
        
    }
    
    public static void invokeLaterInSwingThread(Runnable r) {
        if (EventQueue.isDispatchThread())
            r.run();
        else
            SwingUtilities.invokeLater(r);
    }
    
    public static void invokeInSwingThread(Runnable r) {
        if (EventQueue.isDispatchThread())
            r.run();
        else
            try {
            SwingUtilities.invokeAndWait(r);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static <T> T select(Collection<T> collection) {
        if (collection == null || collection.isEmpty())
            return null;
        else
            return collection.iterator().next();
    }
    
    public static void addContentObject(Object obj, Collection<Object> addTo) {
        if (obj == null)
            return;
        
        if (obj instanceof ContextProducer) {
            ((ContextProducer)obj).addContentObjects(addTo);
        } else
            addTo.add(obj);
    }
    
    
    public static Collection<Object> createCompleteContext(Collection<?> context) {
        if (context == null || context.isEmpty())
            return Collections.EMPTY_SET;
        
        Collection<Object> result = new LinkedHashSet<Object>();
        for(Object obj: context) {
            addContentObject(obj, result);
        }
        return result;
    }
    
    public static MultiValueMap createCompleteContextByClass(Collection<?> context) {
        Collection<Object> fullContext = UIUtils.createCompleteContext(context);
        
        MultiValueMap valuesByClass = new MultiValueMap();
        Set<Class<?>> allClasses = new HashSet<Class<?>>();
        for(Object obj: fullContext) {
            Class<?> clazz = obj.getClass();
            allClasses.clear();
            while(clazz != null) {
                allClasses.add(clazz);
                allClasses.addAll(Arrays.asList(clazz.getInterfaces()));
                clazz = clazz.getSuperclass();
            }
            
            for(Class<?> c: allClasses)
                valuesByClass.put(c, obj);
        }
        return valuesByClass;
    }
    
    public static void openBrowser(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
            return;
        } catch (Exception ignore) { }
        
        List<String> cmd = new ArrayList<String>();
        
        if (SystemUtils.IS_OS_MAC) {
            cmd.add("open");
        } else if (SystemUtils.IS_OS_WINDOWS) {
            cmd.add("cmd");
            cmd.add("/c");
            cmd.add("start");
        } else {
            cmd.add("xdg-open");
        }
        
        cmd.add(url);
        try {
            Runtime.getRuntime().exec(cmd.toArray(new String[cmd.size()]));
        } catch (IOException ex) {
            LoggerFactory.getLogger(UIUtils.class).error("Error opening browsner: " + url, ex);
        }
    }
}