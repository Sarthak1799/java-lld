package filesystem.models;

public abstract class FileSystemNode  {
    private String name;
    private Folder parent;

    public FileSystemNode(String name){
        this.name = name;
        this.parent = null;
    }

    public String getName(){
        return name;
    }

    public Folder getParent(){
        return parent;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setParent(Folder parent){
        this.parent = parent;
    }

    public String getPath(){
        if(parent == null)
            return name;

        String parentPath = parent.getPath();

        return parentPath + "/" + name;
    }

    public abstract boolean isDirectory();
}