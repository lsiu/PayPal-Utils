package hk.com.quantum.paypal;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.security.Security;
import java.util.Map;

import org.bouncycastle.util.encoders.Base64;

/**
 * Class use to encrypt PayPal buttons dynamically
 * @author lsiu
 *
 */
public class ButtonEncryptor {

	static {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	private String exportPass;

	private byte[] prvkey, pubcertpem, paypalcertpem;

	/**
	 * The <code>byte[]</code> ca
	 * @param privkey
	 * @param pubcertpem
	 * @param exportPass
	 * @param paypalcertpem
	 */
	public ButtonEncryptor(byte[] privkey, byte[] pubcertpem, String exportPass,
			byte[] paypalcertpem) {
		super();
		this.exportPass = exportPass;
		this.prvkey = privkey;
		this.pubcertpem = pubcertpem;
		this.paypalcertpem = paypalcertpem;
	}

	/**
	 * Encrypt the button information for PayPal encrypted button "encrypted"
	 * fields
	 * 
	 * @param buttonInfoMap
	 *            Button information as described in PayPal reference link
	 *            below.
	 * @return Encrypted value in PEM format
	 * 
	 * @See <a href=
	 *      "https://cms.paypal.com/us/cgi-bin/?cmd=_render-content&content_ID=developer/e_howto_html_Appx_websitestandard_htmlvariables"
	 *      >PayPal Technical HTML Variables</a>
	 */
	public String encryptButtonValue(Map<String, String> buttonInfoMap) {

		if (prvkey == null || prvkey.length == 0)
			throw new IllegalArgumentException("Cannot read private key");
		else if (pubcertpem == null || pubcertpem.length == 0)
			throw new IllegalArgumentException(
					"Cannot read public cert (pem) key");
		else if (paypalcertpem == null || paypalcertpem.length == 0)
			throw new IllegalArgumentException(
					"Cannot read paypal cert (pem) key");

		StringBuilder sb = new StringBuilder();
		for (String key : buttonInfoMap.keySet()) {
			sb.append(key).append("=").append(buttonInfoMap.get(key))
					.append("\n");
		}

		// Create the Data
		byte[] data = sb.toString().getBytes();

		// Sign Data with Private Key
		byte[] signed = ContentSigner.signData(prvkey, pubcertpem, exportPass,
				data);

		// Envelop Data
		byte[] pkcs7Bytes = ContentEnveloper.envelopData(paypalcertpem, signed);

		byte[] dataPEM = convertDERToPEM(pkcs7Bytes, "PKCS7");

		return new String(dataPEM);
	}

	private static byte[] convertDERToPEM(byte[] bytes, String headfoot) {
		ByteArrayOutputStream pemStream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(pemStream);

		byte[] stringBytes = Base64.encode(bytes);

		String encoded = new String(stringBytes);

		if (headfoot != null) {
			writer.print("-----BEGIN " + headfoot + "-----\n");
		}

		// write 64 chars per line till done
		int i = 0;
		while ((i + 1) * 64 < encoded.length()) {
			writer.print(encoded.substring(i * 64, (i + 1) * 64));
			writer.print("\n");
			i++;
		}
		if (encoded.length() % 64 != 0) {
			writer.print(encoded.substring(i * 64)); // write remainder
			writer.print("\n");
		}
		if (headfoot != null) {
			writer.print("-----END " + headfoot + "-----\n");
		}
		writer.flush();
		return pemStream.toByteArray();
	}
}
