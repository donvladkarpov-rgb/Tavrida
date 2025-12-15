package ru.vtb.msa.detr.tavrida.api.model.tariff;

import java.io.Serializable;

public class TariffTypeDto implements Serializable {
    private Integer tariffTypeId;
    private String code;
    private String name;
    private String description;

    public TariffTypeDto() {}
    public TariffTypeDto(Integer tariffTypeId, String code, String name, String description) {
        this.tariffTypeId = tariffTypeId;
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public Integer getTariffTypeId() { return tariffTypeId; }
    public void setTariffTypeId(Integer tariffTypeId) { this.tariffTypeId = tariffTypeId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}