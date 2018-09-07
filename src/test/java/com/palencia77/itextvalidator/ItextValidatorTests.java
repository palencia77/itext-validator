package com.palencia77.itextvalidator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItextValidatorTests {
    private static final String SRC_PATH = "/home/palencia77/dev/itext-validator/src/main/resources/pdf/";

    @Test
    public void invalidPdfSignatures() {
        assert (!validateTest("DocumentoInvalidoPorCambiosPostFirma.pdf"));
        assert (!validateTest("FirmaInvalidaErrorEnProceso.pdf"));
    }

    @Test
    public void validPdfSignatures() {
        assert (validateTest("TOKEN_OK_IF-2018-01292432-APN-TESTSADE.PDF"));
    }

    private boolean validateTest(String fileName) {
        ItextValidator itextValidator = new ItextValidator();
        boolean result = false;
        try {
            result = itextValidator.validateSignatures(new FileInputStream(SRC_PATH.concat(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
