package ru.vtb.msa.detr.tavrida.api.model;

public class UserRoleDto {
    private Integer userRoleId;
    private String userRoleName;

    public UserRoleDto() {}

    public UserRoleDto(Integer userRoleId, String userRoleName) {
        this.userRoleId = userRoleId;
        this.userRoleName = userRoleName;
    }

    public Integer getUserRoleId() { return userRoleId; }
    public void setUserRoleId(Integer userRoleId) { this.userRoleId = userRoleId; }

    public String getUserRoleName() { return userRoleName; }
    public void setUserRoleName(String userRoleName) { this.userRoleName = userRoleName; }
}