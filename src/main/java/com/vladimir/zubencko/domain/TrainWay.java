package com.vladimir.zubencko.domain;

import com.vladimir.zubencko.ConverterTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;

@Entity
@Table(name = "train_way")
@Data
@NoArgsConstructor
@Log4j
public class TrainWay implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "departure", nullable = false)
    @Convert(converter = ConverterTime.class)
    private LocalTime trainDeparture;

    @Column(name = "stopping_time", nullable = false)
    private LocalTime trainStoppingTime;

    @Column(name = "cost")
    private int cost;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id", nullable = false)
    private City city;

    @ManyToOne
    @JoinColumn(name = "id_train", referencedColumnName = "id", nullable = false)
    private Train train;

    public TrainWay(Train train, City city, LocalTime trainDeparture, LocalTime trainStoppingTime, int cost ) {
        this.trainDeparture = trainDeparture;
        this.trainStoppingTime = trainStoppingTime;
        this.cost = cost;
        this.city = city;
        this.train = train;
    }
}