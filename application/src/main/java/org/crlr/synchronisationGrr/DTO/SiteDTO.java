package org.crlr.synchronisationGrr.DTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author crok
 *
 */
public class SiteDTO {
	
	private String id;
	private String 	sitecode 	;
	private String 	sitename ;
	private String 	adresse_ligne1 ;
	private String 	adresse_ligne2 ;
	private String 	adresse_ligne3 ;
	private String 	cp 	;
	private String 	ville ;
	private String 	pays ;
	private String 	tel ;
	private String 	fax ;

	private List<DomaineDTO> domaines = new ArrayList<DomaineDTO>();
	private List<String> usersAdmin = new ArrayList<String>();

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSitecode() {
		return sitecode;
	}

	public void setSitecode(String sitecode) {
		this.sitecode = sitecode;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public String getAdresse_ligne1() {
		return adresse_ligne1;
	}

	public void setAdresse_ligne1(String adresse_ligne1) {
		this.adresse_ligne1 = adresse_ligne1;
	}

	public String getAdresse_ligne2() {
		return adresse_ligne2;
	}

	public void setAdresse_ligne2(String adresse_ligne2) {
		this.adresse_ligne2 = adresse_ligne2;
	}

	public String getAdresse_ligne3() {
		return adresse_ligne3;
	}

	public void setAdresse_ligne3(String adresse_ligne3) {
		this.adresse_ligne3 = adresse_ligne3;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getPays() {
		return pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public List<DomaineDTO> getDomaines() {
		return domaines;
	}

	public void setDomaines(List<DomaineDTO> domaines) {
		this.domaines = domaines;
	}

	public List<String> getUsersAdmin() {
		return usersAdmin;
	}

	public void setUsersAdmin(List<String> usersAdmin) {
		this.usersAdmin = usersAdmin;
	}


	
	
	
}
