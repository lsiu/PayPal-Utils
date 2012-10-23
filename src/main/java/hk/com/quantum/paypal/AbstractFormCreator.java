package hk.com.quantum.paypal;

import hk.com.quantum.paypal.encrypt.ButtonEncryptor;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractFormCreator {

	/**
	 * Paypal Environments
	 * 
	 * @author lsiu
	 * 
	 */
	public enum ENV {
		LIVE, SANDBOX
	}
	
	private ButtonEncryptor encryptor;
	private String _imgsrc;

	public AbstractFormCreator(ButtonEncryptor encryptor) {
		super();
		this.encryptor = encryptor;
	}

	public AbstractFormCreator(ButtonEncryptor encryptor, String imgsrc) {
		super();
		this.encryptor = encryptor;
		this._imgsrc = imgsrc;
	}

	protected String encrypt(Map<String, String> buttonInfoMap) {
		Map<String, String> map = new LinkedHashMap<String, String>(
				buttonInfoMap);
		
		return encryptor.encryptButtonValue(map);
	}
	
	public String createForm(Map<String, String> buttonInfoMap, ENV env) {
		return createForm(buttonInfoMap, env, null);
	}
	
	protected Map<String, String> updateButtonInfoMap(Map<String, String> buttonInfoMap) {
		return new LinkedHashMap<String, String>(buttonInfoMap);
	}
	

	/**
	 * Create a HTML <code>Form</code> tag with the encrypted button information
	 * provided
	 * 
	 * @param buttonInfoMap
	 *            Button information as described in PayPal reference link
	 *            below.
	 * @param env
	 *            PayPal environment which this PayPal form button will post
	 *            against
	 * @return
	 * 
	 * @See <a href=
	 *      "https://cms.paypal.com/us/cgi-bin/?cmd=_render-content&content_ID=developer/e_howto_html_Appx_websitestandard_htmlvariables"
	 *      >PayPal Technical HTML Variables</a>
	 */
	public String createForm(Map<String, String> buttonInfoMap, ENV env,
			String imgsrc) {

		Map<String, String> map = updateButtonInfoMap(buttonInfoMap);
		String encryptedValue = encrypt(map);

		String stage = "";
		if (env == ENV.SANDBOX)
			stage = "sandbox.";

		String buttonType = "submit";
		if (imgsrc != null || _imgsrc != null) {
			buttonType = "image";
		}
		
		if (imgsrc == null) imgsrc = _imgsrc;

		StringWriter out = new StringWriter();
		out.write("<form action=\"https://www.");
		out.write(stage);
		out.write("paypal.com/cgi-bin/webscr\" method=\"post\">");
        out.write("<input type=\"hidden\" name=\"cmd\" value=\"_s-xclick\">" );
		out.write("<input type=\"" + buttonType + "\" src=\"");
		out.write(imgsrc);
		out.write("\" border=\"0\" name=\"submit\" ");
		out.write("alt=\"Make payments with PayPal - it's fast, free and secure!\">");
		out.write("<input type=\"hidden\" name=\"encrypted\" value=\"");
		out.write(encryptedValue);
		out.write("\">");
		out.write("</form>");
		return out.toString();
	}
}
