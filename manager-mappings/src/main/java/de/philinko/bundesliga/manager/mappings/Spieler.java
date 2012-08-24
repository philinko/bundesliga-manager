package de.philinko.bundesliga.manager.mappings;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

/**
 *
 * @author philippe
 */
@Entity
public class Spieler implements Serializable {
    @Id
    private String name;
    @Enumerated(EnumType.ORDINAL)
    private Position position;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
