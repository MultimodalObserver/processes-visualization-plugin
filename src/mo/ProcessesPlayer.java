package mo;

import mo.visualization.Playable;

import java.io.File;

public class ProcessesPlayer implements Playable {

    private File file;
    private long start;
    private long end;


    public ProcessesPlayer(File file) {
        this.file = file;

    }

    @Override
    public long getStart() {
        return 0;
    }

    @Override
    public long getEnd() {
        return 0;
    }

    @Override
    public void play(long l) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void seek(long l) {

    }

    @Override
    public void stop() {

    }

    @Override
    public void sync(boolean b) {

    }
}
