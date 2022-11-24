package com.issoft.ticketstoreapp.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies")
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String title;

    private Short length;

    @Column(name = "is_3D")
    private Boolean is3D;

    @Column(name = "is_IMAX")
    private Boolean isIMAX;

    @Column(name = "days_in_rotation")
    private Short daysInRotation;

    @ManyToMany(mappedBy = "moviesInRotation")
    private Set<Hall> halls = new HashSet<>();

    @ManyToMany(mappedBy = "moviesInRotation")
    private Set<Cinema> cinemas = new HashSet<>();

    @Column(name = "poster_link")
    private String posterLink;
}