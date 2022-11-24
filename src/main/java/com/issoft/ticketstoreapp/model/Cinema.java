package com.issoft.ticketstoreapp.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cinemas")
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String name;

    @Column(name = "start_work_time")
    private LocalTime startWorkTime;

    @Column(name = "close_work_time")
    private LocalTime closeWorkTime;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "dt_opened")
    private LocalDateTime dtOpened;

    @ManyToMany
    @JoinTable(name = "cinemas_movies_link",
            joinColumns = @JoinColumn(name = "cinema_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private Set<Movie> moviesInRotation = new HashSet<>();
}