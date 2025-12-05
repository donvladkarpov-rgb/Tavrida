package ru.vtb.msa.detr.tavrida.core.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_fio")
    private String userFio;

    @Column(name = "user_password_hash")
    private String userPasswordHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_role_id")
    private UserRole userRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrier_id")
    private Carrier carrier;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

    // --- constructors, getters, setters ---
    public User() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserFio() { return userFio; }
    public void setUserFio(String userFio) { this.userFio = userFio; }

    public String getUserPasswordHash() { return userPasswordHash; }
    public void setUserPasswordHash(String userPasswordHash) { this.userPasswordHash = userPasswordHash; }

    public UserRole getUserRole() { return userRole; }
    public void setUserRole(UserRole userRole) { this.userRole = userRole; }

    public Carrier getCarrier() { return carrier; }
    public void setCarrier(Carrier carrier) { this.carrier = carrier; }

    public List<Card> getCards() { return cards; }
    public void setCards(List<Card> cards) { this.cards = cards; }

    public void addCard(Card card) {
        cards.add(card);
        card.setUser(this);
    }

    public Integer getUserRoleId() {
        if (userRole == null) return null;
        return userRole.getUserRoleId();
    }
}