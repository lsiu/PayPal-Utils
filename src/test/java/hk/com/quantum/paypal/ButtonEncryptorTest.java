package hk.com.quantum.paypal;

import hk.com.quantum.paypal.ButtonEncryptor.ENV;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class ButtonEncryptorTest {

	@Test
	public void testCreateForm() throws IOException {
		byte[] prvkey = IOUtils.toByteArray(ButtonEncryptor.class
				.getResourceAsStream("/private/quantum-prvkey.p12"));
		byte[] pubcertpem = IOUtils.toByteArray(ButtonEncryptor.class
				.getResourceAsStream("/private/quantum-pubcert.pem"));
		byte[] paypalcertpem = IOUtils.toByteArray(ButtonEncryptor.class
				.getResourceAsStream("/private/paypal_cert_sandbox.pem"));
		String exportPass = IOUtils.toString(ButtonEncryptor.class
				.getResourceAsStream("/private/exportPass.txt"));
		
		ButtonEncryptor encryptor = new ButtonEncryptor(prvkey, pubcertpem,
				exportPass, paypalcertpem);

		@SuppressWarnings("serial")
		Map<String, String> buttonInfoMap = new LinkedHashMap<String, String>() {
			{
				put("cert_id", "JXDJU3VHRCFP4");
				put("cmd", "_xclick");
				put("business", "seller_1350982484_biz@quantum.com.hk");
				put("amount", "40");
				put("item_name", "Test add to cart item description");
				put("upload", "1");
			}
		};

		String result = encryptor.createForm(buttonInfoMap, ENV.SANDBOX);
		System.out.println(result);
		FileUtils.write(new File("target/test_paypal_button.htm"), result);
	}
}
