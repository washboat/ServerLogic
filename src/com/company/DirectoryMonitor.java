package com.company;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

public class DirectoryMonitor{
    private WatchService watcher = null;
    private Map<WatchKey, Path> keys = null;
    private boolean recursive = false;
    private boolean trace = false;
    private int port = 0;
    private String ip = null;
    private boolean ipIsMutable = true;
    private boolean portIsMutable = true;

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    private void register(Path directory) throws IOException{
        WatchKey key = directory.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if(trace){
            Path previous = keys.get(key);
            if(previous == null)
                System.out.format("register: %s\n", directory);
            else {
                if (!directory.equals(previous)) {
                    System.out.format("update: %s -> %s\n", previous, directory);
                }
            }
        }
        keys.put(key, directory);
    }

    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException
            {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public DirectoryMonitor(Path directory, boolean recursive, String ip, int port) throws  IOException{
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
        this.recursive = recursive;
        this.ip = ip;
        this.port = port;

        if (recursive){
            System.out.format("Scanning %s ...\n", directory);
            registerAll(directory);
            System.out.println("Done.");
        }
        else {
            register(directory);
        }
        this.trace = true;
    }

    void processEvents(){
        WatchKey key;
        try {
            while ( (key = watcher.take()) != null ){

                Path directory = keys.get(key);
                if(directory == null){
                    System.err.println("WatchKey not recognized!");
                    continue;
                }
                for (WatchEvent<?> event:
                     key.pollEvents()) {
                    WatchEvent.Kind kindOfEvent = event.kind();

                    if (kindOfEvent == OVERFLOW){
                        //ignore overflow
                        //TODO: consider if overflow needs to be handled
                        continue;
                    }

                    WatchEvent<Path> ev = cast(event);
                    Path name = ev.context();
                    Path child = directory.resolve(name);
                    System.out.format("%s: %s\n", event.kind().name(), child);

                    if ( kindOfEvent == ENTRY_CREATE){
                        if (Files.isRegularFile(child, NOFOLLOW_LINKS)){
                            Thread thread = new Client(getIp(), getPort(), child);
                            thread.start();
                        }
                    }
                    if (recursive && (kindOfEvent == ENTRY_CREATE)) {
                        try {
                            if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                                registerAll(child);
                            }
                        } catch (IOException x) {
                            // ignore to keep sample readbale
                        }

                    }

                    boolean valid = key.reset();
                    if (!valid){
                        keys.remove(key);
                        if (keys.isEmpty()){
                            break;
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        Path dir = Paths.get("C:\\Users\\trist\\Documents\\TestFiles");
        try {
            new DirectoryMonitor(dir, false, "192.168.1.76", 32581 ).processEvents();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }

    public boolean setPort(int port) {
        if (portIsMutable) {
            this.port = port;
            portIsMutable = false;
            return true;
        }
        return false;
    }

    public String getIp() {
        return ip;
    }

    public boolean setIp(String ip) {
        if(ipIsMutable){
            this.ip = ip;
            ipIsMutable = false;
            return true;
        }
        return false;
    }
}