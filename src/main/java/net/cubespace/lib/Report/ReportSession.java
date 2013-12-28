package net.cubespace.lib.Report;

import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.plugin.PluginDescription;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Fabian on 23.12.13.
 */
public class ReportSession {
    //The Plugin for which the Report is
    private CubespacePlugin plugin;
    //The ReportFileWriterTask used
    private ReportFileWriterTask reportFileWriterTask;

    /**
     * Create a new ReportSession for writing a Report
     *
     * @param plugin     The Plugin for which the Report should be written
     * @param reportName The Report Filename
     */
    public ReportSession(CubespacePlugin plugin, String reportName) {
        //Check if the Report Directory is there
        File reportDir = new File(plugin.getDataFolder(), "reports" + File.separator);

        if (!reportDir.exists()) {
            if (!reportDir.mkdirs()) {
                throw new RuntimeException("Could not create the Reports Directory. Please check your File Permissions");
            }
        }

        //Create a new Report File
        File reportFile = new File(reportDir, reportName + ".txt");

        if (!reportFile.exists()) {
            try {
                if (!reportFile.createNewFile()) {
                    throw new RuntimeException("Could not create new Report File. Please check your File Permissions");
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not create new Report File. Please check your File Permissions", e);
            }
        }

        this.plugin = plugin;

        //Start the Writer Task
        reportFileWriterTask = new ReportFileWriterTask(reportFile);
        plugin.getProxy().getScheduler().runAsync(plugin, reportFileWriterTask);

        writeHeader();
    }

    private void writeHeader() {
        //Get Plugin information
        PluginDescription pluginDescription = plugin.getDescription();
        write("--- Report File from " + plugin.getSimpleDateFormat().format(new Date()) + " --");
        write("Plugin: " + pluginDescription.getName() + "; Version: " + pluginDescription.getVersion());
        write("BungeeCord: " + plugin.getProxy().getVersion() + "; Java: " + System.getProperty("java.version"));
        write("");
    }

    /**
     * Write a new Line into the Report
     *
     * @param line The line which should be written
     */
    public synchronized void write(final String line) {
        reportFileWriterTask.addLineToWrite(line);
    }

    /**
     * Close the Report
     */
    public void close() {
        reportFileWriterTask.close();
    }
}
