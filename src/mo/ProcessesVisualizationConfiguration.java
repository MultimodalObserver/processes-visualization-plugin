package mo;

import mo.organization.Configuration;
import mo.visualization.Playable;
import mo.visualization.VisualizableConfiguration;

import java.io.File;
import java.util.List;

public class ProcessesVisualizationConfiguration implements VisualizableConfiguration {

    @Override
    public List<String> getCompatibleCreators() {
        return null;
    }

    @Override
    public void addFile(File file) {

    }

    @Override
    public void removeFile(File file) {

    }

    @Override
    public Playable getPlayer() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public File toFile(File file) {
        return null;
    }

    @Override
    public Configuration fromFile(File file) {
        return null;
    }
}
