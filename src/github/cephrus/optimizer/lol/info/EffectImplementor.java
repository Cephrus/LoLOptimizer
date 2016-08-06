package github.cephrus.optimizer.lol.info;

public interface EffectImplementor<T extends Object>
{
	T effect(int number, double value);
}
