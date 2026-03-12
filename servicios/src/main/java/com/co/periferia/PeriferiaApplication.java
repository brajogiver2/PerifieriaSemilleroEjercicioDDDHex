package com.co.periferia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  SkeletonApplication.java                                   ║
 * ║  Punto de entrada. No tiene lógica de negocio.              ║
 * ║                                                              ║
 * ║  @EnableCaching   → activa el caché con Redis               ║
 * ║  @EnableScheduling → activa las tareas programadas (Sonda)  ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class PeriferiaApplication {
    public static void main(String[] args) {
        SpringApplication.run(PeriferiaApplication.class, args);
    }
}
