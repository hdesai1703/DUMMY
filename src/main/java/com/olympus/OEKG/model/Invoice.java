package com.olympus.OEKG.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

	@Id
    private Long id;
    private String name;
    private String location;
    private Double amount;
}
