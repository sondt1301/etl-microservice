package usth.m1.model;

import java.util.List;

public record ULakeFolderRequest(
        long creationTime,
        List<Object> files,
        String name,
        List<String> subFolders
) {}
