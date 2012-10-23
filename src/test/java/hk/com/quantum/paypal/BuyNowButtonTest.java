package hk.com.quantum.paypal;

import hk.com.quantum.paypal.AbstractFormCreator.ENV;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

public class BuyNowButtonTest extends AbstractPayPalTest {

	@Test
	public void testCreateForm() throws IOException {
		Map<String, String> m = getBaseButtonInfoMap();
		m.put("amount", "40");
		m.put("item_name", "Item 1 Name");

		writeResult(new BuyNowButton(getButtonEncryptor(),
				"https://www.sandbox.paypal.com/en_US/i/btn/x-click-but23.gif")
				.createForm(m, ENV.SANDBOX));
	}

}
