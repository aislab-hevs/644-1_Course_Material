package ch.hevs.aislab.demo.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.annotation.NonNull;

/**
 * https://developer.android.com/reference/android/arch/persistence/room/Entity.html
 *
 * Further information to Parcelable:
 * https://developer.android.com/reference/android/os/Parcelable
 * Why we use Parcelable over Serializable:
 * https://android.jlelse.eu/parcelable-vs-serializable-6a2556d51538
 */
@Entity(tableName = "clients", primaryKeys = {"email"})
public class ClientEntity implements Comparable {

    @NonNull
    private String email;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    private String password;

    @Ignore
    public ClientEntity() {
    }

    public ClientEntity(@NonNull String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ClientEntity)) return false;
        ClientEntity o = (ClientEntity) obj;
        return o.getEmail().equals(this.getEmail());
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }
}
