package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "terminals")
public class Terminal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terminal_id")
    private Long terminalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_id")
    private Transport transport;

    @Column(name = "terminal_guid", nullable = false, unique = true)
    private UUID terminalGuid;

    @Column(name = "terminal_number", nullable = false)
    private String terminalNumber;

    @Column(name = "terminal_serial", nullable = false)
    private String terminalSerialNumber;

    // --- getters/setters ---
    public Long getTerminalId() { return terminalId; }
    public void setTerminalId(Long terminalId) { this.terminalId = terminalId; }

    public Transport getTransport() { return transport; }
    public void setTransport(Transport transport) { this.transport = transport; }

    public UUID getTerminalGuid() { return terminalGuid; }
    public void setTerminalGuid(UUID terminalGuid) { this.terminalGuid = terminalGuid; }

    public String getTerminalNumber() { return terminalNumber; }
    public void setTerminalNumber(String terminalNumber) { this.terminalNumber = terminalNumber; }

    public Long getTransportId() {
        if (transport == null) return null;
        return transport.getTransportId();
    }

    public String getTerminalSerialNumber() {
        return terminalSerialNumber;
    }

    public void setTerminalSerialNumber(String terminalSerialNumber) {
        this.terminalSerialNumber = terminalSerialNumber;
    }
}