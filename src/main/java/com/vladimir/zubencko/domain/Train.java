package com.vladimir.zubencko.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "train")
@Data
@NoArgsConstructor
@Log4j
public class Train implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false,unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "train")
    @OrderBy("id ASC")
    private List<TrainWay> trainWays;

    public Train(String name) {
        this.name = name.toLowerCase();
    }
}
