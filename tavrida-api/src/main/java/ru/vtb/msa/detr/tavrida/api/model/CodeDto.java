package ru.vtb.msa.detr.tavrida.api.model;

import java.time.LocalDateTime;

public class CodeDto {
    private String codeUid;
    private String codeType;
    private String codeContent;
    private String allowedUsage;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;

    public CodeDto() {}

    // Геттеры и сеттеры
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