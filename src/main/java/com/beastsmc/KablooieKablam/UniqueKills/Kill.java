package com.beastsmc.KablooieKablam.UniqueKills;

import com.avaje.ebean.validation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity()
@Table(name="playerkills",
        uniqueConstraints=
            @UniqueConstraint(columnNames={"killerUUID", "killedUUID"})
)
public class Kill {
    @Id
    private int id;

    @NotNull
    private String killerUUID;

    @NotNull
    private String victimUUID;

    public String getKillerUUID() {
        return killerUUID;
    }

    public void setKillerUUID(String killerUUID) {
        this.killerUUID = killerUUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVictimUUID() {
        return victimUUID;
    }

    public void setVictimUUID(String victimUUID) {
        this.victimUUID = victimUUID;
    }
}
