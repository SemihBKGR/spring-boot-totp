package com.semihbkgr.springboottotp.qr;

import java.awt.image.BufferedImage;

public interface QrCodeGenerator {

    BufferedImage generate(String username, String secret, int size) throws Exception;

}
