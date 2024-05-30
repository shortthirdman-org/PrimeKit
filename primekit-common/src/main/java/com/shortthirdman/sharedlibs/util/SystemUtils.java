package com.shortthirdman.sharedlibs.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class SystemUtils {

    private SystemUtils() {}

    /**
     * Returns the free Java Virtual Machine memory in bytes
     * @return long
     */
    public static long getFreeMemory() {
        Runtime runtime = Runtime.getRuntime();
        long freeMemory = runtime.freeMemory();
        return freeMemory;
    }

    /**
     * Returns the maximum Java Virtual Machine memory in bytes
     * @return long
     */
    public static long getMaxMemory() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        return maxMemory;
    }

    /**
     * Returns the total Java Virtual Machine memory in bytes
     * @return long
     */
    public static long getTotalMemory() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        return totalMemory;
    }

    /**
     * Kill system process
     * @param port the port number of process to kill
     */
    public static void killProcess(int port) {

        int pid = getPid(port);

        if (pid == 0) {
            return;
        }

        String[] command = { "taskkill", "/F", "/T", "/PID", Integer.toString(pid) };
        if (System.getProperty("os.name").startsWith("Linux")) {
            String[] cmd = { "kill", "-9", Integer.toString(pid) };
            command = cmd;
        }

        try {
            Process killer = Runtime.getRuntime().exec(command);
            int result = killer.waitFor();
            System.out.println("Killed pid " + pid + " exitValue: " + result);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int getPid(int port) {
        if (System.getProperty("os.name").startsWith("Windows")) {
            return getPidWin(port);
        } else {
            return getPidLinux(port);
        }
    }

    /**
     * @param port the port number of Windows process
     * @return int
     */
    private static int getPidWin(int port) {
        String[] command = { "netstat", "-on" };
        try {
            Process netstat = Runtime.getRuntime().exec(command);

            StringBuilder connectionList = new StringBuilder();
            Reader reader = new InputStreamReader(netstat.getInputStream());
            char[] buffer = new char[1024];

            for (int n = reader.read(buffer); n != -1; n = reader.read(buffer)) {
                connectionList.append(buffer, 0, n);
            }

            reader.close();
            String[] connections = connectionList.toString().split("\n");
            int portIdx = 10000;
            String pid = null;

            for (String connection : connections) {
                int idx = connection.indexOf(":" + port);

                if (idx == -1 || idx > portIdx) {
                    continue;
                }

                String state = "ESTABLISHED";
                int stateIdx = connection.indexOf(state);

                if (stateIdx == -1) {
                    continue;
                }

                portIdx = idx;
                idx = stateIdx + state.length();
                pid = connection.substring(idx).trim();
            }

            if (pid != null) {
                return Integer.parseInt(pid);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;

    }

    /**
     * @param port the port number of Linux process
     * @return int
     */
    private static int getPidLinux(int port) {
        String[] command = { "netstat", "-anp" };
        try {
            Process netstat = Runtime.getRuntime().exec(command);

            StringBuilder connectionList = new StringBuilder();
            Reader reader = new InputStreamReader(netstat.getInputStream());
            char[] buffer = new char[1024];

            for (int n = reader.read(buffer); n != -1; n = reader.read(buffer)) {
                connectionList.append(buffer, 0, n);
            }
            reader.close();

            String[] connections = connectionList.toString().split("\n");
            String pid = null;

            for (String connection : connections) {
                if (connection.contains(":" + port) && connection.contains("/soffice.bin")) {
                    int idx = connection.indexOf("/soffice.bin");
                    int idx2 = idx;
                    while (Character.isDigit(connection.charAt(--idx2))) {
                        System.out.println("");
                    }
                    pid = connection.substring(idx2 + 1, idx);
                }
            }

            if (pid != null) {
                return Integer.parseInt(pid);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
