package com.vladimir.zubencko.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "station")
@Data
@NoArgsConstructor
@Log4j
public class Station implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false,unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "station", orphanRemoval = true)
    private List<NeighborStation> stations;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "station")
    private List<NeighborStation> neighborStations;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "station")
    private List<TrainWay> trainWays;



    public Station(String name) {
        this.name = name.toLowerCase();
    }
}
