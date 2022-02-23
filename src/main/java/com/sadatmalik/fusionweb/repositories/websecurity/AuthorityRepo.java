package com.sadatmalik.fusionweb.repositories.websecurity;

import com.sadatmalik.fusionweb.model.websecurity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepo extends JpaRepository<Authority, Long> {
}
