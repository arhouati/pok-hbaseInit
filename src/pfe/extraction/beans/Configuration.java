package pfe.extraction.beans;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T_CONFIGURATION")
public class Configuration {
	@Id
	@Column(name = "Id_configuration")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idConfiguration;
	@Column(name = "Name", nullable = false, length = 20, unique = true)
	private String name;
	@Column(name = "Description", nullable = false, length = 255)
	private String description;
	@OneToOne(mappedBy = "configuration")
	private SourceBlog sourceBlog;
	@OneToMany(mappedBy = "configuration")
	private List<Tag> tags;

	public Configuration() {
	}

	public Configuration(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public Long getIdConfiguration() {
		return idConfiguration;
	}

	public void setIdConfiguration(Long idConfiguration) {
		this.idConfiguration = idConfiguration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SourceBlog getSource() {
		return sourceBlog;
	}

	public void setSource(SourceBlog source) {
		this.sourceBlog = source;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
}
