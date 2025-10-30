package com.example.zellervandecastellpatientenverwaltung.dtos;

import java.time.LocalDateTime;

public class BehandlungDtos {

    public record Minimal(String diagnose, LocalDateTime beginn, LocalDateTime ende) {}

}
