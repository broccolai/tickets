package love.broccolai.tickets.common.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import love.broccolai.tickets.api.action.TicketActionListener;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.TicketAction;
import love.broccolai.tickets.api.model.format.TicketFormData;
import love.broccolai.tickets.api.model.format.TicketFormat;
import love.broccolai.tickets.api.model.profile.Profile;
import love.broccolai.tickets.api.service.ProfileService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.api.service.TicketSearch;
import love.broccolai.tickets.common.service.profile.ProfileCacheProvider;
import love.broccolai.tickets.common.service.profile.ProfileCreateProvider;
import love.broccolai.tickets.common.service.profile.ProfileDataProvider;
import love.broccolai.tickets.common.service.profile.UUIDUsernameConverter;
import love.broccolai.tickets.common.utilities.PremadeTickets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

class PipelineProfileServiceTest {

    private FakeStorageService storageService;
    private FakeUsernameConverter usernameConverter;
    private ProfileService profileService;

    @BeforeEach
    void setupEach() {
        this.storageService = new FakeStorageService();
        this.usernameConverter = new FakeUsernameConverter();

        this.profileService = new PipelineProfileService(
            this.storageService,
            new ProfileCreateProvider(this.storageService, this.usernameConverter),
            new ProfileDataProvider(this.storageService),
            new ProfileCacheProvider(),
            this.usernameConverter
        );
    }

    @Test
    void loadsStoredProfiles() {
        Profile storedProfile = this.storageService.store("carrot");

        Optional<Profile> profile = this.profileService.find(storedProfile.uuid());

        assertThat(profile).hasValue(storedProfile);
    }

    @Test
    void createsMissingProfiles() {
        UUID uniqueId = UUID.randomUUID();
        this.usernameConverter.connect(uniqueId, "potato");

        Optional<Profile> profile = this.profileService.find(uniqueId);

        assertThat(profile).hasValue(new Profile(uniqueId, "potato"));
        assertThat(this.storageService.loadProfiles(List.of(uniqueId))).containsExactly(profile.orElseThrow());
    }

    @Test
    void skipsMissingConvertedUsernames() {
        UUID uniqueId = UUID.randomUUID();

        Optional<Profile> profile = this.profileService.find(uniqueId);

        assertThat(profile).isEmpty();
        assertThat(this.storageService.loadProfiles(List.of(uniqueId))).isEmpty();
    }

    @Test
    void findsByCachedUsername() {
        Profile storedProfile = this.storageService.store("onion");
        this.profileService.find(storedProfile.uuid());

        Optional<Profile> profile = this.profileService.find("OnIon");

        assertThat(profile).hasValue(storedProfile);
        assertThat(this.storageService.profileSearches).isEqualTo(0);
    }

    @Test
    void findsByConvertedUsername() {
        UUID uniqueId = UUID.randomUUID();
        this.usernameConverter.connect(uniqueId, "turnip");

        Optional<Profile> profile = this.profileService.find("turnip");

        assertThat(profile).hasValue(new Profile(uniqueId, "turnip"));
    }

    @Test
    void updatesUsername() {
        Profile profile = this.storageService.store("parsnip");
        this.profileService.find(profile.uuid());

        Optional<Profile> updatedProfile = this.profileService.updateUsername(profile.uuid(), "radish");

        assertThat(updatedProfile).hasValue(new Profile(profile.uuid(), "radish"));
        assertThat(this.profileService.find("radish")).hasValue(new Profile(profile.uuid(), "radish"));
        assertThat(this.profileService.find("parsnip")).isEmpty();
    }

    @Test
    void activeProfilesComeFromActiveTicketCreators() {
        Ticket ticket = PremadeTickets.ticket();
        Profile profile = this.storageService.store(ticket.creator(), "beetroot");
        this.storageService.activeTickets.add(ticket);

        Collection<Profile> profiles = this.profileService.active();

        assertThat(profiles).containsExactly(profile);
    }

    @Test
    void cachedProfilesExposeLoadedProfiles() {
        Profile profile = this.storageService.store("celery");

        this.profileService.find(profile.uuid());

        assertThat(this.profileService.cached()).containsExactly(profile);
    }

    private static final class FakeStorageService implements StorageService {

        private final Map<UUID, Profile> profiles = new HashMap<>();
        private final Collection<Ticket> activeTickets = new ArrayList<>();

        private int profileSearches;

        @Override
        public void addTicketActionRelay(final TicketActionListener listener) {
        }

        @Override
        public Ticket createTicket(
            final UUID creator,
            final TicketFormat type,
            final TicketFormData form
        ) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void appendAction(final Ticket ticket, final TicketAction action) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<Ticket> selectTicket(final int ticketId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<Integer, Ticket> selectTickets(final int... ticketIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<Ticket> findTickets(final TicketSearch search) {
            return this.activeTickets;
        }

        @Override
        public Collection<Profile> loadProfiles(final Collection<UUID> uniqueIds) {
            return uniqueIds.stream()
                .map(this.profiles::get)
                .filter(Objects::nonNull)
                .toList();
        }

        @Override
        public Optional<Profile> findProfile(final String name) {
            this.profileSearches++;

            return this.profiles.values()
                .stream()
                .filter(profile -> profile.username().equalsIgnoreCase(name))
                .findFirst();
        }

        @Override
        public void insertProfile(final Profile profile) {
            this.profiles.put(profile.uuid(), profile);
        }

        @Override
        public void updateProfile(final Profile profile) {
            this.profiles.put(profile.uuid(), profile);
        }

        private Profile store(final String username) {
            return this.store(UUID.randomUUID(), username);
        }

        private Profile store(final UUID uniqueId, final String username) {
            Profile profile = new Profile(uniqueId, username);

            this.insertProfile(profile);

            return profile;
        }
    }

    private static final class FakeUsernameConverter implements UUIDUsernameConverter {

        private final Map<UUID, String> usernames = new HashMap<>();
        private final Map<String, UUID> uniqueIds = new HashMap<>();

        @Override
        public Optional<String> username(final UUID uniqueId) {
            return Optional.ofNullable(this.usernames.get(uniqueId));
        }

        @Override
        public Optional<UUID> uuid(final String username) {
            return Optional.ofNullable(this.uniqueIds.get(this.key(username)));
        }

        private void connect(final UUID uniqueId, final String username) {
            this.usernames.put(uniqueId, username);
            this.uniqueIds.put(this.key(username), uniqueId);
        }

        private String key(final String username) {
            return username.toLowerCase(Locale.ROOT);
        }
    }
}
