package br.com.magu.magu.service.impl;

import br.com.magu.magu.models.Clients.ClientResponseDTO;
import br.com.magu.magu.service.ClientService;
import br.com.magu.magu.utils.IntegrationUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.internal.ErrorsException;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.common.PhoneRequest;
import com.mercadopago.client.customer.CustomerAddressRequest;
import com.mercadopago.client.customer.CustomerClient;
import com.mercadopago.client.customer.CustomerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.customer.Customer;
import com.nimbusds.oauth2.sdk.client.ClientRegistrationResponse;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;


public class clientServiceImpl implements ClientService {
    private static final Logger logger = LoggerFactory.getLogger(clientServiceImpl.class);
    private static final  TypeReference<ClientResponseDTO> clientResponse = new TypeReference<>() {};

    @Value("${url.mp}")
    private String urlMp;

    @Autowired private IntegrationUtil integrationUtil;

    @SneakyThrows
    @Override
    public ClientResponseDTO getCustomerClient(String email) throws IOException, ErrorsException {

        logger.info("Calling Client By Email MP");

        ResponseEntity<ClientResponseDTO> response =
                    integrationUtil.getRestCall(
                            urlMp + "/v1/customers/search?email=" + email ,
                            "GET",
                            null,
                            clientResponse);

        if(response.getBody() != null && response.getBody().getResults().size() != 0){
            return response.getBody();
        }
        return null;

    }

    public Customer createClient(CustomerRequest clientRequest ) throws MPException, MPApiException {
        CustomerClient client = new CustomerClient();
        CustomerRequest customerRequest =
                CustomerRequest.builder()
                        .email(clientRequest.getEmail())
                        .firstName(clientRequest.getFirstName())
                        .lastName(clientRequest.getLastName())
                        .phone(PhoneRequest.builder()
                                .areaCode(clientRequest.getPhone().getAreaCode())
                                .number(clientRequest.getPhone().getNumber()).build())
                        .identification(
                                IdentificationRequest.builder()
                                        .type(clientRequest.getIdentification().getType())
                                        .number(clientRequest.getIdentification().getNumber()).build())
                        .defaultAddress("Home")
                        .address(
                                CustomerAddressRequest.builder()
                                        .id(clientRequest.getAddress().getId())
                                        .zipCode(clientRequest.getAddress().getZipCode())
                                        .streetName(clientRequest.getAddress().getStreetName())
                                        .streetNumber(clientRequest.getAddress().getStreetNumber())
                                        .build())
                        .dateRegistred(OffsetDateTime.of(2000, 1, 18, 0, 0, 0, 0, ZoneOffset.UTC))
                        .description(clientRequest.getDescription())
                        .defaultCard(clientRequest.getDefaultCard())
                        .build();
       return client.create(customerRequest);
    }


}
