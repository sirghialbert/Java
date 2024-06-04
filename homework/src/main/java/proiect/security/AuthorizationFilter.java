package proiect.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import proiect.exception.UnauthorizedException;

import java.io.IOException;

@AllArgsConstructor
@Component
@Slf4j
@Order(1)
public class AuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String path = request.getServletPath();

        try {
            String token = resolveToken(request.getHeader("Authorization"));

            if (request.getHeader("Authorization") != null && jwtProvider.validate(token)) {
                Authentication authentication = jwtProvider.doAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (UnauthorizedException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid jwt token in filter");
        }
    }

    public String resolveToken(String authorizationHeader) {
        log.info(authorizationHeader);

        if (authorizationHeader == null || authorizationHeader.isBlank() || !authorizationHeader.contains("Bearer")) {
            return "";
        }
        return authorizationHeader.substring(7);
    }
}
