package hk.com.quantum.paypal.encrypt;

import hk.com.quantum.paypal.AbstractPayPalTest;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

public class ButtonEncryptorTest extends AbstractPayPalTest {

	@Test
	public void testEncrypt() throws IOException {

		Map<String, String> m = getBaseButtonInfoMap();
		m.put("cmd", "_xclick");
		m.put("amount", "40");
		m.put("item_name", "Test add to cart item description");

		System.out.println(getButtonEncryptor().encryptButtonValue(m));
	}
}
