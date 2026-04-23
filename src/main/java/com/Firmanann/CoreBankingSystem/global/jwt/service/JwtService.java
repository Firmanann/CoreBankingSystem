package com.Firmanann.CoreBankingSystem.global.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long secretExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    // Mengecek apakah tiket ini valid (Milik user yang benar DAN belum hangus)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Fungsi khusus untuk mengambil nama user (Subject) dari dalam tiket
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Mengecek apakah tiket sudah melewati batas waktu (hangus)
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Mengambil info tanggal/waktu kapan tiket ini hangus
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Fungsi serbaguna untuk mengambil data spesifik (Claim) apapun dari dalam tiket
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Mesin pembongkar tiket: Membaca seluruh isi tiket sekaligus mengecek keaslian "stempel"-nya
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()) // Pasang kunci rahasia untuk verifikasi stempel
                .build()
                .parseClaimsJws(token) // Proses bongkar dan verifikasi (akan error kalau tiket dipalsukan)
                .getBody(); // Ambil isi datanya (Claims)
    }

    // Mengubah String kunci rahasia (Base64) menjadi object kriptografi & Sebaliknya (Filter & Generate)
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //1. Perakit JWT
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims) // Isi data tambahan (jika ada)
                .setSubject(userDetails.getUsername()) //Set Username/email
                .setIssuedAt(new Date(System.currentTimeMillis())) //Set Waktu Created
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) //Set Expiration time
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) //Set Signature menggunakan Secret Key
                .compact(); //Bungkus jadi satu string (header.payload.signature)
    }

    //2. Generate Access Token
    public String generateToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, secretExpiration);
    }

    //3. Generate Refresh Token
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }
}