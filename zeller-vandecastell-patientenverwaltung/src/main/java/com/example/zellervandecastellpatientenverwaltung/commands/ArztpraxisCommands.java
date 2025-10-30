package com.example.zellervandecastellpatientenverwaltung.commands;

import com.example.zellervandecastellpatientenverwaltung.domain.Adresse;
import com.example.zellervandecastellpatientenverwaltung.domain.TelefonNummer;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;


public class ArztpraxisCommands {
    public record CreateArztpraxisCommand(
            @NotNull String name,
            Long telefonNummerId,
            Long arztId,
            Long addresseId,
            Boolean istKassenarzt,
            String apiKey
    ) {}

}
