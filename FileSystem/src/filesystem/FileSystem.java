package filesystem;

import filesystem.models.*;
import java.util.*;

public class FileSystem {

    private Folder root;

    public FileSystem() {
        this.root = new Folder("/");
    }

    private String extractName(String path) {
        int idx = path.lastIndexOf("/");
        return path.substring(idx + 1);
    }

    private FileSystemNode resolvePath(String path) {
        if (path == null || path.equals("")) {
            throw new IllegalArgumentException("Path cannot be empty");
        }

        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("Path must be absolute");
        }

        if (path.equals("/")) {
            return root;
        }

        String[] parts = path.substring(1).split("/");
        FileSystemNode current = root;

        for (String part : parts) {
            if (part.isEmpty()) {
                throw new IllegalArgumentException("Invalid path: consecutive slashes");
            }

            if (!current.isDirectory()) {
                throw new IllegalArgumentException("Not a directory");
            }

            FileSystemNode child = ((Folder) current).getChild(part);
            if (child == null) {
                throw new IllegalArgumentException("Invalid Path, not found " + path);
            }

            current = child;
        }

        return current;
    }

    private Folder resolveParent(String path) {
        if (path == null || path.equals("")) {
            throw new IllegalArgumentException("Path cannot be empty");
        }

        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("Path must be absolute");
        }

        if (path.equals("/")) {
            throw new IllegalArgumentException("Root has no parent");
        }

        int lastIdx = path.lastIndexOf("/");
        String parentPath = lastIdx == 0 ? "/" : path.substring(0, lastIdx);

        FileSystemNode parent = resolvePath(parentPath);

        if (!parent.isDirectory()) {
            throw new IllegalArgumentException("parent is not a directory");
        }

        return (Folder) parent;
    }

    public File createFile(String path, String data) {
        if (path.equals("/")) {
            throw new IllegalArgumentException("Cannot create file at root");
        }

        String name = extractName(path);
        Folder parent = resolveParent(path);
        
        parent.acquireWriteLock();
        try {
            if (parent.hasChild(name)) {
                throw new IllegalArgumentException("File already exists at path");
            }

            File child = new File(name, data);
            parent.addChild(child);
            return child;
        } finally {
            parent.releaseWriteLock();
        }
    }

    public Folder createFolder(String path) {
        if (path.equals("/")) {
            throw new IllegalStateException("Root already exists");
        }

        Folder parent = resolveParent(path);
        String folderName = extractName(path);
        
        parent.acquireWriteLock();
        try {
            if (parent.hasChild(folderName)) {
                throw new IllegalStateException("Entry already exists: " + folderName);
            }

            Folder folder = new Folder(folderName);
            parent.addChild(folder);
            return folder;
        } finally {
            parent.releaseWriteLock();
        }
    }

    public void delete(String path) {
        if (path.equals("/")) {
            throw new IllegalArgumentException("Cannot delete root");
        }

        Folder parent = resolveParent(path);
        String name = extractName(path);
        
        parent.acquireWriteLock();
        try {
            FileSystemNode removed = parent.removeChild(name);
            if (removed == null) {
                throw new IllegalArgumentException("Entry not found: " + path);
            }
        } finally {
            parent.releaseWriteLock();
        }
    }

    public List<FileSystemNode> list(String path) {
        FileSystemNode entry = resolvePath(path);

        if (!entry.isDirectory()) {
            throw new IllegalArgumentException("Cannot list a file");
        }
        
        Folder folder = (Folder) entry;
        folder.acquireReadLock();
        try {
            return folder.getChildren();
        } finally {
            folder.releaseReadLock();
        }
    }

    public FileSystemNode get(String path) {
        return resolvePath(path);
    }

    public void rename(String path, String newName) {
        if (path.equals("/")) {
            throw new IllegalArgumentException("Cannot rename root");
        }

        if (newName == null || newName.isEmpty() || newName.contains("/")) {
            throw new IllegalArgumentException("Invalid name");
        }

        Folder parent = resolveParent(path);
        String oldName = extractName(path);
        
        parent.acquireWriteLock();
        try {
            if (!parent.hasChild(oldName)) {
                throw new IllegalArgumentException("Entry not found");
            }

            if (parent.hasChild(newName)) {
                throw new IllegalStateException("Entry already exists: " + newName);
            }

            FileSystemNode entry = parent.removeChild(oldName);
            entry.setName(newName);
            parent.addChild(entry);
        } finally {
            parent.releaseWriteLock();
        }
    }

    public void move(String srcPath, String destPath) {
        if (srcPath.equals("/")) {
            throw new IllegalArgumentException("Cannot move root");
        }

        Folder srcParent = resolveParent(srcPath);
        String srcName = extractName(srcPath);
        
        Folder destParent = resolveParent(destPath);
        String destName = extractName(destPath);
        
        // Lock in consistent order to prevent deadlock
        Folder first = srcParent.getName().compareTo(destParent.getName()) < 0 ? srcParent : destParent;
        Folder second = srcParent.getName().compareTo(destParent.getName()) < 0 ? destParent : srcParent;
       
        
        first.acquireWriteLock();
        try {
            if (srcParent != destParent) {
                second.acquireWriteLock();
            }
            try {
                FileSystemNode entry = srcParent.getChild(srcName);

                if (entry == null) {
                    throw new IllegalArgumentException("Source not found: " + srcPath);
                }

                if (destParent.hasChild(destName)) {
                    throw new IllegalStateException("Destination already exists: " + destPath);
                }

                if (entry.isDirectory()) {
                    Folder current = destParent;
                    while (current != null) {
                        if (current == entry) {
                            throw new IllegalArgumentException("Cannot move folder into itself");
                        }
                        current = current.getParent();
                    }
                }

                srcParent.removeChild(srcName);
                entry.setName(destName);
                destParent.addChild(entry);
            } finally {
               if (srcParent != destParent) {
                    second.releaseWriteLock();
                }
            }
        } finally {
            first.releaseWriteLock();
        }
    }
}
