package net.atlas.SkyblockSandbox.item.ability.functions;

public enum EnumFunctionsData {
    ID("id",0),
    NAME("name",1),
    TELEPORT_RANGE("Teleport_Range",2),
    IMPLOSION_RANGE("Implosion_Range",2),
    IMPLOSION_MESSAGE("Implosion_Message",2),
    TELEPORT_MESSAGE("Teleport_Message",2),
    PARTICLE_TYPE("Particle_Type",2),
    PARTICLE_SHOOTING("Particle_Shooting", 2),
    PARTICLE_SHOOT_RANGE("Particle_Shooting_Range", 2),
    PARTICLE_DAMAGE_ENTITY("Particle_Damage_Entity", 2),
    PARTICLE_DAMAGE_MULTIPLIER("Particle_Damage_Multiplier", 2),
    PARTICLE_MESSAGE("Particle_Message", 2),
    PARTICLE_SHAPE("Particle_Shape", 2),
    SOUND_TYPE("Sound_Type", 2),
    SOUND_PITCH("Sound_Pitch", 2),
    SOUND_VOLUME("Sound_Volume", 2),
    SOUND_AMOUNT("Sound_Amount", 2),
    SOUND_DELAY("Sound_Delay", 2),
    HEAD_SHOOTER_TYPE("Head_Shooter_Type", 2, false),
    HEAD_SHOOTER_DAMAGE_ENTITY("Head_Shooter_Damage_Entity", 2),
    HEAD_SHOOTER_RANGE("Head_Shooter_Range", 2),
    HEAD_SHOOTER_KICKBACK("Head_Shooter_KickBack", 2),
    HEAD_SHOOTER_BASE_DAMAGE("Head_Shooter_Base_Damage", 2),
    HEAD_SHOOTER_KICKBACK_RANGE("Head_Shooter_KickBack_Range", 2),
    PROJECTILE_SHOOTER_TYPE("Projectile_Shooter_Type",2),
    PROJECTILE_SHOOTER_DAMAGE_ENTITY("Projectile_Shooter_Damage_Entity", 2),
    PROJECTILE_SHOOTER_BASE_DAMAGE("Projectile_Shooter_Base_Damage",2);

    private final String a;
    private final int b;
    private final boolean c;

    public String getA() {
        return this.a;
    }

    public int getB() {
        return this.b;
    }

    public boolean getC() {
        return this.c;
    }

    EnumFunctionsData(String a, int b) {
        this.a = a;
        this.b = b;
        this.c = true;
    }
    EnumFunctionsData(String a, int b, boolean c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
}