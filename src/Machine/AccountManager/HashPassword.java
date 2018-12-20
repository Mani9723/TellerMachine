package Machine.AccountManager;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;

public class HashPassword
{
	private String salt, password,securePassword;

	public HashPassword()
	{
		// DO NOTHING
	}

	public void setHashPassword(String password)
	{
		this.salt = "krsna";
		this.password = password;
		securePassword = makeSecurePassword();
	}

	private String makeSecurePassword()
	{
		try{
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			messageDigest.update(salt.getBytes());
			byte[] bytes = messageDigest.digest(password.getBytes());
			StringBuilder tempString = new StringBuilder();
			for(int i = 0; i<bytes.length;i++){
				tempString.append(Integer.toString((bytes[i]&0xff)+0x100,16).substring(1));
			}
			securePassword = tempString.toString();
		}
		catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		return securePassword;
	}
	private byte[] getSalt() throws NoSuchAlgorithmException
	{
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		byte[] saltArray = new byte[16];
		secureRandom.nextBytes(saltArray);
		return saltArray;
	}

	private String getSecurePassword()
	{
		return securePassword;
	}
	@Override
	public String toString()
	{
		return getSecurePassword();
	}
}
