package usth.m1.model;

import java.util.List;

public record ULakeObjectGroup(
        int id,
        String name,
        String gid,
        String parentGid,
        String extra,
        String tags,
        List<Object> objects,
        Object dataset
) {}
