package com.sadatmalik.fusionweb.model.websecurity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sadatmalik.fusionweb.model.User;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.List;

/**
 * This is the main user principal class. It provides an implementation of
 * the UserDetails interface methods, which are designed for use with the
 * Spring Security context.
 *
 * The UserDetails methods required are essentially:
 *
 * {@code getAuthorities()} returns a List of Authority instances held by
 * this User.
 *
 * {@code getPassword()} Returns the password used to authenticate the user
 * - note: the web security implementation uses password encryption.
 *
 * {@code getUsername()} Returns the username used to authenticate the user.
 *
 * {@code isAccountNonExpired()} Indicates whether the user's account has
 * expired. An expired account cannot be authenticated. Returns true if the
 * user's account is valid (ie non-expired), false if no longer valid (i.e.
 * expired).
 *
 * {@code isAccountNonLocked()} Indicates whether the user is locked or
 * unlocked. A locked user cannot be authenticated. Returns true if the user
 * is not locked, false otherwise.
 *
 * {@code isCredentialsNonExpired()} Indicates whether the user's credentials
 * (password) have expired. Expired credentials prevent authentication. Returns
 * true if the user's credentials are valid (ie non-expired), false if no
 * longer valid (i.e. expired).
 *
 * {@code isEnabled()} Indicates whether the user is enabled or disabled. A
 * disabled user cannot be authenticated. Returns true if the user is enabled,
 * false otherwise.
 *
 * @author sadatmalik
 */
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

    //IMPORTANT: Even though the password will be encrypted, always use
    //@JsonIgnore on this field to prevent it from being exposed to the
    //outside world.
    @JsonIgnore
    @Column(nullable = false)
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

    /**
     * Custom constructor - ensures validity and expiry parameters are all
     * set to true - i.e. active - for a newly minted UserPrincipal.
     *
     * @param username username
     * @param password password
     * @param authorities list of authority's
     */
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