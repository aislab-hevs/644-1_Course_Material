package ch.hevs.aislab.demo.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * https://developer.android.com/reference/android/arch/persistence/room/Entity.html
 *
 * interesting: owner column references a foreign key, that's why this column is indexed.
 * If not indexed, it might trigger full table scans whenever parent table is modified so you are
 * highly advised to create an index that covers this column.
 *
 * Further information to Parcelable:
 * https://developer.android.com/reference/android/os/Parcelable
 * Why we use Parcelable over Serializable:
 * https://android.jlelse.eu/parcelable-vs-serializable-6a2556d51538
 */
@Entity(tableName = "accounts",
        foreignKeys =
        @ForeignKey(
                entity = ClientEntity.class,
                parentColumns = "email",
                childColumns = "owner",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
        @Index(
                value = {"owner"}
        )}
)
public class AccountEntity {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;
    private Double balance;
    private String owner;

    public AccountEntity() {
    }

    public AccountEntity(String name, Double balance, String owner) {
        this.name = name;
        this.balance = balance;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof AccountEntity)) return false;
        AccountEntity o = (AccountEntity) obj;
        return o.getId().equals(this.getId());
    }

    @Override
    public String toString() {
        return name;
    }
}
