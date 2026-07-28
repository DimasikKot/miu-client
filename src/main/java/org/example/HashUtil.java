package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtil {

    private static final int BUFFER_SIZE = 1024 * 1024;

    private HashUtil() {
    }

    public static String sha256(Path file) throws IOException {

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            try (InputStream input = Files.newInputStream(file)) {

                byte[] buffer = new byte[BUFFER_SIZE];

                int read;

                while ((read = input.read(buffer)) != -1) {
                    digest.update(buffer, 0, read);
                }
            }

            return bytesToHex(digest.digest());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] hash) {

        StringBuilder builder = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            builder.append(String.format("%02x", b));
        }

        return builder.toString();
    }
}