package hk.com.quantum.paypal;

import java.util.Map;

public class BuyNowButton extends AbstractFormCreator {

	public BuyNowButton(ButtonEncryptor encryptor, String imgsrc) {
		super(encryptor, imgsrc);
	}

	public BuyNowButton(ButtonEncryptor encryptor) {
		super(encryptor);
	}
	
	@Override
	protected Map<String, String> updateButtonInfoMap(
			Map<String, String> buttonInfoMap) {
		Map<String, String> map = super.updateButtonInfoMap(buttonInfoMap);
		
		map.put("cmd", "_xclick");
		
		return map;
	}

}
