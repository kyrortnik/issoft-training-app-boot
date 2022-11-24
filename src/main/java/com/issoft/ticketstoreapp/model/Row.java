package com.issoft.ticketstoreapp.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "rows")
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Row {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private Short number;

    @ManyToOne
    @JoinColumn(name="hall_id", nullable = false)
    private Hall hall;
}