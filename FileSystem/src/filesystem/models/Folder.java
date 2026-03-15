package filesystem.models;


import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Folder extends FileSystemNode {
    private final Map<String, FileSystemNode> children;
    private final ReadWriteLock lock;

    public Folder(String name){
        super(name);
        this.children = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }

    public void acquireReadLock() {
        lock.readLock().lock();
    }

    public void releaseReadLock() {
        lock.readLock().unlock();
    }

    public void acquireWriteLock() {
        lock.writeLock().lock();
    }

    public void releaseWriteLock() {
        lock.writeLock().unlock();
    }

    public boolean hasChild(String name){
        return children.containsKey(name);
    }

    public boolean addChild(FileSystemNode child){
        if(child == null)
            return false;

        if(children.containsKey(child.getName()))
            return false;

        children.put(child.getName(), child);
        child.setParent(this);

        return true;
    }

    public FileSystemNode removeChild(String child){
        FileSystemNode node = children.get(child);

        if(node != null)
            node.setParent(null);

        children.remove(child);
        return node;
    }

    public FileSystemNode getChild(String name){
        return children.get(name);
    }

    @Override
    public boolean isDirectory(){
        return true;
    }

    public List<FileSystemNode> getChildren(){
        return new ArrayList<>(children.values());
    }
}