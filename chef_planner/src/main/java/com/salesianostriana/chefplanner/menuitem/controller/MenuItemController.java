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
            @ApiResponse(responseCode = "201", description = "MenuItem creado",
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
                                                    ...
                                                }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o ya existe planificación para ese día/tipo",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                            ,examples = @ExampleObject(
                                    value = """
                                             {
                                                "detail": "El campo 'name' es obligatorio y no puede estar vacío",
                                                "instance": "/api/v1/ingredient/add",
                                                "status": 400,
                                                "title": "Solicitud inválida",
                                                "type": "chefplanner.com/error/solicitud-invalida"
                                            }
                                            """
                    )
                )
            ),
            @ApiResponse(responseCode = "404", description = "Receta no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/menuitem")
    public ResponseEntity<MenuItemResponse> create(@Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(MenuItemResponse.of(menuItemService.create(request)));
    }

    @Operation(summary = "Obtener planificación entre dos fechas",
            description = "Devuelve una lista de MenuItem planificados entre las fechas indicadas, ordenados por fecha y tipo de comida.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de MenuItem entre fechas",
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
                                                        ...
                                                    }
                                                },
                                                {
                                                    "id": 2,
                                                    "date": "2024-09-24",
                                                    "mealType": "DINNER",
                                                    "receta": {
                                                        "id": 2,
                                                        "title": "Ensalada César",
                                                        ...
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
                                              "type": "https://example.com/invaliddaterange",
                                              "title": "Rango de fechas no válido",
                                              "status": 400,
                                              "detail": "La fecha de inicio debe ser anterior a la fecha de fin."
                                            }
                                            """
                            )

                    )
            ),
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
    @DeleteMapping("/menuitem")
    @Operation(summary = "Eliminar planificación de un día y tipo de comida")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "MenuItem eliminado"),
            @ApiResponse(responseCode = "400", description = "Datos de borrado no válidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Planificación no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<Void> delete(
            @RequestParam LocalDate date,
            @RequestParam MealType mealType) {

        menuItemService.delete(date, mealType);
        return ResponseEntity.noContent().build();
    }
}