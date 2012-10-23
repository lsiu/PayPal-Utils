package hk.com.quantum.paypal;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public abstract class AbstractPayPalTest {

	protected byte[] getPrivateKey() throws IOException {
		return IOUtils.toByteArray(this.getClass().getResourceAsStream(
				"/private/quantum-prvkey.p12"));
	}

	protected byte[] getPublicCert() throws IOException {
		return IOUtils.toByteArray(this.getClass().getResourceAsStream(
				"/private/quantum-pubcert.pem"));
	}

	protected byte[] getPayPalPublicCert() throws IOException {
		return IOUtils.toByteArray(this.getClass().getResourceAsStream(
				"/private/paypal_cert_sandbox.pem"));
	}

	private Properties prop;

	protected synchronized Properties getProperties() {
		if (prop == null) {
			String propstr = "/private/paypal.properties";
			prop = new Properties();
			try {
				prop.load(this.getClass().getResourceAsStream(
						"/private/paypal.properties"));
			} catch (IOException e) {
				throw new IllegalStateException(
						"Cannot find properties from classpath '" + propstr
								+ "'", e);
			}
		}
		return prop;
	}

	protected String getExportPassword() throws IOException {
		return getProperties().getProperty("cert.export.password");
	}

	protected ButtonEncryptor getButtonEncryptor() throws IOException {
		return new ButtonEncryptor(getPrivateKey(), getPublicCert(),
				getExportPassword(), getPayPalPublicCert());
	}

	@SuppressWarnings("serial")
	protected Map<String, String> getBaseButtonInfoMap() {

		return new LinkedHashMap<String, String>() {
			{
				put("cert_id", getProperties().getProperty("cert_id"));
				put("business", getProperties().getProperty("business"));
			}
		};
	}

	private int testCounter = 0;

	protected void writeResult(String result) throws IOException {
		String testCase = this.getClass().getSimpleName() + "_"
				+ (testCounter++);
		System.out.println("++++ Test case: " + testCase);
		System.out.println(result);
		FileUtils.write(new File("target/" + testCase + ".htm"), result);
	}

}
