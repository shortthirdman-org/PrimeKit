package com.shortthirdman.sharedlibs.util;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class FileUtils {

    /**
     * @param sourceFile the source file
     * @param targetFile the target file
     */
    public static void gzipCompress(String sourceFile, String targetFile) {
        try (FileOutputStream fos = new FileOutputStream(targetFile);
             GZIPOutputStream gzos = new GZIPOutputStream(fos);
             FileInputStream fis = new FileInputStream(sourceFile)) {

            byte[] buffer = new byte[1024];
            int length;

            while ((length = fis.read(buffer)) > 0) {
                gzos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param sourceFile
     * @param targetFile
     */
    public static void gzipDecompress(String sourceFile, String targetFile) {
        try (FileInputStream fis = new FileInputStream(sourceFile);
             GZIPInputStream gzis = new GZIPInputStream(fis);
             FileOutputStream fos = new FileOutputStream(targetFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = gzis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            System.err.println(e.getCause() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @param source
     * @param destination
     */
    public static void copyFiles(File source, File destination) {
        try (InputStream is = Files.newInputStream(source.toPath()); OutputStream os = Files.newOutputStream(destination.toPath())) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getCause() + ": " + ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    /**
     * @param source
     * @param destination
     * @throws IOException
     */
    public static void copyFile(String source, String destination) throws IOException {
        try (InputStream is = Files.newInputStream(new File(source).toPath()); OutputStream os = Files.newOutputStream(new File(destination).toPath())) {
            int length;
            byte[] bytes = new byte[1024];
            // copy data from input stream to output stream
            while ((length = is.read(bytes)) != -1) {
                os.write(bytes, 0, length);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param source
     * @param destination
     * @throws IOException
     */
    public static void copyFile(File source, File destination) throws IOException {
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(destination);
             FileChannel sourceChannel = fis.getChannel();
             FileChannel destChannel = fos.getChannel()) {
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }
    }

    /**
     * @param dir
     */
    public static boolean removeDirectory(File dir) {
        boolean result = false;
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File aFile : files) {
                    removeDirectory(aFile);
                }
            }
            result = dir.delete();
        } else {
            result = dir.delete();
        }
        return result;
    }

    /**
     * @param dir
     */
    public static void cleanDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File aFile : files) {
                    removeDirectory(aFile);
                }
            }
        }
    }
}
