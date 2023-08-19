package jp.co.sega.allnet.auth.serialize;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.junit.Test;

@SuppressWarnings("serial")
public class ObjectSerializeUtilsTest implements Serializable {

    @Test
    public void testSerialize() {
        Dummy dummy = new Dummy();
        dummy.str = "rewrite";
        String base64 = ObjectSerializeUtils.serialize(dummy);
        assertNotNull(base64);
        System.out.println(base64);

        Dummy deserialized = ObjectSerializeUtils.deserialize(base64,
                Dummy.class);
        assertEquals("rewrite", deserialized.str);
    }

    @Test
    public void testSerializeBoolean() {
        Dummy dummy = new Dummy();
        dummy.str = "rewrite";
        String base64_true = ObjectSerializeUtils.serialize(dummy, true);
        assertNotNull(base64_true);
        System.out.println(base64_true);
        String base64_false = ObjectSerializeUtils.serialize(dummy, false);
        assertNotNull(base64_false);
        System.out.println(base64_false);

        assertTrue(base64_true.length() < base64_false.length());

        Dummy deserialized_false = ObjectSerializeUtils.deserialize(
                base64_false, Dummy.class, false);
        assertEquals("rewrite", deserialized_false.str);

        Dummy deserialized_true = ObjectSerializeUtils.deserialize(base64_true,
                Dummy.class, true);
        assertEquals(deserialized_false.str, deserialized_true.str);
    }

    private class Dummy implements Serializable {
        private String str = "init";
    }

}
