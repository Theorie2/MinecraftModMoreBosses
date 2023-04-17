package de.theorie.bosses.entity.custom;

import de.theorie.bosses.effect.ModEffects;
import de.theorie.bosses.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.EnumSet;



public class VampireEntity extends Monster implements IAnimatable {

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final EntityDataAccessor<Boolean> SLEEPING =
            SynchedEntityData.defineId(VampireEntity.class, EntityDataSerializers.BOOLEAN);
    public boolean awakend = false;
    private int vampireDeathTime;
    public VampireEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        if (!this.level.isClientSide){
            setItSleeping(true);
        }
    }
    private ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(),
            BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
    public static AttributeSupplier setAttributes(){
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1000)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.ATTACK_SPEED,1)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE,100)
                .build();
    }

    /*BOSSBAR*/
    public void startSeenByPlayer(ServerPlayer pPlayer) {
        super.startSeenByPlayer(pPlayer);
        this.bossEvent.addPlayer(pPlayer);
    }

    public void stopSeenByPlayer(ServerPlayer pPlayer) {
        super.stopSeenByPlayer(pPlayer);
        this.bossEvent.removePlayer(pPlayer);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    /*GOALS*/
    protected void registerGoals(){
        this.goalSelector.addGoal(0,new VampireDoNothingGoal(this));
        this.addBehaviourGoals();
    }
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new VampireAttackGoal(this, 1.0D, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }


    /*ANIMATIONS*/

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this,"walk_and_death_controller",
                0,this::predicate));
        data.addAnimationController(new AnimationController(this,"sleep_controller",
                0,this::spawnAnim));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.vampire.walk", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
            return PlayState.CONTINUE;
        }
       if (this.getHealth()<=0){
           event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.vampire.death", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            return PlayState.CONTINUE;
       }
        return PlayState.CONTINUE;
    }
    private <E extends IAnimatable> PlayState spawnAnim(AnimationEvent<E> event) {

        if (!awakend){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.vampire.idle",
                    ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.vampire.awakening",
                    ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void tick() {
        super.tick();

        if (!awakend && this.getHealth()<1000) {
            //if (isItSleeping()) {
                awakening();
                awakend = true;
            //}
        }
    }

    public void awakening(){
        if (!this.level.isClientSide){
            setItSleeping(false);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        awakend=tag.getBoolean("isItSleeping");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isItSleeping",this.awakend);
    }
    @Override
    protected void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(SLEEPING,Boolean.TRUE);
    }
    public void setItSleeping(boolean sleeping){
        this.entityData.set(SLEEPING,sleeping);
    }
    public boolean isItSleeping(){
        return this.entityData.get(SLEEPING);
    }


    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    protected void tickDeath(){

        ++vampireDeathTime;
        if (this.vampireDeathTime == 1 && this.level instanceof ServerLevel){
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        }
        float f = (this.random.nextFloat() - 0.5F) * 8.0F;
        float f1 = (this.random.nextFloat() - 0.5F) * 4.0F;
        float f2 = (this.random.nextFloat() - 0.5F) * 8.0F;
        this.level.addParticle(ParticleTypes.ELECTRIC_SPARK, this.getX() + (double)f, this.getY() + 1D + (double)f1, this.getZ() + (double)f2, 0.0D, 0.0D, 0.0D);

        if (this.vampireDeathTime == 80 && this.level instanceof ServerLevel) {
            this.remove(Entity.RemovalReason.KILLED);
            this.gameEvent(GameEvent.ENTITY_DIE);
        }
    }


    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return !this.isInvulnerable() && super.hurt(pSource, pAmount);
    }

    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = super.doHurtTarget(pEntity);
        if (flag && this.getMainHandItem().isEmpty() && pEntity instanceof LivingEntity) {
            ((LivingEntity)pEntity).addEffect(new MobEffectInstance(ModEffects.VAMPIRISM.get(), 5000), this);
        }

        return flag;
    }




    /**
     * Called by the /kill command.
     */
    public void kill() {
        this.remove(Entity.RemovalReason.KILLED);
        this.gameEvent(GameEvent.ENTITY_DIE);
    }

    @Override
    protected int calculateFallDamage(float pFallDistance, float pDamageMultiplier) {
        return 0;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public MobType getMobType() {
        return MobType.UNDEAD;
    }



    /*Death-Loot*/
    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        ItemEntity itementity = this.spawnAtLocation(ModItems.BLOODBOTTLE.get());
        if (itementity != null) {
            itementity.setExtendedLifetime();
        }

    }
    /*SOUND*/

    protected SoundEvent getHurtSound(DamageSource damageSourceIn){
    return SoundEvents.CAMPFIRE_CRACKLE;
    }
    protected SoundEvent getDeathSound(){
        return SoundEvents.IRON_GOLEM_DEATH;
    }
    protected float getSoundVolume(){
        return 5.0f;
    }


    /*GOAL-INIT*/
    public class VampireDoNothingGoal extends Goal {
        VampireEntity vampire;
        public VampireDoNothingGoal(VampireEntity vampire) {
            this.vampire=vampire;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        }
        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return vampire.getHealth() == 1000;
        }
        public boolean isInterruptable() {
            return true;
        }

        @Override
        public void tick() {
            super.tick();
            if(vampire.getHealth()<1000){
                stop();
            }
        }
    }

    public class VampireAttackGoal extends MeleeAttackGoal {
        private final VampireEntity vampire;
        private int raiseArmTicks;

        public VampireAttackGoal(VampireEntity pVampire, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            super(pVampire, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            this.vampire = pVampire;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            super.start();
            this.raiseArmTicks = 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            super.stop();
            this.vampire.setAggressive(false);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            super.tick();
            ++this.raiseArmTicks;
            if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
                this.vampire.setAggressive(true);
            } else {
                this.vampire.setAggressive(false);
            }

        }
    }





    /*Invulnerable*/
    /*public void setInvulnerableTicks(int pInvulnerableTicks) {
        this.entityData.set(DATA_ID, pInvulnerableTicks);
    }
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setInvulnerableTicks(pCompound.getInt("Invul"));
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Invul", this.getInvulnerableTicks());
    }
    public int getInvulnerableTicks() {
        return this.entityData.get(DATA_ID);
    }*/

}
