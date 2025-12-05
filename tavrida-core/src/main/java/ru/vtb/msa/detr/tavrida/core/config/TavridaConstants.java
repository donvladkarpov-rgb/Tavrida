package ru.vtb.msa.detr.tavrida.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TavridaConstants {

    // Card Type Constants
    private final Integer cardTypeEmpty;
    private final Integer cardTypeAdmin;
    private final Integer cardTypeCashier;
    private final Integer cardTypeDriver;
    private final Integer cardTypePassenger;
    private final Integer cardTypeTerminalActivation;
    private final Integer cardTypeTerminalReset;

    // User Roles Constants
    private final Integer userRoleOperatorFundsAdmin;
    private final Integer userRoleCarrierAdmin;
    private final Integer userRoleCashier;
    private final Integer userRoleDriver;

    // Codes Constants
    private final String codeTypeA;
    private final String codeTypeS;
    private final String allowedUsageReadWrite;
    private final String allowedUsageReadOnly;
    private final String allowedUsageNone;

    // Service Event Types Constants
    private final String serviceEventTypeUserCreate;
    private final String serviceEventTypeCardCreate;
    private final String serviceEventTypeUserDelete;
    private final String serviceEventTypeCardDelete;
    private final String serviceEventTypeOther;

    // Payment Types Constants
    private final Integer paymentTypeReplenishment;
    private final Integer paymentTypeWriteOff;

    // Payment Results Constants
    private final Integer paymentResultSuccess;
    private final Integer paymentResultErrorCardRead;
    private final Integer paymentResultErrorInsufficientFunds;
    private final Integer paymentResultErrorCardBlocked;

    public TavridaConstants(
            // Card Type Constants
            @Value("${tavrida.common.constant.card-type.empty}") Integer cardTypeEmpty,
            @Value("${tavrida.common.constant.card-type.admin}") Integer cardTypeAdmin,
            @Value("${tavrida.common.constant.card-type.cashier}") Integer cardTypeCashier,
            @Value("${tavrida.common.constant.card-type.driver}") Integer cardTypeDriver,
            @Value("${tavrida.common.constant.card-type.passenger}") Integer cardTypePassenger,
            @Value("${tavrida.common.constant.card-type.terminal-activation}") Integer cardTypeTerminalActivation,
            @Value("${tavrida.common.constant.card-type.terminal-reset}") Integer cardTypeTerminalReset,

            // User Roles Constants
            @Value("${tavrida.common.constant.user-roles.operator-funds-admin}") Integer userRoleOperatorFundsAdmin,
            @Value("${tavrida.common.constant.user-roles.carrier-admin}") Integer userRoleCarrierAdmin,
            @Value("${tavrida.common.constant.user-roles.cashier}") Integer userRoleCashier,
            @Value("${tavrida.common.constant.user-roles.driver}") Integer userRoleDriver,

            // Codes Constants
            @Value("${tavrida.common.constant.codes.code-types.A}") String codeTypeA,
            @Value("${tavrida.common.constant.codes.code-types.S}") String codeTypeS,
            @Value("${tavrida.common.constant.codes.allowed-usages.read-write}") String allowedUsageReadWrite,
            @Value("${tavrida.common.constant.codes.allowed-usages.read-only}") String allowedUsageReadOnly,
            @Value("${tavrida.common.constant.codes.allowed-usages.none}") String allowedUsageNone,

            // Service Event Types Constants
            @Value("${tavrida.common.constant.service-event-types.user-create}") String serviceEventTypeUserCreate,
            @Value("${tavrida.common.constant.service-event-types.card-create}") String serviceEventTypeCardCreate,
            @Value("${tavrida.common.constant.service-event-types.user-delete}") String serviceEventTypeUserDelete,
            @Value("${tavrida.common.constant.service-event-types.card-delete}") String serviceEventTypeCardDelete,
            @Value("${tavrida.common.constant.service-event-types.other-event}") String serviceEventTypeOther,

            // Payment Types Constants
            @Value("${tavrida.common.constant.payment-types.replenishment}") Integer paymentTypeReplenishment,
            @Value("${tavrida.common.constant.payment-types.write-off}") Integer paymentTypeWriteOff,

            // Payment Results Constants
            @Value("${tavrida.common.constant.payment-results.success}") Integer paymentResultSuccess,
            @Value("${tavrida.common.constant.payment-results.error-card-read}") Integer paymentResultErrorCardRead,
            @Value("${tavrida.common.constant.payment-results.error-insufficient-funds}") Integer paymentResultErrorInsufficientFunds,
            @Value("${tavrida.common.constant.payment-results.error-card-blocked}") Integer paymentResultErrorCardBlocked) {

        // Инициализация Card Type Constants
        this.cardTypeEmpty = cardTypeEmpty;
        this.cardTypeAdmin = cardTypeAdmin;
        this.cardTypeCashier = cardTypeCashier;
        this.cardTypeDriver = cardTypeDriver;
        this.cardTypePassenger = cardTypePassenger;
        this.cardTypeTerminalActivation = cardTypeTerminalActivation;
        this.cardTypeTerminalReset = cardTypeTerminalReset;

        // Инициализация User Roles Constants
        this.userRoleOperatorFundsAdmin = userRoleOperatorFundsAdmin;
        this.userRoleCarrierAdmin = userRoleCarrierAdmin;
        this.userRoleCashier = userRoleCashier;
        this.userRoleDriver = userRoleDriver;

        // Инициализация Codes Constants
        this.codeTypeA = codeTypeA;
        this.codeTypeS = codeTypeS;
        this.allowedUsageReadWrite = allowedUsageReadWrite;
        this.allowedUsageReadOnly = allowedUsageReadOnly;
        this.allowedUsageNone = allowedUsageNone;

        // Инициализация Service Event Types Constants
        this.serviceEventTypeUserCreate = serviceEventTypeUserCreate;
        this.serviceEventTypeCardCreate = serviceEventTypeCardCreate;
        this.serviceEventTypeUserDelete = serviceEventTypeUserDelete;
        this.serviceEventTypeCardDelete = serviceEventTypeCardDelete;
        this.serviceEventTypeOther = serviceEventTypeOther;

        // Инициализация Payment Types Constants
        this.paymentTypeReplenishment = paymentTypeReplenishment;
        this.paymentTypeWriteOff = paymentTypeWriteOff;

        // Инициализация Payment Results Constants
        this.paymentResultSuccess = paymentResultSuccess;
        this.paymentResultErrorCardRead = paymentResultErrorCardRead;
        this.paymentResultErrorInsufficientFunds = paymentResultErrorInsufficientFunds;
        this.paymentResultErrorCardBlocked = paymentResultErrorCardBlocked;
    }

    // Геттеры (не изменяются)
    public Integer getCardTypeEmpty() {
        return cardTypeEmpty;
    }

    public Integer getCardTypeAdmin() {
        return cardTypeAdmin;
    }

    public Integer getCardTypeCashier() {
        return cardTypeCashier;
    }

    public Integer getCardTypeDriver() {
        return cardTypeDriver;
    }

    public Integer getCardTypePassenger() {
        return cardTypePassenger;
    }

    public Integer getCardTypeTerminalActivation() {
        return cardTypeTerminalActivation;
    }

    public Integer getCardTypeTerminalReset() {
        return cardTypeTerminalReset;
    }

    public Integer getUserRoleOperatorFundsAdmin() {
        return userRoleOperatorFundsAdmin;
    }

    public Integer getUserRoleCarrierAdmin() {
        return userRoleCarrierAdmin;
    }

    public Integer getUserRoleCashier() {
        return userRoleCashier;
    }

    public Integer getUserRoleDriver() {
        return userRoleDriver;
    }

    public String getCodeTypeA() {
        return codeTypeA;
    }

    public String getCodeTypeS() {
        return codeTypeS;
    }

    public String getAllowedUsageReadWrite() {
        return allowedUsageReadWrite;
    }

    public String getAllowedUsageReadOnly() {
        return allowedUsageReadOnly;
    }

    public String getAllowedUsageNone() {
        return allowedUsageNone;
    }

    public String getServiceEventTypeUserCreate() {
        return serviceEventTypeUserCreate;
    }

    public String getServiceEventTypeCardCreate() {
        return serviceEventTypeCardCreate;
    }

    public String getServiceEventTypeUserDelete() {
        return serviceEventTypeUserDelete;
    }

    public String getServiceEventTypeCardDelete() {
        return serviceEventTypeCardDelete;
    }

    public String getServiceEventTypeOther() {
        return serviceEventTypeOther;
    }

    public Integer getPaymentTypeReplenishment() {
        return paymentTypeReplenishment;
    }

    public Integer getPaymentTypeWriteOff() {
        return paymentTypeWriteOff;
    }

    public Integer getPaymentResultSuccess() {
        return paymentResultSuccess;
    }

    public Integer getPaymentResultErrorCardRead() {
        return paymentResultErrorCardRead;
    }

    public Integer getPaymentResultErrorInsufficientFunds() {
        return paymentResultErrorInsufficientFunds;
    }

    public Integer getPaymentResultErrorCardBlocked() {
        return paymentResultErrorCardBlocked;
    }
}
