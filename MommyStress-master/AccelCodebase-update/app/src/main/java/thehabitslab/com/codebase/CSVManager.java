package thehabitslab.com.codebase;

import android.hardware.SensorEvent;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import android.util.Log;

/**
 * Handles saving CSV data to external memory
 * Created by William on 1/9/2018.
 */

public class CSVManager {

    private String fileName;
    private long sessionStart;

    CSVManager() {
        // Use current time to name the file
        Calendar c = Calendar.getInstance();

        fileName = "Session " + Integer.toString(c.get(Calendar.MONTH)+1) + "-"
                + Integer.toString(c.get(Calendar.DAY_OF_MONTH)) + "-"
                + Integer.toString(c.get(Calendar.YEAR)) + " "
                + Integer.toString(c.get(Calendar.HOUR_OF_DAY)) + ":"
                + Integer.toString(c.get(Calendar.MINUTE)) + ".csv";
    }

    /**
     * Gets the default directory for saving the data
     * @return File object representing the directory
     */
    private File getDirectory() {
        // Build file path
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String dirPath = baseDir + File.separator + "Mobile Health" + File.separator;
        return new File(dirPath);
    }

    /**
     * Gets the file for saving the data
     * @param dir File representing the directory to save the data file in
     * @return File object representing teh data file
     */
    private File getDataFile(File dir) {
        String filePath = dir.getPath() + File.separator + fileName;
        return new File(filePath);
    }


    public String getDataAsString() {
        File dir = getDirectory();
        File dataFile = getDataFile(dir);


        if (!dataFile.exists()) return "";

        try {
            CSVReader reader = new CSVReader(new FileReader(dataFile));
            List<String[]> output = reader.readAll();

            StringBuilder builder = new StringBuilder();
            for (String[] line : output) {
                builder.append(line[0]);
                builder.append("  ");
                builder.append(line[1]);
                builder.append("  ");
                builder.append(line[2]);
                builder.append("  ");
                builder.append(line[3]);
                builder.append("\n");
            }
            return builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    void saveData(List<SensorData> data) {
        File dir = getDirectory();
        File f = getDataFile(dir);

        CSVWriter writer;
        // File exists
        try {
            if (!dir.exists()) {
                //noinspection ResultOfMethodCallIgnored
                dir.mkdirs();
            }
            if (f.exists() && !f.isDirectory()) {
                FileWriter mFileWriter = new FileWriter(f.getPath(), true);
                writer = new CSVWriter(mFileWriter);
            } else {
                //noinspection ResultOfMethodCallIgnored
                f.createNewFile();
                writer = new CSVWriter(new FileWriter(f.getPath()));
                // Write header
                String[] header = {
                        "Time",
                        "AccelX",
                        "AccelY",
                        "AccelZ"
                };

                writer.writeNext(header);
            }

            for (SensorData datum : data) {
                Log.v("Sensor", datum.timestamp + " " + datum.values[0] + " " + datum.values[1] + " " + datum.values[2]);

                String[] row = {
                        String.valueOf(datum.timestamp),
                        String.valueOf(datum.values[0]),
                        String.valueOf(datum.values[1]),
                        String.valueOf(datum.values[2])
                };

                writer.writeNext(row);
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
