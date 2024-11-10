package tn.esprit.spring.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
public class Piste implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long numPiste;

	@NotNull(message = "Piste name cannot be null")
	String namePiste;

	@Enumerated(EnumType.STRING)
	Color color;

	@Min(value = 0, message = "Length must be a non-negative value")
	Integer length;

	@Min(value = 0, message = "Slope must be a non-negative value")
	Integer slope;

	@ManyToMany(mappedBy = "pistes")
	Set<Skier> skiers;

}
