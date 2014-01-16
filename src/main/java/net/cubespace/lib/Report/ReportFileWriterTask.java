package net.cubespace.lib.Report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 26.12.13 11:34
 */
public class ReportFileWriterTask implements Runnable {
    private FileWriter writer;
    private LinkedBlockingQueue<String> linesToWrite = new LinkedBlockingQueue<>();
    private Boolean closed = false;

    public ReportFileWriterTask(File reportFile) {
        //Open the FileWriter
        try {
            writer = new FileWriter(reportFile);
        } catch (IOException e) {
            throw new RuntimeException("Could not open the File for writing", e);
        }
    }

    @Override
    public void run() {
        while (!closed || linesToWrite.size() > 0) {
            try {
                String line = linesToWrite.poll(10, TimeUnit.MILLISECONDS);
                if(line != null)
                    writer.write(line + "\n");
            } catch (Exception e) {

            }
        }

        try {
            writer.close();
        } catch (IOException e) {

        }
    }

    /**
     * Add a new Line to the Writer Queue. If the Session was closed this will not append the new Line into
     * the Queue
     *
     * @param line
     */
    public synchronized void addLineToWrite(String line) {
        if(!closed) {
            linesToWrite.add(line);
        }
    }

    /**
     * If you don't want to accept new Lines and want the Thread to exit if the Queue is empty
     */
    public void close() {
        closed = true;
    }
}
