// File offset entry: 0xa7560

uint FUN_020a7460(int param_1)

{
  uint *puVar1;
  uint uVar2;
  int iVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  undefined1 auVar7 [12];
  uint uStack_3c;
  long lStack_38;
  
  lStack_38 = *plRam0000000002103280;
  uStack_3c = 0;
  if (cRam0000000002124900 == '\0') {
    uVar4 = func_0x020ad98c(0,0x2124908);
    if (uVar4 == 0) {
      cRam0000000002124900 = '\x01';
      goto LAB_020a74a0;
    }
LAB_020a750c:
    uVar2 = 0;
    uVar5 = 0;
LAB_020a757c:
    FUN_0208ada0(0x20c5592,uVar2,uVar5,uRam0000000002100f34);
    FUN_0208ada0(0x20b708f,uVar4);
  }
  else {
LAB_020a74a0:
    if (uRam0000000002100f34 == 0xffffffff) {
      uVar4 = func_0x020a739c(&uStack_3c);
      if (uVar4 != 0) goto LAB_020a750c;
      uRam0000000002100f34 = uStack_3c;
      FUN_0208ada0(0x20caa75,param_1,uRam0000000002124904,iRam0000000002100f30);
    }
    uVar4 = 0x104000;
    uVar2 = *(uint *)(lRam00000000021037f8 + 0x454);
    uVar5 = *(uint *)(lRam0000000002103800 + 0x70);
    if (uVar2 != *(uint *)(lRam0000000002103800 + 0x70)) goto LAB_020a757c;
    uVar5 = uVar2;
    if (param_1 != 0) {
      uVar4 = 0x104003;
      goto LAB_020a757c;
    }
    if (uVar2 < uRam0000000002100f34) {
      uVar4 = 0x104001;
      goto LAB_020a757c;
    }
    if (iRam0000000002100f30 * 0x20 - 1U < uVar2) {
      uVar4 = 0x104002;
      goto LAB_020a757c;
    }
    if (uVar2 < uRam0000000002100f38) {
      uRam0000000002100f38 = uVar2;
    }
    FUN_0208ada0(0x20c5592,uVar2,uVar2);
    FUN_0208ada0(0x20cc927);
    uVar4 = 0;
  }
  if (*plRam0000000002103280 == lStack_38) {
    return uVar4;
  }
  auVar7 = FUN_02091090();
  if (cRam0000000002124900 == '\0') {
    uVar4 = func_0x020ad98c(0,0x2124908);
    if (uVar4 != 0) {
      uVar2 = 0;
      goto LAB_020a7714;
    }
    cRam0000000002124900 = '\x01';
  }
  uVar4 = 0x104003;
  uVar2 = func_0x020ac1b0(0);
  iVar3 = func_0x0208c5b0();
  if (iVar3 != 0) {
    uVar2 = uVar2 | 0x20000;
  }
  uVar5 = auVar7._0_4_;
  if (uVar5 < 8) {
    if (auVar7._8_4_ < 0x20) {
      uVar5 = ~(-1 << (ulong)(auVar7._8_4_ & 0x1f));
    }
    else if ((uVar5 < uRam0000000002124904) ||
            ((uRam0000000002124904 + iRam0000000002100f30) - 1 <= uVar5)) {
      uVar5 = 0x7fffffff;
    }
    else {
      uVar5 = 0xffffffff;
    }
    puVar1 = (uint *)(lRam0000000002124908 + (auVar7._0_8_ & 0xffffffff) * 4);
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
  iVar3 = func_0x0208c664();
  if (iVar3 != 0) {
    uVar5 = uVar5 | 0x20000;
  }
  uVar6 = func_0x020ac1b0(1);
  uVar2 = uVar6 | uVar2 | uVar5;
  if (uVar4 != 0) {
    uVar2 = uVar4;
  }
  return uVar2;
}


