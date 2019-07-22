package mo.plugin;

import bibliothek.util.xml.XElement;
import bibliothek.util.xml.XIO;
import mo.core.I18n;
import mo.core.plugin.Extends;
import mo.core.plugin.Extension;
import mo.organization.Configuration;
import mo.organization.ProjectOrganization;
import mo.organization.StagePlugin;
import mo.plugin.views.ProcessesVisualizationConfigurationDialog;
import mo.visualization.VisualizationProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Extension(
        xtends = {
                @Extends(
                        extensionPointId = "mo.visualization.VisualizationProvider"
                )
        }
)

public class ProcessesVisualizationPlugin implements VisualizationProvider {

    private static final Logger logger = Logger.getLogger(ProcessesVisualizationPlugin.class.getName());
    private I18n i18n;
    List<Configuration> configurations;

    public ProcessesVisualizationPlugin(){
        this.configurations = new ArrayList<>();
        this.i18n = new I18n(ProcessesVisualizationPlugin.class);
    }

    @Override
    public String getName() {
        return this.i18n.s("processesVisualizationPluginDisplayedName");
    }

    @Override
    public Configuration initNewConfiguration(ProjectOrganization projectOrganization) {
        ProcessesVisualizationConfigurationDialog configDialog = new ProcessesVisualizationConfigurationDialog();
        configDialog.showDialog();
        if(!configDialog.isAccepted()){
            return null;
        }
        ProcessesVisualizationConfiguration configuration = new ProcessesVisualizationConfiguration(configDialog.getTemporalConfig());
        this.configurations.add(configuration);
        return configuration;
    }

    @Override
    public List<Configuration> getConfigurations() {
        return this.configurations;
    }

    @Override
    public StagePlugin fromFile(File file) {
        if (file.isFile()) {
            try {
                ProcessesVisualizationPlugin processesVisualizationPlugin = new ProcessesVisualizationPlugin();
                XElement root = XIO.readUTF(new FileInputStream(file));
                XElement[] pathsX = root.getElements("path");
                for (XElement pathX : pathsX) {
                    String path = pathX.getString();
                    File archive = new File(file.getParentFile(), path);
                    Configuration config = new ProcessesVisualizationConfiguration(archive);
                    processesVisualizationPlugin.configurations.add(config);
                }
                return processesVisualizationPlugin;
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Override
    public File toFile(File parent) {
        File file = new File(parent, "processes-visualization.xml");
        if (!file.isFile()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        XElement root = new XElement("vis");
        for (Configuration config : configurations) {
            File p = new File(parent, "processes-visualization");
            p.mkdirs();
            File f = config.toFile(p);

            XElement path = new XElement("path");
            Path parentPath = parent.toPath();
            Path configPath = f.toPath();
            path.setString(parentPath.relativize(configPath).toString());
            root.addElement(path);
        }
        try {
            XIO.writeUTF(root, new FileOutputStream(file));
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return file;
    }
}
