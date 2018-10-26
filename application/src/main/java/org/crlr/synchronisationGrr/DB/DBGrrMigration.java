package org.crlr.synchronisationGrr.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.crlr.synchronisationGrr.DTO.DataEtablissementDTO;
import org.crlr.synchronisationGrr.DTO.DomaineDTO;
import org.crlr.synchronisationGrr.DTO.EntryDTO;
import org.crlr.synchronisationGrr.DTO.EntryModerateDTO;
import org.crlr.synchronisationGrr.DTO.OverloadDTO;
import org.crlr.synchronisationGrr.DTO.PeriodeDTO;
import org.crlr.synchronisationGrr.DTO.RepeatDTO;
import org.crlr.synchronisationGrr.DTO.RoomDTO;
import org.crlr.synchronisationGrr.DTO.SiteDTO;
import org.crlr.synchronisationGrr.DTO.TypeResaDTO;

public class DBGrrMigration implements InterfaceMigrationGrr {
	
	private Connection conn;
	private String driverClassName;
	private String username;
	private String password;
	private String startURL;
	private String prefixeGrr;
	
	
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setStartURL(String startURL) {
		this.startURL = startURL;
	}

	public void setPrefixeGrr(String prefixeGrr) {
		this.prefixeGrr = prefixeGrr;
	}

	@Override
	public Boolean openConnection(String nomBDD) {
		BasicDataSource projectDS = new BasicDataSource();
        projectDS.setDriverClassName(driverClassName);
        projectDS.setUsername(username);
        projectDS.setPassword(password);
        projectDS.setUrl(startURL+nomBDD);
		try {
			conn = projectDS.getConnection();
		} catch (SQLException e) {
			return false;
		}
        return conn != null;
	}

	@Override
	public Boolean closeConnection() {
		try {
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public DataEtablissementDTO findDataEtablissement(Boolean isInCommonDB, String uai) throws SQLException {
		DataEtablissementDTO datas = new DataEtablissementDTO();
		
		List<TypeResaDTO> typesResa = findTypesResa();
		datas.setTypesResa(typesResa );
		
		List<String> days = findDays();
		datas.setDays(days);
		
		List<SiteDTO> sites = new ArrayList<SiteDTO>();
		if (isInCommonDB){
			//Dans la BDD commune, on cherche le site associé à l'établissement
			SiteDTO site = findSiteByCode(uai);
			sites.add(site);
			
		} else {
			//Dans BDD stand alone
			if (existeSites()){
				//Avec des sites
				sites = findAllSites();
			} else {
				//Il faut un site bidon pour les domaines non rattachés aux sites
				SiteDTO site = new SiteDTO();
				sites.add(site);
			}			
		}
		
		for (SiteDTO site : sites){
			
			
			List<String> usersAdmin = findUsersAdminSite(site.getId());
			site.setUsersAdmin(usersAdmin );
			
			List<DomaineDTO> domaines;
			if (existeSites()){
				domaines = findDomainesBySite(site.getId());
			} else {
				domaines = findAllDomaines();
			}
			
			for (DomaineDTO domaine : domaines){
				List<String> usersDomaine = findUsersDomaine(domaine.getId());
				domaine.setUsers(usersDomaine );
				List<String> usersAdminDomaine = findUsersAdminDomaine(domaine.getId());
				domaine.setUsersAdmin(usersAdminDomaine);
				
				List<PeriodeDTO> periodesDomaine = findPeriodeDomaine(domaine.getId());
				domaine.setPeriodes(periodesDomaine);
				
				List<OverloadDTO> overloadDomaine = findOverloadDomaine(domaine.getId());
				domaine.setOverloads(overloadDomaine);
				
				List<String> idsTypeArea = findIdsTypeArea(domaine.getId());
				domaine.setIdTypesArea(idsTypeArea);
				
				List<RoomDTO> rooms = findRoomByDomaine(domaine.getId());
				
				// Désactivé pour ne  pas importer les utilisateurs d'un GRR ancien
				//for (RoomDTO room : rooms){
				//	List<String> usersRoom = findUsersRoom(room.getId());
				//	room.setUser(usersRoom );
				//	List<String> mailuser = findMailUsersRoom(room.getId());
				//	room.setMailuser(mailuser);
				//	List<EntryDTO> entries = findEntriesRoom(room.getId());
				//	room.setEntries(entries);
				//	List<EntryModerateDTO> entriesModerate = findEntriesModerate(room.getId());
 				//	room.setEntriesModerate(entriesModerate);
 				//	List<RepeatDTO> repeat = findRepeatRoom(room.getId());
				//	room.setRepeat(repeat);
				//}
				
				domaine.setRooms(rooms);
			}
			
			site.setDomaines(domaines);
		}
		
		datas.setSites(sites);
		return datas;
	}

	
	
	private List<String> findDays() throws SQLException {
		Statement stmt = null;
		List<String> list = new ArrayList<String>();
		try { 
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+ prefixeGrr +"calendar " );
			try {
				while ( rs.next() ) {
					String day = getStringByColumnName(rs,"DAY");
					list.add(day);
				}
			} finally {
				rs.close();
			} 
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return list;
	}
	

	

	private List<String> findIdsTypeArea(String id) throws SQLException {
		Statement stmt = null;
		List<String> list = new ArrayList<String>();
		try { 
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery( "SELECT id_type FROM "+ prefixeGrr +"j_type_area WHERE id_area = "+id );
			try {
				while ( rs.next() ) {
					String idTypeArea = extractIdTypeAreaFromRS(rs);
					list.add(idTypeArea);
				}
			} finally {
				rs.close(); 
			} 
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return list;
	}

	

	private List<TypeResaDTO> findTypesResa() throws SQLException {
		Statement stmt = null;
		List<TypeResaDTO> list = new ArrayList<TypeResaDTO>();
		try { 
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+ prefixeGrr +"type_area " );
			try {
				while ( rs.next() ) {
					TypeResaDTO typeArea = extractTypeAreaFromRS(rs);
					list.add(typeArea);
				}
			} finally {
				rs.close();
			} 
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return list;
	}

	

	private List<OverloadDTO> findOverloadDomaine(String id) throws SQLException {
		Statement stmt = null;
		List<OverloadDTO> list = new ArrayList<OverloadDTO>();
		try { 
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+ prefixeGrr +"overload WHERE id_area = "+id );
			try {
				while ( rs.next() ) {
					OverloadDTO overload = extractOverloadFromRS(rs);
					list.add(overload);
				}
			} finally {
				rs.close();
			} 
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return list;
	}



	private List<PeriodeDTO> findPeriodeDomaine(String id) throws SQLException {
		Statement stmt = null;
		List<PeriodeDTO> list = new ArrayList<PeriodeDTO>();
		try { 
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+ prefixeGrr +"area_periodes WHERE id_area = "+id );
			try {
				while ( rs.next() ) {
					PeriodeDTO entry = extractPeriodFromRS(rs);
					list.add(entry);
				}
			} finally {
				rs.close();
			} 
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return list;
	}

	private List<RepeatDTO> findRepeatRoom(String id) throws SQLException {
		Statement stmt = null;
		List<RepeatDTO> list = new ArrayList<RepeatDTO>();
		try { 
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+ prefixeGrr +"repeat WHERE room_id = "+id );
			try {
				while ( rs.next() ) {
					RepeatDTO entry = extractRepeatFromRS(rs);
					list.add(entry);
				}
			} finally {
				rs.close();
			} 
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return list;
	}

	private List<EntryModerateDTO> findEntriesModerate(String id) throws SQLException {
		Statement stmt = null;
		List<EntryModerateDTO> list = new ArrayList<EntryModerateDTO>();
		try { 
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+ prefixeGrr +"entry_moderate WHERE room_id = "+id );
			try {
				while ( rs.next() ) {
					EntryModerateDTO entry = extractEntryModerateFromRS(rs);
					list.add(entry);
				}
			} finally {
				rs.close();
			} 
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return list;
	}

	

	private List<EntryDTO> findEntriesRoom(String id) throws SQLException {
		Statement stmt = null;
		List<EntryDTO> list = new ArrayList<EntryDTO>();
		try { 
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+ prefixeGrr +"entry WHERE room_id = "+id );
			try {
				while ( rs.next() ) {
					EntryDTO entry = extractEntryFromRS(rs);
					list.add(entry);
				}
			} finally {
				rs.close();
			} 
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return list;
	}



	private List<String> findMailUsersRoom(String id) throws SQLException {
		Statement stmt = null;
		List<String> listUsersAdmin = new ArrayList<String>();
		try { 
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery( "SELECT login FROM "+ prefixeGrr +"j_mailuser_room WHERE id_room = "+id );
			try {
				while ( rs.next() ) {
					listUsersAdmin.add(extractLoginFromRS(rs));
				}
			} finally {
				rs.close();
			} 
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return listUsersAdmin;
	}

	private List<String> findUsersRoom(String id) throws SQLException {
		Statement stmt = null;
		List<String> listUsersAdmin = new ArrayList<String>();
		try { 
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery( "SELECT login FROM "+ prefixeGrr +"j_user_room WHERE id_room = "+id );
			try {
				while ( rs.next() ) {
					listUsersAdmin.add(extractLoginFromRS(rs));
				}
			} finally {
				rs.close();
			} 
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return listUsersAdmin;
	}

	private List<String> findUsersAdminDomaine(String id) throws SQLException {
		Statement stmt = null;
		List<String> listUsersAdmin = new ArrayList<String>();
		try { 
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery( "SELECT login FROM "+ prefixeGrr +"j_useradmin_area WHERE id_area = "+id );
			try {
				while ( rs.next() ) {
					listUsersAdmin.add(extractLoginFromRS(rs));
				}
			} finally {
				rs.close();
			} 
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return listUsersAdmin;
	}

	private List<String> findUsersDomaine(String id) throws SQLException {
		Statement stmt = null;
		List<String> listUsersAdmin = new ArrayList<String>();
		try { 
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery( "SELECT login FROM "+ prefixeGrr +"j_user_area WHERE id_area = "+id );
			try {
				while ( rs.next() ) {
					listUsersAdmin.add(extractLoginFromRS(rs));
				}
			} finally {
				rs.close();
			} 
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return listUsersAdmin;
	}

	private List<String> findUsersAdminSite(String id) throws SQLException {
		Statement stmt = null;
		List<String> listUsersAdmin = new ArrayList<String>();
		try { 
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery( "SELECT login FROM "+ prefixeGrr +"j_useradmin_site WHERE id_site = "+id );
			try {
				while ( rs.next() ) {
					listUsersAdmin.add(extractLoginFromRS(rs));
				}
			} finally {
				rs.close(); 
			} 
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return listUsersAdmin;
	}

	private Boolean existeSites() throws SQLException{
		
		Statement stmt = null;
		try {
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery( "SHOW columns FROM "+ prefixeGrr +"site" );
			try {

				if (rs.getMetaData().getColumnCount() > 0 ) {
					rs.close();
					
					rs = stmt.executeQuery( "SELECT count(*) FROM "+ prefixeGrr +"site" );
					rs.next();
					return rs.getInt(1) > 0 ;
				} else {
					return false;
				}
				
			} finally {
				rs.close();
			}
		} finally {
			if (stmt != null) stmt.close(); 
		}
	}
	
	private SiteDTO findSiteByCode(String uai) throws SQLException {
		Statement stmt = null;
		SiteDTO site = null; 
		try { 
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+ prefixeGrr +"site WHERE sitecode = '"+uai+"'" );
			try {
				while ( rs.next() ) {
					site = extractSiteFromRS(rs);
					
				}
			} finally {
				rs.close();
			} 
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return site;
	}
	
	private List<SiteDTO> findAllSites() throws SQLException{
		List<SiteDTO> sites = new ArrayList<SiteDTO>();
		
		Statement stmt = null;
		try {
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+ prefixeGrr +"site" );
			try {
				while ( rs.next() ) {
					SiteDTO site = extractSiteFromRS(rs);
				
					sites.add(site);
					
				}
			} finally {
				rs.close();
			}
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return sites;
	}

	
	private List<RoomDTO> findRoomByDomaine(String id) throws SQLException {
		List<RoomDTO> rooms = new ArrayList<RoomDTO>();
		
		Statement stmt = null;
		try {
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+ prefixeGrr +"room where area_id ="+id );
			try {
				while ( rs.next() ) {
					RoomDTO domaine = extractRoomFromRS(rs);
					rooms.add(domaine);
					
				}
			} finally {
				rs.close();
			} 
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return rooms;
	}
	
	private List<DomaineDTO> findDomainesBySite(String id) throws SQLException {
		List<DomaineDTO> domaines = new ArrayList<DomaineDTO>();
		
		Statement stmt = null;
		try {
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+ prefixeGrr +"area join "+ prefixeGrr +"j_site_area ON id_area= id where id_site ="+id );
			try {
				while ( rs.next() ) {
					DomaineDTO domaine = extractDomainFromRS(rs);
					domaines.add(domaine);
					 
				}
			} finally {
				rs.close();
			}
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return domaines;
	}
	
	private List<DomaineDTO> findAllDomaines() throws SQLException{
		List<DomaineDTO> domaines = new ArrayList<DomaineDTO>();
		
		Statement stmt = null;
		try {
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+ prefixeGrr +"area" );
			try { 
				while ( rs.next() ) {
					DomaineDTO domaine = extractDomainFromRS(rs);
					domaines.add(domaine);
					
				}
			} finally {
				rs.close();
			}
		} finally {
			if (stmt != null) stmt.close(); 
		}
		return domaines;
	}
	
	private String extractLoginFromRS(ResultSet rs) throws SQLException {
		return getStringByColumnName(rs,"login");
	}	
	
	private SiteDTO extractSiteFromRS(ResultSet rs) throws SQLException {
		SiteDTO site = new SiteDTO();
		site.setId(getStringByColumnName(rs,"id"));
		site.setSitecode(getStringByColumnName(rs,"sitecode"));
		site.setSitename(getStringByColumnName(rs,"sitename"));
		site.setAdresse_ligne1(getStringByColumnName(rs,"adresse_ligne1"));
		site.setAdresse_ligne2(getStringByColumnName(rs,"adresse_ligne2"));
		site.setAdresse_ligne3(getStringByColumnName(rs,"adresse_ligne3"));
		site.setCp(getStringByColumnName(rs,"cp"));
		site.setVille(getStringByColumnName(rs,"ville"));
		site.setPays(getStringByColumnName(rs,"pays"));
		site.setTel(getStringByColumnName(rs,"tel"));
		site.setFax(getStringByColumnName(rs,"fax"));
		return site;
	}	

	private DomaineDTO extractDomainFromRS(ResultSet rs) throws SQLException {
		DomaineDTO domaine = new DomaineDTO();
		domaine.setId(getStringByColumnName(rs,"id"));
		domaine.setAccess(getStringByColumnName(rs,"access"));
		domaine.setArea_name(getStringByColumnName(rs,"area_name"));
		domaine.setCalendar_default_values(getStringByColumnName(rs,"calendar_default_values"));
		domaine.setDisplay_days(getStringByColumnName(rs,"display_days"));
		domaine.setDuree_max_resa_area(getStringByColumnName(rs,"duree_max_resa_area"));
		domaine.setDuree_par_defaut_reservation_area(getStringByColumnName(rs,"duree_par_defaut_reservation_area"));
		domaine.setEnable_periods(getStringByColumnName(rs,"enable_periods"));
		domaine.setEveningends_area(getStringByColumnName(rs,"eveningends_area"));
		domaine.setEveningends_minutes_area(getStringByColumnName(rs,"eveningends_minutes_area"));
		domaine.setId_type_par_defaut(getStringByColumnName(rs,"id_type_par_defaut"));
		domaine.setIp_adr(getStringByColumnName(rs,"ip_adr"));
		domaine.setMax_booking(getStringByColumnName(rs,"max_booking"));
		domaine.setMorningstarts_area(getStringByColumnName(rs,"morningstarts_area"));
		domaine.setOrder_display(getStringByColumnName(rs,"order_display"));
		domaine.setResolution_area(getStringByColumnName(rs,"resolution_area"));
		domaine.setTwentyfourhour_format_area(getStringByColumnName(rs,"twentyfourhour_format_area"));
		domaine.setWeekstarts_area(getStringByColumnName(rs,"weekstarts_area"));
		return domaine;
	}	
	
	private RoomDTO extractRoomFromRS(ResultSet rs) throws SQLException {
		RoomDTO room = new RoomDTO();
		room.setActive_ressource_empruntee(getStringByColumnName(rs,"active_ressource_empruntee"));
		room.setAllow_action_in_past(getStringByColumnName(rs,"allow_action_in_past"));
		room.setArea_id(getStringByColumnName(rs,"area_id"));
		room.setCapacity(getStringByColumnName(rs,"capacity"));
		room.setComment_room(getStringByColumnName(rs,"comment_room"));
		room.setDelais_max_resa_room(getStringByColumnName(rs,"delais_max_resa_room"));
		room.setDelais_min_resa_room(getStringByColumnName(rs,"delais_min_resa_room"));
		room.setDelais_option_reservation(getStringByColumnName(rs,"delais_option_reservation"));
		room.setDescription(getStringByColumnName(rs,"description"));
		room.setDont_allow_modify(getStringByColumnName(rs,"dont_allow_modify")); 
		room.setId(getStringByColumnName(rs,"id"));
		room.setMax_booking(getStringByColumnName(rs,"max_booking"));
		room.setModerate(getStringByColumnName(rs,"moderate"));
		room.setOrder_display(getStringByColumnName(rs,"order_display"));
		room.setPicture_room(getStringByColumnName(rs,"picture_room"));
		room.setQui_peut_reserver_pour(getStringByColumnName(rs,"qui_peut_reserver_pour"));
		room.setRoom_name(getStringByColumnName(rs,"room_name"));
		room.setShow_comment(getStringByColumnName(rs,"show_comment"));
		room.setShow_fic_room(getStringByColumnName(rs,"show_fic_room"));
		room.setStatut_room(getStringByColumnName(rs,"statut_room"));
		room.setType_affichage_reser(getStringByColumnName(rs,"type_affichage_reser"));
		room.setWho_can_see(getStringByColumnName(rs,"who_can_see"));
		return room;
	}
	
	private EntryDTO extractEntryFromRS(ResultSet rs) throws SQLException {
		EntryDTO entry = new EntryDTO();
		entry.setBeneficiaire(getStringByColumnName(rs,"beneficiaire"));
		entry.setBeneficiaire_ext(getStringByColumnName(rs,"beneficiaire_ext"));
		entry.setCreate_by(getStringByColumnName(rs,"create_by"));
		entry.setDescription(getStringByColumnName(rs,"description"));
		entry.setEnd_time(getStringByColumnName(rs,"end_time"));
		entry.setEntry_type(getStringByColumnName(rs,"entry_type"));
		entry.setId(getStringByColumnName(rs,"id"));
		entry.setJours(getStringByColumnName(rs,"jours"));
		entry.setModerate(getStringByColumnName(rs,"moderate"));
		entry.setName(getStringByColumnName(rs,"name"));
		entry.setOption_reservation(getStringByColumnName(rs,"option_reservation"));
		entry.setOverload_desc(getStringByColumnName(rs,"overload_desc"));
		entry.setRepeat_id(getStringByColumnName(rs,"repeat_id"));
		entry.setStart_time(getStringByColumnName(rs,"start_time"));
		entry.setStatut_entry(getStringByColumnName(rs,"statut_entry"));
		entry.setType(getStringByColumnName(rs,"type"));
		entry.setTimestamp(getStringByColumnName(rs,"timestamp"));
		return entry;
	}
	
	private RepeatDTO extractRepeatFromRS(ResultSet rs) throws SQLException {
		RepeatDTO repeat = new RepeatDTO();
		repeat.setBeneficiaire(getStringByColumnName(rs,"beneficiaire"));
		repeat.setBeneficiaire_ext(getStringByColumnName(rs,"beneficiaire_ext"));
		repeat.setCreate_by(getStringByColumnName(rs,"create_by"));
		repeat.setDescription(getStringByColumnName(rs,"description"));
		repeat.setEnd_time(getStringByColumnName(rs,"end_time"));
		repeat.setId(getStringByColumnName(rs,"id"));
		repeat.setName(getStringByColumnName(rs,"name"));
		repeat.setOverload_desc(getStringByColumnName(rs,"overload_desc"));
		repeat.setStart_time(getStringByColumnName(rs,"start_time"));
		repeat.setType(getStringByColumnName(rs,"type"));
		repeat.setTimestamp(getStringByColumnName(rs,"timestamp"));
		repeat.setEnd_date(getStringByColumnName(rs,"end_date"));
		repeat.setJours(getStringByColumnName(rs,"jours"));
		repeat.setRep_num_weeks(getStringByColumnName(rs,"rep_num_weeks"));
		repeat.setRep_opt(getStringByColumnName(rs,"rep_opt"));
		repeat.setRep_type(getStringByColumnName(rs,"rep_type"));
		return repeat;
	}

	private EntryModerateDTO extractEntryModerateFromRS(ResultSet rs) throws SQLException {
		EntryModerateDTO entry = new EntryModerateDTO();
		entry.setBeneficiaire(getStringByColumnName(rs,"beneficiaire"));
		entry.setBeneficiaire_ext(getStringByColumnName(rs,"beneficiaire_ext"));
		entry.setCreate_by(getStringByColumnName(rs,"create_by"));
		entry.setDescription(getStringByColumnName(rs,"description"));
		entry.setEnd_time(getStringByColumnName(rs,"end_time"));
		entry.setEntry_type(getStringByColumnName(rs,"entry_type"));
		entry.setId(getStringByColumnName(rs,"id"));
		entry.setModerate(getStringByColumnName(rs,"moderate"));
		entry.setName(getStringByColumnName(rs,"name"));
		entry.setOption_reservation(getStringByColumnName(rs,"option_reservation"));
		entry.setOverload_desc(getStringByColumnName(rs,"overload_desc"));
		entry.setRepeat_id(getStringByColumnName(rs,"repeat_id"));
		entry.setStart_time(getStringByColumnName(rs,"start_time"));
		entry.setStatut_entry(getStringByColumnName(rs,"statut_entry"));
		entry.setType(getStringByColumnName(rs,"type"));
		entry.setTimestamp(getStringByColumnName(rs,"timestamp"));
		entry.setLogin_moderateur(getStringByColumnName(rs,"login_moderateur"));
		entry.setMotivation_moderation(getStringByColumnName(rs,"motivation_moderation"));
		return entry;
	}
	
	private PeriodeDTO extractPeriodFromRS(ResultSet rs) throws SQLException {
		PeriodeDTO periode = new PeriodeDTO();
		periode.setNom_periode(getStringByColumnName(rs,"nom_periode"));
		periode.setNum_periode(getStringByColumnName(rs,"num_periode"));
		return periode;
	}
	
	private OverloadDTO extractOverloadFromRS(ResultSet rs) throws SQLException {
		OverloadDTO overload = new OverloadDTO();
		overload.setAffichage(getStringByColumnName(rs,"affichage"));
		overload.setConfidentiel(getStringByColumnName(rs,"confidentiel"));
		overload.setFieldlist(getStringByColumnName(rs,"fieldlist"));
		overload.setFieldname(getStringByColumnName(rs,"fieldname"));
		overload.setFieldtype(getStringByColumnName(rs,"fieldtype"));
		overload.setId(getStringByColumnName(rs,"id"));
		overload.setObligatoire(getStringByColumnName(rs,"obligatoire"));
		overload.setOverload_mail(getStringByColumnName(rs,"overload_mail"));
		return overload;
	}
	
	private TypeResaDTO extractTypeAreaFromRS(ResultSet rs) throws SQLException {
		TypeResaDTO typeResa = new TypeResaDTO();
		typeResa.setCouleur(getStringByColumnName(rs,"couleur"));
		typeResa.setDisponible(getStringByColumnName(rs,"disponible"));
		typeResa.setId(getStringByColumnName(rs,"id"));
		typeResa.setOrder_display(getStringByColumnName(rs,"order_display"));
		typeResa.setType_letter(getStringByColumnName(rs,"type_letter"));
		typeResa.setType_name(getStringByColumnName(rs,"type_name"));
		return typeResa;
	}
	
	private String extractIdTypeAreaFromRS(ResultSet rs) throws SQLException {
		return getStringByColumnName(rs,"id_type");
	}

	private String getStringByColumnName(ResultSet rs, String columns) throws SQLException{
		return rs.getString(rs.findColumn(columns));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
