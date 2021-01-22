package com.udacity.jdnd.course3.critter.schedule;

import javax.persistence.*;


@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
