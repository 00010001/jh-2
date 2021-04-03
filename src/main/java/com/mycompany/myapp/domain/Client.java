package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "wlasciciel_domu")
    private String wlascicielDomu;

    @Column(name = "numer")
    private Integer numer;

    @Column(name = "adres")
    private String adres;

    @OneToMany(mappedBy = "client")
    @JsonIgnoreProperties(value = { "client" }, allowSetters = true)
    private Set<ClientDetails> clientDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client id(Long id) {
        this.id = id;
        return this;
    }

    public String getWlascicielDomu() {
        return this.wlascicielDomu;
    }

    public Client wlascicielDomu(String wlascicielDomu) {
        this.wlascicielDomu = wlascicielDomu;
        return this;
    }

    public void setWlascicielDomu(String wlascicielDomu) {
        this.wlascicielDomu = wlascicielDomu;
    }

    public Integer getNumer() {
        return this.numer;
    }

    public Client numer(Integer numer) {
        this.numer = numer;
        return this;
    }

    public void setNumer(Integer numer) {
        this.numer = numer;
    }

    public String getAdres() {
        return this.adres;
    }

    public Client adres(String adres) {
        this.adres = adres;
        return this;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public Set<ClientDetails> getClientDetails() {
        return this.clientDetails;
    }

    public Client clientDetails(Set<ClientDetails> clientDetails) {
        this.setClientDetails(clientDetails);
        return this;
    }

    public Client addClientDetails(ClientDetails clientDetails) {
        this.clientDetails.add(clientDetails);
        clientDetails.setClient(this);
        return this;
    }

    public Client removeClientDetails(ClientDetails clientDetails) {
        this.clientDetails.remove(clientDetails);
        clientDetails.setClient(null);
        return this;
    }

    public void setClientDetails(Set<ClientDetails> clientDetails) {
        if (this.clientDetails != null) {
            this.clientDetails.forEach(i -> i.setClient(null));
        }
        if (clientDetails != null) {
            clientDetails.forEach(i -> i.setClient(this));
        }
        this.clientDetails = clientDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", wlascicielDomu='" + getWlascicielDomu() + "'" +
            ", numer=" + getNumer() +
            ", adres='" + getAdres() + "'" +
            "}";
    }
}
