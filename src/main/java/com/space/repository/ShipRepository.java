package com.space.repository;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {
    @Query("SELECT ship FROM Ship ship " +
            "WHERE (:name IS NULL OR :name = '' OR LOWER (ship.name) LIKE LOWER (%:name%))" +
            "AND (:planet IS NULL OR :planet = '' OR LOWER (ship.planet) LIKE LOWER(%:planet%)) " +
            "AND (:shipType IS NULL OR ship.shipType = :shipType) " +
            "AND (:isUsed IS NULL OR ship.isUsed = :isUsed) " +
            "AND (:after IS NULL OR :after = 0 OR ship.prodDate >= :after) " +
            "AND (:before IS NULL OR :before = 0 OR ship.prodDate <= :before) " +
            "AND (:minSpeed IS NULL OR :minSpeed = 0 OR ship.speed >= :minSpeed) " +
            "AND (:maxSpeed IS NULL OR :maxSpeed = 0 OR ship.speed <= :maxSpeed) " +
            "AND (:minCrewSize IS NULL OR :minLCrewSize= 0 OR ship.crewSize >= :minCrewSize) " +
            "AND (:maxCrewSize IS NULL OR :maxCrewSize = 0 OR ship.crewSize <= :maxCrewSize)" +
            "AND (:minRating IS NULL OR :minRating = 0 OR ship.rating >= :minRating) " +
            "AND (:minRating IS NULL OR :minRating = 0 OR ship.rating >= :minRating) "
    )
    // https://github.com/Bazuzu89/testtask/blob/4142f13061abdd9ca8d159ee534ac7b2905f36c5/src/main/java/com/game/repository/PlayerRepository.java#L31
    Page<Ship> findParameters(@Param("name") String name,
                           @Param("planet") String planet,
                           @Param("shipType")ShipType shipType,
                           @Param("after") Long after,
                           @Param("before") Long before,
                           @Param("isUsed") Boolean isUsed,
                           @Param("minSpeed") Double minSpeed,
                           @Param("maxSpeed") Double maxSpeed,
                           @Param("minCrewSize") Integer minCrewSize,
                           @Param("maxCrewSize") Integer maxCrewSize,
                           @Param("minRating") Double minRating,
                           @Param("maxRating") Double maxRating,
                           Pageable pageable);
}
