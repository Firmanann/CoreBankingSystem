#Stage 1

# Pakai Maven dengan Amazon Corretto JDK 17 (Ganti angka 17 ke 21 kalau lu pakai Java 21)
FROM maven:3.9-amazoncorretto-21 AS builder
# Bikin folder kerja di dalam container
WORKDIR /app
# Copy pom.xml dulu biar dependency ke-cache (bikin build selanjutnya jauh lebih cepet)
COPY pom.xml .
RUN mvn dependency:go-offline
# Copy semua source code lu
COPY src ./src
# Build project jadi .jar dan skip testing biar proses build ngebut
RUN mvn clean package -DskipTests


#Stage 2

# Pakai versi Alpine yang super ringan (ukurannya cuma belasan MB)
FROM amazoncorretto:21-alpine
# Bikin folder kerja untuk jalannya aplikasi
WORKDIR /app
# Ambil file .jar yang udah mateng dari STAGE 1 (builder)
COPY --from=builder /app/target/*.jar app.jar
# Buka port 8080 biar bisa ditembak Postman
EXPOSE 8080
# Perintah utama untuk nyalain Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]