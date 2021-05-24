package ch.hevs.aislab.intro.util;

import ch.hevs.aislab.intro.database.entity.ClientEntity;

public interface RecyclerViewItemClickListener {
    void onClick(ClientEntity client);
    void onLongClick(ClientEntity client);
}
