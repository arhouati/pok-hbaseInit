package pfe.extraction.beans;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE_SRC", length = 2)
@Table(name = "T_SOURCE")
public abstract class Source {
	@Id
	@Column(name = "Id_source")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idSource;
	@Column(name = "Description", length = 255)
	private String description;

	public Source() {
	}

	public Source(String description) {
		super();

		this.description = description;
	}

	public Long getIdSource() {
		return idSource;
	}

	public void setIdSource(Long idSource) {
		this.idSource = idSource;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
