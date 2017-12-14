package com.vladimir.zubencko.domain;

import lombok.*;
import lombok.extern.log4j.Log4j;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "station_neighbor")
@Data
@NoArgsConstructor
@Log4j
public class NeighborStation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @JoinColumn(name = "id_city", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Station station;

    @JoinColumn(name = "id_neighbor", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Station neighborStation;

    public NeighborStation(Station station, Station neighborStation) {
        this.station = station;
        this.neighborStation = neighborStation;
    }
}
