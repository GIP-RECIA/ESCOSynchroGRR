package org.crlr.synchronisationGrr.DTO;

import java.util.ArrayList;
import java.util.List;

public class DomaineDTO {
	
		String id; 	
		String area_name; 	
		String access; 	
		String order_display; 	
		String ip_adr; 	
		String morningstarts_area; 	
		String eveningends_area; 	
		String duree_max_resa_area; 	
		String resolution_area; 	
		String eveningends_minutes_area; 	
		String weekstarts_area; 	
		String twentyfourhour_format_area; 	
		String calendar_default_values; 	
		String enable_periods; 	
		String display_days; 	
		String id_type_par_defaut ; 	
		String duree_par_defaut_reservation_area ; 	
		String max_booking ;
		
		List<RoomDTO> rooms;
		
		List<String> users = new ArrayList<String>();
		List<String> usersAdmin = new ArrayList<String>();
		
		List<PeriodeDTO> periodes = new ArrayList<PeriodeDTO>();
		List<OverloadDTO> overloads = new ArrayList<OverloadDTO>();
		
		List<String> idTypesArea = new ArrayList<String>();
		
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getArea_name() {
			return area_name;
		}

		public void setArea_name(String area_name) {
			this.area_name = area_name;
		}

		public String getAccess() {
			return access;
		}

		public void setAccess(String access) {
			this.access = access;
		}

		public String getOrder_display() {
			return order_display;
		}

		public void setOrder_display(String order_display) {
			this.order_display = order_display;
		}

		public String getIp_adr() {
			return ip_adr;
		}

		public void setIp_adr(String ip_adr) {
			this.ip_adr = ip_adr;
		}

		public String getMorningstarts_area() {
			return morningstarts_area;
		}

		public void setMorningstarts_area(String morningstarts_area) {
			this.morningstarts_area = morningstarts_area;
		}

		public String getEveningends_area() {
			return eveningends_area;
		}

		public void setEveningends_area(String eveningends_area) {
			this.eveningends_area = eveningends_area;
		}

		public String getDuree_max_resa_area() {
			return duree_max_resa_area;
		}

		public void setDuree_max_resa_area(String duree_max_resa_area) {
			this.duree_max_resa_area = duree_max_resa_area;
		}

		public String getResolution_area() {
			return resolution_area;
		}

		public void setResolution_area(String resolution_area) {
			this.resolution_area = resolution_area;
		}

		public String getEveningends_minutes_area() {
			return eveningends_minutes_area;
		}

		public void setEveningends_minutes_area(String eveningends_minutes_area) {
			this.eveningends_minutes_area = eveningends_minutes_area;
		}

		public String getWeekstarts_area() {
			return weekstarts_area;
		}

		public void setWeekstarts_area(String weekstarts_area) {
			this.weekstarts_area = weekstarts_area;
		}

		public String getTwentyfourhour_format_area() {
			return twentyfourhour_format_area;
		}

		public void setTwentyfourhour_format_area(String twentyfourhour_format_area) {
			this.twentyfourhour_format_area = twentyfourhour_format_area;
		}

		public String getCalendar_default_values() {
			return calendar_default_values;
		}

		public void setCalendar_default_values(String calendar_default_values) {
			this.calendar_default_values = calendar_default_values;
		}

		public String getEnable_periods() {
			return enable_periods;
		}

		public void setEnable_periods(String enable_periods) {
			this.enable_periods = enable_periods;
		}

		public String getDisplay_days() {
			return display_days;
		}

		public void setDisplay_days(String display_days) {
			this.display_days = display_days;
		}

		public String getId_type_par_defaut() {
			return id_type_par_defaut;
		}

		public void setId_type_par_defaut(String id_type_par_defaut) {
			this.id_type_par_defaut = id_type_par_defaut;
		}

		public String getDuree_par_defaut_reservation_area() {
			return duree_par_defaut_reservation_area;
		}

		public void setDuree_par_defaut_reservation_area(
				String duree_par_defaut_reservation_area) {
			this.duree_par_defaut_reservation_area = duree_par_defaut_reservation_area;
		}

		public String getMax_booking() {
			return max_booking;
		}

		public void setMax_booking(String max_booking) {
			this.max_booking = max_booking;
		}

		public List<RoomDTO> getRooms() {
			return rooms;
		}

		public void setRooms(List<RoomDTO> rooms) {
			this.rooms = rooms;
		}

		public List<String> getUsers() {
			return users;
		}

		public void setUsers(List<String> users) {
			this.users = users;
		}

		public List<String> getUsersAdmin() {
			return usersAdmin;
		}

		public void setUsersAdmin(List<String> usersAdmin) {
			this.usersAdmin = usersAdmin;
		}

		public List<PeriodeDTO> getPeriodes() {
			return periodes;
		}

		public void setPeriodes(List<PeriodeDTO> periodes) {
			this.periodes = periodes;
		}

		public List<OverloadDTO> getOverloads() {
			return overloads;
		}

		public void setOverloads(List<OverloadDTO> overloads) {
			this.overloads = overloads;
		}

		public List<String> getIdTypesArea() {
			return idTypesArea;
		}

		public void setIdTypesArea(List<String> idTypesArea) {
			this.idTypesArea = idTypesArea;
		}

		
		
}
