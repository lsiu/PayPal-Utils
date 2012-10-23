package hk.com.quantum.paypal;

import hk.com.quantum.paypal.ButtonEncryptor.ENV;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

public class ButtonEncryptorTest extends AbstractPayPalTest {

	@Test
	public void testCreateForm() throws IOException {

		ButtonEncryptor encryptor = getButtonEncryptor();

		Map<String, String> m = getBaseButtonInfoMap();

		m.put("cmd", "_xclick");
		m.put("amount", "40");
		m.put("item_name", "Test add to cart item description");

		String result = encryptor.createForm(m, ENV.SANDBOX);
		System.out.println(result);
		writeResult(result);
	}
}
