package bph.entities.qtr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;

@Entity
@Table(name = "quarters_log")
public class KuaLog {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "action")
	private int action;
	
	@Column(name = "time_stamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;
	
	@Column(name = "table_name")
	private String tableName;
	
	@ManyToOne
	@JoinColumn(name = "id_log")
	private Users usersLog;

	@Column(name = "id_reference")
	private String idReference;
	
	public KuaLog() {
		setId(UID.getUID());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getActionDetail() {
		String actionDetail = "";
		
		if ( getAction() == 1 ) {
			actionDetail = "INSERT";
		} else if ( getAction() == 2 ) {
			actionDetail = "UPDATE";
		} else if ( getAction() == 3 ) {
			actionDetail = "DELETE";
		}
		
		return actionDetail;
	}
	
	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Users getUsersLog() {
		return usersLog;
	}

	public void setUsersLog(Users usersLog) {
		this.usersLog = usersLog;
	}

	public String getIdReference() {
		return idReference;
	}

	public void setIdReference(String idReference) {
		this.idReference = idReference;
	}

}
