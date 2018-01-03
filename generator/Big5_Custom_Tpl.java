import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;


/** 編碼器主程式
 */
public class Big5_Custom_Tpl extends Charset {

	private static volatile boolean b2cInitialized = false;
	private static volatile boolean c2bInitialized = false;
	
	private static char[][] b2c = new char[0][];
	private static char[][] c2b = new char[0][];
	
    private static final String BASE_CHARSET = "Big5";
    private static final String NAME = "X-Big5-Custom";
    private static final String[] ALIASES = { "X-Big5_Custom" };
    
    private Charset baseCharset;


    public Big5_Custom_Tpl() {
    	this(NAME, ALIASES);
    }
    
    public Big5_Custom_Tpl(String canonical, String[] aliases) {
        super(canonical, aliases);
        baseCharset = Charset.forName(BASE_CHARSET);
    }
    
    public boolean contains(Charset cs) {
        return this.getClass().isInstance(cs);
    } 
    
        
    public CharsetDecoder newDecoder() {
    	initb2c();
        return new Decoder(this, baseCharset.newDecoder(), b2c);
    }

    public CharsetEncoder newEncoder() {
    	initc2b();
    	return new Encoder(this, baseCharset.newEncoder(), c2b);
    }


	/** 初始化 Big5 到 Unicode 對照表 
	 */
	public static void initb2c() {
		if (b2cInitialized) { return; }
		synchronized (b2c) {
			if (b2cInitialized) { return; }
			/*b2cMappingTable*/
			b2cInitialized = true;
		}
	}
    
	
	/** 初始化 Unicode 到 Big5 對照表 
	 */
    public static void initc2b() {
    	if (c2bInitialized) { return; }
    	synchronized (c2b) {
    		if (c2bInitialized) { return; }
    		/*c2bMappingTable*/
	        c2bInitialized = true;
    	}
    }    
    

    
    
    /*=====================================================*/
    
    /** Decoder 從 Big5 byte[] 轉成 unicode char[] 
     * */
    private class Decoder extends CharsetDecoder {
    	
        /* Big5 到 Unicode 對照表 */
    	private final char[][] b2c;
    	
    	/* JVM 原始的 Big5 解碼器 */
    	private final CharsetDecoder baseDecoder; 

    	
        Decoder(Charset cs, CharsetDecoder baseDecoder, char[][] b2c) {
            super(cs, baseDecoder.averageCharsPerByte(), baseDecoder.maxCharsPerByte());
            this.baseDecoder = baseDecoder;
            this.b2c = b2c;
        }

        
        /** 解碼迭代，先用原始的 Big5 進行解碼，如果無法轉換才進一步使用自訂的解碼表。
         * @see java.nio.charset.CharsetDecoder#decodeLoop(java.nio.ByteBuffer, java.nio.CharBuffer)
         */
        @Override
        protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out) {
        	
            baseDecoder.reset();
            CoderResult result = baseDecoder.decode(in, out, true);
            if(!result.isUnmappable() || in.remaining() < 2){ return result; }
            
            
            /* 無法轉換，進一步使用自訂的解碼表 */
            int pos = in.position();
            int high = in.get(pos) & 0xFF; 
            int low = in.get(pos + 1) & 0xFF; 
            if(high >= b2c.length || b2c[high] == null){ return result; }
            
            
            /* 減去偏移量， mapping table 第一個值為 offset*/
            low -= (short)b2c[high][0]; 
            if(low < 1 || low >= b2c[high].length){ return result; }

            /* 檢查解碼表是否有對應的轉換，沒有就直接回傳沒對應的字元 */
            int j = b2c[high][low];            
            if(j == 0){ return result; }            

            out.put((char)j);        
            
            in.position(pos + 2);
            return decodeLoop(in, out);            
        }

    }


    
    /*=====================================================*/
    
    /** Encoder 從 unicode char[] 轉成 Big5 byte[] 
     * */
    private class Encoder extends CharsetEncoder {
    	
        /* Unicode 到 Big5 對照表 */
    	private final char[][] c2b;
    	
        /* JVM 原始的 Big5 編碼器 */
    	private final CharsetEncoder baseEncoder;

        Encoder(Charset cs, CharsetEncoder baseEncoder, char[][] c2b) {
            super(cs, baseEncoder.averageBytesPerChar(), baseEncoder.maxBytesPerChar());
            this.baseEncoder = baseEncoder;
            this.c2b = c2b;
        }

        
        /** 編碼迭代，先用 JVM 原始的 Big5 進行編碼，如果無法轉換才進一步使用自訂的編碼表。
         * @see java.nio.charset.CharsetDecoder#decodeLoop(java.nio.ByteBuffer, java.nio.CharBuffer)
         */
        @Override
        protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out) {
            baseEncoder.reset();
            CoderResult result = baseEncoder.encode(in, out, true);
            if(!result.isUnmappable() || out.remaining() < 2){ return result; }
            
            
            /* 無法轉換，進一步使用自訂的編碼表 */
            int pos = in.position();
            int index = in.get(pos);
            int high = index >> 8; 
            int low = index & 0xFF; 
            if(high >= c2b.length || c2b[high] == null){ return result; }

            
            /* 減去偏移量， mapping table 第一個值為 offset*/
            low -= (short)c2b[high][0]; 
            if(low < 1 || low >= c2b[high].length){ return result; }

            /* 檢查編碼表是否有對應的轉換，沒有就直接回傳沒對應的字元 */
            int j = c2b[high][low];
            if(j <= 255){ return result; }
            
            out.put((byte)(j >> 8));
            out.put((byte)j);        
            
            in.position(pos + 1);
            return encodeLoop(in, out);
        }
    }

}