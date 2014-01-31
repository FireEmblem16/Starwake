void main()
{
	int a=1;

	a=a^0x0F;

	return;
}

//#include <iostream>
//using std::cout;
//using std::cin;
//
//#include <string>
//using std::string;
//
//void encrypt(int);
//void decrypt(int);
//
//string line;
//int crypt=0;
//
//void main()
//{
//	while(true)
//	{
//		cin>>line;
//
//		const char* t=line.c_str();
//		line=t;
//		
//		cout<<line<<"\n";
//
//		crypt=0;
//
//		encrypt(0);
//		//decrypt(10);
//		cout<<line<<"\n";
//	}
//}
//
//void encrypt(int seed)
//{
//	if(crypt==0)
//		crypt+=seed;
//	string basic="!#$%&()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~";
//	string level1="|?E{+Tkcw%O9o$<gPKh.L2^p8qj/ld*,Be0f3XImJ:!zxVa;yr~R`ZN[&}M1>C4i]QW_=-bU57S@(uAFYt6)Hn#sDGv";
//	string level2="H6t+nq.3!)Q]a:rXJyIwhe-SN,>1M&`8=AO@*lK}g9C%4vb|^_[2(dEcisu#UoGL{P?B5$z0WmVFZ7j<xT/;RkY~fDp";
//	string level3="~GWe39{DK5L$_k(H&[joS7?J@M]}zywdx+<:*iB=Vs/%N^4-b)YI>R2AEtpOQ#X8Chgn.|mPZv6,FrlTq1f;`Uau!c0";
//	string level4="}uLHtV(zQ>S)h{fJo*xy#|MaTkpj/`].bgAPEn4_edlD[KIR!$v2r;Yi&O3^XC97w@-~sZ,05+qmN6:1GW8Bc=<U%?F";
//	string level5="o[:jvq0#Y%2}-{`CA.MT&KzW?=]b6mSget9rUO8D4,_y~wPs>$X<Rki@LpGxf;)3F*I|NcV1/lB7^EH!hnaQ+J5Z(ud";
//	string level6="^oGCjVcWs-{m9HB&eOP1R?/zUS%yixfw_v<dDJ`u]@qgA$a8L|Xrk(;.=,QT)20pMF!65~hZEI:t*K7Y3#+4nl}[>Nb";
//	string level7="@KN5+a[WOMD>`]yhn,4;dm|Cu17vkL*-BR}%Vc3xf?irASYI8<H~UP$=tFgl{J2.X6QjeZ#p&_^!wo09/zbT)qsGE:(";
//	string level8="O0dqK^p9.cTHi=5LsF<!_EIlSUN?JGv}y4ZX;k+>M(`bRAx8*QCea&m{7z-VohtYPBg]%$1|~/3n)#W,D2@j:rfw6[u";
//	string level9="uGiC[-5B0d=?#AEPH)szM1jVh@7%_n6q;}elQ(LOtx`Ug*pwND>:$/~],Kv23+YoWf!^X|<bR.TS9mcJFa8Z{r&y4Ik";
//	string level10="`O:eo,_8=m>yC%[X#P-FBfIqVr){c$NAaY0/Db?TL@h^v;5.R}n~z(K1|jUdkpQ]GEHgtlsu4WiwxJ96732<M+!S&Z*";
//	int turn=1;
//	int place=0;
//	string temp="";
//
//	while(turn<=line.length())
//	{
//		temp=line.substr(turn-1,1);
//		place=basic.find_first_of(temp);
//		if(turn%10==0)
//		{
//			line.replace(turn-1,1,level1.substr(place,1));
//		}
//		else if(turn%10==1)
//		{
//			line.replace(turn-1,1,level2.substr(place,1));
//		}
//		else if(turn%10==2)
//		{
//			line.replace(turn-1,1,level3.substr(place,1));
//		}
//		else if(turn%10==3)
//		{
//			line.replace(turn-1,1,level4.substr(place,1));
//		}
//		else if(turn%10==4)
//		{
//			line.replace(turn-1,1,level5.substr(place,1));
//		}
//		else if(turn%10==5)
//		{
//			line.replace(turn-1,1,level6.substr(place,1));
//		}
//		else if(turn%10==6)
//		{
//			line.replace(turn-1,1,level7.substr(place,1));
//		}
//		else if(turn%10==7)
//		{
//			line.replace(turn-1,1,level8.substr(place,1));
//		}
//		else if(turn%10==8)
//		{
//			line.replace(turn-1,1,level9.substr(place,1));
//		}
//		else if(turn%10==9)
//		{
//			line.replace(turn-1,1,level10.substr(place,1));
//		}
//		turn++;
//	}
//	crypt++;
//	if(crypt<100)
//		encrypt(0);
//}
//
//void decrypt(int seed)
//{
//	if(crypt==0)
//		crypt+=seed;
//	string basic="!#$%&()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~";
//	string level1="|?E{+Tkcw%O9o$<gPKh.L2^p8qj/ld*,Be0f3XImJ:!zxVa;yr~R`ZN[&}M1>C4i]QW_=-bU57S@(uAFYt6)Hn#sDGv";
//	string level2="H6t+nq.3!)Q]a:rXJyIwhe-SN,>1M&`8=AO@*lK}g9C%4vb|^_[2(dEcisu#UoGL{P?B5$z0WmVFZ7j<xT/;RkY~fDp";
//	string level3="~GWe39{DK5L$_k(H&[joS7?J@M]}zywdx+<:*iB=Vs/%N^4-b)YI>R2AEtpOQ#X8Chgn.|mPZv6,FrlTq1f;`Uau!c0";
//	string level4="}uLHtV(zQ>S)h{fJo*xy#|MaTkpj/`].bgAPEn4_edlD[KIR!$v2r;Yi&O3^XC97w@-~sZ,05+qmN6:1GW8Bc=<U%?F";
//	string level5="o[:jvq0#Y%2}-{`CA.MT&KzW?=]b6mSget9rUO8D4,_y~wPs>$X<Rki@LpGxf;)3F*I|NcV1/lB7^EH!hnaQ+J5Z(ud";
//	string level6="^oGCjVcWs-{m9HB&eOP1R?/zUS%yixfw_v<dDJ`u]@qgA$a8L|Xrk(;.=,QT)20pMF!65~hZEI:t*K7Y3#+4nl}[>Nb";
//	string level7="@KN5+a[WOMD>`]yhn,4;dm|Cu17vkL*-BR}%Vc3xf?irASYI8<H~UP$=tFgl{J2.X6QjeZ#p&_^!wo09/zbT)qsGE:(";
//	string level8="O0dqK^p9.cTHi=5LsF<!_EIlSUN?JGv}y4ZX;k+>M(`bRAx8*QCea&m{7z-VohtYPBg]%$1|~/3n)#W,D2@j:rfw6[u";
//	string level9="uGiC[-5B0d=?#AEPH)szM1jVh@7%_n6q;}elQ(LOtx`Ug*pwND>:$/~],Kv23+YoWf!^X|<bR.TS9mcJFa8Z{r&y4Ik";
//	string level10="`O:eo,_8=m>yC%[X#P-FBfIqVr){c$NAaY0/Db?TL@h^v;5.R}n~z(K1|jUdkpQ]GEHgtlsu4WiwxJ96732<M+!S&Z*";
//	int turn=1;
//	int place=0;
//	string temp="";
//
//	while(turn<=line.length())
//	{
//		temp=line.substr(turn-1,1);
//		if(turn%10==0)
//		{
//			place=level1.find_first_of(temp);
//		}
//		else if(turn%10==1)
//		{
//			place=level2.find_first_of(temp);
//		}
//		else if(turn%10==2)
//		{
//			place=level3.find_first_of(temp);
//		}
//		else if(turn%10==3)
//		{
//			place=level4.find_first_of(temp);
//		}
//		else if(turn%10==4)
//		{
//			place=level5.find_first_of(temp);
//		}
//		else if(turn%10==5)
//		{
//			place=level6.find_first_of(temp);
//		}
//		else if(turn%10==6)
//		{
//			place=level7.find_first_of(temp);
//		}
//		else if(turn%10==7)
//		{
//			place=level8.find_first_of(temp);
//		}
//		else if(turn%10==8)
//		{
//			place=level9.find_first_of(temp);
//		}
//		else if(turn%10==9)
//		{
//			place=level10.find_first_of(temp);
//		}
//		line.replace(turn-1,1,basic.substr(place,1));
//
//		turn++;
//	}
//	crypt++;
//	if(crypt<100)
//		decrypt(0);
//}