package com.vladimir.zubencko.domain;

import lombok.*;
import lombok.extern.log4j.Log4j;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "neighbor_city")
@Data
@NoArgsConstructor
@Log4j
public class NeighborCity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @JoinColumn(name = "id_city", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private City city;

    @JoinColumn(name = "id_neighbor", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private City neighborCity;

    public NeighborCity(City city, City neighborCity) {
        this.city = city;
        this.neighborCity = neighborCity;
    }
}
