package com.salesianostriana.chefplanner.menuitem.repository;

import com.salesianostriana.chefplanner.menuitem.model.MealType;
import com.salesianostriana.chefplanner.menuitem.model.MenuItem;
import com.salesianostriana.chefplanner.user.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de entidades {@link MenuItem}.
 *
 * <p>Proporciona operaciones CRUD estándar y consultas personalizadas
 * para acceder y manipular elementos de menú en la base de datos.
 *
 * <p>Operaciones disponibles:
 * <ul>
 *   <li>Búsqueda de MenuItem por fecha y tipo de comida</li>
 *   <li>Búsqueda de rango de fechas</li>
 *   <li>Búsqueda de MenuItem asociados a una receta</li>
 *   <li>Eliminación de MenuItem asociados a un perfil de usuario</li>
 * </ul>
 *
 * @author ChefPlanner Team
 * @version 1.0
 * @see MenuItem
 * @see JpaRepository
 */
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {


    boolean existsByDateAndMealType(LocalDate date, MealType mealType);


    List<MenuItem> findByDateBetweenOrderByDateAscMealTypeAsc(LocalDate startDate, LocalDate endDate);


    Optional<MenuItem> findByDateAndMealType(LocalDate date, MealType mealType);


    MenuItem findByRecipe_Id(Long id);


    List<MenuItem> findMenuItemByRecipe_Id(Long id);


    @Modifying
    @Transactional
    @Query("DELETE FROM MenuItem m WHERE m.profile = :profile")
    void deleteAllByProfile(UserProfile profile);
}
