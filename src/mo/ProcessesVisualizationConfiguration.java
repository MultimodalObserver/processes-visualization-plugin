package mo;

import mo.organization.Configuration;
import mo.visualization.Playable;
import mo.visualization.VisualizableConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ProcessesVisualizationConfiguration implements VisualizableConfiguration {

    private static final Logger LOGGER = Logger.getLogger(ProcessesVisualizationConfiguration.class.getName());
    private VisualizationConfiguration temporalConfig;
    private List<File> files;
    private ProcessesPlayer player;

    public ProcessesVisualizationConfiguration(VisualizationConfiguration temporalConfig) {
        this.temporalConfig = temporalConfig;
        this.files = new ArrayList<>();
        this.player =null;
    }

    public ProcessesVisualizationConfiguration(File file){
        String fileName = file.getName();
        String configData = fileName.substring(0, fileName.lastIndexOf("."));
        String[] configElements = configData.split("_");
        /* El elemento 0 es la palabra processes-visualization*/
        String configurationName = configElements[1];
        this.temporalConfig  = new VisualizationConfiguration(configurationName);
        this.files = new ArrayList<>();
        this.player = null;
    }

    public VisualizationConfiguration getTemporalConfig() {
        return temporalConfig;
    }

    @Override
    public List<String> getCompatibleCreators() {
        String[] array = {"mo.controllers.ProcessRecorder"};
        return Arrays.asList(array);
    }

    @Override
    public void addFile(File file) {
        if(this.files.contains(file)){
            return;
        }
        this.files.add(file);
        int fileIndex = files.size() - 1;
        this.player = new ProcessesPlayer(this.files.get(fileIndex));
    }

    @Override
    public void removeFile(File file) {
        if(!this.files.contains(file)){
            return;
        }
        this.files.remove(file);
    }

    @Override
    public Playable getPlayer() {
        if(this.player == null){
            this.player = new ProcessesPlayer(this.files.get(0));
        }
        return this.player;
    }

    @Override
    public String getId() {
        return this.temporalConfig.getName();
    }

    @Override
    public File toFile(File parent) {
        String childFileName = "processes-visualization_"+this.temporalConfig.getName()+".xml";
        File file = new File(parent, childFileName);
        try {
            file.createNewFile();
            return file;
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Configuration fromFile(File file) {
        String fileName = file.getName();
        if(!fileName.contains("_") || !fileName.contains(".")){
            return null;
        }
        String configData = fileName.substring(0, fileName.lastIndexOf("."));
        String[] configElements = configData.split("_");
        /* El elemento 0 es processes*/
        String configurationName = configElements[1];
        VisualizationConfiguration auxConfig = new VisualizationConfiguration(configurationName);
        return new ProcessesVisualizationConfiguration(auxConfig);
    }

}
