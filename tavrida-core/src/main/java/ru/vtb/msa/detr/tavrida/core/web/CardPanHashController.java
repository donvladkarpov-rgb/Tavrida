package ru.vtb.msa.detr.tavrida.core.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.msa.detr.tavrida.api.web.CardPanHashApi;
import ru.vtb.msa.detr.tavrida.api.model.CardPanHashDto;
import ru.vtb.msa.detr.tavrida.core.service.CardPanHashService;

import java.util.List;

@RestController
public class CardPanHashController implements CardPanHashApi {

    private final CardPanHashService service;

    public CardPanHashController(CardPanHashService service) {
        this.service = service;
    }

    @Override
    public List<CardPanHashDto> getAll() {
        return service.findAll();
    }

    @Override
    public ResponseEntity<CardPanHashDto> getById(Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Override
    public ResponseEntity<CardPanHashDto> getByHash(String hash) {
        return ResponseEntity.ok(service.findByPanHash(hash));
    }

    @Override
    public ResponseEntity<CardPanHashDto> create(CardPanHashDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Override
    public ResponseEntity<CardPanHashDto> update(Long id, CardPanHashDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}