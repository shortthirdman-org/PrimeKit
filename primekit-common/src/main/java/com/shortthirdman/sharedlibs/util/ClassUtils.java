package com.shortthirdman.sharedlibs.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A collection of class management utility methods.
 */
public class ClassUtils {

    private ClassUtils() {

    }

    /**
     * Return a resource URL.
     * BL: if this is command line operation, the classloading issues
     * are more sane.  During servlet execution, we explicitly set
     * the ClassLoader.
     *
     * @return The context classloader.
     * @throws MalformedURLException If a loading error occurs
     */
    public static URL getResource(String resource) throws MalformedURLException {
        return getClassLoader().getResource(resource);
    }

    /**
     * Return the context classloader.
     * BL: if this is command line operation, the classloading issues
     * are more sane.  During servlet execution, we explicitly set
     * the ClassLoader.
     *
     * @return The context classloader.
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * Load a given resource.
     * <p/>
     * This method will try to load the resource using the following methods (in order):
     * <ul>
     * <li>From {@link Thread#getContextClassLoader() Thread.currentThread().getContextClassLoader()}
     * <li>From {@link Class#getClassLoader() ClassLoaderUtil.class.getClassLoader()}
     * <li>From the {@link Class#getClassLoader() callingClass.getClassLoader() }
     * </ul>
     *
     * @param resourceName The name of the resource to load
     * @param caller       The Class object of the calling object
     */
    public static URL getResource(String resourceName, Class<?> caller) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);

        if (url == null) {
            url = ClassUtils.class.getClassLoader().getResource(resourceName);
        }

        if (url == null) {
            url = caller.getClassLoader().getResource(resourceName);
        }

        return url;
    }

    /**
     * This is a convenience method to load a resource as a stream.
     * <p/>
     * The algorithm used to find the resource is given in getResource()
     *
     * @param resourceName The name of the resource to load
     * @param caller       The Class object of the calling object
     */
    public static InputStream getResourceAsStream(String resourceName, Class<?> caller) {
        URL url = getResource(resourceName, caller);
        try {
            return (url != null) ? url.openStream() : null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Load a class with a given name.
     * <p/>
     * It will try to load the class in the following order:
     * <ul>
     * <li>From {@link Thread#getContextClassLoader() Thread.currentThread().getContextClassLoader()}
     * <li>Using the basic {@link Class#forName(java.lang.String) }
     * <li>From {@link Class#getClassLoader() ClassLoaderUtil.class.getClassLoader()}
     * <li>From the {@link Class#getClassLoader() callingClass.getClassLoader() }
     * </ul>
     *
     * @param className The name of the class to load
     * @param caller    The Class object of the calling object
     * @throws ClassNotFoundException If the class cannot be found anywhere.
     */
    public static Class<?> loadClass(String className, Class<?> caller) throws ClassNotFoundException {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException ex) {
                try {
                    return ClassUtils.class.getClassLoader().loadClass(className);
                } catch (ClassNotFoundException cnfe) {
                    return caller.getClassLoader().loadClass(className);
                }
            }
        }
    }
}
