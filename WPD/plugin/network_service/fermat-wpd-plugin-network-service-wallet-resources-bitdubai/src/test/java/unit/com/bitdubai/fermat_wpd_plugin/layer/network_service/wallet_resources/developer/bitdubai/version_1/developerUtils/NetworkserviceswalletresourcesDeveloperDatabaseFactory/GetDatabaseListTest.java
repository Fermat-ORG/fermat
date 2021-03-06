package unit.com.bitdubai.fermat_wpd_plugin.layer.network_service.wallet_resources.developer.bitdubai.version_1.developerUtils.NetworkserviceswalletresourcesDeveloperDatabaseFactory;

import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_wpd_plugin.layer.network_service.wallet_resources.developer.bitdubai.version_1.developerUtils.NetworkserviceswalletresourcesDeveloperDatabaseFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.UUID;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by natalia on 09/09/15.
 */

@RunWith(MockitoJUnitRunner.class)
public class GetDatabaseListTest {
    @Mock
    private DeveloperObjectFactory developerObjectFactory;

    @Mock
    private Database mockDatabase;

    @Mock
    private PluginDatabaseSystem mockPluginDatabaseSystem;

    private NetworkserviceswalletresourcesDeveloperDatabaseFactory developerDatabaseFactory;

    @Before
    public void setUp() throws Exception {
        UUID testOwnerId = UUID.randomUUID();

        when(mockPluginDatabaseSystem.openDatabase(any(UUID.class), anyString())).thenReturn(mockDatabase);

        developerDatabaseFactory = new NetworkserviceswalletresourcesDeveloperDatabaseFactory(mockPluginDatabaseSystem, testOwnerId);

        developerDatabaseFactory.initializeDatabase();

    }

    @Test
    public void getDatabaseListTest() {

        assertThat(developerDatabaseFactory.getDatabaseList(developerObjectFactory)).isInstanceOf(List.class);
    }

}

