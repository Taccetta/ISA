package com.ar.edu.um.taccetta.cars.repository;

import com.ar.edu.um.taccetta.cars.domain.PurchasedCar;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PurchasedCar entity.
 */
@Repository
public interface PurchasedCarRepository extends JpaRepository<PurchasedCar, Long>, JpaSpecificationExecutor<PurchasedCar> {
    default Optional<PurchasedCar> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PurchasedCar> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PurchasedCar> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct purchasedCar from PurchasedCar purchasedCar left join fetch purchasedCar.car left join fetch purchasedCar.client",
        countQuery = "select count(distinct purchasedCar) from PurchasedCar purchasedCar"
    )
    Page<PurchasedCar> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct purchasedCar from PurchasedCar purchasedCar left join fetch purchasedCar.car left join fetch purchasedCar.client"
    )
    List<PurchasedCar> findAllWithToOneRelationships();

    @Query(
        "select purchasedCar from PurchasedCar purchasedCar left join fetch purchasedCar.car left join fetch purchasedCar.client where purchasedCar.id =:id"
    )
    Optional<PurchasedCar> findOneWithToOneRelationships(@Param("id") Long id);
}
