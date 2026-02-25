package com.broker.asset_service.DTO;

import java.util.List;

public class AssetsIdsDto {
    private List<Long> ids;

    public AssetsIdsDto(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
