package org.eclipse.jetty.websocket.ab;

import static org.hamcrest.Matchers.*;

import java.nio.ByteBuffer;

import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.websocket.ByteBufferAssert;
import org.eclipse.jetty.websocket.api.WebSocketBehavior;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.frames.BinaryFrame;
import org.eclipse.jetty.websocket.generator.Generator;
import org.eclipse.jetty.websocket.parser.FrameParseCapture;
import org.eclipse.jetty.websocket.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Binary Message Spec testing the {@link Generator} and {@link Parser}
 */
public class ABCase1_2
{
    private WebSocketPolicy policy = WebSocketPolicy.newServerPolicy();

    @Test
    public void testGenerate125ByteBinaryCase1_2_2()
    {
        int length = 125;

        ByteBuffer bb = ByteBuffer.allocate(length);

        for ( int i = 0 ; i < length ; ++i)
        {
            bb.put("*".getBytes());
        }

        bb.flip();

        BinaryFrame binaryFrame = new BinaryFrame( BufferUtil.toArray(bb) );
        binaryFrame.setFin(true);

        Generator generator = new Generator(policy);

        ByteBuffer actual = ByteBuffer.allocate(length + 11);

        generator.generate(actual, binaryFrame);

        ByteBuffer expected = ByteBuffer.allocate(length + 5);

        expected.put(new byte[]
                { (byte)0x82 });

        byte b = 0x00; // no masking
        b |= length & 0x7F;
        expected.put(b);

        for ( int i = 0 ; i < length ; ++i )
        {
            expected.put("*".getBytes());
        }

        actual.flip();
        expected.flip();

        ByteBufferAssert.assertEquals("buffers do not match",expected,actual);

    }

    @Test
    public void testGenerate126ByteBinaryCase1_2_3()
    {
        int length = 126;

        ByteBuffer bb = ByteBuffer.allocate(length);

        for ( int i = 0 ; i < length ; ++i)
        {
            bb.put("*".getBytes());
        }

        bb.flip();

        BinaryFrame binaryFrame = new BinaryFrame(BufferUtil.toArray(bb));
        binaryFrame.setFin(true);

        Generator generator = new Generator(policy);

        ByteBuffer actual = ByteBuffer.allocate(length + 11);

        generator.generate(actual, binaryFrame);

        ByteBuffer expected = ByteBuffer.allocate(length + 5);

        expected.put(new byte[]
                { (byte)0x82 });

        byte b = 0x00; // no masking
        b |= length & 0x7E;
        expected.put(b);

        //expected.put((byte)((length>>8) & 0xFF));
        //expected.put((byte)(length & 0xFF));
        expected.putShort((short)length);

        for ( int i = 0 ; i < length ; ++i )
        {
            expected.put("*".getBytes());
        }

        actual.flip();
        expected.flip();

        ByteBufferAssert.assertEquals("buffers do not match",expected,actual);

    }

    @Test
    public void testGenerate127ByteBinaryCase1_2_4()
    {
        int length = 127;

        ByteBuffer bb = ByteBuffer.allocate(length);

        for ( int i = 0 ; i < length ; ++i)
        {
            bb.put("*".getBytes());

        }

        bb.flip();

        BinaryFrame binaryFrame = new BinaryFrame(BufferUtil.toArray(bb));
        binaryFrame.setFin(true);

        Generator generator = new Generator(policy);

        ByteBuffer actual = ByteBuffer.allocate(length + 11);
        generator.generate(actual, binaryFrame);

        ByteBuffer expected = ByteBuffer.allocate(length + 5);

        expected.put(new byte[]
                { (byte)0x82 });

        byte b = 0x00; // no masking
        b |= length & 0x7E;
        expected.put(b);

        //expected.put((byte)((length>>8) & 0xFF));
        //expected.put((byte)(length & 0xFF));
        expected.putShort((short)length);

        for ( int i = 0 ; i < length ; ++i )
        {
            expected.put("*".getBytes());
        }

        actual.flip();
        expected.flip();

        ByteBufferAssert.assertEquals("buffers do not match",expected,actual);

    }

    @Test
    public void testGenerate128ByteBinaryCase1_2_5()
    {
        int length = 128;

        ByteBuffer bb = ByteBuffer.allocate(length);

        for ( int i = 0 ; i < length ; ++i)
        {
            bb.put("*".getBytes());

        }

        bb.flip();
        BinaryFrame textFrame = new BinaryFrame(BufferUtil.toArray(bb));
        textFrame.setFin(true);

        Generator generator = new Generator(policy);
        ByteBuffer actual = ByteBuffer.allocate(length + 11);
        generator.generate(actual, textFrame);

        ByteBuffer expected = ByteBuffer.allocate(length + 5);

        expected.put(new byte[]
                { (byte)0x82 });

        byte b = 0x00; // no masking
        b |= 0x7E;
        expected.put(b);

        expected.put((byte)(length>>8));
        expected.put((byte)(length & 0xFF));
        //expected.putShort((short)length);

        for ( int i = 0 ; i < length ; ++i )
        {
            expected.put("*".getBytes());
        }

        actual.flip();
        expected.flip();

        ByteBufferAssert.assertEquals("buffers do not match",expected,actual);

    }

    @Test
    public void testGenerate65535ByteBinaryCase1_2_6()
    {
        int length = 65535;

        ByteBuffer bb = ByteBuffer.allocate(length);

        for ( int i = 0 ; i < length ; ++i)
        {
            bb.put("*".getBytes());

        }

        bb.flip();

        BinaryFrame textFrame = new BinaryFrame(BufferUtil.toArray(bb));
        textFrame.setFin(true);

        Generator generator = new Generator(policy);
        ByteBuffer actual = ByteBuffer.allocate(length + 11);
        generator.generate(actual, textFrame);

        ByteBuffer expected = ByteBuffer.allocate(length + 5);

        expected.put(new byte[]
                { (byte)0x82 });

        byte b = 0x00; // no masking
        b |= 0x7E;
        expected.put(b);
        expected.put(new byte[]{ (byte)0xff, (byte)0xff});

        for ( int i = 0 ; i < length ; ++i )
        {
            expected.put("*".getBytes());
        }

        actual.flip();
        expected.flip();

        ByteBufferAssert.assertEquals("buffers do not match",expected,actual);

    }

    @Test
    public void testGenerate65536ByteBinaryCase1_2_7()
    {
        int length = 65536;

        ByteBuffer bb = ByteBuffer.allocate(length);

        for ( int i = 0 ; i < length ; ++i)
        {
            bb.put("*".getBytes());

        }

        bb.flip();

        BinaryFrame textFrame = new BinaryFrame(BufferUtil.toArray(bb));
        textFrame.setFin(true);

        Generator generator = new Generator(policy);
        ByteBuffer actual = ByteBuffer.allocate(length + 32);
        generator.generate(actual, textFrame);

        ByteBuffer expected = ByteBuffer.allocate(length + 11);

        expected.put(new byte[]
                { (byte)0x82 });

        byte b = 0x00; // no masking
        b |= 0x7F;
        expected.put(b);
        expected.put(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00});


        for ( int i = 0 ; i < length ; ++i )
        {
            expected.put("*".getBytes());
        }

        actual.flip();
        expected.flip();

        ByteBufferAssert.assertEquals("buffers do not match",expected,actual);

    }

    @Test
    public void testGenerateEmptyBinaryCase1_2_1()
    {
        BinaryFrame binaryFrame = new BinaryFrame(new byte[]{});
        binaryFrame.setFin(true);

        Generator generator = new Generator(policy);
        ByteBuffer actual = ByteBuffer.allocate(32);
        generator.generate(actual, binaryFrame);

        ByteBuffer expected = ByteBuffer.allocate(5);

        expected.put(new byte[]
                { (byte)0x82, (byte)0x00 });

        actual.flip();
        expected.flip();

        ByteBufferAssert.assertEquals("buffers do not match",expected,actual);

    }

    @Test
    public void testParse125ByteBinaryCase1_2_2()
    {
        int length = 125;

        ByteBuffer expected = ByteBuffer.allocate(length + 5);

        expected.put(new byte[]
                { (byte)0x82 });
        byte b = 0x00; // no masking
        b |= length & 0x7F;
        expected.put(b);

        for ( int i = 0 ; i < length ; ++i )
        {
            expected.put("*".getBytes());
        }

        expected.flip();

        Parser parser = new Parser(policy);
        FrameParseCapture capture = new FrameParseCapture();
        parser.addListener(capture);
        parser.parse(expected);

        capture.assertNoErrors();
        capture.assertHasFrame(BinaryFrame.class,1);

        BinaryFrame pActual = (BinaryFrame)capture.getFrames().get(0);
        Assert.assertThat("BinaryFrame.payloadLength",pActual.getPayloadLength(),is(length));
        ByteBufferAssert.assertSize("BinaryFrame.payload",length,pActual.getPayload());
    }

    @Test
    public void testParse126ByteBinaryCase1_2_3()
    {
        int length = 126;

        ByteBuffer expected = ByteBuffer.allocate(length + 5);

        expected.put(new byte[]
                { (byte)0x82 });
        byte b = 0x00; // no masking
        b |= length & 0x7E;
        expected.put(b);
        expected.putShort((short)length);

        for ( int i = 0 ; i < length ; ++i )
        {
            expected.put("*".getBytes());
        }

        expected.flip();

        Parser parser = new Parser(policy);
        FrameParseCapture capture = new FrameParseCapture();
        parser.addListener(capture);
        parser.parse(expected);

        capture.assertNoErrors();
        capture.assertHasFrame(BinaryFrame.class,1);

        BinaryFrame pActual = (BinaryFrame)capture.getFrames().get(0);
        Assert.assertThat("BinaryFrame.payloadLength",pActual.getPayloadLength(),is(length));
        ByteBufferAssert.assertSize("BinaryFrame.payload",length,pActual.getPayload());
    }

    @Test
    public void testParse127ByteBinaryCase1_2_4()
    {
        int length = 127;

        ByteBuffer expected = ByteBuffer.allocate(length + 5);

        expected.put(new byte[]
                { (byte)0x82 });
        byte b = 0x00; // no masking
        b |= length & 0x7E;
        expected.put(b);
        expected.putShort((short)length);

        for ( int i = 0 ; i < length ; ++i )
        {
            expected.put("*".getBytes());
        }

        expected.flip();

        Parser parser = new Parser(policy);
        FrameParseCapture capture = new FrameParseCapture();
        parser.addListener(capture);
        parser.parse(expected);

        capture.assertNoErrors();
        capture.assertHasFrame(BinaryFrame.class,1);

        BinaryFrame pActual = (BinaryFrame)capture.getFrames().get(0);
        Assert.assertThat("BinaryFrame.payloadLength",pActual.getPayloadLength(),is(length));
        ByteBufferAssert.assertSize("BinaryFrame.payload",length,pActual.getPayload());
    }

    @Test
    public void testParse128ByteBinaryCase1_2_5()
    {
        int length = 128;

        ByteBuffer expected = ByteBuffer.allocate(length + 5);

        expected.put(new byte[]
                { (byte)0x82 });
        byte b = 0x00; // no masking
        b |= 0x7E;
        expected.put(b);
        expected.putShort((short)length);

        for ( int i = 0 ; i < length ; ++i )
        {
            expected.put("*".getBytes());
        }

        expected.flip();

        Parser parser = new Parser(policy);
        FrameParseCapture capture = new FrameParseCapture();
        parser.addListener(capture);
        parser.parse(expected);

        capture.assertNoErrors();
        capture.assertHasFrame(BinaryFrame.class,1);

        BinaryFrame pActual = (BinaryFrame)capture.getFrames().get(0);
        Assert.assertThat("BinaryFrame.payloadLength",pActual.getPayloadLength(),is(length));
        ByteBufferAssert.assertSize("BinaryFrame.payload",length,pActual.getPayload());
    }

    @Test
    public void testParse65535ByteBinaryCase1_2_6()
    {
        int length = 65535;

        ByteBuffer expected = ByteBuffer.allocate(length + 5);

        expected.put(new byte[]
                { (byte)0x82 });
        byte b = 0x00; // no masking
        b |= 0x7E;
        expected.put(b);
        expected.put(new byte[]{ (byte)0xff, (byte)0xff});

        for ( int i = 0 ; i < length ; ++i )
        {
            expected.put("*".getBytes());
        }

        expected.flip();
        WebSocketPolicy policy = new WebSocketPolicy(WebSocketBehavior.SERVER);
        policy.setMaxTextMessageSize(length);
        Parser parser = new Parser(policy);
        FrameParseCapture capture = new FrameParseCapture();
        parser.addListener(capture);
        parser.parse(expected);

        capture.assertNoErrors();
        capture.assertHasFrame(BinaryFrame.class,1);

        BinaryFrame pActual = (BinaryFrame)capture.getFrames().get(0);
        Assert.assertThat("BinaryFrame.payloadLength",pActual.getPayloadLength(),is(length));
        ByteBufferAssert.assertSize("BinaryFrame.payload",length,pActual.getPayload());
    }


    @Test
    public void testParse65536ByteBinaryCase1_2_7()
    {
        int length = 65536;

        ByteBuffer expected = ByteBuffer.allocate(length + 11);

        expected.put(new byte[]
                { (byte)0x82 });
        byte b = 0x00; // no masking
        b |= 0x7F;
        expected.put(b);
        expected.put(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00});

        for ( int i = 0 ; i < length ; ++i )
        {
            expected.put("*".getBytes());
        }

        expected.flip();

        WebSocketPolicy policy = new WebSocketPolicy(WebSocketBehavior.SERVER);
        policy.setMaxTextMessageSize(length);
        Parser parser = new Parser(policy);
        FrameParseCapture capture = new FrameParseCapture();
        parser.addListener(capture);
        parser.parse(expected);

        capture.assertNoErrors();
        capture.assertHasFrame(BinaryFrame.class,1);

        BinaryFrame pActual = (BinaryFrame)capture.getFrames().get(0);
        Assert.assertThat("BinaryFrame.payloadLength",pActual.getPayloadLength(),is(length));
        ByteBufferAssert.assertSize("BinaryFrame.payload",length,pActual.getPayload());
    }

    @Test
    public void testParseEmptyBinaryCase1_2_1()
    {

        ByteBuffer expected = ByteBuffer.allocate(5);

        expected.put(new byte[]
                { (byte)0x82, (byte)0x00 });

        expected.flip();

        Parser parser = new Parser(policy);
        FrameParseCapture capture = new FrameParseCapture();
        parser.addListener(capture);
        parser.parse(expected);

        capture.assertNoErrors();
        capture.assertHasFrame(BinaryFrame.class,1);

        BinaryFrame pActual = (BinaryFrame)capture.getFrames().get(0);
        Assert.assertThat("BinaryFrame.payloadLength",pActual.getPayloadLength(),is(0));
        ByteBufferAssert.assertSize("BinaryFrame.payload",0,pActual.getPayload());
    }
}