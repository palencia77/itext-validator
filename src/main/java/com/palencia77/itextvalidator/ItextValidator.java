package com.palencia77.itextvalidator;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.HashMap;

public class ItextValidator {
    private static final Logger logger = LoggerFactory.getLogger(ItextValidator.class);
    public static final String ALL_VALID = "allValid";
    public static final String VALID_CLOSING = "validClosing";
    public static final String BLANK_SIGNATURES = "blankSignatures";
    public static final String EXCEPTION = "exception";
    public static final String REVISIONS_COVERED = "revisionsCovered";

    public boolean validateSignatures(InputStream file) throws IOException {
        HashMap<String, Boolean> result = getResultMap();
        PdfReader pdfReader = null;
        try {
            pdfReader = new PdfReader(file);
            logger.info("###########################################################################################");
            logger.info("pdf info: " + pdfReader.getInfo().toString());
        } catch (IOException e) {
            logger.error("Error reading file", e);
            throw e;
        }

        AcroFields acroFields = pdfReader.getAcroFields();
        logger.info("total revisions: " + acroFields.getTotalRevisions());
        logger.info("blank signatures names: " + acroFields.getBlankSignatureNames());

        result.put(BLANK_SIGNATURES, acroFields.getBlankSignatureNames().size() > 0);

        if (!CollectionUtils.isEmpty(acroFields.getSignatureNames())) {
            for (String signature : acroFields.getSignatureNames()) {
                try {
                    logger.info("****************************");
                    logger.info("signature: " + signature);
                    logger.info("signature revision: " + acroFields.getRevision(signature));

                    // Validating revision signature
                    if (acroFields.getRevision(signature) < acroFields.getTotalRevisions()) {
                        InputStream pdfRevision = acroFields.extractRevision(signature);
                        PdfReader readerRevision = new PdfReader(pdfRevision);
                        AcroFields acroFieldsRevision = readerRevision.getAcroFields();
                        boolean revisionCoverWholeDoc = acroFieldsRevision.signatureCoversWholeDocument(signature);
                        logger.info("revisionCoverWholeDoc: " + revisionCoverWholeDoc);
                        if (!revisionCoverWholeDoc) {
                            result.put(REVISIONS_COVERED, Boolean.FALSE);
                        }
                    }

                    boolean coverWholeFinalDoc = acroFields.signatureCoversWholeDocument(signature);
                    logger.info("coverWholeFinalDoc: " + coverWholeFinalDoc);

                    Security.addProvider(new BouncyCastleProvider());
                    PdfPKCS7 pkcs7 = acroFields.verifySignature(signature);
                    boolean isValidSignature = pkcs7.verify();
                    logger.info("isValidSignature: " + isValidSignature);


                    if (!isValidSignature) {
                        result.put(ALL_VALID, Boolean.FALSE);
                    }
                    result.put(VALID_CLOSING, signature.equals("signature_cierre") && isValidSignature && coverWholeFinalDoc);

                } catch (GeneralSecurityException e) {
                    logger.info("Error verifying signature: " + signature);
                    logger.debug("Error verifying signature: " + signature, e); //Signature length not correct: got 107 but was expecting 256
                    result.put(ALL_VALID, Boolean.FALSE);
                    result.put(EXCEPTION, Boolean.TRUE);
                }
            }
        }

        boolean isDocValid = result.get(ALL_VALID) && result.get(REVISIONS_COVERED) && result.get(VALID_CLOSING)
                && !result.get(BLANK_SIGNATURES) && !result.get(EXCEPTION);
        logger.info("----------------------");
        logger.info("isDocValid: " + isDocValid);
        logger.info("----------------------");
        return isDocValid;
    }

    private HashMap<String, Boolean> getResultMap() {
        HashMap<String, Boolean> result = new HashMap<>();
        result.put(ALL_VALID, Boolean.TRUE);
        result.put(REVISIONS_COVERED, Boolean.TRUE);
        result.put(VALID_CLOSING, Boolean.FALSE);
        result.put(BLANK_SIGNATURES, Boolean.FALSE);
        result.put(EXCEPTION, Boolean.FALSE);
        return result;
    }
}
