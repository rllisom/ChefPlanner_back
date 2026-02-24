package com.salesianostriana.chefplanner.menuitem.repository;

import com.salesianostriana.chefplanner.menuitem.model.MealType;
import com.salesianostriana.chefplanner.menuitem.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    boolean existsByDateAndMealType(LocalDate date, MealType mealType);

    List<MenuItem> findByDateBetweenOrderByDateAscMealTypeAsc(LocalDate startDate, LocalDate endDate);

    Optional<MenuItem> findByDateAndMealType(LocalDate date, MealType mealType);

    MenuItem findByRecipe_Id(Long id);

    List<MenuItem> findMenuItemByRecipe_Id(Long id);

}
