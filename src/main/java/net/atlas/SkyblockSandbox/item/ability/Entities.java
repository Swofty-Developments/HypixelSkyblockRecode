package net.atlas.SkyblockSandbox.item.ability;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public enum Entities {
    ARROW("Arrow", Material.ARROW, null),
    SNOWBALL("Snowball", Material.SNOW_BALL, EntitySnowball.class),
    FIREBALL("Fireball", Material.FIREBALL, EntityFireball.class),
    SMALL_FIREBALL("SmallFireball", Material.FIREBALL, EntitySmallFireball.class),
    WITHER_SKULL("WitherSkull", "http://textures.minecraft.net/texture/ddafb23efc57f251878e5328d11cb0eef87b79c87b254a7ec72296f9363ef7c", EntityWitherSkull.class),
    PRIMED_TNT("PrimedTnt", Material.TNT, EntityTNTPrimed.class),
    FIREWORK("FireworksRocketEntity", Material.FIREWORK, EntityFireworks.class),
    CREEPER("Creeper", "http://textures.minecraft.net/texture/f4254838c33ea227ffca223dddaabfe0b0215f70da649e944477f44370ca6952", EntityCreeper.class),
    SKELETON("Skeleton", "http://textures.minecraft.net/texture/301268e9c492da1f0d88271cb492a4b302395f515a7bbf77f4a20b95fc02eb2", EntityCreeper.class),
    SPIDER("Spider", "http://textures.minecraft.net/texture/cd541541daaff50896cd258bdbdd4cf80c3ba816735726078bfe393927e57f1", EntitySpider.class),
    ZOMBIE("Zombie", "http://textures.minecraft.net/texture/56fc854bb84cf4b7697297973e02b79bc10698460b51a639c60e5e417734e11", EntityZombie.class),
    SLIME("Slime", "http://textures.minecraft.net/texture/a20e84d32d1e9c919d3fdbb53f2b37ba274c121c57b2810e5a472f40dacf004f", EntitySlime.class),
    PIG_ZOMBIE("PigZombie", "http://textures.minecraft.net/texture/285386f7803343e5e6b7e8ee061677ff17e4f561005479d39472b2657e08842e", EntityPigZombie.class),
    ENDERMAN("Enderman", "http://textures.minecraft.net/texture/7a59bb0a7a32965b3d90d8eafa899d1835f424509eadd4e6b709ada50b9cf", EntityEnderman.class),
    CAVE_SPIDER("CaveSpider", "http://textures.minecraft.net/texture/41645dfd77d09923107b3496e94eeb5c30329f97efc96ed76e226e98224", EntityCaveSpider.class),
    SILVERFISH("Silverfish", "http://textures.minecraft.net/texture/da91dab8391af5fda54acd2c0b18fbd819b865e1a8f1d623813fa761e924540", EntitySilverfish.class),
    BLAZE("Blaze", "http://textures.minecraft.net/texture/b78ef2e4cf2c41a2d14bfde9caff10219f5b1bf5b35a49eb51c6467882cb5f0", EntityBlaze.class),
    MAGMA_CUBE("LavaSlime", "http://textures.minecraft.net/texture/38957d5023c937c4c41aa2412d43410bda23cf79a9f6ab36b76fef2d7c429", EntityMagmaCube.class),
    BAT("Bat", "http://textures.minecraft.net/texture/9e99deef919db66ac2bd28d6302756ccd57c7f8b12b9dca8f41c3e0a04ac1cc", EntityBat.class),
    WITCH("Witch", "http://textures.minecraft.net/texture/20e13d18474fc94ed55aeb7069566e4687d773dac16f4c3f8722fc95bf9f2dfa", EntityWitch.class),
    ENDERMITE("Endermite", "http://textures.minecraft.net/texture/5a1a0831aa03afb4212adcbb24e5dfaa7f476a1173fce259ef75a85855", EntityEndermite.class),
    GUARDIAN("Guardian", "http://textures.minecraft.net/texture/a0bf34a71e7715b6ba52d5dd1bae5cb85f773dc9b0d457b4bfc5f9dd3cc7c94", EntityGuardian.class),
    PIG("Pig", "http://textures.minecraft.net/texture/621668ef7cb79dd9c22ce3d1f3f4cb6e2559893b6df4a469514e667c16aa4", EntityPig.class),
    SHEEP("Sheep", "http://textures.minecraft.net/texture/2840f34e5fd054c0e6ac18ebd76ea0f3a0263182aa4a7bbe92430fb276346dd0", EntitySheep.class),
    COW("Cow", "http://textures.minecraft.net/texture/5d6c6eda942f7f5f71c3161c7306f4aed307d82895f9d2b07ab4525718edc5", EntityCow.class),
    CHICKEN("Chicken", "http://textures.minecraft.net/texture/1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893", EntityChicken.class),
    SQUID("Squid", "http://textures.minecraft.net/texture/d8705624daa2956aa45956c81bab5f4fdb2c74a596051e24192039aea3a8b8", EntitySquid.class),
    WOLF("Wolf", "http://textures.minecraft.net/texture/1d83731d77f54f5d4f93ddd99b9476e4f1fe5b7e1318f1e1626f7d3fa3aa847", EntityWolf.class),
    MUSHROOM_COW("MushroomCow", "http://textures.minecraft.net/texture/2b52841f2fd589e0bc84cbabf9e1c27cb70cac98f8d6b3dd065e55a4dcb70d77", EntityMushroomCow.class),
    SNOWMAN("SnowMan", "http://textures.minecraft.net/texture/cf9ba69aa2467dedae69ceaf78ed8c734a705d37cfe51afbe02a75b6fb98bce7", EntitySnowman.class),
    OCELOT("Ozelot", "http://textures.minecraft.net/texture/5657cd5c2989ff97570fec4ddcdc6926a68a3393250c1be1f0b114a1db1", EntityOcelot.class),
    RABBIT("Rabbit", "http://textures.minecraft.net/texture/117bffc1972acd7f3b4a8f43b5b6c7534695b8fd62677e0306b2831574b", EntityRabbit.class),
    VILLAGER("Villager", "http://textures.minecraft.net/texture/41b830eb4082acec836bc835e40a11282bb51193315f91184337e8d3555583", EntityVillager.class),
    EGG("Egg", Material.EGG, EntityEgg.class);

    private final EntityType a;
    private final String b;
    private final Material c;
    private final Class<? extends Entity> d;

    public EntityType getA() {
        return this.a;
    }

    public String getB() {
        return this.b;
    }

    public Material getC(){return this.c;}

    public Class<? extends Entity> getD(){return this.d;}

    Entities(String a, String b, Class<? extends Entity> entityType) {
        this.a = EntityType.valueOf(name());
        this.b = b;
        this.c = null;
        d = entityType;
    }
    Entities(String a, Material c, Class<? extends Entity> entityType) {
        this.a = EntityType.valueOf(name());
        this.b = null;
        this.c = c;
        d = entityType;
    }
}
