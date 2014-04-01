package vino.shiro;

import org.apache.shiro.authc.AuthenticationToken;

import vino.facebook.FacebookDetails;

public class FacebookToken implements AuthenticationToken {
	 
	private static final long serialVersionUID = 1L;

	private FacebookDetails facebookDetails;
	private String facebookAuthCode;
	private String facebookAccessToken;
	private Long facebookExpiresIn;
 	
	public FacebookToken(FacebookDetails facebookDetails, String facebookAuthCode, String facebookAccessToken, Long facebookExpiresIn){
		this.facebookDetails = facebookDetails;
		this.facebookAuthCode = facebookAuthCode;
		this.facebookAccessToken = facebookAccessToken;
		this.facebookExpiresIn = facebookExpiresIn;
	}

	@Override
	public Object getPrincipal() {
		return "!FB"+facebookDetails.getId();
	}
	 
	@Override
	public Object getCredentials() {
		return null;
	}

	public FacebookDetails getFacebookDetails() {
		return facebookDetails;
	}

	public String getFacebookAuthCode() {
		return facebookAuthCode;
	}

	public String getFacebookAccessToken() {
		return facebookAccessToken;
	}

	public Long getFacebookExpiresIn() {
		return facebookExpiresIn;
	}
	
}