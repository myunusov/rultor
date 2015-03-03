/**
 * Copyright (c) 2009-2015, rultor.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the rultor.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.rultor.web;

import com.rultor.agents.daemons.KillsDaemon;
import com.rultor.spi.Talk;
import com.rultor.spi.Talks;
import java.io.IOException;
import java.util.logging.Level;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;

/**
 * Kill a talk.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 1.50
 */
final class TkTalkKill implements Take {

    /**
     * Talks.
     */
    private final transient Talks talks;

    /**
     * Talk unique number.
     */
    private final transient long number;

    /**
     * Ctor.
     * @param tks Talks
     * @param talk Talk number
     */
    TkTalkKill(final Talks tks, final long talk) {
        this.talks = tks;
        this.number = talk;
    }

    @Override
    public Response act() throws IOException {
        if (!this.talks.exists(this.number)) {
            throw new RsForward(
                new RsFlash(
                    "there is no such page here",
                    Level.WARNING
                ),
                "/"
            );
        }
        final Talk talk = this.talks.get(this.number);
        new KillsDaemon().process(talk.read());
        return new RsForward(
            new RsFlash(
                String.format(
                    "talk #%d daemon requested to kill", talk.number()
                ),
                Level.INFO
            ),
            "/"
        );
    }

}
