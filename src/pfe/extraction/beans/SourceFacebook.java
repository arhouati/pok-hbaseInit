package pfe.extraction.beans;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "FB")
public class SourceFacebook extends Source {
	@Column(name = "Id_Page", nullable = true, length = 255, unique = true)
	private String idPage;
	@Column(name = "Access_Token", nullable = true, length = 255)
	private String accessToken;

	

	public SourceFacebook() {
		super();
	}

	public SourceFacebook(String description, String idPage,
			String accessToken) {
		super(description);
		this.idPage = idPage;
		this.accessToken = accessToken;
	}

	public String getIdPage() {
		return idPage;
	}

	public void setIdPage(String idPage) {
		this.idPage = idPage;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
