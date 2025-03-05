package com.ar.edu.um.montana.cars.domain;

import static com.ar.edu.um.montana.cars.domain.ClientTestSamples.*;
import static com.ar.edu.um.montana.cars.domain.PurchasedCarTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ar.edu.um.montana.cars.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = getClientSample1();
        Client client2 = new Client();
        assertThat(client1).isNotEqualTo(client2);

        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);

        client2 = getClientSample2();
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    void purchasedCarTest() {
        Client client = getClientRandomSampleGenerator();
        PurchasedCar purchasedCarBack = getPurchasedCarRandomSampleGenerator();

        client.setPurchasedCar(purchasedCarBack);
        assertThat(client.getPurchasedCar()).isEqualTo(purchasedCarBack);
        assertThat(purchasedCarBack.getClient()).isEqualTo(client);

        client.purchasedCar(null);
        assertThat(client.getPurchasedCar()).isNull();
        assertThat(purchasedCarBack.getClient()).isNull();
    }
}
