package ru.vtb.msa.detr.tavrida.api.model;

public class MasterPasswordDto {
    private Long id;
    private String masterPasswordHash;

    public MasterPasswordDto() {}

    public MasterPasswordDto(String masterPasswordHash) {
        this.masterPasswordHash = masterPasswordHash;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMasterPasswordHash() { return masterPasswordHash; }
    public void setMasterPasswordHash(String masterPasswordHash) { this.masterPasswordHash = masterPasswordHash; }
}