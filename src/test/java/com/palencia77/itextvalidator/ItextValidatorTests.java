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
    private static final String SRC_PATH = "C:\\GDEGIT\\itext-validator\\src\\main\\resources\\pdf\\";

    @Test
    public void invalidPdfSignatures() {
        //assert (!validateTest("Invalido_CambiosPostFirma.pdf"));
        assert (!validateTest("Invalido_ErrorEnProceso.pdf"));
    }

    @Test
    public void validPdfSignatures() {
        assert (validateTest("Valido_FirmaSimpleToken.PDF"));
        //assert (validateTest("Valido_FirmaConjuntaToken.pdf"));
        //assert (validateTest("Valido_FirmaSimpleCertificado.pdf"));
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
