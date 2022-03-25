package com.semihbkgr.springboottotp.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class DefaultQrCodeGenerator implements QrCodeGenerator {

    private static final String UNFORMATTED_URL_PATTERN = "otpauth://totp/%s:%s?secret=%s&issuer=%s";

    private final QRCodeWriter writer;
    private final String issuer;

    public DefaultQrCodeGenerator(QRCodeWriter writer, @Value("${qr.issuer}") String issuer) {
        this.writer = writer;
        this.issuer = issuer;
    }

    @Override
    public BufferedImage generate(String username, String secret, int size) throws WriterException {
        var uri = UNFORMATTED_URL_PATTERN.formatted(issuer, username, secret, issuer);
        var qr = writer.encode(uri, BarcodeFormat.QR_CODE, size, size);
        return MatrixToImageWriter.toBufferedImage(qr);
    }

}
