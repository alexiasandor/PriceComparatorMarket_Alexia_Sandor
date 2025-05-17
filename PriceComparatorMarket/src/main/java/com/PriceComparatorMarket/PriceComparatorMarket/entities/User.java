package com.PriceComparatorMarket.PriceComparatorMarket.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name= "user_t")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    @Column(name= "userName", nullable = false)
    private String userName;
    @Column(name= "messageFromAlerts", nullable = true)
    private String messageFromAlerts;
    @OneToMany (mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<PriceAlert> listOfAlerts;


}
