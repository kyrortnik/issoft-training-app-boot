package com.issoft.ticketstoreapp.model.audit;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "arguments")
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Argument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "arg_type")
    @EqualsAndHashCode.Include
    private String argType;

    @Column(name = "arg_value")
    @EqualsAndHashCode.Include
    private String argValue;

    @ManyToOne
    @JoinColumn(name = "audit_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private Audit audit;
}