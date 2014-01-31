class UltimaChargeEmitter extends xEmitter;

defaultproperties
{
	//mLifeRange(0)=0.10000
	//mLifeRange(1)=0.10000
	//mSpeedRange(0)=-50.000000
	//mSpeedRange(1)=-50.000000
	mLifeRange(0)=0.20000
	mLifeRange(1)=0.20000
	mSpeedRange(0)=-50.00000
	mSpeedRange(1)=-50.00000

	//mSizeRange(0)=2.00000
	//mSizeRange(1)=4.00000
	//mSizeRange(0)=4.00000
	//mSizeRange(1)=8.00000
	mSizeRange(0)=8.00000
	mSizeRange(1)=16.00000

	mGrowthRate=0.000000
	mPosDev=(X=5.000000,Y=5.000000,Z=5.000000)
	//mSpawnVecB=(X=10.000000,Z=0.160000)
	//mSpawnVecB=(X=20.00000,Z=0.32000)
	//mSpawnVecB=(X=40.00000,Z=0.64000)
	mSpawnVecB=(X=60.00000,Z=0.96000)

	mParticleType=PT_Line
	mSpawningType=ST_Explode

	mStartParticles=0
	mMaxParticles=100

	mRegenRange(0)=50.000000
	mRegenRange(1)=50.000000

	mMassRange(0)=0.000000
	mMassRange(1)=0.000000
	mAirResistance=0.000000

	//mColorRange(0)=(B=220,G=45,R=45,A=25)
	//mColorRange(1)=(B=250,G=65,R=65,A=200)
	mColorRange(0)=(B=45,G=220,R=45,A=255)
	mColorRange(1)=(B=65,G=250,R=65,A=255)

	mAttenKa=0.200000
	bForceAffected=False
	Physics=PHYS_Rotating
	Style=STY_Additive
	Skins(0)=Texture'FlakTrailTex'
	bFixedRotationDir=True
	RotationRate=(Yaw=16000)

	mPosRelative=true
	bUnlit=true
}
