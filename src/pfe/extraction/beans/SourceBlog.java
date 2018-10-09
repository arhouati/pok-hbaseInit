package pfe.extraction.beans;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@DiscriminatorValue(value = "BL")
public class SourceBlog extends Source {
	@Column(name = "Url", nullable = true, length = 255)
	private String url;
	@OneToOne
	@PrimaryKeyJoinColumn
	private Configuration configuration;
	
	
	public SourceBlog() {
		super();
	}

	public SourceBlog(String description, String url,
			Configuration configuration) {
		super(description);
		this.url = url;
		this.configuration = configuration;
	}

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}


	public Configuration getConfiguration() {
		return configuration;
	}


	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
}
