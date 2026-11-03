// File offset entry: 0x5cd04

long FUN_0205cc04(undefined4 param_1,undefined8 param_2,undefined8 param_3,uint param_4)

{
  ulong uVar1;
  uint uVar2;
  uint uVar3;
  int iVar4;
  uint uVar5;
  undefined4 uVar6;
  long lVar7;
  int *piVar8;
  ulong uVar9;
  undefined8 extraout_x1;
  long lVar10;
  long extraout_x8;
  ulong uVar11;
  undefined1 auStack_c4 [72];
  ushort uStack_7c;
  byte bStack_7a;
  byte bStack_79;
  ushort uStack_78;
  byte bStack_76;
  byte bStack_75;
  ushort uStack_74;
  byte bStack_72;
  byte bStack_71;
  ushort uStack_70;
  byte bStack_6e;
  byte bStack_6d;
  ushort uStack_6c;
  byte bStack_6a;
  byte bStack_69;
  long lStack_68;
  
  lStack_68 = *plRam0000000002103280;
  func_0x0205ca98(0x102d00);
  uRam0000000000103100 = 0;
  iVar4 = func_0x0205d430(param_1,param_2,0x101d00,param_4,0x102d00);
  if (iVar4 == 0) {
    FUN_02091664(auStack_c4,0,0x5c);
    FUN_020915ac(auStack_c4,0x101d00,0x5c);
    lVar7 = 0;
    uVar11 = ((ulong)bStack_6e << 0x10 | (ulong)bStack_6d << 0x18 | (ulong)uStack_70) *
             ((ulong)bStack_72 << 0x10 | (ulong)bStack_71 << 0x18 | (ulong)uStack_74);
    uVar5 = 0xffffffff;
    uVar9 = (ulong)bStack_7a << 0x10 | (ulong)bStack_79 << 0x18 | (ulong)uStack_7c |
            (ulong)((uint)bStack_76 << 0x10 | (uint)bStack_75 << 0x18 | (uint)uStack_78) << 0x20;
LAB_0205cd60:
    if (uVar11 != 0) {
      uVar1 = uVar11;
      if (0xfff < uVar11) {
        uVar1 = 0x1000;
      }
      iVar4 = func_0x0205d648(0x101d00,param_1,uVar9,uVar1);
      if (iVar4 != 0) {
        func_0x0205d900(0x20c5e50,extraout_x1,iVar4);
        FUN_0208ada0();
      }
      uVar2 = 0;
      if (param_4 != 0) {
        uVar2 = (uint)uVar1 / param_4;
      }
      uVar9 = uVar9 + uVar2;
      uVar5 = func_0x0205cae8(uVar5,0x101d00,uVar1 & 0xffffffff,0x102d00);
      uVar11 = uVar11 - uVar1;
      if ((int)lVar7 == 0) {
        uVar2 = (uint)bStack_6e << 0x10 | (uint)bStack_6d << 0x18 | (uint)uStack_70;
        uVar3 = 0;
        if (uVar2 != 0) {
          uVar3 = 0x1000 / uVar2;
        }
        lVar7 = (ulong)uVar3 + 1;
        lVar10 = 0x101d00;
        do {
          lVar7 = lVar7 + -1;
          if (lVar7 == 0) goto LAB_0205cd60;
          func_0x0205d88c(lVar10);
          if (extraout_x8 == 0) {
            lVar7 = 0;
            goto LAB_0205cd60;
          }
          lVar10 = lVar10 + 0x80;
          func_0x0205d6e4(param_1,0x103100);
          iVar4 = FUN_02091544(uRam0000000000103120,param_3);
        } while (iVar4 != 0);
        lVar7 = 1;
      }
      goto LAB_0205cd60;
    }
    if (((uint)bStack_6a << 0x10 | (uint)bStack_69 << 0x18 | (uint)uStack_6c) == ~uVar5) {
      if ((int)lVar7 == 0) {
        FUN_02091664(0x103100,0,0x28);
      }
      lVar7 = 0;
      goto LAB_0205ceb8;
    }
    FUN_0208ada0(0x20b290e,0x20ca029);
    FUN_02091664(0x103100,0,0x28);
  }
  else {
    FUN_0208ada0(0x20b9b94,0x20ca029);
  }
  lVar7 = 1;
LAB_0205ceb8:
  if (*plRam0000000002103280 == lStack_68) {
    return lVar7;
  }
  uVar6 = FUN_02091090(lVar7);
  piVar8 = (int *)FUN_020036c8(1);
  lVar7 = 0;
  if (piVar8 != (int *)0x0) {
    if (*piVar8 == 2) {
      uVar9 = (**(code **)(piVar8 + 0x12))(piVar8,uVar6);
      if (uVar9 == 0) {
        return 0;
      }
      uVar11 = 0;
      if ((ulong)(uint)piVar8[1] != 0) {
        uVar11 = uVar9 / (uint)piVar8[1];
      }
    }
    else {
      if (*piVar8 != 1) {
        return 0;
      }
      uVar11 = uRam000000000211be88 >> 9;
    }
    lVar7 = uVar11 - 1;
  }
  return lVar7;
}


