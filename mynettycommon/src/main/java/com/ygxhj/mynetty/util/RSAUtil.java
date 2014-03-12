package com.ygxhj.mynetty.util;

import java.io.File;
import java.io.PrintWriter;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.naming.ConfigurationException;


public class RSAUtil {

	public static String puKeyFile = "d:\\public.key";
	public static String prKeyFile = "d:\\private.key";
	public static void main(String[] args) {
		try {
			String encryptText = "encryptText";

			// Generate keys
			KeyPair keyPair = RSAUtil.generateKey();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//			RSAPrivateKey privateKey = (RSAPrivateKey)RSAUtil.loadKey(RSAUtil.prKeyFile, 0);
//			RSAPublicKey publicKey = (RSAPublicKey) RSAUtil.loadKey(RSAUtil.puKeyFile, 1);

			byte[] e = RSAUtil.encrypt(publicKey, encryptText.getBytes());
			byte[] de = RSAUtil.decrypt(privateKey, e);
			System.out.println("1111===="+toHexString(e));
			System.out.println("1111===="+new String(e));
			System.out.println("2222===="+toHexString(de));
			System.out.println("3333===="+new String(toBytes(toHexString(de))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static KeyPair generateKey() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024, new SecureRandom());

		KeyPair keyPair = keyPairGen.generateKeyPair();
//		try {
//			saveKey(keyPair, puKeyFile, prKeyFile);
//		} catch (ConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return keyPair;
	}

	public void saveKey(KeyPair keyPair, String publicKeyFile,
			String privateKeyFile) throws ConfigurationException {
		PublicKey pubkey = keyPair.getPublic();
		PrivateKey prikey = keyPair.getPrivate();

		// save public key
		Properties publicConfig = new Properties();
		publicConfig.setProperty("PULIICKEY", toHexString(pubkey.getEncoded()));
		
		try {
			PrintWriter pw = new PrintWriter(new File(publicKeyFile));
			publicConfig.store(pw, "test");
			pw.flush();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// save private key
		Properties privateConfig = new Properties();
		privateConfig.setProperty("PRIVATEKEY",
				toHexString(prikey.getEncoded()));
		try {
			PrintWriter pw = new PrintWriter(new File(privateKeyFile));
			privateConfig.store(pw,"test");
			pw.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param filename
	 * @param type
	 *            ： 1-public 0-private
	 * @return
	 * @throws ConfigurationException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static Key loadKey(String filename, int type)
			throws ConfigurationException, NoSuchAlgorithmException,
			InvalidKeySpecException {
		Properties config = PropertiesUtil.loadProperties(filename);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		if (type == 0) {
			// privateKey
			String privateKeyValue = config.getProperty("PRIVATEKEY");
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					toBytes(privateKeyValue));
			PrivateKey privateKey = keyFactory.generatePrivate(priPKCS8);
			return privateKey;

		} else {
			// publicKey
			String privateKeyValue = config.getProperty("PULIICKEY");
			X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(
					toBytes(privateKeyValue));
			PublicKey publicKey = keyFactory.generatePublic(bobPubKeySpec);
			return publicKey;
		}
	}

	/**
	 * Encrypt String.
	 * 
	 * @return byte[]
	 */
	protected static byte[] encrypt(RSAPublicKey publicKey, byte[] data) {
		if (publicKey != null) {
			try {
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);
				return cipher.doFinal(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Basic decrypt method
	 * 
	 * @return byte[]
	 */
	protected static byte[] decrypt(RSAPrivateKey privateKey, byte[] raw) {
		if (privateKey != null) {
			try {
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.DECRYPT_MODE, privateKey);
				return cipher.doFinal(raw);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}


	
	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEXCHAR[(b[i] & 0xf0) >>> 4]);
			sb.append(HEXCHAR[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static final byte[] toBytes(String s) {
		byte[] bytes;
		bytes = new byte[s.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),
					16);
		}
		return bytes;
	}

	private static char[] HEXCHAR = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' }; 
	
	
}
