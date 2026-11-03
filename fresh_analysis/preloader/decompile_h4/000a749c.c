// File offset entry: 0xa749c

ulong FUN_020a739c(undefined4 *param_1)

{
  uint *puVar1;
  int iVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  ulong uVar7;
  ulong uVar8;
  ulong unaff_x22;
  undefined1 auVar9 [12];
  uint uStack_8c;
  long lStack_88;
  undefined1 *puStack_80;
  code *pcStack_78;
  ulong uStack_70;
  undefined8 uStack_68;
  ulong uStack_60;
  undefined4 *puStack_58;
  int iStack_4c;
  long lStack_48;
  
  lStack_48 = *plRam0000000002103280;
  *param_1 = 0;
  iStack_4c = 0;
  if (uRam0000000002100f30 == 0) {
    uVar8 = 0;
  }
  else {
    unaff_x22 = 0;
    uVar8 = 0;
    do {
      uVar7 = func_0x020a72b8((int)unaff_x22 + uRam0000000002124904,&iStack_4c);
      if ((int)uVar7 != 0) goto LAB_020a7428;
      uVar4 = (int)unaff_x22 + 1;
      unaff_x22 = (ulong)uVar4;
      uVar8 = (ulong)(uint)(iStack_4c + (int)uVar8);
    } while (uVar4 < uRam0000000002100f30);
  }
  uVar7 = 0;
  *param_1 = (int)uVar8;
LAB_020a7428:
  if (*plRam0000000002103280 == lStack_48) {
    return uVar7;
  }
  iVar2 = FUN_02091090(uVar7);
  pcStack_78 = FUN_020a7460;
  uStack_68 = 0x2100000;
  lStack_88 = *plRam0000000002103280;
  uStack_8c = 0;
  puStack_80 = &stack0xffffffffffffffc0;
  uStack_70 = unaff_x22;
  uStack_60 = uVar8;
  puStack_58 = param_1;
  if (cRam0000000002124900 == '\0') {
    uVar4 = FUN_020ad98c(0,0x2124908);
    if (uVar4 == 0) {
      cRam0000000002124900 = '\x01';
      goto LAB_020a74a0;
    }
LAB_020a750c:
    uVar8 = (ulong)uVar4;
    uVar4 = 0;
    uVar3 = 0;
LAB_020a757c:
    FUN_0208ada0(0x20c5592,uVar4,uVar3,uRam0000000002100f34);
    FUN_0208ada0(0x20b708f,uVar8);
  }
  else {
LAB_020a74a0:
    if (uRam0000000002100f34 == 0xffffffff) {
      uVar4 = FUN_020a739c(&uStack_8c);
      if (uVar4 != 0) goto LAB_020a750c;
      uRam0000000002100f34 = uStack_8c;
      FUN_0208ada0(0x20caa75,iVar2,uRam0000000002124904,uRam0000000002100f30);
    }
    uVar8 = 0x104000;
    uVar4 = *(uint *)(lRam00000000021037f8 + 0x454);
    uVar3 = *(uint *)(lRam0000000002103800 + 0x70);
    if (uVar4 != *(uint *)(lRam0000000002103800 + 0x70)) goto LAB_020a757c;
    uVar3 = uVar4;
    if (iVar2 != 0) {
      uVar8 = 0x104003;
      goto LAB_020a757c;
    }
    if (uVar4 < uRam0000000002100f34) {
      uVar8 = 0x104001;
      goto LAB_020a757c;
    }
    if (uRam0000000002100f30 * 0x20 - 1 < uVar4) {
      uVar8 = 0x104002;
      goto LAB_020a757c;
    }
    if (uVar4 < uRam0000000002100f38) {
      uRam0000000002100f38 = uVar4;
    }
    FUN_0208ada0(0x20c5592,uVar4,uVar4);
    FUN_0208ada0(0x20cc927);
    uVar8 = 0;
  }
  if (*plRam0000000002103280 == lStack_88) {
    return uVar8;
  }
  auVar9 = FUN_02091090();
  if (cRam0000000002124900 == '\0') {
    uVar4 = FUN_020ad98c(0,0x2124908);
    if (uVar4 != 0) {
      uVar3 = 0;
      goto LAB_020a7714;
    }
    cRam0000000002124900 = '\x01';
  }
  uVar4 = 0x104003;
  uVar3 = func_0x020ac1b0(0);
  iVar2 = func_0x0208c5b0();
  if (iVar2 != 0) {
    uVar3 = uVar3 | 0x20000;
  }
  uVar5 = auVar9._0_4_;
  if (uVar5 < 8) {
    if (auVar9._8_4_ < 0x20) {
      uVar5 = ~(-1 << (ulong)(auVar9._8_4_ & 0x1f));
    }
    else if ((uVar5 < uRam0000000002124904) ||
            ((uRam0000000002124904 + uRam0000000002100f30) - 1 <= uVar5)) {
      uVar5 = 0x7fffffff;
    }
    else {
      uVar5 = 0xffffffff;
    }
    puVar1 = (uint *)(lRam0000000002124908 + (auVar9._0_8_ & 0xffffffff) * 4);
    if (*puVar1 == uVar5) {
      uVar4 = func_0x020ac13c();
    }
    else {
      uVar4 = func_0x020ac0c4(puVar1);
      if ((uVar4 == 0) && (uVar4 = func_0x020ac13c(), uVar4 == 0)) {
        if (uVar5 == *puVar1) {
          uVar4 = 0;
        }
        else {
          FUN_0208ada0(0x20cdbe3,uVar5,*puVar1);
          uVar4 = 0x104005;
        }
      }
    }
  }
LAB_020a7714:
  uVar5 = func_0x020ac13c();
  iVar2 = func_0x0208c664();
  if (iVar2 != 0) {
    uVar5 = uVar5 | 0x20000;
  }
  uVar6 = func_0x020ac1b0(1);
  uVar3 = uVar6 | uVar3 | uVar5;
  if (uVar4 != 0) {
    uVar3 = uVar4;
  }
  return (ulong)uVar3;
}


