package com.example.zellervandecastellpatientenverwaltung.commands;

import java.time.LocalDateTime;

public class BehandlungCommands {
    public record CreateBehandlungCommand(
            Long arztId,
            Long patientId,
            LocalDateTime beginn,
            LocalDateTime ende,
            String diagnose
    ) {}

    public record UpdateBehandlungCommand(
            LocalDateTime beginn,
            LocalDateTime ende,
            String diagnose,
            Long arztId,
            Long patientId
    ) {}
}
