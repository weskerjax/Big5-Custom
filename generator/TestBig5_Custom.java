import org.junit.Assert;
import org.junit.Test;

public class TestBig5_Custom {

	public static void main(String[] args) throws Throwable {	
    	TestBig5_Custom test = new TestBig5_Custom();
    	test.charTest2();
    }

    
	@Test
    public void decodeTest() throws Exception {
    	byte[] source = { 
			(byte)0xa4, (byte)0xa4, 
			(byte)0x91, (byte)0x78, 
			(byte)0x91, (byte)0x78, 
			(byte)0xa4, (byte)0xa4, 
		};
    	String result = new String(source, "X-Big5-Custom");

    	Assert.assertEquals("中譱譱中", result);
    }      
	
	
    @Test
    public void convertTest() throws Throwable {
    	String source = "中譱譱中中";
    	
    	byte[] bytes = source.getBytes("X-Big5-Custom");
	    for (byte b : bytes) {
	    	System.out.printf("%x ", b);
	    }
	    System.out.printf("\n");
	    
	    String result = new String(bytes, "X-Big5-Custom");
	    
	    Assert.assertEquals(source, result);
        
    }	
    
    
    @Test
    public void charTest() throws Throwable {
        
        String source = "隆";
        
        byte[] bytes = source.getBytes("X-Big5-Custom");
        for (byte b : bytes) {
            System.out.printf("%x ", b);
        }
        System.out.printf("\n");
    }
    
    @Test
    public void charTest2() throws Throwable {
        
        String source = "譱";
        
        byte[] bytes = source.getBytes("X-Big5-Custom");
        for (byte b : bytes) {
            System.out.printf("%x ", b);
        }
        System.out.printf("\n");
    }

}
