package com.salesianostriana.chefplanner.menuitem.controller;


import com.salesianostriana.chefplanner.menuitem.dto.MenuItemRequest;
import com.salesianostriana.chefplanner.menuitem.dto.MenuItemResponse;
import com.salesianostriana.chefplanner.menuitem.model.MealType;
import com.salesianostriana.chefplanner.menuitem.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Menu", description = "Planificación del menú semanal (crear, listar, borrar)")
@PreAuthorize("hasRole('USER')")
public class MenuItemController {

    private final MenuItemService menuItemService;

    @Operation(summary = "Planificar una comida (crear MenuItem)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "MenuItem creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MenuItemResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id": 1,
                                                "date": "2024-09-23",
                                                "mealType": "LUNCH",
                                                "receta": {
                                                    "id": 1,
                                                    "title": "Tortilla Española",
                                                    "minutes": 20
                                                }
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(responseCode = "400", description = "Datos inválidos o ya existe planificación para ese día/tipo",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "chefplanner.com/error/solicitud-invalida",
                                                "title": "Solicitud inválida",
                                                "status": 400,
                                                "detail": "Ya existe una planificación para el 2024-09-23 a las 12:00"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(responseCode = "404", description = "Receta no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "chefplanner.com/error",
                                                "title": "Entidad no encontrada",
                                                "status": 404,
                                                "detail": "No se encontró la receta con ID: 999"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/menuitem")
    public ResponseEntity<MenuItemResponse> create(@Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(MenuItemResponse.of(menuItemService.create(request)));
    }

    @Operation(summary = "Obtener planificación entre dos fechas",
            description = "Devuelve una lista de MenuItem planificados entre las fechas indicadas, ordenados por fecha y tipo de comida.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de MenuItem entre fechas obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MenuItemResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "id": 1,
                                                    "date": "2024-09-23",
                                                    "mealType": "LUNCH",
                                                    "receta": {
                                                        "id": 1,
                                                        "title": "Tortilla Española",
                                                        "minutes": 20
                                                    }
                                                },
                                                {
                                                    "id": 2,
                                                    "date": "2024-09-24",
                                                    "mealType": "DINNER",
                                                    "receta": {
                                                        "id": 2,
                                                        "title": "Ensalada César",
                                                        "minutes": 10
                                                    }
                                                }
                                            ]
                                            """
                                )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Rango de fechas no válido",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "chefplanner.com/error/solicitud-invalida",
                                                "title": "Solicitud inválida",
                                                "status": 400,
                                                "detail": "La fecha de inicio debe ser anterior a la fecha de fin."
                                            }
                                            """
                            )

                    )
            )
    })
    @GetMapping("/menuitem")
    public ResponseEntity<List<MenuItemResponse>> getByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {


        return ResponseEntity.ok(
                menuItemService.getByRange(startDate,endDate).stream()
                        .map(MenuItemResponse::of)
                        .toList());
    }

    @Operation(summary = "Eliminar planificación de un día y tipo de comida")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "MenuItem eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de borrado no válidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "chefplanner.com/error/solicitud-invalida",
                                                "title": "Solicitud inválida",
                                                "status": 400,
                                                "detail": "Los parámetros de fecha y tipo de comida son requeridos"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Planificación no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "chefplanner.com/error",
                                                "title": "Entidad no encontrada",
                                                "status": 404,
                                                "detail": "No se encontró planificación para el 2024-09-23 a las 12:00"
                                            }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping("/menuitem")
    public ResponseEntity<Void> delete(
            @RequestParam LocalDate date,
            @RequestParam MealType mealType) {

        menuItemService.delete(date, mealType);
        return ResponseEntity.noContent().build();
    }
}