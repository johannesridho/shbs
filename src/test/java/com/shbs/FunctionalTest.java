package com.shbs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.RestAssured;
import io.restassured.internal.mapping.Jackson2Mapper;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Function;

@SpringBootTest(classes = ShbsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class
})
@FlywayTest(locationsForMigrate = {"db/migration"})
public class FunctionalTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(0);

    @Autowired
    protected ObjectMapper objectMapper;

    @Value("${local.server.port}")
    protected int port;

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    protected TestHelper testHelper;

    @Before
    public void baseSetup() {
        RestAssured.port = port;
        RestAssured.objectMapper(new Jackson2Mapper((cls, charset) -> objectMapper));
    }

    @After
    public void cleanUp() {
        testHelper.cleanUp();
    }

    public <R> R executeTx(final Function<TransactionStatus, R> lambda) {
        return new TransactionTemplate(txManager).execute(status -> lambda.apply(status));
    }
}