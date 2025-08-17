package usth.m1.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ULakeFolderInfo(
        Long id,
        Long ownerId,
        Long creationTime,
        List<Object> files,
        String name,
        List<String> subFolders
) {}
