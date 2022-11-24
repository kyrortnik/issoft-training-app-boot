package com.issoft.ticketstoreapp.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "halls")
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="cinema_id", nullable = false)
    private Cinema cinema;

    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String name;

    @Column(name = "has_3d_support")
    private Boolean has3DSupport;

    @Column(name = "has_imax_support")
    private Boolean hasIMAXSupport;
}