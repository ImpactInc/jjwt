/*
 * Copyright (C) 2021 jsonwebtoken.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jsonwebtoken.impl.lang

import org.junit.Test

import static org.junit.Assert.*

class FieldsTest {

    static final Field<String> STRING = Fields.builder(String.class).setId('foo').setName('FooName').build()
    static final Field<Set<String>> STRSET = Fields.builder(String.class).setId('fooSet').setName('FooSet').set().build()
    static final Field<List<String>> STRLIST = Fields.builder(String.class).setId('fooList').setName('FooList').list().build()

    @Test
    void testPrivateCtor() { // for code coverage only
        new Fields()
    }

    @Test
    void testString() {
        assertEquals 'FooName', STRING.getName()
        assertEquals 'foo', STRING.getId()
    }

    @Test
    void testSupports() {
        assertTrue STRING.supports('bar') // any string
    }

    @Test
    void testSupportsNull() {
        assertTrue STRING.supports(null) // null values allowed by default
    }

    @Test
    void testSupportsSet() {
        def set = ['test'] as Set
        assertTrue STRSET.supports(set)
    }

    @Test
    void testSupportsSetNull() {
        assertTrue STRSET.supports(null)
    }

    @Test
    void testSupportsSetEmpty() {
        def set = [] as Set
        assertTrue STRSET.supports(set)
    }

    @Test
    void testSupportsSetWrongElementType() {
        def set = [42] as Set // correct collection type, but wrong element type
        assertFalse STRSET.supports(set)
    }

    @Test
    void testSupportsList() {
        def list = ['test']
        assertTrue STRLIST.supports(list)
    }

    @Test
    void testSupportsListNull() {
        assertTrue STRLIST.supports(null)
    }

    @Test
    void testSupportsListEmpty() {
        def list = [] as List
        assertTrue STRLIST.supports(list)
    }

    @Test
    void testSupportsListWrongElementType() {
        def list = [42] // correct collection type, but wrong element type
        assertFalse STRLIST.supports(list)
    }

    @Test
    void testSupportsFailsForDifferentType() {
        def field = Fields.builder(String.class).setId('foo').setName('fooName').build()
        Object val = 42
        assertFalse field.supports(val)
    }

    @Test
    void testCast() {
        Object val = 'test'
        assertEquals val, STRING.cast(val)
    }

    @Test
    void testCastNull() {
        assertNull STRING.cast(null)
        assertNull STRSET.cast(null)
        assertNull STRLIST.cast(null)
    }

    @Test
    void testCastWrongType() {
        try {
            STRING.cast(42)
            fail()
        } catch (ClassCastException expected) {
            String msg = 'Cannot cast java.lang.Integer to java.lang.String'
            assertEquals msg, expected.getMessage()
        }
    }

    @Test
    void testCastSet() {
        Object set = ['test'] as Set
        assertSame set, STRSET.cast(set)
    }

    @Test
    void testCastSetEmpty() {
        Object set = [] as Set
        assertSame set, STRSET.cast(set)
    }

    @Test
    void testCastSetWrongType() {
        try {
            STRSET.cast(42) // not a set
            fail()
        } catch (ClassCastException expected) {
            String msg = "Cannot cast java.lang.Integer to java.util.Set<java.lang.String>"
            assertEquals msg, expected.getMessage()
        }
    }

    @Test
    void testCastSetWrongElementType() {
        Object set = [42] as Set
        try {
            STRSET.cast(set)
            fail()
        } catch (ClassCastException expected) {
            String msg = "Cannot cast java.util.LinkedHashSet to java.util.Set<java.lang.String>: At least " +
                    "one element is not an instance of java.lang.String"
            assertEquals msg, expected.getMessage()
        }
    }

    @Test
    void testCastList() {
        Object list = ['test']
        assertSame list, STRLIST.cast(list)
    }

    @Test
    void testCastListEmpty() {
        Object list = []
        assertSame list, STRLIST.cast(list)
    }

    @Test
    void testCastListWrongType() {
        try {
            STRLIST.cast(42) // not a list
            fail()
        } catch (ClassCastException expected) {
            String msg = "Cannot cast java.lang.Integer to java.util.List<java.lang.String>"
            assertEquals msg, expected.getMessage()
        }
    }

    @Test
    void testCastListWrongElementType() {
        Object list = [42]
        try {
            STRLIST.cast(list)
            fail()
        } catch (ClassCastException expected) {
            String msg = "Cannot cast java.util.ArrayList to java.util.List<java.lang.String>: At least " +
                    "one element is not an instance of java.lang.String"
            assertEquals msg, expected.getMessage()
        }
    }

    @Test
    void testEquals() {
        def a = Fields.string('foo', "NameA")
        def b = Fields.builder(Object.class).setId('foo').setName("NameB").build()
        //ensure equality only based on id:
        assertEquals a, b
    }

    @Test
    void testHashCode() {
        def a = Fields.string('foo', "NameA")
        def b = Fields.builder(Object.class).setId('foo').setName("NameB").build()
        //ensure only based on id:
        assertEquals a.hashCode(), b.hashCode()
    }

    @Test
    void testToString() {
        assertEquals "'foo' (FooName)", Fields.string('foo', 'FooName').toString()
    }

    @Test
    void testEqualsNonField() {
        def field = Fields.builder(String.class).setId('foo').setName("FooName").build()
        assertFalse field.equals(new Object())
    }

    @Test
    void testBigIntegerBytesNull() {
        assertNull Fields.bytes(null)
    }

    @Test
    void testBytesEqualsWhenBothAreNull() {
        assertTrue Fields.bytesEquals(null, null)
    }

    @Test
    void testBytesEqualsIdentity() {
        assertTrue Fields.bytesEquals(BigInteger.ONE, BigInteger.ONE)
    }

    @Test
    void testBytesEqualsWhenAIsNull() {
        assertFalse Fields.bytesEquals(null, BigInteger.ONE)
    }

    @Test
    void testBytesEqualsWhenBIsNull() {
        assertFalse Fields.bytesEquals(BigInteger.ONE, null)
    }

    @Test
    void testFieldValueEqualsWhenAIsNull() {
        BigInteger a = null
        BigInteger b = BigInteger.ONE
        Field<BigInteger> field = Fields.bigInt('foo', 'bar').build()
        assertFalse Fields.equals(a, b, field)
    }

    @Test
    void testFieldValueEqualsWhenBIsNull() {
        BigInteger a = BigInteger.ONE
        BigInteger b = null
        Field<BigInteger> field = Fields.bigInt('foo', 'bar').build()
        assertFalse Fields.equals(a, b, field)
    }

    @Test
    void testFieldValueEqualsSecretString() {
        String a = 'hello'
        String b = new String('hello'.toCharArray()) // new instance not in the string table (Groovy side effect)
        Field<String> field = Fields.builder(String.class).setId('foo').setName('bar').setSecret(true).build()
        assertTrue Fields.equals(a, b, field)
    }

    @Test
    void testEqualsIdentity() {
        FieldReadable r = new TestFieldReadable()
        assertTrue Fields.equals(r, r, Fields.string('foo', 'bar'))
    }

    @Test
    void testEqualsWhenAIsNull() {
        assertFalse Fields.equals(null, "hello", Fields.string('foo', 'bar'))
    }

    @Test
    void testEqualsWhenAIsFieldReadableButBIsNot() {
        FieldReadable r = new TestFieldReadable()
        assertFalse Fields.equals(r, "hello", Fields.string('foo', 'bar'))
    }
}
