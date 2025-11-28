package ru.vtb.msa.detr.tavrida.api.model;

public class CarrierDto {
    private Long carrierId;
    private String carrierName;

    public CarrierDto() {}

    public CarrierDto(Long carrierId, String carrierName) {
        this.carrierId = carrierId;
        this.carrierName = carrierName;
    }

    public Long getCarrierId() { return carrierId; }
    public void setCarrierId(Long carrierId) { this.carrierId = carrierId; }

    public String getCarrierName() { return carrierName; }
    public void setCarrierName(String carrierName) { this.carrierName = carrierName; }
}