package org.crlr.synchronisationGrr.DTO;

import java.util.List;

public class DataEtablissementDTO {

	private List<SiteDTO> sites;
	private List<TypeResaDTO> typesResa;
	private List<String> days;
	public List<SiteDTO> getSites() {
		return sites;
	}
	public void setSites(List<SiteDTO> sites) {
		this.sites = sites;
	}
	public List<TypeResaDTO> getTypesResa() {
		return typesResa;
	}
	public void setTypesResa(List<TypeResaDTO> typesResa) {
		this.typesResa = typesResa;
	}
	public List<String> getDays() {
		return days;
	}
	public void setDays(List<String> days) {
		this.days = days;
	}
	
}
