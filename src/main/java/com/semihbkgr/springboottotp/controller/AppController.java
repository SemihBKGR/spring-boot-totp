package com.semihbkgr.springboottotp.controller;

import com.google.zxing.qrcode.QRCodeWriter;
import com.semihbkgr.springboottotp.qr.QrCodeGenerator;
import com.semihbkgr.springboottotp.user.UserService;
import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class AppController {

    private static final int QR_CODE_SIZE = 200;

    private final UserService userService;
    private final QRCodeWriter qrCodeWriter;
    private final QrCodeGenerator qrCodeGenerator;

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/signin")
    public String signin() {
        return "signin";
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request, Model model) {
        var username = request.getUserPrincipal().getName();
        var user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/2fa")
    public String activate2fa() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByUsername(username);
        var secretKey = GoogleAuthenticator.Companion.createRandomSecretAsByteArray();
        var secretKeyStr = new String(secretKey, StandardCharsets.UTF_8);
        user.getUserDetail().setSecretKey(secretKeyStr);
        userService.save(user);
        return "2fa";
    }

    @PostMapping("/2fa")
    public String verify2fa(@RequestParam("totp") String totp, HttpServletRequest request) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByUsername(username);
        var secretKey = user.getUserDetail().getSecretKey();
        var isCorrectTotp = new GoogleAuthenticator(secretKey.getBytes(StandardCharsets.UTF_8)).isValid(totp, new Date());
        if (isCorrectTotp) {
            user.getUserDetail().setIs2faEnabled(true);
            userService.save(user);
            return "redirect:/profile";
        } else {
            return "redirect:/2fa?incorrect";
        }
    }

    @GetMapping(value = "/2fa/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> qr2fa() throws Exception {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByUsername(username);
        var secretKey = user.getUserDetail().getSecretKey();
        if (secretKey != null) {
            return ResponseEntity.ok(qrCodeGenerator.generate(username, secretKey, QR_CODE_SIZE));
        }
        return null;
    }

    @GetMapping("/totp")
    public String totp() {
        return "totp";
    }

}
