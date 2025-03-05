package com.ar.edu.um.montana.cars.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ar.edu.um.montana.cars.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchasedCarDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchasedCarDTO.class);
        PurchasedCarDTO purchasedCarDTO1 = new PurchasedCarDTO();
        purchasedCarDTO1.setId(1L);
        PurchasedCarDTO purchasedCarDTO2 = new PurchasedCarDTO();
        assertThat(purchasedCarDTO1).isNotEqualTo(purchasedCarDTO2);
        purchasedCarDTO2.setId(purchasedCarDTO1.getId());
        assertThat(purchasedCarDTO1).isEqualTo(purchasedCarDTO2);
        purchasedCarDTO2.setId(2L);
        assertThat(purchasedCarDTO1).isNotEqualTo(purchasedCarDTO2);
        purchasedCarDTO1.setId(null);
        assertThat(purchasedCarDTO1).isNotEqualTo(purchasedCarDTO2);
    }
}
