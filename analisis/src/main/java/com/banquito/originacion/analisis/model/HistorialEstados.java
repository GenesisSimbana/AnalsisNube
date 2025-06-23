package com.banquito.originacion.analisis.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.banquito.originacion.analisis.enums.EstadoHistorialEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "historial_estados", schema = "analisis_creditos")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class HistorialEstados {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Integer idHistorial;
    
    @Column(name = "id_solicitud", nullable = false)
    private Integer idSolicitud;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoHistorialEnum estado;
    
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    
    @Column(name = "usuario", nullable = false, length = 50)
    private String usuario;
    
    @Column(name = "motivo", nullable = false, length = 120)
    private String motivo;
    
    @Column(name = "version", nullable = false, precision = 9, scale = 0)
    private BigDecimal version;
    
    // Constructor solo para la primary key
    public HistorialEstados(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idHistorial == null) ? 0 : idHistorial.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HistorialEstados other = (HistorialEstados) obj;
        if (idHistorial == null) {
            if (other.idHistorial != null)
                return false;
        } else if (!idHistorial.equals(other.idHistorial))
            return false;
        return true;
    }
    
} 