package com.example.zellervandecastellpatientenverwaltung.assertions;

import com.example.zellervandecastellpatientenverwaltung.domain.Adresse;
import org.assertj.core.api.AbstractAssert;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

public class AdresseAssert extends AbstractAssert<AdresseAssert, Adresse> {

    public AdresseAssert(Adresse actual) {
        super(actual, AdresseAssert.class);
    }

    public static AdresseAssert assertThat(Adresse actual) {
        return new AdresseAssert(actual);
    }

    public AdresseAssert hasStrasse(String strasse) {
        isNotNull();
        if (!actual.getStrasse().equals(strasse)) {
            failWithMessage("Expected Adresses strasse to be <%s> but was <%s>", strasse, actual.getStrasse());
        }
        return this;
    }

    public AdresseAssert hasHausNr(String hausNr) {
        isNotNull();
        if (!actual.getHausNr().equals(hausNr)) {
            failWithMessage("Expected Adresses hausNr to be <%s> but was <%s>", hausNr, actual.getHausNr());
        }
        return this;
    }

    public AdresseAssert hasPlz(String plz) {
        isNotNull();
        if (!actual.getPlz().equals(plz)) {
            failWithMessage("Expected Adresses plz to be <%s> but was <%s>", plz, actual.getPlz());
        }
        return this;
    }

    public AdresseAssert hasStadt(String stadt) {
        isNotNull();
        if (!actual.getStadt().equals(stadt)) {
            failWithMessage("Expected Adresses stadt to be <%s> but was <%s>", stadt, actual.getStadt());
        }
        return this;
    }

    public AdresseAssert hasValidAdress(String strasse, String hausNr, String plz, String stadt) {
        hasStrasse(strasse);
        hasHausNr(hausNr);
        hasPlz(plz);
        hasStadt(stadt);
        return this;
    }
}
