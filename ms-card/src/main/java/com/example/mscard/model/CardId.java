package com.example.mscard.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class CardId implements Serializable {

    private String maskedPan;
    private LocalDate expirationDate;
    private String cvv;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardId cardId = (CardId) o;
        return maskedPan.equals(cardId.maskedPan)
                && expirationDate.isEqual(cardId.expirationDate)
                && cvv.equals(cardId.cvv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maskedPan, expirationDate, cvv);
    }
}
