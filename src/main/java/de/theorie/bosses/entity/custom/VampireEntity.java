package de.theorie.bosses.entity.custom;

import de.theorie.bosses.effect.ModEffects;
import de.theorie.bosses.entity.ModEntityTypes;
import de.theorie.bosses.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
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
    private static final EntityDataAccessor<Boolean> HALF_LIFE_EVENT =
            SynchedEntityData.defineId(VampireEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> AWAKEND =
            SynchedEntityData.defineId(VampireEntity.class, EntityDataSerializers.BOOLEAN);
    private int vampireDeathTime;
    public VampireEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

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

    public void setHalfLifeEvent(boolean triggered){
        this.entityData.set(HALF_LIFE_EVENT, triggered);
    }
    public boolean isHalfLifeEvent(){
        return this.entityData.get(HALF_LIFE_EVENT);
    }
    public void setAwakend(boolean triggered){
        this.entityData.set(AWAKEND, triggered);
    }
    public boolean isAwakend(){
        return this.entityData.get(AWAKEND);
    }
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HALF_LIFE_EVENT, false);
        this.entityData.define(AWAKEND, false);
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
        this.goalSelector.addGoal(1, new HalfLifeVampireAttackGoal(this,0D, false));
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
        data.addAnimationController(new AnimationController(this,"attack_controller",
                0,this::attackAnim));
    }
    private <E extends IAnimatable> PlayState attackAnim(AnimationEvent<E> event) {
        if (isHalfLifeEvent()){
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.vampire.summoning"));
        }
        return PlayState.CONTINUE;
    }
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
       if (vampireDeathTime==1){
           event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.vampire.death", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            return PlayState.STOP;
       }
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.vampire.walk", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
            return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;
    }
    private <E extends IAnimatable> PlayState spawnAnim(AnimationEvent<E> event) {

        if (this.getHealth()>=1000&&!isAwakend()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.vampire.idle",
                    ILoopType.EDefaultLoopTypes.LOOP));
        } else if (this.getHealth()<1000 && !isAwakend()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.vampire.awakening",
                    ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            setAwakend(true);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void tick() {
        super.tick();
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
        int rInt = this.random.nextInt(5);
        switch (rInt){
            case 0:break;

            case 1: break;

            case 2: break;

            default:
                if (flag && this.getMainHandItem().isEmpty() && pEntity instanceof LivingEntity) {
                    ((LivingEntity)pEntity).addEffect(new MobEffectInstance(ModEffects.VAMPIRISM.get(), 5000), this);
                }
                break;
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

    @Override
    public int getExperienceReward() {
        return 10000;
    }
    /*SOUND*/

    protected SoundEvent getHurtSound(DamageSource damageSourceIn){
    return SoundEvents.AMETHYST_BLOCK_PLACE;
    }
    protected SoundEvent getDeathSound(){
        return SoundEvents.IRON_GOLEM_DEATH;
    }
    protected float getSoundVolume(){
        return 5.0f;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        setHalfLifeEvent(pCompound.getBoolean("halfLifeTriggered"));
        setAwakend(pCompound.getBoolean("awakened"));
    }
    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("halfLifeTriggered", isHalfLifeEvent());
        pCompound.putBoolean("awakened", isAwakend());
    }

    /*GOAL-INIT*/
    public class VampireDoNothingGoal extends Goal {
        private int ticks = 0;
        private final int tickstwo=40;
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
            return tickstwo >= ticks;
        }
        public boolean isInterruptable() {
            return true;
        }

        @Override
        public void tick() {
            super.tick();
            if (vampire.getHealth()<1000){
                ticks++;
            }

        }
    }

    public class HalfLifeVampireAttackGoal extends MeleeAttackGoal{
        PathfinderMob vampire;

        public HalfLifeVampireAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            vampire = pMob;
        }
        @Override
        public boolean canUse() {
            return vampire.getHealth()<=500 && !isHalfLifeEvent();
        }
        @Override
        public boolean canContinueToUse() {
            return false;
        }
        @Override
        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
            ServerLevel serverlevel = (ServerLevel)vampire.level;
            setHalfLifeEvent(true);
            for(int i = 0; i < 25; ++i) {
            BlockPos blockpos = vampire.blockPosition().offset(-2 + VampireEntity.this.random.nextInt(5), 1, -2 + VampireEntity.this.random.nextInt(5));
            Vex bad = ModEntityTypes.BAD.get().create(vampire.level);
            bad.moveTo(blockpos, 0.0F, 0.0F);
            bad.finalizeSpawn(serverlevel, vampire.level.getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null, (CompoundTag)null);
            bad.setOwner(vampire);
            bad.setBoundOrigin(blockpos);
            bad.setLimitedLife(30 * (30 + VampireEntity.this.random.nextInt(90)));
            serverlevel.addFreshEntityWithPassengers(bad);
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
