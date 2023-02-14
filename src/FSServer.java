import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class FSServer {

    private String dir;
    private ArrayList<FSMonitor> clients;
    private volatile boolean canWork; //Для отслежевание переменной

    public FSServer(String dir) {
        this.dir = dir;
        clients = new ArrayList<FSMonitor>();
    }

    public void addClient(FSMonitor client){
        clients.add(client);
    }

    public void removeClient(FSMonitor client){
        clients.remove(client);
    }


    public void start(){
        canWork = true;
        run();
    }

    public void stop(){
        canWork = false;
    }


    public void run(){
        try {
            WatchService watch = FileSystems.getDefault().newWatchService(); //newWatchService наблюдатель
            Paths.get(dir).register(watch, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);

            while (canWork){
                WatchKey key = watch.take();
                for (WatchEvent<?> pollEvent: key.pollEvents()) {
                    String fName =pollEvent.context().toString();
                    int kind;
                    if (pollEvent.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        kind = FSMonitor.CREATED;
                    }else{
                            kind = FSMonitor.REMOVED;
                        }
                    for (FSMonitor client: clients){
                        client.fireEvent(fName, kind);
                    }
                          {

                    }

                    key.reset();
                }
            }

            watch.close();

        }catch (IOException ex){
            System.out.println(ex.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
