package com.example.CommunistDate.Users;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
    public Optional<User> findByUsername(String username);

    @Query(value = "SELECT * from users ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    User findRandomUser();

    @Query("SELECT u FROM User u WHERE u.id NOT IN :excludedUserIds AND " +
       "(:minAge IS NULL OR u.age >= :minAge) AND " +
       "(:maxAge IS NULL OR u.age <= :maxAge) AND " +
       "(:politicalBelief IS NULL OR u.politicalBelief = :politicalBelief) AND " +
       "(:gender IS NULL OR u.gender = :gender) AND " +
       "(:partnerShare IS NULL OR u.partnerShare = :partnerShare)")
    Page<User> findRandomUserExcluding(@Param("excludedUserIds") List<Long> excludedUserIds,
                                   @Param("minAge") Integer minAge,
                                   @Param("maxAge") Integer maxAge,
                                   @Param("politicalBelief") String politicalBelief,
                                   @Param("gender") String gender,
                                   @Param("partnerShare") Boolean partnerShare,
                                   Pageable pageable);

    boolean existsByUsername(String username);
}