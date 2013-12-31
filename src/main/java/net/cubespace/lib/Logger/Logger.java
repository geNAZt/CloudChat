package net.cubespace.lib.Logger;

import net.cubespace.lib.CubespacePlugin;

import java.util.Date;

/**
 * Created by Fabian on 23.12.13.
 */
public class Logger {
    //Hold the LogLevel for this Logger
    private Level logLevel = Level.INFO;
    //Hold the plugin for this Logger (to get access to the ReportManager)
    private CubespacePlugin plugin;

    /**
     * Create a new Logger for the Plugin
     *
     * @param plugin
     */
    public Logger(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Write a line into the Reporter Session
     *
     * @param line Line to write into the Report
     */
    private void writeToReport(String line) {
        plugin.getReportManager().getOpenSession().write(plugin.getSimpleDateFormat().format(new Date()) + line);
    }

    /**
     * Print a whole Throwable with all Causes and Stacktraces into the Report
     *
     * @param logLevel The Loglevel under which this Throwable should be saved
     * @param throwable The Throwable to report
     */
    private void writeExceptionToReport(String logLevel, Throwable throwable) {
        writeToReport("  [Logger/" + logLevel + "] Exception in Thread \"" + Thread.currentThread().getName() + "\" " + throwable.getClass().getName() + ": " + throwable.getMessage());

        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        for(StackTraceElement element : stackTraceElements) {
            writeToReport("  [Logger/" + logLevel + "]   at " + element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() +":" + element.getLineNumber() +")");
        }

        //Print all Exceptions which are appended on this one
        if(throwable.getCause() != null) {
            writeToReport("  [Logger/" + logLevel + "] caused by: ");
            writeExceptionToReport(logLevel, throwable.getCause());
        }
    }

    /**
     * Change the Loglevel for this Logger so he outputs more or lesser
     * @param logLevel
     */
    public void setLogLevel(Level logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * Get the current set Loglevel
     * @return
     */
    public Level getLogLevel() {
        return logLevel;
    }

    /**
     * Log a debug message together with a Exception
     *
     * @param message The message to Log
     * @param throwable The Throwable to print
     */
    public void debug(String message, Throwable throwable) {
        //Check if LogLevel is ok
        if(logLevel.getLevelCode() < Level.DEBUG.getLevelCode()) return;

        //Check if Report is open and if so write this Log Message to it
        if(plugin.getReportManager().isSessionOpen()) {
            writeToReport("  [Logger/DEBUG] " + message);
            writeExceptionToReport("DEBUG", throwable);
        }

        //Print the Message to the BungeeCord logger
        plugin.getLogger().log(java.util.logging.Level.INFO, message, throwable);
    }

    /**
     * Log a debug Message
     *
     * @param message The message to Log
     */
    public void debug(String message) {
        //Check if LogLevel is ok
        if(logLevel.getLevelCode() < Level.DEBUG.getLevelCode()) return;

        //Check if Report is open and if so write this Log Message to it
        if(plugin.getReportManager().isSessionOpen()) {
            writeToReport("  [Logger/DEBUG] " + message);
        }

        //Print the Message to the BungeeCord logger
        plugin.getLogger().log(java.util.logging.Level.INFO, message);
    }

    /**
     * Log a info message together with a Exception
     *
     * @param message The message to Log
     * @param throwable The Throwable to print
     */
    public void info(String message, Throwable throwable) {
        //Check if LogLevel is ok
        if(logLevel.getLevelCode() < Level.INFO.getLevelCode()) return;

        //Check if Report is open and if so write this Log Message to it
        if(plugin.getReportManager().isSessionOpen()) {
            writeToReport("  [Logger/INFO] " + message);
            writeExceptionToReport("INFO", throwable);
        }

        //Print the Message to the BungeeCord logger
        plugin.getLogger().log(java.util.logging.Level.INFO, message, throwable);
    }

    /**
     * Log a info Message
     *
     * @param message The message to Log
     */
    public void info(String message) {
        //Check if LogLevel is ok
        if(logLevel.getLevelCode() < Level.INFO.getLevelCode()) return;

        //Check if Report is open and if so write this Log Message to it
        if(plugin.getReportManager().isSessionOpen()) {
            writeToReport("  [Logger/INFO] " + message);
        }

        //Print the Message to the BungeeCord logger
        plugin.getLogger().log(java.util.logging.Level.INFO, message);
    }

    /**
     * Log a warn message together with a Exception
     *
     * @param message The message to Log
     * @param throwable The Throwable to print
     */
    public void warn(String message, Throwable throwable) {
        //Check if LogLevel is ok
        if(logLevel.getLevelCode() < Level.WARN.getLevelCode()) return;

        //Check if Report is open and if so write this Log Message to it
        if(plugin.getReportManager().isSessionOpen()) {
            writeToReport("  [Logger/WARN] " + message);
            writeExceptionToReport("WARN", throwable);
        }

        //Print the Message to the BungeeCord logger
        plugin.getLogger().log(java.util.logging.Level.WARNING, message, throwable);
    }

    /**
     * Log a warn Message
     *
     * @param message The message to Log
     */
    public void warn(String message) {
        //Check if LogLevel is ok
        if(logLevel.getLevelCode() < Level.WARN.getLevelCode()) return;

        //Check if Report is open and if so write this Log Message to it
        if(plugin.getReportManager().isSessionOpen()) {
            writeToReport("  [Logger/WARN] " + message);
        }

        //Print the Message to the BungeeCord logger
        plugin.getLogger().log(java.util.logging.Level.WARNING, message);
    }

    /**
     * Log a error message together with a Exception
     *
     * @param message The message to Log
     * @param throwable The Throwable to print
     */
    public void error(String message, Throwable throwable) {
        //Check if LogLevel is ok
        if(logLevel.getLevelCode() < Level.ERROR.getLevelCode()) return;

        //Check if Report is open and if so write this Log Message to it
        if(plugin.getReportManager().isSessionOpen()) {
            writeToReport("  [Logger/ERROR] " + message);
            writeExceptionToReport("ERROR", throwable);
        }

        //Print the Message to the BungeeCord logger
        plugin.getLogger().log(java.util.logging.Level.SEVERE, message, throwable);
    }

    /**
     * Log a error Message
     *
     * @param message The message to Log
     */
    public void error(String message) {
        //Check if LogLevel is ok
        if(logLevel.getLevelCode() < Level.ERROR.getLevelCode()) return;

        //Check if Report is open and if so write this Log Message to it
        if(plugin.getReportManager().isSessionOpen()) {
            writeToReport("  [Logger/ERROR] " + message);
        }

        //Print the Message to the BungeeCord logger
        plugin.getLogger().log(java.util.logging.Level.SEVERE, message);
    }
}
