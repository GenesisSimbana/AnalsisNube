package com.banquito.originacion.analisis.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.banquito.originacion.analisis.controller.dto.ObservacionAnalistasDTO;
import com.banquito.originacion.analisis.controller.mapper.ObservacionAnalistasMapper;
import com.banquito.originacion.analisis.exception.ObservacionAnalistasNotFoundException;
import com.banquito.originacion.analisis.model.ObservacionAnalistas;
import com.banquito.originacion.analisis.service.ObservacionAnalistasService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/observaciones-analistas")
@Tag(name = "Observaciones de Analistas", description = "API para gestionar las observaciones de analistas")
public class ObservacionAnalistasController {

    private static final Logger log = LoggerFactory.getLogger(ObservacionAnalistasController.class);

    private final ObservacionAnalistasService service;
    private final ObservacionAnalistasMapper mapper;

    public ObservacionAnalistasController(ObservacionAnalistasService service, ObservacionAnalistasMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las observaciones de analistas", 
               description = "Retorna una lista paginada de todas las observaciones de analistas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Observaciones encontradas exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros de paginación inválidos")
    })
    public ResponseEntity<Page<ObservacionAnalistasDTO>> getAllObservacionesAnalistas(
            @Parameter(description = "Número de página (0-based)") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página") 
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Campo para ordenar") 
            @RequestParam(defaultValue = "fechaHora") String sortBy,
            @Parameter(description = "Dirección del ordenamiento (asc/desc)") 
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        log.info("Received request to get all ObservacionAnalistas. Page: {}, Size: {}, SortBy: {}, SortDir: {}", page, size, sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ObservacionAnalistas> observaciones = service.findAllPaginated(pageable);
        Page<ObservacionAnalistasDTO> dtoPage = observaciones.map(mapper::toDTO);
        
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener observación por ID", 
               description = "Retorna una observación específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Observación encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Observación no encontrada")
    })
    public ResponseEntity<ObservacionAnalistasDTO> getObservacionAnalistasById(
            @Parameter(description = "ID de la observación") 
            @PathVariable Integer id) {
        
        log.info("Received request to get ObservacionAnalistas by id: {}", id);
        ObservacionAnalistas observacion = service.findById(id);
        return ResponseEntity.ok(mapper.toDTO(observacion));
    }

    @GetMapping("/solicitud/{idSolicitud}")
    @Operation(summary = "Obtener observaciones por ID de solicitud", 
               description = "Retorna todas las observaciones para una solicitud específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Observaciones encontradas exitosamente")
    })
    public ResponseEntity<List<ObservacionAnalistasDTO>> getObservacionesByIdSolicitud(
            @Parameter(description = "ID de la solicitud") 
            @PathVariable Integer idSolicitud) {
        
        log.info("Received request to get ObservacionAnalistas by idSolicitud: {}", idSolicitud);
        List<ObservacionAnalistas> observaciones = service.findByIdSolicitudOrderByFechaHoraDesc(idSolicitud);
        if (observaciones.isEmpty()) {
            throw new ObservacionAnalistasNotFoundException(idSolicitud.toString(), "ID de solicitud");
        }
        List<ObservacionAnalistasDTO> dtos = mapper.toDTOList(observaciones);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/usuario/{usuario}")
    @Operation(summary = "Obtener observaciones por usuario", 
               description = "Retorna todas las observaciones realizadas por un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Observaciones encontradas exitosamente")
    })
    public ResponseEntity<List<ObservacionAnalistasDTO>> getObservacionesByUsuario(
            @Parameter(description = "Usuario que realizó la observación") 
            @PathVariable String usuario) {
        
        log.info("Received request to get ObservacionAnalistas by usuario: {}", usuario);
        List<ObservacionAnalistas> observaciones = service.findByUsuarioOrderByFechaHoraDesc(usuario);
        if (observaciones.isEmpty()) {
            throw new ObservacionAnalistasNotFoundException(usuario, "usuario");
        }
        List<ObservacionAnalistasDTO> dtos = mapper.toDTOList(observaciones);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    @Operation(summary = "Crear nueva observación de analista", 
               description = "Crea una nueva observación de analista")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Observación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<ObservacionAnalistasDTO> createObservacionAnalistas(
            @Parameter(description = "Datos de la observación a crear") 
            @Valid @RequestBody ObservacionAnalistasDTO observacionAnalistasDTO) {
        
        log.info("Received request to create new ObservacionAnalistas.");
        log.debug("Request body: {}", observacionAnalistasDTO);
        ObservacionAnalistas observacion = mapper.toEntity(observacionAnalistasDTO);
        ObservacionAnalistas savedObservacion = service.save(observacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(savedObservacion));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar observación de analista", 
               description = "Actualiza completamente una observación de analista")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Observación actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Observación no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<ObservacionAnalistasDTO> updateObservacionAnalistas(
            @Parameter(description = "ID de la observación") 
            @PathVariable Integer id,
            @Parameter(description = "Datos actualizados de la observación") 
            @Valid @RequestBody ObservacionAnalistasDTO observacionAnalistasDTO) {
        
        log.info("Received request to update ObservacionAnalistas with id: {}", id);
        log.debug("Request body: {}", observacionAnalistasDTO);
        ObservacionAnalistas observacion = mapper.toEntity(observacionAnalistasDTO);
        ObservacionAnalistas updatedObservacion = service.update(id, observacion);
        return ResponseEntity.ok(mapper.toDTO(updatedObservacion));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente observación de analista", 
               description = "Actualiza parcialmente una observación de analista")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Observación actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Observación no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<ObservacionAnalistasDTO> partialUpdateObservacionAnalistas(
            @Parameter(description = "ID de la observación") 
            @PathVariable Integer id,
            @Parameter(description = "Datos parciales de la observación") 
            @RequestBody ObservacionAnalistasDTO observacionAnalistasDTO) {
        
        log.info("Received request to partially update ObservacionAnalistas with id: {}", id);
        log.debug("Request body: {}", observacionAnalistasDTO);
        ObservacionAnalistas observacion = mapper.toEntity(observacionAnalistasDTO);
        ObservacionAnalistas updatedObservacion = service.partialUpdate(id, observacion);
        return ResponseEntity.ok(mapper.toDTO(updatedObservacion));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar observación de analista", 
               description = "Elimina una observación de analista")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Observación eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Observación no encontrada")
    })
    public ResponseEntity<Void> deleteObservacionAnalistas(
            @Parameter(description = "ID de la observación") 
            @PathVariable Integer id) {
        
        log.info("Received request to delete ObservacionAnalistas with id: {}", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 