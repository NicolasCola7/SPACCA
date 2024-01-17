package cards;
public class EnergeticShieldCard extends StaticCard {
	
	public EnergeticShieldCard() {
		super("EnergeticShieldCard",Seed.SW);
	}
	public boolean getEffect(Player targetPlayer) {
		String weaponName=targetPlayer.getEquipedWeapon().getName();
		if(weaponName.equals("Pirate's Saber") ||weaponName.equals("Common Sword") || weaponName.equals("King's Sword"))
			return true;
		else
			return false;
	}
}
