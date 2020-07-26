package com.qdw.common.work;

import lombok.Data;

import java.util.Map;

@Data
public class Work {
    private long id;
    private WorkTypeEnum workType;
    private Map<String,Object> Resources;
    public Work(WorkTypeEnum workType,Map<String,Object> resources){
        this.workType = workType;
        setResources(resources);
    }
}
