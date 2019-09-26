package mo.visualization.process.plugin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import mo.core.I18n;
import mo.core.ui.dockables.DockableElement;
import mo.core.ui.dockables.DockablesRegistry;
import mo.visualization.process.plugin.model.Snapshot;
import mo.visualization.process.plugin.view.ProcessesPlayerPanel;
import mo.visualization.Playable;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessesPlayer implements Playable {

    private static final Logger LOGGER = Logger.getLogger(ProcessesPlayer.class.getName());
    private List<Snapshot> snapshots;
    private ProcessesPlayerPanel panel;
    private final Gson gson;


    public ProcessesPlayer(File file, String configurationName) {
        this.gson = new Gson();
        this.snapshots = this.readCapturedJsonData(file);
        this.sortByCaptureTime(this.snapshots);
        this.panel = new ProcessesPlayerPanel();
        DockableElement dockableElement = new DockableElement();
        I18n i18n = new I18n(ProcessesPlayer.class);
        dockableElement.setTitleText(i18n.s("processesVisualizationPluginDisplayedName")+ ": " + configurationName);
        dockableElement.add(this.panel);
        DockablesRegistry registry = DockablesRegistry.getInstance();
        registry.addAppWideDockable(dockableElement);
    }



    @Override
    public long getStart() {
        long start = this.snapshots.get(0).getCaptureTimestamp();
        return start - 1 ;
    }

    @Override
    public long getEnd() {
        int lastIndex = this.snapshots.size() - 1;
        return this.snapshots.get(lastIndex).getCaptureTimestamp();
    }

    @Override
    public void play(long l) {
        Snapshot processesSnapshot = this.getProcessesSnapshotByCaptureMilli(l);
        if(processesSnapshot == null){
            return;
        }
        this.panel.updateData(processesSnapshot);
    }

    @Override
    public void pause() {

    }

    @Override
    public void seek(long l) {
        this.play(l);
    }

    @Override
    public void stop() {
    }

    @Override
    public void sync(boolean b) {

    }


    /* En el archivo con formato json, cada linea es un registro de un snapshot.*/
    private List<Snapshot> readCapturedJsonData(File file){
        JsonReader jsonReader;
        try {
            jsonReader = new JsonReader(new FileReader(file.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Unable to read " + file.getAbsolutePath() + " file", e);
            return null;
        }
        return gson.fromJson(jsonReader, new TypeToken<List<Snapshot>>() {}.getType());
    }

    private Snapshot getProcessesSnapshotByCaptureMilli(long milliseconds){
        if(this.snapshots == null || this.snapshots.isEmpty()){
            return null;
        }
        return this.snapshots.stream().filter( snapshot -> snapshot.getCaptureTimestamp() <= milliseconds)
                .reduce((first, second) -> second).orElse(null);
    }

    private void sortByCaptureTime(List<Snapshot> snapshots){
        if(snapshots == null || snapshots.isEmpty() || snapshots.size() == 1){
            return;
        }
        snapshots.sort((o1, o2) -> (int) (o1.getCaptureTimestamp() - o2.getCaptureTimestamp()));
    }
}
