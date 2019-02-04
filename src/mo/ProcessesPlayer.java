package mo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mo.core.ui.dockables.DockableElement;
import mo.core.ui.dockables.DockablesRegistry;
import mo.visualization.Playable;

import javax.print.attribute.standard.Severity;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class ProcessesPlayer implements Playable {

    private static final Logger LOGGER = Logger.getLogger(ProcessesPlayer.class.getName());
    private static final String CAPTURE_MILLISECONDS_KEY = "captureMilliseconds";
    private static final String MO_FORMAT = "yyyy-MM-dd_HH.mm.ss.SSS";
    private List<JsonObject> processesSnapshotsData;
    private long start;
    private long end;
    private ProcessesPlayerPanel panel;
    private DockablesRegistry registry;
    private DockableElement dockableElement;


    public ProcessesPlayer(File file, String configurationName) {
        this.processesSnapshotsData = this.readCapturedData(file);
        this.panel = new ProcessesPlayerPanel();
        this.dockableElement = new DockableElement();
        this.dockableElement.setTitleText("Visualization: " + configurationName);
        this.dockableElement.add(this.panel);
        this.registry = DockablesRegistry.getInstance();
        this.registry.addAppWideDockable(dockableElement);
    }

    @Override
    public long getStart() {
        this.start =  this.processesSnapshotsData.get(0).get(CAPTURE_MILLISECONDS_KEY).getAsLong();
        return this.start;
    }

    @Override
    public long getEnd() {
        int lastIndex = this.processesSnapshotsData.size() - 1;
        this.end =  this.processesSnapshotsData.get(lastIndex).get(CAPTURE_MILLISECONDS_KEY).getAsLong();
        return this.end;
    }

    @Override
    public void play(long l) {
        JsonObject processesSnapshot = this.getProcessesSnapshotByCaptureMilli(l);
        /* Solo actualizamos el panel cuando se encuentre un registro con ese tiempo*/
        if(processesSnapshot == null){
            return;
        }
        else if(l == this.start){
            LOGGER.log(Level.SEVERE, "Creando tabla");
            this.panel.createTable(processesSnapshot);
            return;
        }
        LOGGER.log(Level.SEVERE, "Actualizando tabla");
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
        this.panel.removeTable();
    }

    @Override
    public void sync(boolean b) {

    }

    private List<JsonObject> readCapturedData(File file){
        List<JsonObject> processesSnapshotsData= null;
        try {
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            JsonParser parser = new JsonParser();
            processesSnapshotsData = new ArrayList<>();
            //Obtenemos los datos de los snapshots de procesos y los agregamos a una lista
            while((line = bufferedReader.readLine()) != null){
                line = line.replace("\n", "");
                JsonObject processesSnapshotData = parser.parse(line).getAsJsonObject();
                processesSnapshotsData.add(processesSnapshotData);
            }
            /* EVALUAR SI ES NECESARIO ORDENAR, ya que la captura se realiza de forma "ordenada"
            //Ordenamos la lista segun el segundo de captura
            processesSnapshotsData.sort((o1, o2) -> {
                long captureSeconds1 = o1.get(CAPTURE_MILLISECONDS_KEY).getAsLong();
                long captureSeconds2 = o2.get(CAPTURE_MILLISECONDS_KEY).getAsLong();
                int returnValue = 0;
                if(captureSeconds1 < captureSeconds2){
                    returnValue = -1;
                }
                else if(captureSeconds1 == captureSeconds2){
                    returnValue = 0;
                }
                else if(captureSeconds1 > captureSeconds2){
                    returnValue = 1;
                }
                return returnValue;
            });
            */
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
        return processesSnapshotsData;
    }

    private JsonObject getProcessesSnapshotByCaptureMilli(long milliseconds){
        for(JsonObject processesSnapshot: this.processesSnapshotsData){
            if(processesSnapshot.get(CAPTURE_MILLISECONDS_KEY).getAsLong() == milliseconds){
                return processesSnapshot;
            }
        }
        return null;
    }
}
