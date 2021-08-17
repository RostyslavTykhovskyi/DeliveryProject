package com.mycompany.delivery.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "role", schema = "public")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq_gen")
    @SequenceGenerator(name = "role_seq_gen", sequenceName = "role_id_seq")
    @Column(name="role_id", nullable = false)
    private long id;

    @Column(name="name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "authorities")
    private Set<User> users;

    @Override
    public String getAuthority() {
        return getName();
    }
}
