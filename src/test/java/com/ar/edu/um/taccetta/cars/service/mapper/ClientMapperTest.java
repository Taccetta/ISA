package com.ar.edu.um.taccetta.cars.service.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.ar.edu.um.taccetta.cars.domain.Client;
import com.ar.edu.um.taccetta.cars.service.dto.ClientDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientMapperTest {

    private ClientMapper clientMapper;

    @BeforeEach
    public void setUp() {
        clientMapper = new ClientMapperImpl();
    }

    @Test
    public void testClientDtoToEntity() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setFirstName("John");
        clientDTO.setLastName("Doe");
        clientDTO.setAddress("Avenida Montenegro 123");
        clientDTO.setEmail("john.doe@gmail.com");
        clientDTO.setPhone("1234567890");

        Client client = clientMapper.toEntity(clientDTO);

        System.out.println(client);

        assertEquals(client.getId(), clientDTO.getId());
        assertEquals(client.getFirstName(), clientDTO.getFirstName());
        assertEquals(client.getLastName(), clientDTO.getLastName());
        assertEquals(client.getAddress(), clientDTO.getAddress());
        assertEquals(client.getEmail(), clientDTO.getEmail());
        assertEquals(client.getPhone(), clientDTO.getPhone());
    }

    @Test
    public void testClientEntitytoDto() {
        Client client = new Client();
        client.setId(2L);
        client.setFirstName("Jane");
        client.setLastName("Doe");
        client.setAddress("Avenida Montenegro 123");
        client.setEmail("jane.doe@gmail.com");
        client.setPhone("1234567890");

        ClientDTO clientDTO = clientMapper.toDto(client);

        System.out.println(clientDTO);

        assertEquals(client.getId(), clientDTO.getId());
        assertEquals(client.getFirstName(), clientDTO.getFirstName());
        assertEquals(client.getLastName(), clientDTO.getLastName());
        assertEquals(client.getAddress(), clientDTO.getAddress());
        assertEquals(client.getEmail(), clientDTO.getEmail());
        assertEquals(client.getPhone(), clientDTO.getPhone());
    }

    @Test
    public void testNullClientDtoToEntity() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(null);
        clientDTO.setFirstName(null);
        clientDTO.setLastName(null);
        clientDTO.setAddress(null);
        clientDTO.setEmail(null);
        clientDTO.setPhone(null);

        Client client = clientMapper.toEntity(clientDTO);

        System.out.println(client);

        assertNull(client.getId());
        assertNull(client.getFirstName());
        assertNull(client.getLastName());
        assertNull(client.getAddress());
        assertNull(client.getEmail());
        assertNull(client.getPhone());
    }
}
