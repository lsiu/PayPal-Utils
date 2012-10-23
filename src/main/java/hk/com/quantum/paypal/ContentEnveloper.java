package hk.com.quantum.paypal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.RecipientInfoGenerator;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.operator.OutputEncryptor;

class ContentEnveloper {

	private static CertificateFactory getCerficationFactory() {
		try {
			return CertificateFactory.getInstance("X509", "BC");
		} catch (CertificateException e) {
			throw new RuntimeException(e);
		} catch (NoSuchProviderException e) {
			throw new RuntimeException(e);
		}
	}

	private static CMSEnvelopedDataGenerator getEnvelopedDataGenerator(
			byte[] paypalcertpem) {

		CertificateFactory cf = getCerficationFactory();

		try {
			// Read the PayPal Cert
			X509Certificate payPalCert = (X509Certificate) cf
					.generateCertificate(new ByteArrayInputStream(paypalcertpem));

			CMSEnvelopedDataGenerator envGenerator = new CMSEnvelopedDataGenerator();

			RecipientInfoGenerator recipientGenerator = new JceKeyTransRecipientInfoGenerator(
					payPalCert);
			envGenerator.addRecipientInfoGenerator(recipientGenerator);

			return envGenerator;
		} catch (CertificateException e) {
			throw new Error("Program Error: " + e.getMessage(), e);
		}
	}

	static byte[] envelopData(byte[] paypalcertpem, byte[] signedData) {
		CMSEnvelopedDataGenerator envGenerator = getEnvelopedDataGenerator(paypalcertpem);
		try {
			OutputEncryptor encryptor = new JceCMSContentEncryptorBuilder(
					CMSAlgorithm.DES_EDE3_CBC).setProvider("BC").build();
			CMSEnvelopedData envData = envGenerator.generate(
					new CMSProcessableByteArray(signedData), encryptor);
			return envData.getEncoded();
		} catch (CMSException e) {
			throw new Error("Program Error: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new Error("Program Error: " + e.getMessage(), e);
		}
	}

}
