package com.Firmanann.CoreBankingSystem.global.jwt.filter;

import com.Firmanann.CoreBankingSystem.global.jwt.service.JwtService;
import com.Firmanann.CoreBankingSystem.global.jwt.userDetails.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Checker Token
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        //1. Pengecekan endpoint Jika tidak terdapat header "Bearer", lempar ke securityFilterChain
        //Jika terdapat header "Bearer", lanjut ke next steps
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            //Lempar ke securityFilterChain
            filterChain.doFilter(request, response);
            return;
        }

        //Proses jika terdapat header "Bearer"
        // 2. Extract Tokennya
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        // 3. Kalau ada email di token dan user belum tersimpan di context
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            //Buat objek/user berdasarkan email dari token
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            //Cek Keaslian Token
            if (jwtService.isTokenValid(jwt, userDetails)) {

                //berikan authorization kepada user
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                //Lengkapi data user dengan IP, Session ID/browser
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //Simpan data akhir userDetails ke Security Context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //Jika Proses 3 gagal maka lempar ke securityFilterChain
        filterChain.doFilter(request, response);
    }
}