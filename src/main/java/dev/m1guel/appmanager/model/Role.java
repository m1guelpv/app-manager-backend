package dev.m1guel.appmanager.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @ElementCollection
    @Column(columnDefinition = "TEXT[]")
    private List<String> permissions;

    @Override
    public String getAuthority() {
        return name;
    }

}