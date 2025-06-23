package com.banquito.originacion.analisis.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "observacion_analistas", schema = "analisis_creditos")
@Getter
@Setter
@NoArgsConstructor
@ToString

public class ObservacionAnalistas {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_observacion_analista")
    private Integer idObservacionAnalista;
    
    @Column(name = "id_solicitud", nullable = false)
    private Integer idSolicitud;
    
    @Column(name = "usuario", nullable = false, length = 50)
    private String usuario;
    
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    
    @Column(name = "razon_intervencion", nullable = false, length = 500)
    private String razonIntervencion;
    
    @Column(name = "version", nullable = false, precision = 9, scale = 0)
    private BigDecimal version;
    
    public ObservacionAnalistas(Integer idObservacionAnalista) {
        this.idObservacionAnalista = idObservacionAnalista;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idObservacionAnalista == null) ? 0 : idObservacionAnalista.hashCode());
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
        ObservacionAnalistas other = (ObservacionAnalistas) obj;
        if (idObservacionAnalista == null) {
            if (other.idObservacionAnalista != null)
                return false;
        } else if (!idObservacionAnalista.equals(other.idObservacionAnalista))
            return false;
        return true;
    }

} 