package net.atlas.SkyblockSandbox.gui.guis.itemCreator;

public enum ItemCreatorPage {
    /*MAIN(ItemCreatorGUIMain.class),
    SET_LORE(ItemDescriptionGUI.class),
    SET_RARITY(RaritiesGUI.class),
    STATS_EDITOR(StatsEditorGUI.class),
    ABILITY_EDITOR(AbilityEditorGUI.class),
    ABILITY_CREATOR_GUI_MAIN(AbilityCreatorGUI.class),
    ABILITY_DESCRIPTION_PICKER(AbilityDescriptionPicker.class),
    BASE_ABILITIES(BaseAbilitiesGUI.class),
    ENTITY_SHOOTER(EntityShooterGUI.class),
    FUNCTIONS_CREATOR(FunctionsCreatorGUI.class),
    FUNCTIONS_EDITOR(FunctionsEditorGUI.class),
    FUNCTIONS_GUI_MAIN(FunctionsGUI.class),
    HEAD_CHOOSER(HeadChooserGUI.class),
    PARTICLE_CHOOSER(ParticleChooserGUI.class),
    PARTICLE_SHOOTER(ParticleShooterGUI.class),
    SET_ABILITY_DESCRIPTION(SetAbilityDescriptionMenu.class),
    PROJECTILE_CHOOSER(ProjectileChooserGUI.class),
    SHAPE_SELECTOR(ShapeSlectorGUI.class),
    SOUND_CHOOSER(SoundChooserGUI.class);


    private Class<? extends SBGUI> gui;

    ItemCreatorPage(Class<? extends SBGUI> gui) {
        this.gui = gui;
    }

    public SBGUI getGui(Object... objects) {
        try {
            if (ConstructorUtils.invokeConstructor(gui, objects).getClass() == NormalGUI.class) {
                return (NormalGUI) ConstructorUtils.invokeConstructor(gui, objects);
            } else {
                return (PaginatedGUI) ConstructorUtils.invokeConstructor(gui, objects);
            }

        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


    public SBGUI getGui(SBPlayer pl) {

        try {
            if (ConstructorUtils.invokeConstructor(gui, pl).getClass() == NormalGUI.class) {
                return (NormalGUI) ConstructorUtils.invokeConstructor(gui, pl);
            } else {
                return (PaginatedGUI) ConstructorUtils.invokeConstructor(gui, pl);
            }

        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SBGUI getGui(SBPlayer pl, int index) {
        try {
            if (ConstructorUtils.invokeConstructor(gui, new Object[]{pl, index}).getClass() == NormalGUI.class) {
                return (NormalGUI) ConstructorUtils.invokeConstructor(gui, new Object[]{pl, index});
            } else {
                return (PaginatedGUI) ConstructorUtils.invokeConstructor(gui, new Object[]{pl, index});
            }

        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SBGUI getGui(SBPlayer pl, int index, int indexx) {
        try {
            if(ConstructorUtils.invokeConstructor(gui, new Object[]{pl, index, indexx}).getClass()==NormalGUI.class) {
                return (NormalGUI) ConstructorUtils.invokeConstructor(gui, new Object[]{pl, index, indexx});
            } else {
                return (PaginatedGUI) ConstructorUtils.invokeConstructor(gui, new Object[]{pl, index, indexx});
            }

        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }*/

}
