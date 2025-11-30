package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;

@Entity
@Table(name = "master_password")
public class MasterPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "master_password_hash")
    private String masterPasswordHash;

    // Constructors
    public MasterPassword() {}

    public MasterPassword(String masterPasswordHash) {
        this.masterPasswordHash = masterPasswordHash;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMasterPasswordHash() { return masterPasswordHash; }
    public void setMasterPasswordHash(String masterPasswordHash) { this.masterPasswordHash = masterPasswordHash; }
}