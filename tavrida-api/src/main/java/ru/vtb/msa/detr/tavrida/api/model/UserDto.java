package ru.vtb.msa.detr.tavrida.api.model;

import java.util.List;

public class UserDto {
    private Long userId;
    private String userFio;
    private UserRoleDto userRole;
    private CarrierDto carrier;
    private List<CardDto> cards;

    // Constructors
    public UserDto() {}

    public UserDto(Long userId, String userFio, UserRoleDto userRole, CarrierDto carrier, List<CardDto> cards) {
        this.userId = userId;
        this.userFio = userFio;
        this.userRole = userRole;
        this.carrier = carrier;
        this.cards = cards;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserFio() { return userFio; }
    public void setUserFio(String userFio) { this.userFio = userFio; }

    public UserRoleDto getUserRole() { return userRole; }
    public void setUserRole(UserRoleDto userRole) { this.userRole = userRole; }

    public CarrierDto getCarrier() { return carrier; }
    public void setCarrier(CarrierDto carrier) { this.carrier = carrier; }

    public List<CardDto> getCards() { return cards; }
    public void setCards(List<CardDto> cards) { this.cards = cards; }
}