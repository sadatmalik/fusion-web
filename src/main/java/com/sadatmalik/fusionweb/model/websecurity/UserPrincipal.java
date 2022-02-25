package com.sadatmalik.fusionweb.model.websecurity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sadatmalik.fusionweb.model.User;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "principals")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    //IMPORTANT: Even though the password will be encrypted, always use
    //@JsonIgnore on this field to prevent it from being exposed to the
    //outside world.
    @JsonIgnore
    private String password;

    //Eager fetch - want to load the users authorities
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "principal_authority_join_table",
            joinColumns = @JoinColumn(name = "principal_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private List<Authority> authorities;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public UserPrincipal(String username, String password, List<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        accountNonExpired = true;
        accountNonLocked = true;
        credentialsNonExpired = true;
        enabled = true;
    }
}