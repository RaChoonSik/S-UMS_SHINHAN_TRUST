/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.06
 * 설명 : 문자열을 암호화 처리
 */
package kr.co.sict.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Arrays;
 
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
 
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import org.apache.log4j.Logger; 

public class EncryptAccUtil {
	private static Logger logger = Logger.getLogger(EncryptUtil.class);
	
	public static String getEncryptedSHA256(String ciphertext, String passphrase) {
		try {
			final int keySize = 256;
			final int ivSize = 128;
			
			//텍스트를 BASE64 형식으로 디코드 한다.
			byte[] ctBytes = Base64.decodeBase64(ciphertext.getBytes("UTF-8"));
			
			// 솔트를 구한다. (생략된 8비트는 Salted__ 시작되는 문자열이다.) 
			byte[] saltBytes = Arrays.copyOfRange(ctBytes, 8, 16);
			
			// 암호화된 테스트를 구한다.( 솔트값 이후가 암호화된 텍스트 값이다.)
			byte[] ciphertextBytes = Arrays.copyOfRange(ctBytes, 16, ctBytes.length);
			
			// 비밀번호와 솔트에서 키와 IV값을 가져온다.
			byte[] key = new byte[keySize / 8];
			byte[] iv = new byte[ivSize / 8];
			EvpKDF(passphrase.getBytes("UTF-8"), keySize, ivSize, saltBytes, key, iv);
			// 복호화 
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
			byte[] recoveredPlaintextBytes = cipher.doFinal(ciphertextBytes);
			
			return new String(recoveredPlaintextBytes);
		} catch(Exception e) {
			logger.error("EncrytAccUtil getEncryptedSHA256 error = " + e);
			return null;
		}
	}
	
	private static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
		return EvpKDF(password, keySize, ivSize, salt, 1, "MD5", resultKey, resultIv);
	}
	
	private static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, int iterations, String hashAlgorithm, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
		
		keySize = keySize / 32;
		ivSize = ivSize / 32;
		
		int targetKeySize = keySize + ivSize;
		byte[] derivedBytes = new byte[targetKeySize * 4];
		int numberOfDerivedWords = 0;
		byte[] block = null;
		MessageDigest hasher = MessageDigest.getInstance(hashAlgorithm);
		
		while (numberOfDerivedWords < targetKeySize) {
			if (block != null) {
				hasher.update(block);
			}
			hasher.update(password);
			// Salting 
			block = hasher.digest(salt);
			hasher.reset();
			// Iterations : 키 스트레칭(key stretching)  
			for (int i = 1; i < iterations; i++) {
				block = hasher.digest(block);
				hasher.reset();
			}
			System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4, Math.min(block.length, (targetKeySize - numberOfDerivedWords) * 4));
			numberOfDerivedWords += block.length / 4;
		}
		System.arraycopy(derivedBytes, 0, resultKey, 0, keySize * 4);
		System.arraycopy(derivedBytes, keySize * 4, resultIv, 0, ivSize * 4);
		return derivedBytes; // key + iv
	}
	
	public static String getEncryptedPBKDF2(String ciphertext) {
		String retStr = "";
		
		try {
			String passPhrase = "kjbank";
			String salt = "18b00b2fc5f0e0ee40447bba4dabc952"; 
			String iv = "4378110db6392f93e95d5159dabdee9b";
			int iterationCount  = 10000;
			int keySize  = 128;
			
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), Hex.decodeHex(salt.toCharArray()), iterationCount, keySize);
			SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(Hex.decodeHex(iv.toCharArray())));
			byte[] decrypted = cipher.doFinal(Base64.decodeBase64(ciphertext));
			
			retStr =new String(decrypted, "UTF-8");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return retStr;
	}
		
}
