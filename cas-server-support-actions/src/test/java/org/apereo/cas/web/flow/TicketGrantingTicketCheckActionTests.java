package org.apereo.cas.web.flow;

import org.apereo.cas.ticket.TicketGrantingTicket;
import org.apereo.cas.AbstractCentralAuthenticationServiceTests;
import org.apereo.cas.authentication.AuthenticationResult;
import org.apereo.cas.authentication.TestUtils;
import org.apereo.cas.mock.MockTicketGrantingTicket;
import org.apereo.cas.web.config.CasCookieConfiguration;
import org.apereo.cas.web.config.CasSupportActionsConfiguration;
import org.apereo.cas.web.flow.config.CasCoreWebflowConfiguration;
import org.apereo.cas.web.support.WebUtils;
import org.junit.Test;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.test.MockRequestContext;

import static org.junit.Assert.*;

/**
 * Handles tests for {@link TicketGrantingTicketCheckAction}.
 *
 * @author Misagh Moayyed mmoayyed@unicon.net
 * @since 4.1.0
 */
@SpringApplicationConfiguration(classes = {CasSupportActionsConfiguration.class,
        CasCoreWebflowConfiguration.class, CasCookieConfiguration.class})
public class TicketGrantingTicketCheckActionTests extends AbstractCentralAuthenticationServiceTests {

    @Test
    public void verifyNullTicket() throws Exception {
        final MockRequestContext ctx = new MockRequestContext();
        final TicketGrantingTicketCheckAction action = new
                TicketGrantingTicketCheckAction(this.getCentralAuthenticationService());
        final Event event = action.doExecute(ctx);
        assertEquals(event.getId(), TicketGrantingTicketCheckAction.NOT_EXISTS);
    }

    @Test
    public void verifyInvalidTicket() throws Exception {

        final MockRequestContext ctx = new MockRequestContext();
        final MockTicketGrantingTicket tgt = new MockTicketGrantingTicket("user");

        WebUtils.putTicketGrantingTicketInScopes(ctx, tgt);
        final TicketGrantingTicketCheckAction action = new
                TicketGrantingTicketCheckAction(this.getCentralAuthenticationService());
        final Event event = action.doExecute(ctx);
        assertEquals(event.getId(), TicketGrantingTicketCheckAction.INVALID);
    }

    @Test
    public void verifyValidTicket() throws Exception {

        final MockRequestContext ctx = new MockRequestContext();
        final AuthenticationResult ctxAuthN = TestUtils.getAuthenticationResult(getAuthenticationSystemSupport());

        final TicketGrantingTicket tgt = this.getCentralAuthenticationService()
                .createTicketGrantingTicket(ctxAuthN);

        WebUtils.putTicketGrantingTicketInScopes(ctx, tgt);
        final TicketGrantingTicketCheckAction action = new
                TicketGrantingTicketCheckAction(this.getCentralAuthenticationService());
        final Event event = action.doExecute(ctx);
        assertEquals(event.getId(), TicketGrantingTicketCheckAction.VALID);
    }


}
