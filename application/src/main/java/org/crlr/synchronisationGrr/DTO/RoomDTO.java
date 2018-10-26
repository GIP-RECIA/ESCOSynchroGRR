package org.crlr.synchronisationGrr.DTO;

import java.util.ArrayList;
import java.util.List;


public class RoomDTO {
	
	String id;
	  String area_id;
	  String room_name;
	  String description;
	  String capacity;
	  String max_booking;
	  String statut_room;
	  String show_fic_room;
	  String picture_room;
	  String comment_room;
	  String show_comment;
	  String delais_max_resa_room;
	  String delais_min_resa_room;
	  String allow_action_in_past;
	  String dont_allow_modify;
	  String order_display;
	  String delais_option_reservation;
	  String type_affichage_reser;
	  String moderate;
	  String qui_peut_reserver_pour;
	  String active_ressource_empruntee;
	  String who_can_see;
	  List<String> user = new ArrayList<String>();
	  List<String> mailuser = new ArrayList<String>();
	  List<EntryDTO> entries = new ArrayList<EntryDTO>();
	  List<EntryModerateDTO> entriesModerate = new ArrayList<EntryModerateDTO>();
	  List<RepeatDTO> repeat = new ArrayList<RepeatDTO>();
	  
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}
	public String getRoom_name() {
		return room_name;
	}
	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	public String getMax_booking() {
		return max_booking;
	}
	public void setMax_booking(String max_booking) {
		this.max_booking = max_booking;
	}
	public String getStatut_room() {
		return statut_room;
	}
	public void setStatut_room(String statut_room) {
		this.statut_room = statut_room;
	}
	public String getShow_fic_room() {
		return show_fic_room;
	}
	public void setShow_fic_room(String show_fic_room) {
		this.show_fic_room = show_fic_room;
	}
	public String getPicture_room() {
		return picture_room;
	}
	public void setPicture_room(String picture_room) {
		this.picture_room = picture_room;
	}
	public String getComment_room() {
		return comment_room;
	}
	public void setComment_room(String comment_room) {
		this.comment_room = comment_room;
	}
	public String getShow_comment() {
		return show_comment;
	}
	public void setShow_comment(String show_comment) {
		this.show_comment = show_comment;
	}
	public String getDelais_max_resa_room() {
		return delais_max_resa_room;
	}
	public void setDelais_max_resa_room(String delais_max_resa_room) {
		this.delais_max_resa_room = delais_max_resa_room;
	}
	public String getDelais_min_resa_room() {
		return delais_min_resa_room;
	}
	public void setDelais_min_resa_room(String delais_min_resa_room) {
		this.delais_min_resa_room = delais_min_resa_room;
	}
	public String getAllow_action_in_past() {
		return allow_action_in_past;
	}
	public void setAllow_action_in_past(String allow_action_in_past) {
		this.allow_action_in_past = allow_action_in_past;
	}
	public String getDont_allow_modify() {
		return dont_allow_modify;
	}
	public void setDont_allow_modify(String dont_allow_modify) {
		this.dont_allow_modify = dont_allow_modify;
	}
	public String getOrder_display() {
		return order_display;
	}
	public void setOrder_display(String order_display) {
		this.order_display = order_display;
	}
	public String getDelais_option_reservation() {
		return delais_option_reservation;
	}
	public void setDelais_option_reservation(String delais_option_reservation) {
		this.delais_option_reservation = delais_option_reservation;
	}
	public String getType_affichage_reser() {
		return type_affichage_reser;
	}
	public void setType_affichage_reser(String type_affichage_reser) {
		this.type_affichage_reser = type_affichage_reser;
	}
	public String getModerate() {
		return moderate;
	}
	public void setModerate(String moderate) {
		this.moderate = moderate;
	}
	public String getQui_peut_reserver_pour() {
		return qui_peut_reserver_pour;
	}
	public void setQui_peut_reserver_pour(String qui_peut_reserver_pour) {
		this.qui_peut_reserver_pour = qui_peut_reserver_pour;
	}
	public String getActive_ressource_empruntee() {
		return active_ressource_empruntee;
	}
	public void setActive_ressource_empruntee(String active_ressource_empruntee) {
		this.active_ressource_empruntee = active_ressource_empruntee;
	}
	public String getWho_can_see() {
		return who_can_see;
	}
	public void setWho_can_see(String who_can_see) {
		this.who_can_see = who_can_see;
	}
	public List<String> getUser() {
		return user;
	}
	public void setUser(List<String> user) {
		this.user = user;
	}
	public List<String> getMailuser() {
		return mailuser;
	}
	public void setMailuser(List<String> mailuser) {
		this.mailuser = mailuser;
	}
	public List<EntryDTO> getEntries() {
		return entries;
	}
	public void setEntries(List<EntryDTO> entries) {
		this.entries = entries;
	}
	public List<EntryModerateDTO> getEntriesModerate() {
		return entriesModerate;
	}
	public void setEntriesModerate(List<EntryModerateDTO> entriesModerate) {
		this.entriesModerate = entriesModerate;
	}
	public List<RepeatDTO> getRepeat() {
		return repeat;
	}
	public void setRepeat(List<RepeatDTO> repeat) {
		this.repeat = repeat;
	}

	  
}
