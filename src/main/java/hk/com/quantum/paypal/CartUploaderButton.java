package hk.com.quantum.paypal;

import java.util.Map;

public class CartUploaderButton extends AbstractFormCreator {

	public CartUploaderButton(ButtonEncryptor encryptor) {
		super(encryptor);
	}
	
	@Override
	protected Map<String, String> updateButtonInfoMap(
			Map<String, String> buttonInfoMap) {
		
		Map<String, String> map = super.updateButtonInfoMap(buttonInfoMap);
		
		map.put("cmd", "_cart");
		map.put("upload", "1");
		
		return map;
	}

}
