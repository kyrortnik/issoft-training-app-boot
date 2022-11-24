package com.issoft.ticketstoreapp.model.audit;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "audits")
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "method_signature")
    @EqualsAndHashCode.Include
    private String methodSignature;

    @Column(name = "return_value")
    private String returnValue;

    @Column(name = "exception_type")
    private String exceptionType;

    @Column(name = "exception_message")
    private String exceptionMessage;

    @Column(name = "execution_datetime")
    private LocalDateTime executionDatetime;

    @Builder.Default
    @OneToMany(mappedBy = "audit", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Argument> methodArgs = new ArrayList<>();

    public void addToMethodArgs(Argument arg) {

        arg.setAudit(this);
        this.methodArgs.add(arg);
    }
}