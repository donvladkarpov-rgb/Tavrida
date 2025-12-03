package ru.vtb.msa.detr.tavrida.core.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.msa.detr.tavrida.api.model.*;
import ru.vtb.msa.detr.tavrida.api.model.admin.*;
import ru.vtb.msa.detr.tavrida.api.web.AdminApi;
import ru.vtb.msa.detr.tavrida.core.service.AdminService;

@RestController
public class AdminController implements AdminApi {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public ResponseEntity<CarrierDto> registerCarrier(@Valid @RequestBody CarrierRegistrationRequest request) {
        CarrierDto dto = adminService.registerCarrier(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Override
    public ResponseEntity<TransportDto> registerTransport(@Valid @RequestBody TransportRegistrationRequest request) {
        TransportDto dto = adminService.registerTransport(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Override
    public ResponseEntity<TerminalDto> registerTerminal(@Valid @RequestBody TerminalRegistrationRequest request) {
        TerminalDto dto = adminService.registerTerminal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Override
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        UserDto dto = adminService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Override
    public ResponseEntity<CardDto> registerCard(@Valid @RequestBody CardRegistrationRequest request) {
        CardDto dto = adminService.registerCard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}