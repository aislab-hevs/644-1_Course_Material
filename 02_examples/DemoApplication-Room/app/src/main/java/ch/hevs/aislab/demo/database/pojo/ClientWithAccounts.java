package ch.hevs.aislab.demo.database.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.entity.ClientEntity;

/**
 * https://developer.android.com/reference/android/arch/persistence/room/Relation
 */
public class ClientWithAccounts {
    @Embedded
    public ClientEntity client;

    @Relation(parentColumn = "email", entityColumn = "owner", entity = AccountEntity.class)
    public List<AccountEntity> accounts;
}