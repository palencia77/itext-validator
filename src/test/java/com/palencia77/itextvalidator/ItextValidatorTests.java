package com.palencia77.itextvalidator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.stream.FileImageInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItextValidatorTests {
    private static final String SRC_PATH = "C:\\GDEGIT\\itext-validator\\src\\main\\resources\\pdf\\";

    @Test
    public void invalidPdfSignatures() {
        Date inicio = new Date();
        //assert (!validateTest("Invalido_CambiosPostFirma.pdf"));
        assert (!validateTest("Invalido_ErrorEnProceso.pdf"));
        System.out.println("Duracion = " + ((new Date().getTime()-inicio.getTime())) + "ms");
    }

    @Test
    public void validPdfSignatures() {
        Date inicio = new Date();
        //assert (validateTest("Valido_FirmaSimpleToken.PDF"));
        assert (validateTest("Valido_FirmaConjuntaToken.pdf"));
        //assert (validateTest("Valido_FirmaSimpleCertificado.pdf"));
        System.out.println("Duracion = " + ((new Date().getTime()-inicio.getTime())) + "ms");
    }

    private boolean validateTest(String fileName) {
        ItextValidator itextValidator = new ItextValidator();
        boolean result = false;
        try {
            //Thread.sleep(8000);
            //byte[] bytes = Files.readAllBytes(new java.io.File("C:\\POC\\PDFs\\MESYA_ADIFSE.pdf").toPath());
            //System.out.println("bytes.length = " + bytes.length);
            //result = itextValidator.validateSignatures(new FileInputStream("C:\\POC\\PDFs\\MESYA_ADIFSE.pdf"));
            //result = itextValidator.validateSignatures(new FileInputStream(SRC_PATH.concat(fileName)));
            result = itextValidator.validateSignatures(new File("C:\\POC\\PDFs\\Valid_750_FirmaSimpleImportado.pdf"));
            //result = itextValidator.validateSignatures(new File(SRC_PATH.concat(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        } /*catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return result;
    }
}
