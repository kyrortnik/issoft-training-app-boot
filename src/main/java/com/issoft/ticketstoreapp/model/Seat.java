package com.issoft.ticketstoreapp.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "seats")
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private Short number;

    @ManyToOne
    @JoinColumn(name = "row_id", nullable = false)
    private Row row;
}