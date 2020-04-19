package uoc.rewards.rewardsapi.util;


import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import uoc.rewards.rewardsapi.common.exception.RSAException;

import java.io.*;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class JWTPemUtils {

    private static final KeyFactory RSA_KEY_FACTORY;

    private JWTPemUtils() {
    }

    static {
        try {
            RSA_KEY_FACTORY = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }


    private static byte[] parsePEMFile(File pemFile) throws IOException {
        if (!pemFile.isFile() || !pemFile.exists()) {
            throw new FileNotFoundException(String.format("The file '%s' doesn't exist.", pemFile.getAbsolutePath()));
        }
        PemReader reader = new PemReader(new FileReader(pemFile));
        PemObject pemObject = reader.readPemObject();
        byte[] content = pemObject.getContent();
        reader.close();
        return content;
    }

    private static PublicKey getPublicKey(byte[] keyBytes) {
        try {
            EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            return RSA_KEY_FACTORY.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new RSAException(String.format("Could not reconstruct the public key. Reason: %s", e.getMessage()));
        }
    }

    private static PrivateKey getPrivateKey(byte[] keyBytes) {
        try {
            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            return RSA_KEY_FACTORY.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new RSAException(String.format("Could not reconstruct the private key. Reason: %s", e.getMessage()));
        }
    }

    public static PublicKey readPublicKeyFromFile(String filepath) throws IOException {
        byte[] bytes = JWTPemUtils.parsePEMFile(new File(filepath));
        return JWTPemUtils.getPublicKey(bytes);
    }

    public static PrivateKey readPrivateKeyFromFile(String filepath) throws IOException {
        byte[] bytes = JWTPemUtils.parsePEMFile(new File(filepath));
        return JWTPemUtils.getPrivateKey(bytes);
    }
}