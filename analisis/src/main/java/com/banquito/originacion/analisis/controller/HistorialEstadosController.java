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

import com.banquito.originacion.analisis.controller.dto.HistorialEstadosDTO;
import com.banquito.originacion.analisis.controller.mapper.HistorialEstadosMapper;
import com.banquito.originacion.analisis.exception.HistorialEstadosNotFoundException;
import com.banquito.originacion.analisis.enums.EstadoHistorialEnum;
import com.banquito.originacion.analisis.model.HistorialEstados;
import com.banquito.originacion.analisis.service.HistorialEstadosService;
import com.banquito.originacion.analisis.exception.InvalidTransitionException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/historial-estados")
@Tag(name = "Historial de Estados", description = "API para gestionar el historial de estados de solicitudes")
public class HistorialEstadosController {

    private static final Logger log = LoggerFactory.getLogger(HistorialEstadosController.class);

    private final HistorialEstadosService service;
    private final HistorialEstadosMapper mapper;

    public HistorialEstadosController(HistorialEstadosService service, HistorialEstadosMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los historiales de estados", 
               description = "Retorna una lista paginada de todos los historiales de estados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historiales encontrados exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros de paginación inválidos")
    })
    public ResponseEntity<Page<HistorialEstadosDTO>> getAllHistorialEstados(
            @Parameter(description = "Número de página (0-based)") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página") 
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Campo para ordenar") 
            @RequestParam(defaultValue = "fechaHora") String sortBy,
            @Parameter(description = "Dirección del ordenamiento (asc/desc)") 
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        log.info("Received request to get all HistorialEstados. Page: {}, Size: {}, SortBy: {}, SortDir: {}", page, size, sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<HistorialEstados> historiales = service.findAllPaginated(pageable);
        Page<HistorialEstadosDTO> dtoPage = historiales.map(mapper::toDTO);
        
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener historial por ID", 
               description = "Retorna un historial de estados específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Historial no encontrado")
    })
    public ResponseEntity<HistorialEstadosDTO> getHistorialEstadosById(
            @Parameter(description = "ID del historial") 
            @PathVariable Integer id) {
        
        log.info("Received request to get HistorialEstados by id: {}", id);
        HistorialEstados historial = service.findById(id);
        return ResponseEntity.ok(mapper.toDTO(historial));
    }

    @GetMapping("/solicitud/{idSolicitud}")
    @Operation(summary = "Obtener historiales por ID de solicitud", 
               description = "Retorna todos los historiales de estados para una solicitud específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historiales encontrados exitosamente")
    })
    public ResponseEntity<List<HistorialEstadosDTO>> getHistorialEstadosByIdSolicitud(
            @Parameter(description = "ID de la solicitud") 
            @PathVariable Integer idSolicitud) {
        
        log.info("Received request to get HistorialEstados by idSolicitud: {}", idSolicitud);
        List<HistorialEstados> historiales = service.findByIdSolicitudOrderByFechaHoraDesc(idSolicitud);
        if (historiales.isEmpty()) {
            throw new HistorialEstadosNotFoundException(idSolicitud.toString(), "ID de solicitud");
        }
        List<HistorialEstadosDTO> dtos = mapper.toDTOList(historiales);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener historiales por estado", 
               description = "Retorna todos los historiales de estados con un estado específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historiales encontrados exitosamente")
    })
    public ResponseEntity<List<HistorialEstadosDTO>> getHistorialEstadosByEstado(
            @Parameter(description = "Estado del historial") 
            @PathVariable EstadoHistorialEnum estado) {
        
        log.info("Received request to get HistorialEstados by estado: {}", estado);
        List<HistorialEstados> historiales = service.findByEstado(estado);
        List<HistorialEstadosDTO> dtos = mapper.toDTOList(historiales);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/usuario/{usuario}")
    @Operation(summary = "Obtener historiales por usuario", 
               description = "Retorna todos los historiales de estados realizados por un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historiales encontrados exitosamente")
    })
    public ResponseEntity<List<HistorialEstadosDTO>> getHistorialEstadosByUsuario(
            @Parameter(description = "Usuario que realizó el cambio") 
            @PathVariable String usuario) {
        
        log.info("Received request to get HistorialEstados by usuario: {}", usuario);
        List<HistorialEstados> historiales = service.findByUsuario(usuario);
        if (historiales.isEmpty()) {
            throw new HistorialEstadosNotFoundException(usuario, "usuario");
        }
        List<HistorialEstadosDTO> dtos = mapper.toDTOList(historiales);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo historial de estados", 
               description = "Crea un nuevo registro en el historial de estados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Historial creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<HistorialEstadosDTO> createHistorialEstados(
            @Parameter(description = "Datos del historial a crear") 
            @Valid @RequestBody HistorialEstadosDTO historialEstadosDTO) {
        
        log.info("Received request to create new HistorialEstados.");
        log.debug("Request body: {}", historialEstadosDTO);
        HistorialEstados historial = mapper.toEntity(historialEstadosDTO);
        HistorialEstados savedHistorial = service.save(historial);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(savedHistorial));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar historial de estados", 
               description = "Actualiza completamente un registro del historial de estados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Historial no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<HistorialEstadosDTO> updateHistorialEstados(
            @Parameter(description = "ID del historial") 
            @PathVariable Integer id,
            @Parameter(description = "Datos actualizados del historial") 
            @Valid @RequestBody HistorialEstadosDTO historialEstadosDTO) {
        
        log.info("Received request to update HistorialEstados with id: {}", id);
        log.debug("Request body: {}", historialEstadosDTO);
        HistorialEstados historial = mapper.toEntity(historialEstadosDTO);
        HistorialEstados updatedHistorial = service.update(id, historial);
        return ResponseEntity.ok(mapper.toDTO(updatedHistorial));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente historial de estados", 
               description = "Actualiza parcialmente un registro del historial de estados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Historial no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<HistorialEstadosDTO> partialUpdateHistorialEstados(
            @Parameter(description = "ID del historial") 
            @PathVariable Integer id,
            @Parameter(description = "Datos parciales del historial") 
            @RequestBody HistorialEstadosDTO historialEstadosDTO) {
        
        log.info("Received request to partially update HistorialEstados with id: {}", id);
        log.debug("Request body: {}", historialEstadosDTO);
        HistorialEstados historial = mapper.toEntity(historialEstadosDTO);
        HistorialEstados updatedHistorial = service.partialUpdate(id, historial);
        return ResponseEntity.ok(mapper.toDTO(updatedHistorial));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar historial de estados", 
               description = "Elimina un registro del historial de estados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Historial eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Historial no encontrado")
    })
    public ResponseEntity<Void> deleteHistorialEstados(
            @Parameter(description = "ID del historial") 
            @PathVariable Integer id) {
        
        log.info("Received request to delete HistorialEstados with id: {}", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 