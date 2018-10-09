package pfe.extraction.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T_TAG")
public class Tag {
	@Id
	@Column(name = "Id_tag")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idTag;
	@Column(name = "Identifier_tag", nullable = false, length = 20, unique = true)
	private String identifierTag;
	@Column(name = "Name", nullable = false, length = 20)
	private String name;
	@Column(name = "Selector", nullable = false, length = 255)
	private String selector;
	@Column(name = "Type_content", nullable = false, length = 255)
	private String typeContent;
	@ManyToOne
	@JoinColumn(name = "Id_configuration")
	private Configuration configuration;

	public Tag() {
	}

	public Tag(String identifierTag, String name, String selector,
			String typeContent, Configuration configuration) {
		super();
		this.identifierTag = identifierTag;
		this.name = name;
		this.selector = selector;
		this.typeContent = typeContent;
		this.configuration = configuration;
	}

	public Long getIdTag() {
		return idTag;
	}

	public void setIdTag(Long idTag) {
		this.idTag = idTag;
	}

	public String getIdentifierTag() {
		return identifierTag;
	}

	public void setIdentifierTag(String identifierTag) {
		this.identifierTag = identifierTag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public String getTypeContent() {
		return typeContent;
	}

	public void setTypeContent(String typeContent) {
		this.typeContent = typeContent;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

}
