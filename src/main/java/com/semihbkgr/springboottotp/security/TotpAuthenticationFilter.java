package com.semihbkgr.springboottotp.security;

import com.semihbkgr.springboottotp.user.UserService;
import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@RequiredArgsConstructor
public class TotpAuthenticationFilter extends OncePerRequestFilter {

    private static final String SESSION_ATTRIB_2FA_AUTHENTICATED = "2fa-authenticated";
    private static final String PARAMETER_NAME_TOTP_FIELD = "totp";
    private static final String URL_PARAM_FAILED_AUTH = "?incorrect";

    private final String totpAuthUrl;
    private final String successAuthUrl;
    private final UserService userService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if (request.getRequestURI().equals(totpAuthUrl)) {
            return false;
        } else if (request.getUserPrincipal() != null) {
            var auth = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
            var user = (SecurityUser) auth.getPrincipal();
            var attrib2faAuth = request.getSession().getAttribute(SESSION_ATTRIB_2FA_AUTHENTICATED);
            return !user.is2faEnabled() || (attrib2faAuth != null && (boolean) attrib2faAuth);
        }
        return true;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(totpAuthUrl) && request.getMethod().equals(HttpMethod.POST.name())) {
            var user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var totp = request.getParameter(PARAMETER_NAME_TOTP_FIELD);
            if (totp != null && new GoogleAuthenticator(user.getSecretKey().getBytes(StandardCharsets.UTF_8)).isValid(totp, new Date())) {
                request.getSession().setAttribute(SESSION_ATTRIB_2FA_AUTHENTICATED, true);
                response.sendRedirect(successAuthUrl);
            } else {
                response.sendRedirect(totpAuthUrl + URL_PARAM_FAILED_AUTH);
            }
        } else if (request.getRequestURI().equals(totpAuthUrl) && request.getMethod().equals(HttpMethod.GET.name())) {
            filterChain.doFilter(request, response);
        } else {
            response.sendRedirect(totpAuthUrl);
        }
    }

}
