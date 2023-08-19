/**
 * 
 */
package jp.co.sega_am2.crypt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author TsuboiY
 * 
 */
public class CryptExTest {

    /**
     * Test method for
     * {@link jp.co.sega_am2.crypt.CryptEx#encodeName(java.lang.String)}.
     */
    @Test
    public final void testEncodeName() {
        CryptEx crypt = new CryptEx();
        crypt.setCryptKey(11312, 0, 0);
        String enc = crypt.encodeValue("SBTY");
        assertEquals("BhHT9qQT", enc);
    }

}
