package ui;

import model.Filter;
import model.Image;
import persistence.JsonWriter;
import persistence.JsonWriterCurrentProjects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

//represents the tool menu panel that is shown while a user is editing
public class ToolMenuPanel extends JPanel {
    private String doRandomize;
    private final ImageAppGUI iaGUI;
    private Image myImage;
    private JPanel lowerPanel;
    private JPanel menuPanel;
    private JPanel historyPanel;
    private AddFilterPanel addFilterPanel;
//    private LoadingPanel loadingPanel;
//    private OriginalIcon originalIcon;

    private JsonWriter jsonWriter;
    private JsonWriterCurrentProjects jsonWriterCurrentProjects;

    public ToolMenuPanel(ImageAppGUI iaGUI) {
        super();
        this.iaGUI = iaGUI;
        this.jsonWriter = iaGUI.getJsonWriter();
        this.jsonWriterCurrentProjects = iaGUI.getCurrentProjectsWriter();
        myImage = iaGUI.getMyImage();

        setBorder(BorderFactory.createEmptyBorder());
        setLayout(new GridLayout(0, 1));
        createAndAddSubPanels();

        int width = iaGUI.getMyImage().getImageWidth();
        int height = iaGUI.getMyImage().getImageHeight();
        this.addFilterPanel = new AddFilterPanel(this, width, height);
    }

    //MODIFIES: this
    //EFFECTS: creates subPanels for tool menu view history to be added to this
    private void createAndAddSubPanels() {
        createLogoPanel();
        createLowerPanels();
    }

    //MODIFIES: this
    //EFFECTS: creates and adds logo panel to this
    private void createLogoPanel() {
        ImageIcon tool = new ImageIcon("./data/UI/tool.png");
        JPanel logoPanel = new JPanel();
        logoPanel.add(new JLabel(tool, JLabel.CENTER));
        logoPanel.setBackground(new Color(206,226,255));

        add(logoPanel);
    }

    //MODIFIES: this
    //EFFECTS: creates and adds lower panels to this
    private void createLowerPanels() {
        lowerPanel = new JPanel();
        lowerPanel.setBorder(BorderFactory.createEmptyBorder());
        lowerPanel.setLayout(new GridLayout(0,2));
        createMenuPanel();
        createHistoryPanel();

        add(lowerPanel);
    }

    //MODIFIES: this
    //EFFECTS: creates menu panel to be added to tool menu panel
    private void createMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setBorder(BorderFactory.createEmptyBorder());
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBackground(new Color(50, 135, 251));

        createButtons();

        lowerPanel.add(menuPanel);
    }

    //MODIFIES: this
    //EFFECTS: adds menu buttons to menuPanel
    private void createButtons() {
        createAddFilterButton();
        createUndoButton();
//        JComboBox undoType = new JComboBox(createOptions());
//        menuPanel.add(undoType);
        createUndoAllButton();
        createProcessButton();
        createRandomizeChooser();
        createSaveButton();
    }

    //MODIFIES: this
    //EFFECTS: creates add filter button such that when pressed, panel changes to add filter panel
    private void createAddFilterButton() {
        GridBagConstraints c = new GridBagConstraints();
        JButton addFilter = new JButton("Add Filter");

        ActionListener addFilterListener = e -> {
            historyPanel.removeAll();
            setVisible(false);
            iaGUI.add(addFilterPanel);
        };

        addFilter.addActionListener(addFilterListener);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;

        menuPanel.add(addFilter, c);
    }

    //MODIFIES: this
    //EFFECTS: creates undo button that undoes most recent filter added
    private void createUndoButton() {
        GridBagConstraints c = new GridBagConstraints();
        JButton undo = new JButton("Undo");

        ActionListener undoListener = e -> {
            historyPanel.removeAll();
            setVisible(false);
            myImage.undoLast();
            updateHistoryPanel();
            setVisible(true);
        };

        undo.addActionListener(undoListener);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;

        menuPanel.add(undo, c);
    }

    //MODIFIES: this
    //EFFECTS: creates undo all button that undoes all filters added
    private void createUndoAllButton() {
        GridBagConstraints c = new GridBagConstraints();
        JButton undoAll = new JButton("Undo All");

        ActionListener undoAllListener = e -> {
            historyPanel.removeAll();
            setVisible(false);
            myImage.undoAll();
            updateHistoryPanel();
            setVisible(true);
        };

        undoAll.addActionListener(undoAllListener);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;

        menuPanel.add(undoAll, c);
    }

    //MODIFIES: this
    //EFFECTS: creates combo box dropdown so user can choose to randomize project color
    public void createRandomizeChooser() {
        GridBagConstraints c = new GridBagConstraints();
        String[] chooseRandomizeOptions = {"Randomize Color?", "yes", "no"};

        ActionListener comboBoxListener = e -> {
            JComboBox cb = (JComboBox) e.getSource();
            doRandomize = (String) cb.getSelectedItem();

            if (doRandomize.equals("yes")) {
                myImage.randomizeColor();
            }
        };

        JComboBox chooseRandomize = new JComboBox(chooseRandomizeOptions);
        chooseRandomize.addActionListener(comboBoxListener);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 3;

        menuPanel.add(chooseRandomize, c);
    }

    //MODIFIES: this
    //EFFECTS: creates process button with action listener to switch to viewResultPanel if pressed
    private void createProcessButton() {
        GridBagConstraints c = new GridBagConstraints();

        ActionListener processListener = e -> {
            if (doRandomize.equals("yes")) {
                iaGUI.getMyImage().randomizeColor();
            }
//            loadingPanel = new LoadingPanel(iaGUI);
            new OriginalIcon(myImage, iaGUI);
            setVisible(false);
        };

        JButton processProject = new JButton("Process Project");
        processProject.addActionListener(processListener);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 3;

        menuPanel.add(processProject, c);
    }

    //MODIFIES: this
    //EFFECTS: creates panel to display history of applied filters
    public void createHistoryPanel() {
        historyPanel = new JPanel();
        historyPanel.setLayout(new GridLayout(0,1));
        historyPanel.setBorder(BorderFactory.createLineBorder(new Color(50, 135, 251), 10));
        historyPanel.setBackground(new Color(79, 153, 253));

        updateHistoryPanel();

        lowerPanel.add(historyPanel);
    }

    //MODIFIES: this
    //EFFECTS: creates history panel to show what edits have been made so far
    public void updateHistoryPanel() {
        createAndAddJLabels();
    }

    //MODIFIES: this
    //EFFECTS: creates JLabel for each applied filter and adds it to historyPanel
    private void createAndAddJLabels() {
        int index = 1;
        for (Filter filter: myImage.getListOfFilter()) {
            JPanel editPanel = new JPanel();
            editPanel.setBackground(new Color(79, 153, 253));
            String name = filter.getFilterName();
            JLabel edit = new JLabel(index + ": " + name);
            editPanel.add(edit);
            historyPanel.add(editPanel);
            index++;
        }
    }

    //MODIFIES: this
    //EFFECTS: creates quit and save button that saves current edit progress
    private void createSaveButton() {
        GridBagConstraints c = new GridBagConstraints();
        JButton quitAndSave = new JButton("Save Progress");

        ActionListener quitListener = e -> {
            try {
                jsonWriter.open();
                jsonWriter.write(myImage);
                jsonWriter.close();
                jsonWriterCurrentProjects.open();
                jsonWriterCurrentProjects.write(iaGUI.getProjects());
                jsonWriterCurrentProjects.close();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        };

        quitAndSave.addActionListener(quitListener);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 4;

        menuPanel.add(quitAndSave, c);
    }

    public ImageAppGUI getImageAppGUI() {
        return this.iaGUI;
    }
}
