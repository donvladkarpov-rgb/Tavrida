package ru.vtb.msa.detr.tavrida.core.model.mapper;

import ru.vtb.msa.detr.tavrida.api.model.*;
import ru.vtb.msa.detr.tavrida.core.model.*;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TavridaMapper {

    // ============= USER =============

    public static UserDto toUserDto(User user) {
        if (user == null) return null;
        return new UserDto(
                user.getUserId(),
                user.getUserFio(),
                toUserRoleDto(user.getUserRole()),
                toCarrierDto(user.getCarrier()),
                // Не загружаем cards по умолчанию, чтобы избежать циклов
                new ArrayList<>()
        );
    }

    public static UserDto toUserDtoWithCards(User user) {
        if (user == null) return null;
        List<CardDto> cardDtos = user.getCards().stream()
                .map(TavridaMapper::toCardDtoWithoutUser)
                .collect(Collectors.toList());
        return new UserDto(
                user.getUserId(),
                user.getUserFio(),
                toUserRoleDto(user.getUserRole()),
                toCarrierDto(user.getCarrier()),
                cardDtos
        );
    }

    public static User toUserEntity(UserDto dto) {
        if (dto == null) return null;
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setUserFio(dto.getUserFio());
        user.setUserPasswordHash(dto.getUserRole() != null ? "temp" : null); // пароль не из DTO!
        user.setUserRole(toUserRoleEntity(dto.getUserRole()));
        user.setCarrier(toCarrierEntity(dto.getCarrier()));
        // cards маппятся отдельно
        return user;
    }

    // ============= USER ROLE =============

    public static UserRoleDto toUserRoleDto(UserRole role) {
        if (role == null) return null;
        return new UserRoleDto(
                role.getUserRoleId(),
                role.getUserRoleName()
        );
    }

    public static UserRole toUserRoleEntity(UserRoleDto dto) {
        if (dto == null) return null;
        UserRole role = new UserRole();
        role.setUserRoleId(dto.getUserRoleId());
        role.setUserRoleName(dto.getUserRoleName());
        return role;
    }

    // ============= CARRIER =============

    public static CarrierDto toCarrierDto(Carrier carrier) {
        if (carrier == null) return null;
        return new CarrierDto(
                carrier.getCarrierId(),
                carrier.getCarrierName()
        );
    }

    public static Carrier toCarrierEntity(CarrierDto dto) {
        if (dto == null) return null;
        Carrier carrier = new Carrier();
        carrier.setCarrierId(dto.getCarrierId());
        carrier.setCarrierName(dto.getCarrierName());
        return carrier;
    }

    // ============= TRANSPORT =============

    public static TransportDto toTransportDto(Transport transport) {
        if (transport == null) return null;
        return new TransportDto(
                transport.getTransportId(),
                transport.getTransportGuid(),
                toCarrierDto(transport.getCarrier()),
                transport.getTransportNumber(),
                transport.getTransportName()
        );
    }

    public static Transport toTransportEntity(TransportDto dto) {
        if (dto == null) return null;
        Transport transport = new Transport();
        transport.setTransportId(dto.getTransportId());
        transport.setTransportGuid(dto.getTransportGuid());
        transport.setCarrier(toCarrierEntity(dto.getCarrier()));
        transport.setTransportNumber(dto.getTransportNumber());
        transport.setTransportName(dto.getTransportName());
        return transport;
    }

    // ============= TERMINAL =============

    public static TerminalDto toTerminalDto(Terminal terminal) {
        if (terminal == null) return null;
        return new TerminalDto(
                terminal.getTerminalId(),
                toTransportDto(terminal.getTransport()),
                terminal.getTerminalGuid(),
                terminal.getTerminalNumber()
        );
    }

    public static Terminal toTerminalEntity(TerminalDto dto) {
        if (dto == null) return null;
        Terminal terminal = new Terminal();
        terminal.setTerminalId(dto.getTerminalId());
        terminal.setTransport(toTransportEntity(dto.getTransport()));
        terminal.setTerminalGuid(dto.getTerminalGuid());
        terminal.setTerminalNumber(dto.getTerminalNumber());
        return terminal;
    }

    // ============= CARD TYPE =============

    public static CardTypeDto toCardTypeDto(CardType type) {
        if (type == null) return null;
        return new CardTypeDto(
                type.getCardTypeId(),
                type.getCardTypeName()
        );
    }

    public static CardType toCardTypeEntity(CardTypeDto dto) {
        if (dto == null) return null;
        CardType type = new CardType();
        type.setCardTypeId(dto.getCardTypeId());
        type.setCardTypeName(dto.getCardTypeName());
        return type;
    }

    // ============= CARD =============

    /**
     * Маппинг Card → CardDto (без User, чтобы избежать цикла)
     */
    public static CardDto toCardDtoWithoutUser(Card card) {
        if (card == null) return null;
        return new CardDto(
                card.getCardId(),
                card.getCardGuid(),
                toCardTypeDto(card.getCardType()),
                null, // не включаем User
                null, //не включать транспорт
                card.getUniqueTravelCount(),
                card.getMaximumUniqueCount(),
                card.getAvailableTravelCount(),
                card.getExpirationDate()
        );
    }

    /**
     * Маппинг Card → CardDto (с userId только)
     */
    public static CardDto toCardDtoWithUserId(Card card) {
        if (card == null) return null;
        Long userId = (card.getUser() != null) ? card.getUser().getUserId() : null;
        Long transportId = (card.getTransport() != null) ? card.getTransport().getTransportId() : null;
        return new CardDto(
                card.getCardId(),
                card.getCardGuid(),
                toCardTypeDto(card.getCardType()),
                userId != null ? new UserDto(userId, null, null, null, null) : null,
                transportId != null ? new TransportDto(transportId, null, null, null, null) : null,
                card.getUniqueTravelCount(),
                card.getMaximumUniqueCount(),
                card.getAvailableTravelCount(),
                card.getExpirationDate()
        );
    }

    public static CardDto toCardDto(Card card) {
        if (card == null) return null;
        return new CardDto(
                card.getCardId(),
                card.getCardGuid(),
                toCardTypeDto(card.getCardType()),
                toUserDto(card.getUser()),
                toTransportDto(card.getTransport()),
                card.getUniqueTravelCount(),
                card.getMaximumUniqueCount(),
                card.getAvailableTravelCount(),
                card.getExpirationDate()
        );
    }

    public static Card toCardEntity(CardDto dto) {
        if (dto == null) return null;
        Card card = new Card();
        card.setCardId(dto.getCardId());
        card.setCardGuid(dto.getCardGuid());
        card.setCardType(toCardTypeEntity(dto.getCardType()));
        // USER не маппится из DTO — устанавливается отдельно
        card.setUniqueTravelCount(dto.getUniqueTravelCount());
        card.setMaximumUniqueCount(dto.getMaximumUniqueCount());
        card.setAvailableTravelCount(dto.getAvailableTravelCount());
        card.setExpirationDate(dto.getExpirationDate());
        return card;
    }

    // ============= BLACK LIST =============

    public static BlackListDto toBlackListDto(BlackList blackList) {
        if (blackList == null) return null;
        return new BlackListDto(blackList.getCardGuid());
    }

    public static BlackList toBlackListEntity(BlackListDto dto) {
        if (dto == null) return null;
        BlackList blackList = new BlackList();
        blackList.setCardGuid(dto.getCardGuid());
        return blackList;
    }
    // UserSession
    public static UserSessionDto toUserSessionDto(UserSession s) {
        if (s == null) return null;
        return new UserSessionDto(s.getSessionId(), s.getUserId(), s.getTerminalId(), s.getExpirationTime().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }
    public static UserSession toUserSessionEntity(UserSessionDto dto, User user, Terminal terminal) {
        if (dto == null) return null;
        UserSession s = new UserSession();
        s.setSessionId(dto.getSessionId());
        s.setUser(user);
        s.setTerminal(terminal);
        s.setExpirationTime(dto.getExpirationTime().atZone(ZoneId.systemDefault()).toInstant());
        return s;
    }

    // Code
    public static CodeDto toCodeDto(Code c) {
        if (c == null) return null;
        CodeDto dto = new CodeDto();
        dto.setCodeUid(c.getCodeUid());
        dto.setCodeType(c.getCodeType());
        dto.setCodeContent(c.getCodeContent());
        dto.setAllowedUsage(c.getAllowedUsage());
        dto.setValidFrom(c.getValidFrom());
        dto.setValidTo(c.getValidTo());
        return dto;
    }
    public static Code toCodeEntity(CodeDto dto) {
        if (dto == null) return null;
        Code c = new Code();
        c.setCodeUid(dto.getCodeUid());
        c.setCodeType(dto.getCodeType());
        c.setCodeContent(dto.getCodeContent());
        c.setAllowedUsage(dto.getAllowedUsage());
        c.setValidFrom(dto.getValidFrom());
        c.setValidTo(dto.getValidTo());
        return c;
    }

    // MasterPassword
    public static MasterPasswordDto toMasterPasswordDto(MasterPassword mp) {
        if (mp == null) return null;
        MasterPasswordDto dto = new MasterPasswordDto();
        dto.setId(mp.getId());
        dto.setMasterPasswordHash(mp.getMasterPasswordHash());
        return dto;
    }
    public static MasterPassword toMasterPasswordEntity(MasterPasswordDto dto) {
        if (dto == null) return null;
        MasterPassword mp = new MasterPassword();
        mp.setMasterPasswordHash(dto.getMasterPasswordHash());
        return mp;
    }

    // ServiceEventType
    public static ServiceEventTypeDto toServiceEventTypeDto(ServiceEventType t) {
        if (t == null) return null;
        ServiceEventTypeDto dto = new ServiceEventTypeDto();
        dto.setEventType(t.getEventType());
        dto.setEventTypeName(t.getEventTypeName());
        return dto;
    }
    public static ServiceEventType toServiceEventTypeEntity(ServiceEventTypeDto dto) {
        if (dto == null) return null;
        ServiceEventType t = new ServiceEventType();
        t.setEventType(dto.getEventType());
        t.setEventTypeName(dto.getEventTypeName());
        return t;
    }

    // ServiceEvent
    public static ServiceEventDto toServiceEventDto(ServiceEvent e) {
        if (e == null) return null;
        ServiceEventDto dto = new ServiceEventDto();
        dto.setServiceEventId(e.getServiceEventId());
        dto.setEventTime(e.getEventTime());
        dto.setServiceEventType(toServiceEventTypeDto(e.getServiceEventType()));
        dto.setDoerUserId(e.getDoerUserId());
        dto.setReferenceTypeId(e.getReferenceTypeId());
        dto.setReferenceId(e.getReferenceId());
        dto.setEventDetails(e.getEventDetails());
        return dto;
    }
    public static ServiceEvent toServiceEventEntity(ServiceEventDto dto) {
        if (dto == null) return null;
        ServiceEvent e = new ServiceEvent();
        e.setServiceEventId(dto.getServiceEventId());
        e.setEventTime(dto.getEventTime());
        e.setServiceEventType(toServiceEventTypeEntity(dto.getServiceEventType()));
        e.setDoerUserId(dto.getDoerUserId());
        e.setReferenceTypeId(dto.getReferenceTypeId());
        e.setReferenceId(dto.getReferenceId());
        e.setEventDetails(dto.getEventDetails());
        return e;
    }

    // PaymentType
    public static PaymentTypeDto toPaymentTypeDto(PaymentType t) {
        if (t == null) return null;
        PaymentTypeDto dto = new PaymentTypeDto();
        dto.setPaymentTypeId(t.getPaymentTypeId());
        dto.setPaymentTypeName(t.getPaymentTypeName());
        return dto;
    }
    public static PaymentType toPaymentTypeEntity(PaymentTypeDto dto) {
        if (dto == null) return null;
        PaymentType t = new PaymentType();
        t.setPaymentTypeId(dto.getPaymentTypeId());
        t.setPaymentTypeName(dto.getPaymentTypeName());
        return t;
    }

    // PaymentResult
    public static PaymentResultDto toPaymentResultDto(PaymentResult r) {
        if (r == null) return null;
        PaymentResultDto dto = new PaymentResultDto();
        dto.setPaymentResultId(r.getPaymentResultId());
        dto.setPaymentResultName(r.getPaymentResultName());
        return dto;
    }
    public static PaymentResult toPaymentResultEntity(PaymentResultDto dto) {
        if (dto == null) return null;
        PaymentResult r = new PaymentResult();
        r.setPaymentResultId(dto.getPaymentResultId());
        r.setPaymentResultName(dto.getPaymentResultName());
        return r;
    }

    // Payment
    public static PaymentDto toPaymentDto(Payment p) {
        if (p == null) return null;
        PaymentDto dto = new PaymentDto();
        dto.setPaymentId(p.getPaymentId());
        dto.setPaymentTime(p.getPaymentTime());
        dto.setCardId(p.getCardId());
        dto.setBalanceBefore(p.getBalanceBefore());
        dto.setBalanceAfter(p.getBalanceAfter());
        dto.setPaymentType(toPaymentTypeDto(p.getPaymentType()));
        dto.setPaymentResult(toPaymentResultDto(p.getPaymentResult()));
        dto.setTerminalId(p.getTerminalId());
        return dto;
    }
    public static Payment toPaymentEntity(PaymentDto dto) {
        if (dto == null) return null;
        Payment p = new Payment();
        p.setPaymentId(dto.getPaymentId());
        p.setPaymentTime(dto.getPaymentTime());
        p.setCardId(dto.getCardId());
        p.setBalanceBefore(dto.getBalanceBefore());
        p.setBalanceAfter(dto.getBalanceAfter());
        p.setPaymentType(toPaymentTypeEntity(dto.getPaymentType()));
        p.setPaymentResult(toPaymentResultEntity(dto.getPaymentResult()));
        p.setTerminalId(dto.getTerminalId());
        return p;
    }

}