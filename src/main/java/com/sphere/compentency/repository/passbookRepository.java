package com.sphere.compentency.repository;


import com.sphere.compentency.model.Passbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface passbookRepository extends JpaRepository<Passbook, Long> {

    @Query(value = "SELECT * FROM passbook where request->>'typeName' = ?1 ",nativeQuery = true)
    public List<Passbook> searchByTypeName(String typeName);

    @Query(value = "SELECT * FROM passbook where request->>'userId' IN (?1) and request->>'typeName' = ?2 ",nativeQuery = true)
    public List<Passbook> searchByTypeNameUserId(String typeName,List<String> user_ids);

}

