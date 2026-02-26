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

    /**
     * Verifica si existe un MenuItem con la fecha y tipo de comida especificados.
     *
     * <p>Método booleano que retorna true si existe un MenuItem exacto
     * con esa combinación de fecha y tipo de comida.
     *
     * @param date la fecha a buscar
     * @param mealType el tipo de comida a buscar
     * @return true si existe un MenuItem con esa combinación, false en caso contrario
     *
     * <p>Ejemplo:</p>
     * <pre>{@code
     * boolean exists = menuItemRepository.existsByDateAndMealType(
     *     LocalDate.of(2026, 2, 25),
     *     MealType.LUNCH
     * );
     * }</pre>
     */
    boolean existsByDateAndMealType(LocalDate date, MealType mealType);

    /**
     * Busca todos los MenuItem dentro de un rango de fechas.
     *
     * <p>Retorna una lista de MenuItem ordenados por fecha (ascendente)
     * y luego por tipo de comida (ascendente). Útil para obtener
     * el plan de menú de una semana o período específico.
     *
     * @param startDate la fecha inicial del rango (inclusive)
     * @param endDate la fecha final del rango (inclusive)
     * @return Lista de MenuItem dentro del rango ordenados por fecha y tipo de comida,
     *         o lista vacía si no hay resultados
     *
     * <p>Ejemplo:</p>
     * <pre>{@code
     * LocalDate start = LocalDate.of(2026, 2, 23);
     * LocalDate end = LocalDate.of(2026, 2, 27);
     * List<MenuItem> weeklyMenu = menuItemRepository.findByDateBetweenOrderByDateAscMealTypeAsc(
     *     start, end
     * );
     * }</pre>
     */
    List<MenuItem> findByDateBetweenOrderByDateAscMealTypeAsc(LocalDate startDate, LocalDate endDate);

    /**
     * Busca un MenuItem específico por su fecha y tipo de comida.
     *
     * <p>Retorna un Optional que contiene el MenuItem si existe,
     * o vacío si no se encuentra. Esta combinación de fecha y tipo de comida
     * debería ser única por usuario.
     *
     * @param date la fecha del MenuItem a buscar
     * @param mealType el tipo de comida del MenuItem a buscar
     * @return Optional con el MenuItem si existe, Optional vacío en caso contrario
     *
     * <p>Ejemplo:</p>
     * <pre>{@code
     * Optional<MenuItem> lunch = menuItemRepository.findByDateAndMealType(
     *     LocalDate.of(2026, 2, 25),
     *     MealType.LUNCH
     * );
     * if (lunch.isPresent()) {
     *     Recipe recipe = lunch.get().getRecipe();
     * }
     * }</pre>
     */
    Optional<MenuItem> findByDateAndMealType(LocalDate date, MealType mealType);

    /**
     * Busca un MenuItem asociado a una receta específica por su ID.
     *
     * <p>Retorna el primer MenuItem encontrado que contiene la receta
     * con el ID especificado. Si existen múltiples MenuItem con esa receta,
     * solo retorna uno (no especificado cuál).
     *
     * <p><strong>Nota:</strong> Para obtener todos los MenuItem asociados
     * a una receta, usa {@link #findMenuItemByRecipe_Id(Long)}.
     *
     * @param id el ID de la receta
     * @return un MenuItem que contiene la receta, o null si no se encuentra
     *
     * <p>Ejemplo:</p>
     * <pre>{@code
     * MenuItem menuItem = menuItemRepository.findByRecipe_Id(42L);
     * }</pre>
     *
     * @see #findMenuItemByRecipe_Id(Long)
     */
    MenuItem findByRecipe_Id(Long id);

    /**
     * Busca todos los MenuItem asociados a una receta específica.
     *
     * <p>Retorna una lista de todos los MenuItem que contienen la receta
     * con el ID especificado. Útil para ver en qué fechas/comidas se ha
     * planificado una receta específica.
     *
     * @param id el ID de la receta
     * @return Lista de MenuItem que contienen esa receta,
     *         o lista vacía si no hay coincidencias
     *
     * <p>Ejemplo:</p>
     * <pre>{@code
     * List<MenuItem> plannedRecipes = menuItemRepository.findMenuItemByRecipe_Id(42L);
     * for (MenuItem item : plannedRecipes) {
     *     System.out.println("Receta planificada para: " + item.getDate() + " (" + item.getMealType() + ")");
     * }
     * }</pre>
     *
     * @see #findByRecipe_Id(Long)
     */
    List<MenuItem> findMenuItemByRecipe_Id(Long id);

    /**
     * Elimina todos los MenuItem asociados a un perfil de usuario específico.
     *
     * <p>Operación destructiva que elimina todos los elementos de menú
     * pertenecientes a un usuario. Típicamente utilizada cuando se elimina
     * una cuenta de usuario o se limpian sus datos.
     *
     * <p><strong>Precaución:</strong> Esta operación es irreversible.
     *
     * @param profile el perfil de usuario cuyos MenuItem se eliminarán
     *
     * <p>Ejemplo:</p>
     * <pre>{@code
     * @Transactional
     * public void deleteUserMenuItems(UserProfile profile) {
     *     menuItemRepository.deleteAllByProfile(profile);
     * }
     * }</pre>
     *
     * @see Modifying
     * @see Transactional
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM MenuItem m WHERE m.profile = :profile")
    void deleteAllByProfile(UserProfile profile);
}
