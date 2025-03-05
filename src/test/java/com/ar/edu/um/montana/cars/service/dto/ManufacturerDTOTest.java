package com.ar.edu.um.montana.cars.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ar.edu.um.montana.cars.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManufacturerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManufacturerDTO.class);
        ManufacturerDTO manufacturerDTO1 = new ManufacturerDTO();
        manufacturerDTO1.setId(1L);
        ManufacturerDTO manufacturerDTO2 = new ManufacturerDTO();
        assertThat(manufacturerDTO1).isNotEqualTo(manufacturerDTO2);
        manufacturerDTO2.setId(manufacturerDTO1.getId());
        assertThat(manufacturerDTO1).isEqualTo(manufacturerDTO2);
        manufacturerDTO2.setId(2L);
        assertThat(manufacturerDTO1).isNotEqualTo(manufacturerDTO2);
        manufacturerDTO1.setId(null);
        assertThat(manufacturerDTO1).isNotEqualTo(manufacturerDTO2);
    }
}
