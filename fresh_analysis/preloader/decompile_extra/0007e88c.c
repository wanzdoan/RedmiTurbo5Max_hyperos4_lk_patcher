// File offset entry: 0x7e88c

void FUN_0207e78c(undefined8 param_1,undefined8 param_2,ulong param_3,ulong param_4,ulong param_5)

{
  undefined1 in_ZR;
  undefined1 uVar1;
  uint uVar2;
  undefined8 uVar3;
  long *plVar4;
  ulong uVar5;
  ulong uVar6;
  ulong uVar7;
  ulong uVar8;
  ulong uVar9;
  undefined1 auVar10 [16];
  undefined1 auStack_168 [72];
  undefined1 **ppuStack_120;
  code *pcStack_118;
  ulong uStack_110;
  ulong uStack_100;
  ulong uStack_f8;
  ulong uStack_f0;
  ulong uStack_e8;
  uint uStack_dc;
  long lStack_d8;
  undefined1 *puStack_d0;
  code *pcStack_c8;
  undefined1 auStack_88 [72];
  
  auVar10 = FUN_0207ea1c();
  uVar7 = auVar10._8_8_;
  uVar6 = param_3;
  if (auVar10._0_8_ == 0) {
    uVar3 = 0x200002;
LAB_0207e810:
    uVar9 = param_5;
    param_3 = param_4;
    uVar7 = uVar6;
    FUN_02063328(uVar3);
    uVar3 = 0xffffffffffffffff;
  }
  else {
    uVar9 = param_4 & 0xffffffff;
    uVar2 = FUN_02091524();
    in_ZR = (uVar2 & 0xffffffc0) == 0;
    if (!(bool)in_ZR) {
      uVar3 = FUN_0207ea80();
      goto LAB_0207e810;
    }
    FUN_020915ac(auStack_88,auVar10._0_8_,uVar2);
    uVar6 = 0;
    auStack_88[uVar2] = 0;
    uVar3 = FUN_0207e3d0(auStack_88,0x40);
    if ((int)uVar3 != 0) goto LAB_0207e810;
    uVar3 = FUN_0207e850(uVar3,auStack_88);
  }
  FUN_0207ea48(uVar3);
  if ((bool)in_ZR) {
    FUN_0207ea68();
    return;
  }
  auVar10 = FUN_02091090();
  uVar5 = auVar10._8_8_;
  pcStack_c8 = FUN_0207e850;
  uVar9 = uVar9 & 0xffffffff;
  lStack_d8 = *plRam0000000002103280;
  uStack_dc = 0;
  uVar6 = uVar7;
  uVar8 = param_3;
  puStack_d0 = &stack0xffffffffffffffc0;
  plVar4 = (long *)FUN_0207e380(uVar5);
  if (plVar4 == (long *)0x0) {
    uVar3 = 0x200000;
LAB_0207e8ec:
    FUN_02063328(uVar3);
LAB_0207e8f0:
    uVar3 = 0xffffffffffffffff;
  }
  else {
    uVar3 = FUN_0209132c(&uStack_dc);
    if ((int)uVar3 != 0) goto LAB_0207e8ec;
    uVar8 = plVar4[1] * (ulong)uStack_dc;
    if (uVar8 < uVar7 + uVar9) {
      uVar6 = uVar5;
      FUN_0208ada0(0x20b9fac,0x20c958b,uVar5,uVar8,uVar9);
      goto LAB_0207e8f0;
    }
    uVar6 = param_3;
    uVar8 = uVar9;
    if (auVar10._0_4_ == 0) {
      uVar3 = FUN_02091394(3,uVar7 + *plVar4 * (ulong)uStack_dc,param_3,uVar9);
    }
    else {
      uVar3 = FUN_02091450();
    }
  }
  uVar1 = lRam0000000002100880 == lStack_d8;
  if ((bool)uVar1) {
    return;
  }
  FUN_02091090(uVar3);
  pcStack_118 = FUN_0207e954;
  ppuStack_120 = &puStack_d0;
  uStack_110 = uVar7;
  uStack_100 = auVar10._0_8_ & 0xffffffff;
  uStack_f8 = uVar5;
  uStack_f0 = param_3;
  uStack_e8 = uVar9;
  auVar10 = FUN_0207ea1c();
  if (auVar10._0_8_ == 0) {
    uVar3 = 0x200002;
  }
  else {
    uVar2 = FUN_02091524();
    uVar1 = (uVar2 & 0xffffffc0) == 0;
    if ((bool)uVar1) {
      FUN_020915ac(auStack_168,auVar10._0_8_,uVar2);
      auStack_168[uVar2] = 0;
      uVar3 = FUN_0207e3d0(auStack_168,0x40,0);
      if ((int)uVar3 == 0) {
        uVar3 = FUN_0207e850(1,auStack_168,auVar10._8_8_,uVar6,uVar8 & 0xffffffff);
        goto LAB_0207e9e0;
      }
    }
    else {
      uVar3 = FUN_0207ea80();
    }
  }
  FUN_02063328(uVar3);
  uVar3 = 0xffffffffffffffff;
LAB_0207e9e0:
  FUN_0207ea48(uVar3);
  if ((bool)uVar1) {
    FUN_0207ea68();
    return;
  }
  FUN_02091090();
  return;
}


