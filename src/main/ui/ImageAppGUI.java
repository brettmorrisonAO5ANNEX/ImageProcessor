package ui;

import model.CurrentProjects;
import model.Event;
import model.EventLog;
import model.Image;
import persistence.JsonReader;
import persistence.JsonReaderCurrentProjects;
import persistence.JsonWriter;
import persistence.JsonWriterCurrentProjects;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

//represents the main GUI of image.(in)
public class ImageAppGUI extends JFrame {
    private static final String FILE_BEGIN = "./data/";
    private static final String FILE_END = ".json";
    private CurrentProjects currentProjects;
    private static final String currentProjectsDest = "./data/currentProjects.json";
    private Image myImage;
    private OpenPanel openPanel;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JsonReaderCurrentProjects currentProjectsReader;
    private JsonWriterCurrentProjects currentProjectsWriter;

    //EFFECTS: prints out all events logged during session
    public void printLog(EventLog el) {
        for (Event e: el) {
            System.out.println(e.getDescription());
        }
    }

    //EFFECTS: sets up editing window from which user can choose to create new project or choose to
    //         load previous or view finished projects
    public ImageAppGUI() throws IOException {
        super("image.(in)");

        currentProjectsReader = new JsonReaderCurrentProjects(currentProjectsDest);
        currentProjectsWriter = new JsonWriterCurrentProjects(currentProjectsDest);
        currentProjects = currentProjectsReader.read();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        openPanel = new OpenPanel(this);
        add(openPanel);
        pack();
        setVisible(true);
        setBounds(400, 300, 600, 400);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printLog(EventLog.getInstance());
                System.exit(0);
            }
        });
    }

    public String getFileBegin() {
        return FILE_BEGIN;
    }

    public String getFileEnd() {
        return FILE_END;
    }

    public void setMyImage(Image myImage) {
        this.myImage = myImage;
    }

    public Image getMyImage() {
        return this.myImage;
    }

    public CurrentProjects getProjects() {
        return this.currentProjects;
    }

    public void setJsonWriter(String projectDestination) {
        this.jsonWriter = new JsonWriter(projectDestination);
    }

    public JsonWriter getJsonWriter() {
        return this.jsonWriter;
    }

    public void setJsonReader(String currentProjectsDest) {
        this.jsonReader = new JsonReader(currentProjectsDest);
    }

    public JsonReader getJsonReader() {
        return this.jsonReader;
    }

    public JsonReaderCurrentProjects getCurrentProjectsReader() {
        return this.currentProjectsReader;
    }

    public JsonWriterCurrentProjects getCurrentProjectsWriter() {
        return this.currentProjectsWriter;
    }
}
