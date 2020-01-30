/*
# These sample codes are provided for information purposes only. It does not imply any recommendation or endorsement by anyone.
  These sample codes are provided for FREE, and no additional support will be provided for these sample pages. 
  There is no warranty and no additional document. USE AT YOUR OWN RISK.
 */

package bph.integrasi.fpx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;

public class FPXPkiImplementation {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public static String signData(String pvtKeyFileName, String dataToSign,
			String signatureAlg) throws IOException, NoSuchAlgorithmException,
			NoSuchProviderException, InvalidKeyException, SignatureException {
		PrivateKey privateKey = getPrivateKey(pvtKeyFileName);
		Signature signature = Signature.getInstance(signatureAlg, "BC");
		signature.initSign(privateKey);

		signature.update(dataToSign.getBytes());
		byte[] signatureBytes = signature.sign();

		return byteArrayToHexString(signatureBytes);

	}

	public static boolean verifyData(String pubKeyFileName,
			String calcCheckSum, String checkSumFromMsg, String signatureAlg)
			throws NoSuchAlgorithmException, NoSuchProviderException,
			IOException, InvalidKeySpecException, InvalidKeyException,
			SignatureException, CertificateException {
		PublicKey pubKey = getPublicKey(pubKeyFileName);
		Signature verifier = Signature.getInstance(signatureAlg, "BC");
		verifier.initVerify(pubKey);

		verifier.update(calcCheckSum.getBytes());
		return verifier.verify(HexStringToByteArray(checkSumFromMsg));
	}

	private static PublicKey getPublicKey(String pubKeyFileName)
			throws CertificateException, IOException {
		InputStream inStream = new FileInputStream(pubKeyFileName);
		CertificateFactory certFactory = CertificateFactory
				.getInstance("X.509");
		X509Certificate cert = (X509Certificate) certFactory
				.generateCertificate(inStream);
		inStream.close();
		return (RSAPublicKey) cert.getPublicKey();
	}

	private static PrivateKey getPrivateKey(String pvtKeyFileName)
			throws IOException {
		FileReader pvtFileReader = getPVTKeyFile(new File(pvtKeyFileName));
		PEMReader pvtPemReader = getPvtPemReader(pvtFileReader);
		KeyPair keyPair = (KeyPair) pvtPemReader.readObject();

		pvtFileReader.close();
		pvtFileReader = null;
		pvtPemReader.close();
		pvtPemReader = null;
		return keyPair.getPrivate();
	}

	private static FileReader getPVTKeyFile(File pvtFile) {
		FileReader pvtFileReader = null;
		try {
			pvtFileReader = new FileReader(pvtFile);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			pvtFileReader = null;
		}

		return pvtFileReader;
	}

	private static PEMReader getPvtPemReader(Reader pvtFile) {
		return new PEMReader(pvtFile);
	}

	static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F' };

	public static String byteArrayToHexString(byte b[]) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static byte[] HexStringToByteArray(String strHex) {
		byte bytKey[] = new byte[(strHex.length() / 2)];
		int y = 0;
		String strbyte;
		for (int x = 0; x < bytKey.length; x++) {
			strbyte = strHex.substring(y, (y + 2));
			if (strbyte.equals("FF")) {
				bytKey[x] = (byte) 0xFF;
			} else {
				bytKey[x] = (byte) Integer.parseInt(strbyte, 16);
			}
			y = y + 2;
		}
		return bytKey;
	}

}