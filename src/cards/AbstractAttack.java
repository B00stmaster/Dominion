package cards;


public abstract class AbstractAttack extends AbstractAction {

	public AbstractAttack() {
		super();
		this.types.add(Type.ATTACK);
	}
}
