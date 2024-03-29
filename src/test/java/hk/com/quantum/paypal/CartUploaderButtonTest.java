package hk.com.quantum.paypal;

import hk.com.quantum.paypal.AbstractFormCreator.ENV;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

public class CartUploaderButtonTest extends AbstractPayPalTest {

	@Test
	public void testEncrypt() throws IOException {
		
		Map<String, String> m = getBaseButtonInfoMap();
		m.put("amount_1", "40");
		m.put("item_name_1", "Item 1 Name");
		m.put("quantity_1", "3");
		m.put("amount_2", "4");
		m.put("item_name_2", "Item 2 Name");
		m.put("quantity_2", "48");

		writeResult(new CartUploaderButton(getButtonEncryptor()).createForm(m,
				ENV.SANDBOX));
	}
}
