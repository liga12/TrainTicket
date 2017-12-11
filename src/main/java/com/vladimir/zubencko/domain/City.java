package com.vladimir.zubencko.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "city")
@Data
@NoArgsConstructor
@Log4j
public class City implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false,unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "city", orphanRemoval = true)
    private List<NeighborCity> cities;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "city",  orphanRemoval = true)
    private List<NeighborCity> neighborCities;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "city", cascade = CascadeType.ALL)
    private List<TrainWay> trainWays;



    public City(String name) {
        this.name = name.toLowerCase();
    }
}
