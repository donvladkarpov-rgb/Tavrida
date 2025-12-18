package ru.vtb.msa.detr.tavrida.api.model.cashier;

import ru.vtb.msa.detr.tavrida.api.model.UserSessionDto;

public class CashierLoginResponse {

    private UserSessionDto session;

    public CashierLoginResponse() {
    }

    public CashierLoginResponse(UserSessionDto session) {
        this.session = session;
    }

    public UserSessionDto getSession() {
        return session;
    }

    public void setSession(UserSessionDto session) {
        this.session = session;
    }
}
