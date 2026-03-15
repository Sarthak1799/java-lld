package filesystem.models;

public class File extends FileSystemNode {
    private String data;

    public File(String name, String data){
        super(name);
        this.data = data;
    }

    public String getData(){
        return data;
    }

    public void setData(String data){
        this.data = data;
    }

    @Override
    public boolean isDirectory(){
        return false;
    }
}
