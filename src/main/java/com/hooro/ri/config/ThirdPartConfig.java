package com.hooro.ri.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThirdPartConfig
{
	public static String SMS_SEND_URL;

	public static String SMS_APP_KEY;
	
	public static long PUSH_MOBILE_APPID;

	public static String PUSH_MOBILE_SECRE_KEY;
	
	public static long PUSH_APP_APPID ;

	public static String PUSH_APP_SECRE_KEY;
	
	public static long PUSH_APP_APPID_IOS ;

	public static String PUSH_APP_SECRE_KEY_IOS;
	
	public static String RSA_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\r\n" + 
			"MIIEpQIBAAKCAQEA1TrGNYtUf1NKCctha/SFjRnQWGd4BcNng1UijYl61GlzTdPc\r\n" + 
			"33+ahs5A+zad1nmOXEakJ3VBZWJnY8ny0xDYLxMfStc+izDExheQ0QA5ME9Nvnoz\r\n" + 
			"euPO9yRZMCHsSAl/4LBYyQggD8HI6AiyT1MbVGPkM2aB2s92SHiwTe+N8yCIXU85\r\n" + 
			"xA5PeShvqbTZ+Z+hK65CxW/iKQ6TWdhYo/rCKvLRwZwmPSiowvLyvYyoMPLJ7RPZ\r\n" + 
			"IGMiuLkXdqdgV4/Y09PRp5HcnqxutdInLw8AYlXP9YGQlRmkr1jvjbZ8tjRT6kzL\r\n" + 
			"z4h3hY7+tLlLvHOc+Ej5pjlbaVSX9PaLGHYYHQIDAQABAoIBAG4Mh3/bCSEtu2iS\r\n" + 
			"rxUofkT0rbLys99FmocNyWeDLQGz6u6gCb7P/NDWYnNFHPE0i2TUkKRMwBOzVekK\r\n" + 
			"sWh9qRWiqOPXc1AjO2uBfhxgISPtyhIkv60NJiQnfmB5IZmMTZOJcElRCDsZqLcY\r\n" + 
			"xe5q1hAX/g8JOLvVlxaQM24F1R1FG8uhDw8J950xU26yVlQ9ffYy7+ks51ZiGTFY\r\n" + 
			"ysRCmTnrIa9+zDV1taDxYYh29gvl3uoUrpFCYEhVW44YfXwJ3qiJDkeCfrKyuILq\r\n" + 
			"7jBWY2GnP7D0Yb/O8ct3gPaWdC8fIRJfyBJllmMfut7clJC8Vq5bucLeDBNC2xNe\r\n" + 
			"dkJzjAECgYEA8e35xNLdhXtmxnUwjF+I8j5JPMBELx+UyduTNJwtZvsepX/rSO+7\r\n" + 
			"0DsjDJRVf0A5QZWp0dHA68G+otRUTx8tWlgEyL4sG3XFxUpvK7+AIzDdgDbHlpZB\r\n" + 
			"DotY57MLOikOQtzdxoOcz/2grKgRCIpr6b6adSPVmTKNQ6hwhW0fqOUCgYEA4aF9\r\n" + 
			"8Yz5hKtXsiqLC+rU6SWyuMoZaOaHO2t8Jf2HYYKgYF6CJ0LBC7RSqmKsJr0rCkeF\r\n" + 
			"78TwT5ZrxfxE7qxXJqt2dgQndqVB4mnoTVVdypq+ibBI+N+3/8enSxPkViB/zGD8\r\n" + 
			"BSiy6hGSizfVKegIXtxMIAJUoST8Qu9T0J8nVtkCgYEAgDWYdwk0oieaeLPkLY/3\r\n" + 
			"eEEv+MT/nWWEKVF2+puFqByOyjA3VWjOxHSCh4kYoh47+ZjB7VLWhVogmBQkNeYU\r\n" + 
			"19rrdiLqJwdL7tkafzm2Q3ADAo6FSTWJdpa8X9XPrlvRlfeMhEZ9VPBZNQbAOCF7\r\n" + 
			"PN1o54k9CjnzCRb7wonpDE0CgYEAjQKecV3HezCSSBjqeXIzydnzjiVQKA0aqvoL\r\n" + 
			"xrph54D5tgCamurPNv9lQnMUX8mNooj8ndKY1USO6lc0xW0TsmkqSati9nVlbZj+\r\n" + 
			"w4X8beiz990iXdeDaOlgFj3CUElTVWmvV5IN7tc4nKUyJVSMFqzbLo4GUgeJWFO2\r\n" + 
			"MAWnUlECgYEAiOZ9VT7zzwe+r3xGChRofuzyzJgPHu9Z1+ehBWe1hjHHBAQZJnfm\r\n" + 
			"FIEofuBEcNHvMB7wpZ1AAkkqM2J7MMdh0Pe3oieciVXOtKAYh9AAwErl/SjMvETH\r\n" + 
			"Km3eqenju9Ys8gnArdT5TwZ2QTtuV1CL052zJLC2ktBj9h5uXLgKmbg=\r\n" + 
			"-----END RSA PRIVATE KEY-----\r\n";
	
	public static String RSA_PUBLIC_KEY = "﻿-----BEGIN PUBLIC KEY-----\r\n" + 
			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvwCjy+wssVSJ3TvahsNU\r\n" + 
			"tpRJDv8wITHmfgn89TwZZP2rn8owC5ms8EgoKeliKtfR/P3Ddpsq8Q6xV9DzWvj7\r\n" + 
			"xZoo8pm6YLSWFGtHY3PgGirCbeIgVG0fcJdNpm5TpYB+38U/CAqhtp/HK9cKkT9o\r\n" + 
			"8j4Swwh1WTFdolXCpgL1RDz9328j5Ikgc8PS0JGoiFrQJGb2b62umIsXoEvlyRwZ\r\n" + 
			"uaW6495uxaQseu7ty6VohGqOuCaEM+SBrKC65ffzNKPM98X996detLIb8vGhsTUr\r\n" + 
			"S6nfvlLrrQcVprdy6+RyRvCEdOx2KDcPgO9FmLj0um7BNqkxPZJJhzYfiULOlfq4\r\n" + 
			"UwIDAQAB\r\n" + 
			"-----END PUBLIC KEY-----";

	@Value("${3rd.smsSendURL}")
	public void setSMS_SEND_URL(String sMS_SEND_URL) {
		ThirdPartConfig.SMS_SEND_URL = sMS_SEND_URL;
	}

	@Value("${3rd.smsAppKey}")
	public void setSMS_APP_KEY(String sMS_APP_KEY) {
		ThirdPartConfig.SMS_APP_KEY = sMS_APP_KEY;
	}
	

	@Value("${3rd.pushMobileAppId}")
	public void setPUSH_MOBILE_APPID(long PUSH_MOBILE_APPID) {
		ThirdPartConfig.PUSH_MOBILE_APPID = PUSH_MOBILE_APPID;
	}

	@Value("${3rd.pushAppAppId}")
	public void setPUSH_APP_APPID(long pUSH_APP_APPID) {
		ThirdPartConfig.PUSH_APP_APPID = pUSH_APP_APPID;
	}

	@Value("${3rd.pushMobileSecretKey}")
	public void setPUSH_MOBILE_SECRE_KEY(String pUSH_MOBILE_SECRE_KEY) {
		ThirdPartConfig.PUSH_MOBILE_SECRE_KEY = pUSH_MOBILE_SECRE_KEY;
	}

	@Value("${3rd.pushAppSecretKey}")
	public void setPUSH_APP_SECRE_KEY(String pUSH_APP_SECRE_KEY) {
		ThirdPartConfig.PUSH_APP_SECRE_KEY =pUSH_APP_SECRE_KEY;
	}

	@Value("${3rd.pushAppAppIdIos}")
	public  void setPUSH_APP_APPID_IOS(long pUSH_APP_APPID_IOS) {
		ThirdPartConfig.PUSH_APP_APPID_IOS = pUSH_APP_APPID_IOS;
	}

	@Value("${3rd.pushAppSecretKeyIos}")
	public  void setPUSH_APP_SECRE_KEY_IOS(String pUSH_APP_SECRE_KEY_IOS) {
		ThirdPartConfig.PUSH_APP_SECRE_KEY_IOS = pUSH_APP_SECRE_KEY_IOS;
	}


}
