package br.com.darlan.urlvalidation.messaging.consumer;

import br.com.darlan.urlvalidation.messaging.message.InsertionInMessage;
import br.com.darlan.urlvalidation.model.Whitelist;
import br.com.darlan.urlvalidation.repository.WhitelistRepository;
import br.com.darlan.urlvalidation.service.WhitelistService;
import br.com.darlan.urlvalidation.service.impl.WhitelistServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InsertionConsumerTest {

    @Mock
    private WhitelistRepository whitelistRepository;
    private InsertionConsumer insertionConsumer;

    @Before
    public void setUp() {
        final WhitelistService whitelistService = new WhitelistServiceImpl(whitelistRepository);
        this.insertionConsumer = new InsertionConsumer(whitelistService);
    }

    @Test
    public void givenInsertionMessageThenSaveWhitelist(){
        final InsertionInMessage message = new InsertionInMessage("client1", "http://.*");
        insertionConsumer.consume(message);
        final Whitelist whitelist = Whitelist.builder()
                    .client(message.getClient())
                    .regex(message.getRegex())
                .build();
        verify(whitelistRepository).save(whitelist);
    }
}