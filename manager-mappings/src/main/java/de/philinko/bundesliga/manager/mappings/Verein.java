package de.philinko.bundesliga.manager.mappings;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author philippe
 */
@Embeddable
public class Verein implements Serializable {
    private String name;

    public Verein() {}

    public Verein(String vereinsName) {
        this.name = vereinsName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Verein other = (Verein) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
