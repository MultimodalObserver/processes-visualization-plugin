package mo;

import mo.core.plugin.Extends;
import mo.core.plugin.Extension;
import mo.organization.Configuration;
import mo.organization.ProjectOrganization;
import mo.organization.StagePlugin;
import mo.visualization.VisualizationProvider;

import java.io.File;
import java.util.List;

@Extension(
        xtends = {
                @Extends(
                        extensionPointId = "mo.visualization.VisualizationProvider"
                )
        }
)

public class ProcessesVisualizationPlugin implements VisualizationProvider {


    @Override
    public String getName() {
        return null;
    }

    @Override
    public Configuration initNewConfiguration(ProjectOrganization projectOrganization) {
        return null;
    }

    @Override
    public List<Configuration> getConfigurations() {
        return null;
    }

    @Override
    public StagePlugin fromFile(File file) {
        return null;
    }

    @Override
    public File toFile(File file) {
        return null;
    }
}
