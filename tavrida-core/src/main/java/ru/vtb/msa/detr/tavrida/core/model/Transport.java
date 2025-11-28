package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "transports")
public class Transport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transport_id")
    private Long transportId;

    @Column(name = "transport_guid", nullable = false, unique = true)
    private UUID transportGuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrier_id", nullable = false)
    private Carrier carrier;

    @Column(name = "transport_number", nullable = false)
    private String transportNumber;

    @Column(name = "transport_name", nullable = false)
    private String transportName;

    // --- getters/setters ---
    public Long getTransportId() { return transportId; }
    public void setTransportId(Long transportId) { this.transportId = transportId; }

    public UUID getTransportGuid() { return transportGuid; }
    public void setTransportGuid(UUID transportGuid) { this.transportGuid = transportGuid; }

    public Carrier getCarrier() { return carrier; }
    public void setCarrier(Carrier carrier) { this.carrier = carrier; }

    public String getTransportNumber() { return transportNumber; }
    public void setTransportNumber(String transportNumber) { this.transportNumber = transportNumber; }

    public String getTransportName() { return transportName; }
    public void setTransportName(String transportName) { this.transportName = transportName; }
}