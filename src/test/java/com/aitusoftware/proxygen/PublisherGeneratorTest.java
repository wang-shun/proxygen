package com.aitusoftware.proxygen;

import com.aitusoftware.proxygen.common.MethodDescriptor;
import com.aitusoftware.proxygen.common.ParameterDescriptor;
import com.aitusoftware.proxygen.publisher.PublisherGenerator;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PublisherGeneratorTest {
    private static final String EXPECTED_SOURCE =
            "package com.package;\n" +
                    "\n" +
                    "import com.aitusoftware.transport.messaging.proxy.AbstractPublisher;\n" +
                    "import com.aitusoftware.transport.buffer.WritableRecord;\n" +
                    "import com.aitusoftware.transport.buffer.PageCache;\n" +
                    "import com.aitusoftware.transport.messaging.proxy.Encoder;\n" +
                    "\n" +
                    "\n" +
                    "public class TestPublisherImpl extends AbstractPublisher implements TestPublisher {\n" +
                    "\n" +
                    "\tpublic TestPublisherImpl(final PageCache pageCache) {\n" +
                    "\t\tsuper(pageCache);\n" +
                    "\t}\n" +
                    "\n" +
                    "\tpublic void say(\n" +
                    "\t\tfinal java.lang.CharSequence word, final int count) {\n" +
                    "\t\t\n" +
                    "\t\tfinal int recordLength = (word.length() * 4) + 4  +  4;\n" +
                    "\t\tfinal WritableRecord wr = acquireRecord(recordLength, (byte) 0);\n" +
                    "\t\tEncoder.encodeInt(wr.buffer(), count);\n" +
                    "\t\tEncoder.encodeCharSequence(wr.buffer(), word);\n" +
                    "\t\twr.commit();\n" +
                    "\t}\n" +
                    "\n" +
                    "\tpublic void shout(\n" +
                    "\t\tfinal java.lang.CharSequence word, final int count) {\n" +
                    "\t\t\n" +
                    "\t\tfinal int recordLength = (word.length() * 4) + 4  +  4;\n" +
                    "\t\tfinal WritableRecord wr = acquireRecord(recordLength, (byte) 1);\n" +
                    "\t\tEncoder.encodeInt(wr.buffer(), count);\n" +
                    "\t\tEncoder.encodeCharSequence(wr.buffer(), word);\n" +
                    "\t\twr.commit();\n" +
                    "\t}\n" +
                    "\n" +
                    "\tpublic void composite(\n" +
                    "\t\tfinal java.lang.CharSequence word, final com.example.OrderDetails orderDetails, final com.example.OrderDetails moreOrderDetails) {\n" +
                    "\t\t\n" +
                    "\t\tfinal int recordLength = (word.length() * 4) + 4  + ((Sized) orderDetails).length() + ((Sized) moreOrderDetails).length() +  0;\n" +
                    "\t\tfinal WritableRecord wr = acquireRecord(recordLength, (byte) 2);\n" +
                    "\t\tfinal com.example.OrderDetailsSerialiser serialiser = new com.example.OrderDetailsSerialiser();\n" +
                    "\t\tserialiser.serialise(orderDetails, wr.buffer());\n" +
                    "\t\tfinal com.example.OrderDetailsSerialiser serialiser = new com.example.OrderDetailsSerialiser();\n" +
                    "\t\tserialiser.serialise(moreOrderDetails, wr.buffer());\n" +
                    "\t\tEncoder.encodeCharSequence(wr.buffer(), word);\n" +
                    "\t\twr.commit();\n" +
                    "\t}\n" +
                    "\n" +
                    "}\n";

    private final PublisherGenerator generator = new PublisherGenerator();

    @Test
    public void shouldGeneratePublisherImplementation() throws Exception
    {
        final StringWriter writer = new StringWriter();
        generator.generatePublisher(
                "com.package", "TestPublisherImpl",
                "TestPublisher",
                new MethodDescriptor[]{
                        new MethodDescriptor(0, "say",
                                new ParameterDescriptor[]{
                                        new ParameterDescriptor("word", CharSequence.class, "java.lang.CharSequence"),
                                        new ParameterDescriptor("count", int.class, "int")
                                }),
                        new MethodDescriptor(0, "shout",
                                new ParameterDescriptor[]{
                                        new ParameterDescriptor("word", CharSequence.class, "java.lang.CharSequence"),
                                        new ParameterDescriptor("count", int.class, "int")
                                }),
                        new MethodDescriptor(0, "composite",
                                new ParameterDescriptor[]{
                                        new ParameterDescriptor("word", CharSequence.class, "java.lang.CharSequence"),
                                        new ParameterDescriptor("orderDetails", null, "com.example.OrderDetails"),
                                        new ParameterDescriptor("moreOrderDetails", null, "com.example.OrderDetails")
                                }),
                },
                Collections.emptyList(),
                writer);

        assertThat(writer.toString(), is(EXPECTED_SOURCE));
    }
}