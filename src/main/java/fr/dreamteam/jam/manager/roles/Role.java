package fr.dreamteam.jam.manager.roles;

import fr.dreamteam.jam.manager.EpiPlayer;

public enum Role {

    HUNTER(Hunter.class),
    VAMPIRE(Vampire.class);

    public final Class<? extends EpiPlayer> clazz;

    Role(Class<? extends EpiPlayer> clazz) {
        this.clazz = clazz;
    }

}
