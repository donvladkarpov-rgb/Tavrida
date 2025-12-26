package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tariff_types")
public class TariffType {

    @Id
    @Column(name = "tariff_type_id", nullable = false)
    private Integer tariffTypeId;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Getters and Setters

    public Integer getTariffTypeId() {
        return tariffTypeId;
    }

    public void setTariffTypeId(Integer tariffTypeId) {
        this.tariffTypeId = tariffTypeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TariffType{" +
                "tariffTypeId=" + tariffTypeId +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}