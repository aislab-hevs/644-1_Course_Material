package ch.hevs.aislab.demo.database.pojo;

import java.util.List;

import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.entity.ClientEntity;

public class ClientWithAccounts {
    public ClientEntity client;

    public List<AccountEntity> accounts;
}
