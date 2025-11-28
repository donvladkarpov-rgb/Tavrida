package ru.vtb.msa.detr.tavrida.core.model.mapers;

import ru.vtb.msa.detr.tavrida.api.model.*;
import ru.vtb.msa.detr.tavrida.core.model.*;

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
        return new CardDto(
                card.getCardId(),
                card.getCardGuid(),
                toCardTypeDto(card.getCardType()),
                userId != null ? new UserDto(userId, null, null, null, null) : null,
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
}