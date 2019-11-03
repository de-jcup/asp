/*
 * Copyright 2019 Albert Tregnaghi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */
package de.jcup.asp.core;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.jcup.asp.core.CryptoAccess.DecryptionException;

public class CryptoAccessTest {

    @Rule
    public ExpectedException expected = ExpectedException.none();
    
    @Test
    public void encrypted_string_is_not_equal_to_given_one() {
        /* prepare */
        CryptoAccess cryptoAccess = new CryptoAccess();
        
        /* execute */
        String encrypted = cryptoAccess.encrypt("hello world");
        
        /* test */
        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty());
        assertFalse(encrypted.equals("hello world"));

    }
    
    @Test
    public void encrypted_string_can_be_reversed_by_another_crypto_access_with_same_key() throws Exception {
        /* prepare */
        CryptoAccess cryptoAccess = new CryptoAccess();
        String key = cryptoAccess.getSecretKey();
        String encrypted = cryptoAccess.encrypt("hello world");
        
        /* execute */
        CryptoAccess cryptoAccess2 = new CryptoAccess(key);
        String decrypted = cryptoAccess2.decrypt(encrypted);
        
        /* test */
        assertNotNull(encrypted);
        assertEquals("hello world",decrypted);

    }
    
    @Test
    public void two_crypto_access_do_not_have_same_secret_key() {
        /* prepare */
        CryptoAccess cryptoAccess = new CryptoAccess();
        CryptoAccess cryptoAccess2 = new CryptoAccess();
        
        /* test */
        assertNotEquals(cryptoAccess.getSecretKey(),cryptoAccess2.getSecretKey());

    }
    
    @Test
    public void encrypted_string_can_be_reversed_by_another_crypto_access_with_wrong_key() throws Exception{
        /* prepare */
        CryptoAccess cryptoAccess = new CryptoAccess();
        String encrypted = cryptoAccess.encrypt("hello world");
        expected.expect(DecryptionException.class);
        
        /* execute */
        CryptoAccess cryptoAccess2 = new CryptoAccess();
        String decrypted = cryptoAccess2.decrypt(encrypted);
        
        /* test */
        assertNotNull(encrypted);
        assertNotEquals("hello world",decrypted);

    }
    
    @Test
    public void decrypted_string_support_UTF8_characters() throws Exception {
        /* prepare */
        CryptoAccess cryptoAccess = new CryptoAccess();
        String key = cryptoAccess.getSecretKey();
        String encrypted = cryptoAccess.encrypt("héllo world");
        
        /* execute */
        CryptoAccess cryptoAccess2 = new CryptoAccess(key);
        String decrypted = cryptoAccess2.decrypt(encrypted);
        
        /* test */
        assertNotNull(encrypted);
        assertEquals("héllo world",decrypted);

    }
    

}
