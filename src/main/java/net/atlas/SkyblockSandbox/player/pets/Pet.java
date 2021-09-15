package net.atlas.SkyblockSandbox.player.pets;

public class Pet {

    public enum PetType {
        COMBAT("Combat Pet"),
        MINING("Mining Pet"),
        FISHING("Fishing Pet"),
        FORAGING("Foraging Pet"),
        SUSSY("Suspicious Pet");

        String petType;
        private PetType(String petType) {
            this.petType = petType;
        }

        public String getPetModifier() {
            return petType;
        }

    }

}
