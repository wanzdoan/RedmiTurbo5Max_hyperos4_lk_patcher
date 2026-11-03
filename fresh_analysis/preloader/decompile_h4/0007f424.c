// File offset entry: 0x7f424

undefined8 FUN_0207f324(long param_1,long *param_2,uint *param_3,undefined8 param_4)

{
  uint uVar1;
  undefined1 uVar2;
  int iVar3;
  undefined8 uVar4;
  ulong uVar5;
  ulong uVar6;
  int iStack_258;
  int iStack_254;
  undefined1 auStack_250 [60];
  int iStack_214;
  undefined8 uStack_58;
  
  uStack_58 = *puRam0000000002103280;
  func_0x02091664(&iStack_258,0,0x200);
  uVar6 = param_2[1] * (ulong)*(uint *)(param_1 + 4);
  func_0x02091664(&iStack_258,0,0x200);
  uVar1 = *param_3;
  do {
    uVar5 = (ulong)uVar1;
    uVar2 = uVar6 - uVar5 == 0;
    if (uVar6 < uVar5 || (bool)uVar2) {
      func_0x0208ada0(0x20bdd42,0x20c744a,param_4);
LAB_0207f45c:
      uVar4 = 0xffffffff;
LAB_0207f460:
      uVar4 = func_0x0207f4b0(uVar4);
      if ((bool)uVar2) {
        return uVar4;
      }
      func_0x02091090();
      return 0x20cb7c2;
    }
    iVar3 = func_0x02003ac0(param_1,uVar5 + *param_2 * (ulong)*(uint *)(param_1 + 4),0x200,
                            &iStack_258,(int)param_2[3]);
    if (iVar3 != 0) {
      uVar4 = 0x20cfcd4;
LAB_0207f450:
      func_0x0208ada0(uVar4,0x20c744a);
      goto LAB_0207f45c;
    }
    uVar2 = iStack_258 == 0x58881688;
    if (!(bool)uVar2) {
      uVar4 = 0x20bae65;
      goto LAB_0207f450;
    }
    uVar4 = func_0x0209156c(auStack_250,param_4,0x20);
    if ((int)uVar4 == 0) goto LAB_0207f460;
    uVar1 = *param_3 + ((iStack_254 + iStack_214) - 1U & -iStack_214) + 0x200;
    *param_3 = uVar1;
  } while( true );
}


