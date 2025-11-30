package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.CharJdbcType;

import java.time.LocalDateTime;

@Entity
@Table(name = "codes")
public class Code {

    @Id
    @Column(name = "code_uid", length = 16)
    private String codeUid;

    @Column(name = "code_type", length = 1, nullable = false)
    @JdbcType(CharJdbcType.class)
    private String codeType; // 'A' = AES, 'S' = SHA1

    @Column(name = "code_content", length = 256, nullable = false)
    private String codeContent;

    @Column(name = "allowed_usage", length = 2, nullable = false)
    private String allowedUsage; // 'RW', 'R', '-'

    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFrom;

    @Column(name = "valid_to", nullable = false)
    private LocalDateTime validTo;

    // Constructors
    public Code() {}

    // Getters and Setters
    public String getCodeUid() { return codeUid; }
    public void setCodeUid(String codeUid) { this.codeUid = codeUid; }

    public String getCodeType() { return codeType; }
    public void setCodeType(String codeType) { this.codeType = codeType; }

    public String getCodeContent() { return codeContent; }
    public void setCodeContent(String codeContent) { this.codeContent = codeContent; }

    public String getAllowedUsage() { return allowedUsage; }
    public void setAllowedUsage(String allowedUsage) { this.allowedUsage = allowedUsage; }

    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }

    public LocalDateTime getValidTo() { return validTo; }
    public void setValidTo(LocalDateTime validTo) { this.validTo = validTo; }
}