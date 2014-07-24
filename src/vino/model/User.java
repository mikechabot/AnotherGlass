package vino.model;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.javalite.activejdbc.Model;
import org.joda.time.DateTime;

import vino.utils.StringUtils;

public class User extends Model {
	
	private transient UserFacebook userFacebook;
	
	public Date getCreated() {
		return getDate("created_at");
	}
	
	public void setCreated(Date created) {
		set("created_at", created);
	}
	
	public Date getUpdated() {
		return getDate("updated_at");
	}
	
	public void setUpdated(Date updated) {
		set("updated_at", updated);
	}
	
	public boolean isActive() {
		return getBoolean("active");
	}
	
	public void setActive(boolean active) {
		set("active", active);
	}
	
	public String getUsername() {
		return getString("username");
	}
	
	public void setUsername(String username) {
		set("username", username);
	}
	
	public String getName() {
		return getString("name");
	}
	
	public void setName(String name) {
		set("name", name);
	}
	
	public String getDisplay() {
		String display = getName();
		if (display == null) {
			display = getUsername();
		}
		return display;
	}
	
	public String getEmail() {
		return getString("email");
	}
	
	public void setEmail(String email) {
		set("email", email);
	}
	
	public String getPassword() {
		return "******";
	}
	
    public void setPassword(String password) {
    	String salt = StringUtils.generatePseudoRandomToken();
    	set("password_salt", salt);
    	set("password_hash", DigestUtils.sha512Hex(password + salt));
     }	
    
    private String getPasswordSalt() {
    	return getString("password_salt");
    }

    private String getPasswordHash() {
    	return getString("password_hash");
    }

	public boolean authenticate(String password) {
        return (getPasswordHash().equals(DigestUtils.sha512Hex(password + getPasswordSalt())));
    }
	
	public Date getResetExpiration() {
		return getDate("reset_expiration");
	}
	
	public String getResetToken() {
		return getString("reset_token");
	}
	
	public void setAvatarSource(AvatarSource avatarSource) {
		if (avatarSource != null) {
			set("avatar_source", avatarSource.getMnemonic());
		}
	}
		
	public AvatarSource getAvatarSource() {
		String str = getString("avatar_source");
		if (str != null) {
			AvatarSource avatarSource = AvatarSource.lookup(str);
			if (avatarSource != null) {
				return avatarSource;
			}
		}
		return AvatarSource.GRAVATAR;
	}
	
	public void setUserAvatar(UserAvatar userAvatar) {
		if (userAvatar != null) {
			set("useravatars_id", userAvatar.getId());
		}
	}
	
	public UserAvatar getUserAvatar() {
		Long id = getLong("useravatars_id");
		if (id != null) {
			return UserAvatar.findById(id);
		}
		return null;
	}
	
	public String getAvatarUrl() {
		AvatarSource avatarSource = getAvatarSource();
		if (AvatarSource.GRAVATAR == avatarSource) {
			return getGravatarAvatarUrl();
		}
		else if (AvatarSource.FACEBOOK == avatarSource) {
			return getFacebookAvatarUrl();
		}
		else {
			return getLocalAvatarUrl();
		}
	}
	
	public String getFacebookAvatarUrl() {
		return "http://graph.facebook.com/"+getUsername().substring(3)+"/picture?type=large";
	}
	
	public String getGravatarAvatarUrl() {
		return "http://www.gravatar.com/avatar/"+StringUtils.toMD5(getEmail())+"?d=identicon&s=220";
	}
	
	public String getLocalAvatarUrl() {
		return "/avatar/"+getUsername()+".jpg";
	}		
	
	public void setUserFacebook(UserFacebook userFacebook) {
		if (userFacebook != null) {
			this.userFacebook = userFacebook;
			set("userfacebooks_id", userFacebook.getId());
		}
	}
	
	public UserFacebook getUserFacebook() {
		if (userFacebook != null) return userFacebook;
		Long id = getLong("userfacebooks_id");
		if (id != null) {
			return UserFacebook.findById(id);
		}
		return null;
	}
	
	public void initReset() {
        set("reset_expiration", new Timestamp(new DateTime().plusDays(1).toDate().getTime()));
		set("reset_token", StringUtils.generatePseudoRandomToken());
	}
	
	public void clearReset() {
        set("reset_expiration", null);
		set("reset_token", null);	
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("User=[");
		sb.append("id=").append(getId()).append(",");
		sb.append("created=").append(getCreated()).append(",");
		sb.append("updated=").append(getUpdated()).append(",");
		sb.append("active=").append(isActive()).append(",");
		sb.append("username=").append(getUsername()).append(",");
		sb.append("email=").append(getEmail()).append(",");
		sb.append("password=").append(getPassword()).append(",");
		sb.append("resetExpriation=").append(getResetExpiration()).append(",");
		sb.append("resetToken=").append(getResetToken());
		sb.append("]");
		return sb.toString();
	}
	
}
