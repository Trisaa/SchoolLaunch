package com.link.schoolunch.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import com.link.schoolunch.model.ShopOrder;
import com.link.schoolunch.model.Version;

import android.os.Environment;

public class Utils {

	public static boolean FIRST_LOGIN;
	public static Version VERSION;
	public static Version NEW_VERSION;
	public static String TOKEN = "";
	public static HashMap<Integer , ShopOrder> Shop_list = new HashMap<Integer , ShopOrder>();
	
	public final static String PATH = Environment.getExternalStorageDirectory().getPath();
	public final static String FILE_PATH 	= PATH + "/Schoolunch/file/";
	public final static String IMG_PATH 	= PATH + "/Schoolunch/image/";
	public final static String CACHE_PATH 	= PATH + "/Schoolunch/cache/";
	
	public final static int CLIENT_TYPE = 2;
	
	
	public final static int NETWORK_AVALIABLE		= 1;
	public final static int NETWORK_UNREACHED		= 2;
	
	public final static int MSG_SERVICE_CALLBACK = 10;
	
	public final static int SERVICE_MAIN 			= 21;
	public final static int SERVICE_PRODUCE			= 22;
	public final static int SERVICE_ORDER	 		= 23;
	
	public final static String TAG_SHOPLIST_FRAGMENT = "tag_shop_list_fragment";
	public final static String TAG_MYORDER_FRAGMENT = "tag_my_order_fragment";
	public final static String TAG_DRAWER_FRAGMENT = "tag_drawer_fragment";
	public final static String TAG_HELP_FRAGMENT = "tag_help_fragment";
	
	
	public static void init() {
		VERSION = new Version();
		VERSION.version = "1.2";
		File file = new File( FILE_PATH );
		if( !file.exists() )
			file.mkdirs();
		file = new File( IMG_PATH );
		if( !file.exists() )
			file.mkdirs();
		file = new File( CACHE_PATH );
		if( !file.exists() )
			file.mkdirs();
		
		
		Properties pro = new Properties();
		try {
			file = new File( CACHE_PATH + "cache.properties" );
			if( !file.exists() ) {
				pro.load( Utils.class.getResourceAsStream("/assets/cache.properties"));
				
				FileOutputStream s = new FileOutputStream( CACHE_PATH + "cache.properties", false);
				pro.store(s, "");
			}
			pro.load( new FileInputStream( file ) );
			FIRST_LOGIN = Boolean.valueOf( pro.get("first_login").toString() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void setFirstLogin( boolean flag ) {
		Properties pro = new Properties();
		try {
			pro.setProperty( "first_login", flag + "" );  
			FileOutputStream s = new FileOutputStream(CACHE_PATH + "cache.properties", false);
			pro.store(s, "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
