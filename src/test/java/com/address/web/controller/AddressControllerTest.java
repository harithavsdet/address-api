package com.address.web.controller;

import com.address.web.entity.Address;
import com.address.web.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import javax.annotation.PostConstruct;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddressControllerTest {

    @LocalServerPort
    private int port;

    private String GET_ADDRESS_BY_COUNTRY;

    @PostConstruct
    public void init() {
        GET_ADDRESS_BY_COUNTRY = "http://localhost:" + port + "/address/country/{country}";
    }

    private static final String USA = "USA";

    private static final String CANADA = "CANADA";

    private static final String UNKNOWN = "UNKNOWN";

    @Autowired
    private AddressRepository addressRepository;

    private Address us_address1 = null;
    private Address us_address2 = null;
    private Address can_address1 = null;

    @BeforeEach
    public void before(){

        us_address1= new Address();
        us_address1.setAddress1("Los angles-Address1");
        us_address1.setAddress2("Los angles-Address2");
        us_address1.setCity("Los angles");
        us_address1.setCountry(USA);
        us_address1.setZipCode("90005");

        us_address2= new Address();
        us_address2.setAddress1("Tallahassee-Address1");
        us_address2.setAddress2("Tallahassee-Address2");
        us_address2.setCity("Tallahassee");
        us_address2.setCountry(USA);
        us_address2.setZipCode("32301");

        can_address1= new Address();
        can_address1.setAddress1("Toronto-Address1");
        can_address1.setAddress2("Toronto-Address2");
        can_address1.setCity("Toronto");
        can_address1.setCountry(CANADA);
        can_address1.setZipCode("M4B 1B4");

    }
    @Test
    public void testUSAddress() {

        //saving the db data
        Address dbAddress1 = addressRepository.save(us_address1);
        Address dbAddress2 = addressRepository.save(us_address2);
        Address[] addresses = given()
                .pathParam("country", USA)
                .when()
                .get(GET_ADDRESS_BY_COUNTRY)
                .then()
                .statusCode(200)
                .extract()
                .as(Address[].class);
        assertThat(addresses.length).isEqualTo(2);
        assertThat(addresses).containsExactlyInAnyOrder(dbAddress1, dbAddress2);
    }

    @Test
    public void testCanadaAddress() {
        //saving db data
        Address dbAddress = addressRepository.save(can_address1);
        Address[] addresses = given()
                .pathParam("country", CANADA)
                .when()
                .get(GET_ADDRESS_BY_COUNTRY)
                .then()
                .statusCode(200)
                .extract()
                .as(Address[].class);
        assertThat(addresses.length).isEqualTo(1);
        assertThat(addresses).containsExactlyInAnyOrder(dbAddress);
    }

    @Test
    public void testUnknownAddress() {
        given()
                .pathParam("country", UNKNOWN)
                .when()
                .get(GET_ADDRESS_BY_COUNTRY)
                .then()
                .statusCode(200)
                .body("size()", is(0));
    }
}
