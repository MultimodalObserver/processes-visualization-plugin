package mo.plugin;

import com.google.gson.Gson;
import mo.core.ui.dockables.DockableElement;
import mo.core.ui.dockables.DockablesRegistry;
import mo.plugin.models.Process;
import mo.plugin.models.Snapshot;
import mo.plugin.views.ProcessesPlayerPanel;
import mo.visualization.Playable;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessesPlayer implements Playable {

    private static final Logger LOGGER = Logger.getLogger(ProcessesPlayer.class.getName());
    private static final String CAPTURE_MILLISECONDS_KEY = "captureMilliseconds";
    private static final String MO_FORMAT = "yyyy-MM-dd_HH.mm.ss.SSS";
    private static final String CSV_FORMAT = "csv";
    private static final String JSON_FORMAT = "json";
    private List<Snapshot> snapshots;
    private long start;
    private long end;
    private ProcessesPlayerPanel panel;
    private DockablesRegistry registry;
    private DockableElement dockableElement;
    private final Gson gson;


    public ProcessesPlayer(File file, String configurationName) {
        this.snapshots = file.getName().contains("." + CSV_FORMAT) ?
                this.readCapturedCsvData(file): this.readCapturedJsonData(file);
        this.panel = new ProcessesPlayerPanel();
        this.dockableElement = new DockableElement();
        this.dockableElement.setTitleText("Visualization: " + configurationName);
        this.dockableElement.add(this.panel);
        this.registry = DockablesRegistry.getInstance();
        this.registry.addAppWideDockable(dockableElement);
        this.gson = new Gson();
    }



    @Override
    public long getStart() {
        this.start =  this.snapshots.get(0).getCaptureMilliseconds();
        System.out.println("START: " +this.start);
        return this.start;
    }

    @Override
    public long getEnd() {
        int lastIndex = this.snapshots.size() - 1;
        this.end =  this.snapshots.get(lastIndex).getCaptureMilliseconds();
        System.out.println("END: " + this.end);
        return this.end;
    }

    @Override
    public void play(long l) {
        Snapshot processesSnapshot = this.getProcessesSnapshotByCaptureMilli(l);
        /* Solo actualizamos el panel cuando se encuentre un registro con ese tiempo*/
        if(processesSnapshot != null){
            if(l == this.start){
                LOGGER.log(Level.SEVERE, "Creando tabla");
                this.panel.removeTable();
                this.panel.createTable(processesSnapshot);
            }
            else if(l > this.start && l <= this.end){
                LOGGER.log(Level.SEVERE, "Actualizando tabla");
                this.panel.updateData(processesSnapshot);
            }
        }
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
        List<Snapshot> snapshots = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String lineSeparator = System.getProperty("line.separator");
            String line;
            while((line = bufferedReader.readLine()) != null){
                line = line.replace(",", "");
                line = line.replace(lineSeparator, "");
                if(line.isEmpty()){
                    continue;
                }
                Snapshot snapshot = this.gson.fromJson(line, Snapshot.class);
                snapshots.add(snapshot);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
        return snapshots;
    }

    /* EN ESTE ARCHIVO, CADA LINEA ES UN PROCESO, POR TANTO PARA CONSTRUIR LOS SNAPSHOTS
                DEBEMOS AGRUPAR LOS PROCESOS POR TIEMPO DE CAPTURA. EN ESPECIFICO, TODOS LOS PROCESOS CON EL MISMO TIEMPO
                DE CAPTURA PERTENECEN AL MISMO SNAPSHOT.
    */
    private List<Snapshot> readCapturedCsvData(File file) {
        List<Snapshot> snapshots = new ArrayList<>();
        try{
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            /* Leemos los headers del csv*/
            String line = bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null){
                line = line.replace("\n", "");
                Process process = new Process(line);
                this.addToOrCreateSnapshot(snapshots, process);
            }
        }
        catch(IOException e){
            LOGGER.log(Level.SEVERE, "", e);
        }
        this.sortByCaptureTime(snapshots);
        return snapshots;
    }

    /* Metodo que agrega un proceso a un snapshot en base a su tiempo de captura

        Si no existen snapshots, crea una y asocia el proceso a ella.

        Si existen snapshots, busca alguna que tenga el mismo tiempo de captura:

            En caso de encontrarse, se asocia el proceso a ella.

            En caso de no encontrarse, se crea una nueva snapshot y se asocia el proceso a ella.
     */
    private void addToOrCreateSnapshot(List<Snapshot> snapshots, Process process) {
        if (snapshots.isEmpty()){
            Snapshot snapshot = this.createNewSnapshot(process);
            snapshots.add(snapshot);
            return;
        }
        for(Snapshot snapshot: snapshots){
            if(snapshot.getCaptureMilliseconds() == process.getCaptureMilliseconds()){
                snapshot.getProcesses().add(process);
                return;
            }
        }
        Snapshot snapshot = this.createNewSnapshot(process);
        snapshots.add(snapshot);
    }

    private Snapshot createNewSnapshot(Process process){
        Snapshot snapshot = new Snapshot();
        List<Process> processes = new ArrayList<>();
        snapshot.setCaptureMilliseconds(process.getCaptureMilliseconds());
        processes.add(process);
        snapshot.setProcesses(processes);
        return snapshot;
    }

    private void sortByCaptureTime(List<Snapshot> snapshots){
        if(snapshots.isEmpty() || snapshots.size() == 1){
            return;
        }
        snapshots.sort((o1, o2) -> (int) (o1.getCaptureMilliseconds() - o2.getCaptureMilliseconds()));
    }

    private Snapshot getProcessesSnapshotByCaptureMilli(long milliseconds){
        for(Snapshot snapshot: this.snapshots){
            if(snapshot.getCaptureMilliseconds() == milliseconds){
                return snapshot;
            }
        }
        return null;
    }
}
