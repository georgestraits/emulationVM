/*
 * Copyright 2019-present Open Networking Foundation
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

package org.onosproject.p4runtime.ctl.codec;

import com.google.protobuf.ByteString;
import org.onosproject.net.pi.model.PiPipeconf;
import org.onosproject.net.pi.runtime.PiPacketOperation;
import org.onosproject.p4runtime.ctl.utils.P4InfoBrowser;
import p4.config.v1.P4InfoOuterClass;
import p4.v1.P4RuntimeOuterClass;

import static org.onosproject.p4runtime.ctl.codec.Codecs.CODECS;

/**
 * Codec for P4Runtime PacketOut. Only encoding is supported, as decoding is not
 * meaningful in this case (packet-outs are always generated by the client).
 */
public final class PacketOutCodec
        extends AbstractCodec<PiPacketOperation,
        P4RuntimeOuterClass.PacketOut, Object> {

    private static final String PACKET_OUT = "packet_out";

    @Override
    protected P4RuntimeOuterClass.PacketOut encode(
            PiPacketOperation piPacket, Object ignored, PiPipeconf pipeconf,
            P4InfoBrowser browser)
            throws CodecException, P4InfoBrowser.NotFoundException {
        final P4InfoOuterClass.Preamble ctrlPktMetaPreamble = browser
                .controllerPacketMetadatas()
                .getByName(PACKET_OUT)
                .getPreamble();
        return P4RuntimeOuterClass.PacketOut.newBuilder()
                .addAllMetadata(CODECS.packetMetadata().encodeAll(
                        piPacket.metadatas(), ctrlPktMetaPreamble, pipeconf))
                .setPayload(ByteString.copyFrom(piPacket.data().asReadOnlyBuffer()))
                .build();

    }

    @Override
    protected PiPacketOperation decode(
            P4RuntimeOuterClass.PacketOut message, Object ignored,
            PiPipeconf pipeconf, P4InfoBrowser browser)
            throws CodecException {
        throw new CodecException("Decoding of packet-out is not supported");
    }
}