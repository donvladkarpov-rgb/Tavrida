package ru.vtb.msa.detr.tavrida.api.model.admin;

public class CarrierRegistrationRequest {
    private String carrierName;

    public CarrierRegistrationRequest() {}

    public CarrierRegistrationRequest(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCarrierName() { return carrierName; }
    public void setCarrierName(String carrierName) { this.carrierName = carrierName; }
}