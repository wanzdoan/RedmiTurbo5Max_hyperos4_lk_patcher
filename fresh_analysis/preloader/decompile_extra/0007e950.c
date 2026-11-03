// File offset entry: 0x7e950

void FUN_0207e850(uint param_1,ulong param_2,ulong param_3,ulong param_4,ulong param_5)

{
  undefined1 uVar1;
  uint uVar2;
  long *plVar3;
  undefined8 uVar4;
  ulong uVar5;
  ulong uVar6;
  undefined1 auVar7 [16];
  undefined1 auStack_d8 [72];
  undefined1 *puStack_90;
  code *pcStack_88;
  ulong uStack_80;
  ulong uStack_70;
  ulong uStack_68;
  ulong uStack_60;
  ulong uStack_58;
  uint uStack_4c;
  long lStack_48;
  
  param_5 = param_5 & 0xffffffff;
  lStack_48 = *plRam0000000002103280;
  uStack_4c = 0;
  uVar5 = param_3;
  uVar6 = param_4;
  plVar3 = (long *)FUN_0207e380(param_2);
  if (plVar3 == (long *)0x0) {
    uVar4 = 0x200000;
LAB_0207e8ec:
    FUN_02063328(uVar4);
LAB_0207e8f0:
    uVar4 = 0xffffffffffffffff;
  }
  else {
    uVar4 = FUN_0209132c(&uStack_4c);
    if ((int)uVar4 != 0) goto LAB_0207e8ec;
    uVar6 = plVar3[1] * (ulong)uStack_4c;
    if (uVar6 < param_3 + param_5) {
      uVar5 = param_2;
      FUN_0208ada0(0x20b9fac,0x20c958b,param_2,uVar6,param_5);
      goto LAB_0207e8f0;
    }
    uVar5 = param_4;
    uVar6 = param_5;
    if (param_1 == 0) {
      uVar4 = FUN_02091394(3,param_3 + *plVar3 * (ulong)uStack_4c,param_4,param_5);
    }
    else {
      uVar4 = FUN_02091450();
    }
  }
  uVar1 = lRam0000000002100880 == lStack_48;
  if ((bool)uVar1) {
    return;
  }
  FUN_02091090(uVar4);
  pcStack_88 = FUN_0207e954;
  puStack_90 = &stack0xffffffffffffffc0;
  uStack_80 = param_3;
  uStack_70 = (ulong)param_1;
  uStack_68 = param_2;
  uStack_60 = param_4;
  uStack_58 = param_5;
  auVar7 = FUN_0207ea1c();
  if (auVar7._0_8_ == 0) {
    uVar4 = 0x200002;
  }
  else {
    uVar2 = FUN_02091524();
    uVar1 = (uVar2 & 0xffffffc0) == 0;
    if ((bool)uVar1) {
      FUN_020915ac(auStack_d8,auVar7._0_8_,uVar2);
      auStack_d8[uVar2] = 0;
      uVar4 = FUN_0207e3d0(auStack_d8,0x40,0);
      if ((int)uVar4 == 0) {
        uVar4 = FUN_0207e850(1,auStack_d8,auVar7._8_8_,uVar5,uVar6 & 0xffffffff);
        goto LAB_0207e9e0;
      }
    }
    else {
      uVar4 = FUN_0207ea80();
    }
  }
  FUN_02063328(uVar4);
  uVar4 = 0xffffffffffffffff;
LAB_0207e9e0:
  FUN_0207ea48(uVar4);
  if ((bool)uVar1) {
    FUN_0207ea68();
    return;
  }
  FUN_02091090();
  return;
}


