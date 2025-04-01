package com.shortthirdman.primekit.essentials.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class FileUtils {

    private FileUtils() {
    }

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
            throw new RuntimeException(ioe);
        }
    }

    /**
     * @param source the source file name or path
     * @param destination the destination file name or path
     * @throws IOException
     */
    public static void copyFile(String source, String destination) throws IOException {
        try (InputStream is = Files.newInputStream(new File(source).toPath());
             OutputStream os = Files.newOutputStream(new File(destination).toPath())) {
            int length;
            byte[] bytes = new byte[1024];
            // copy data from input stream to output stream
            while ((length = is.read(bytes)) != -1) {
                os.write(bytes, 0, length);
            }
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    /**
     * @param source the source file
     * @param destination the destination file
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
     * @param directory the directory to delete
     */
    public static boolean removeDirectory(File directory) {
        boolean result = false;
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File aFile : files) {
                    removeDirectory(aFile);
                }
            }
            result = directory.delete();
        } else {
            result = directory.delete();
        }

        return result;
    }

    /**
     * @param directory the directory to clean
     */
    public static void cleanDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File aFile : files) {
                    removeDirectory(aFile);
                }
            }
        }
    }
}
