package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClientDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientDetails.class);
        ClientDetails clientDetails1 = new ClientDetails();
        clientDetails1.setId(1L);
        ClientDetails clientDetails2 = new ClientDetails();
        clientDetails2.setId(clientDetails1.getId());
        assertThat(clientDetails1).isEqualTo(clientDetails2);
        clientDetails2.setId(2L);
        assertThat(clientDetails1).isNotEqualTo(clientDetails2);
        clientDetails1.setId(null);
        assertThat(clientDetails1).isNotEqualTo(clientDetails2);
    }
}
