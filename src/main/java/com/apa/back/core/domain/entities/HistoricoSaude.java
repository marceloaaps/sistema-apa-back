package com.apa.back.core.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "historico_saude")
public class HistoricoSaude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_animal", nullable = false)
    private Animal animal;

    @Column(name = "tipo_evento", nullable = false, length = 50)
    private String tipoEvento;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "castrado")
    private Boolean castrado = false;

    @Column(name = "data_evento")
    private LocalDateTime dataEvento;

    public HistoricoSaude(Animal animal, String tipoEvento, String descricao, Boolean castrado) {
        this.animal = animal;
        this.tipoEvento = tipoEvento;
        this.descricao = descricao;
        this.castrado = castrado != null ? castrado : false;
        this.dataEvento = LocalDateTime.now();
    }
}

