package net.cubespace.lib.Report;

import net.cubespace.lib.CubespacePlugin;

public class ReportManager {
    //Currently open Reports
    private ReportSession reportSession;
    //Hold the Plugin instance
    private CubespacePlugin plugin;

    public ReportManager(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates a new ReportSession
     *
     * @param reportName The name for the Report
     */
    public void openSession(String reportName) {
        reportSession = new ReportSession(plugin, reportName);
    }

    /**
     * Check if Session is open
     * @return True if open
     */
    public boolean isSessionOpen() {
        return (reportSession != null);
    }

    /**
     * Get the current open ReportSession
     *
     * @return The current opened ReportSession or null
     */
    public ReportSession getOpenSession() {
        return reportSession;
    }

    /**
     * Close the Report Session and give back the written File
     */
    public void closeSession() {
        if(this.reportSession != null) {
            this.reportSession.close();
            this.reportSession = null;
        }
    }
}
