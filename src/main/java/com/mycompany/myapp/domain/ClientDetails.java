package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A ClientDetails.
 */
@Entity
@Table(name = "client_details")
public class ClientDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "kominy_dymowe")
    private String kominyDymowe;

    @Column(name = "kominy_spalinowe")
    private String kominySpalinowe;

    @Column(name = "przewody_wentylacyjne")
    private String przewodyWentylacyjne;

    @Column(name = "ryczalt_jednorazowy")
    private String ryczaltJednorazowy;

    @Column(name = "data_czyszczenia")
    private LocalDate dataCzyszczenia;

    @Column(name = "dzien_czyszczenia")
    private Integer dzienCzyszczenia;

    @Column(name = "zaplacono")
    private String zaplacono;

    @Column(name = "podpis")
    private String podpis;

    @ManyToOne
    @JsonIgnoreProperties(value = { "clientDetails" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientDetails id(Long id) {
        this.id = id;
        return this;
    }

    public String getKominyDymowe() {
        return this.kominyDymowe;
    }

    public ClientDetails kominyDymowe(String kominyDymowe) {
        this.kominyDymowe = kominyDymowe;
        return this;
    }

    public void setKominyDymowe(String kominyDymowe) {
        this.kominyDymowe = kominyDymowe;
    }

    public String getKominySpalinowe() {
        return this.kominySpalinowe;
    }

    public ClientDetails kominySpalinowe(String kominySpalinowe) {
        this.kominySpalinowe = kominySpalinowe;
        return this;
    }

    public void setKominySpalinowe(String kominySpalinowe) {
        this.kominySpalinowe = kominySpalinowe;
    }

    public String getPrzewodyWentylacyjne() {
        return this.przewodyWentylacyjne;
    }

    public ClientDetails przewodyWentylacyjne(String przewodyWentylacyjne) {
        this.przewodyWentylacyjne = przewodyWentylacyjne;
        return this;
    }

    public void setPrzewodyWentylacyjne(String przewodyWentylacyjne) {
        this.przewodyWentylacyjne = przewodyWentylacyjne;
    }

    public String getRyczaltJednorazowy() {
        return this.ryczaltJednorazowy;
    }

    public ClientDetails ryczaltJednorazowy(String ryczaltJednorazowy) {
        this.ryczaltJednorazowy = ryczaltJednorazowy;
        return this;
    }

    public void setRyczaltJednorazowy(String ryczaltJednorazowy) {
        this.ryczaltJednorazowy = ryczaltJednorazowy;
    }

    public LocalDate getDataCzyszczenia() {
        return this.dataCzyszczenia;
    }

    public ClientDetails dataCzyszczenia(LocalDate dataCzyszczenia) {
        this.dataCzyszczenia = dataCzyszczenia;
        return this;
    }

    public void setDataCzyszczenia(LocalDate dataCzyszczenia) {
        this.dataCzyszczenia = dataCzyszczenia;
    }

    public Integer getDzienCzyszczenia() {
        return this.dzienCzyszczenia;
    }

    public ClientDetails dzienCzyszczenia(Integer dzienCzyszczenia) {
        this.dzienCzyszczenia = dzienCzyszczenia;
        return this;
    }

    public void setDzienCzyszczenia(Integer dzienCzyszczenia) {
        this.dzienCzyszczenia = dzienCzyszczenia;
    }

    public String getZaplacono() {
        return this.zaplacono;
    }

    public ClientDetails zaplacono(String zaplacono) {
        this.zaplacono = zaplacono;
        return this;
    }

    public void setZaplacono(String zaplacono) {
        this.zaplacono = zaplacono;
    }

    public String getPodpis() {
        return this.podpis;
    }

    public ClientDetails podpis(String podpis) {
        this.podpis = podpis;
        return this;
    }

    public void setPodpis(String podpis) {
        this.podpis = podpis;
    }

    public Client getClient() {
        return this.client;
    }

    public ClientDetails client(Client client) {
        this.setClient(client);
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientDetails)) {
            return false;
        }
        return id != null && id.equals(((ClientDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientDetails{" +
            "id=" + getId() +
            ", kominyDymowe='" + getKominyDymowe() + "'" +
            ", kominySpalinowe='" + getKominySpalinowe() + "'" +
            ", przewodyWentylacyjne='" + getPrzewodyWentylacyjne() + "'" +
            ", ryczaltJednorazowy='" + getRyczaltJednorazowy() + "'" +
            ", dataCzyszczenia='" + getDataCzyszczenia() + "'" +
            ", dzienCzyszczenia=" + getDzienCzyszczenia() +
            ", zaplacono='" + getZaplacono() + "'" +
            ", podpis='" + getPodpis() + "'" +
            "}";
    }
}
