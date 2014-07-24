package vino.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.shiro.SecurityUtils;

import com.google.gson.Gson;

import vino.Configuration;
import vino.Controller;
import vino.database.Database;
import vino.facebook.FacebookDetails;
import vino.model.AvatarSource;
import vino.model.User;
import vino.model.UserFacebook;
import vino.shiro.FacebookToken;
import vino.utils.WebUtils;

public class FacebookController extends Controller {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(FacebookController.class);
		
	@Override
	protected String basePath() { return "facebook"; }
	
	@Override
	protected void initActions() {
		addAction("/login", new Login());
		addAction("/redirect", new Redirect());
	}

	@Override
	protected Action defaultAction() {
		return new Login();
	}
		
	public class Login extends Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			String baseUrl = WebUtils.baseUrl(request);
			
			OAuthClientRequest oAuthClientRequest = OAuthClientRequest
					   .authorizationProvider(OAuthProviderType.FACEBOOK)
					   .setClientId(Configuration.getInstance().getFacebookApi().getKey())
					   .setRedirectURI(baseUrl+"/facebook/redirect")
					   .buildQueryMessage();
			
			return "redirect:"+oAuthClientRequest.getLocationUri();
		}
	}
		
	public class Redirect extends Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			String baseUrl = WebUtils.baseUrl(request);

			OAuthAuthzResponse oAuthResponse = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
			String facebookAuthCode = oAuthResponse.getCode();
						
			OAuthClientRequest oAuthClientRequest = OAuthClientRequest
	                .tokenProvider(OAuthProviderType.FACEBOOK)
	                .setGrantType(GrantType.AUTHORIZATION_CODE)
	                .setClientId(Configuration.getInstance().getFacebookApi().getKey())
	                .setClientSecret(Configuration.getInstance().getFacebookApi().getSecret())
					.setRedirectURI(baseUrl+"/facebook/redirect")
	                .setCode(facebookAuthCode)
	                .buildQueryMessage();
			
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
 
            //Facebook is not fully compatible with OAuth 2.0 draft 10, access token response is
            //application/x-www-form-urlencoded, not json encoded so we use dedicated response class for that
            //Custom response classes are an easy way to deal with oauth providers that introduce modifications to
            //OAuth 2.0 specification
            GitHubTokenResponse oAuthTokenResponse = oAuthClient.accessToken(oAuthClientRequest, GitHubTokenResponse.class);
 
            String facebookAccessToken = oAuthTokenResponse.getAccessToken();
            Long facebookExpiresIn = oAuthTokenResponse.getExpiresIn();
            
			OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(Configuration.getInstance().getFacebookApi().getUrl())
			         .setAccessToken(facebookAccessToken)
			         .buildQueryMessage();
			
			OAuthResourceResponse oAuthResourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);            
			String json = oAuthResourceResponse.getBody();
			
			FacebookDetails facebookDetails = new Gson().fromJson(json, FacebookDetails.class);
			String username = "!FB"+facebookDetails.getId();
			
			User user = null;
			Database db = null;
			
			try {
				db = new Database();
				db.open();
				
				user = User.findFirst("username = ?", username);
				if (user == null) {
					log.debug("Creating a new user for facebook user ["+username+"]");
					user = new User();
					user.setActive(true);
					user.setUsername(username);
					user.setPassword(username);
					user.setEmail(facebookDetails.getId()+"@facebook.com");
					user.setAvatarSource(AvatarSource.FACEBOOK);										
					user.save();
					
					UserFacebook userFacebook = new UserFacebook();
					userFacebook.setUser(user);
					userFacebook.setAccessToken(facebookAccessToken);
					userFacebook.setAuthCode(facebookAuthCode);
					userFacebook.setExpiresIn(facebookExpiresIn);
					userFacebook.setDetails(facebookDetails);
					userFacebook.save();
					
					user.setUserFacebook(userFacebook);
					user.save();
				}
				
			}
			catch (Exception e) {
				log.debug("There was a SQL error while looking up facebook user [" + username + "]", e);
				throw new ServletException("There was a SQL error while looking up facebook user [" + username + "]", e);
			}
			finally {
				if (db != null) {
					db.close();
				}
			}
			
			SecurityUtils.getSubject().login(new FacebookToken(facebookDetails, facebookAuthCode, facebookAccessToken, facebookExpiresIn));
			
			return "redirect:/index.jsp";
		}
	}	
}