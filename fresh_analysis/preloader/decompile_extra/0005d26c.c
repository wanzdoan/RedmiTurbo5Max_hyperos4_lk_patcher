// File offset entry: 0x5d26c

int FUN_0205d16c(undefined4 param_1,undefined8 param_2,long param_3,long param_4)

{
  long lVar1;
  uint uVar2;
  long *plVar3;
  int iVar4;
  uint uVar5;
  long lVar6;
  undefined8 uVar7;
  uint extraout_w8;
  ulong extraout_x8;
  ulong extraout_x8_00;
  ulong extraout_x8_01;
  long extraout_x8_02;
  uint extraout_w9;
  ulong extraout_x9;
  ulong extraout_x9_00;
  long extraout_x10;
  ulong extraout_x11;
  int extraout_w12;
  uint extraout_w13;
  int extraout_w14;
  int extraout_w15;
  ulong uVar8;
  long lVar9;
  
  plVar3 = plRam0000000002103338;
  FUN_0205ca98(*plRam0000000002103338 + 0x610a0);
  lVar6 = FUN_020036c8(1);
  if (lVar6 == 0) {
    uVar7 = 0x20c1d33;
LAB_0205d1f8:
    FUN_0208ada0(uVar7,0x20ca029);
    return 1;
  }
  iVar4 = FUN_0205d430(param_1,param_2,param_3,*(undefined4 *)(lVar6 + 4),*plVar3 + 0x610a0);
  if (iVar4 != 0) {
    uVar7 = 0x20b9b94;
    goto LAB_0205d1f8;
  }
  func_0x0205d8dc();
  uVar5 = (uint)(extraout_x8 | extraout_x9);
  if (uVar5 < 0xc1) {
    func_0x0205d8dc();
    if ((extraout_w8 | extraout_w9) != 0x80) {
      FUN_0205d900(0x20b2941);
      goto LAB_0205d284;
    }
    uVar2 = *(uint *)(lVar6 + 4);
    uVar8 = 0;
    if ((ulong)(uVar2 >> 7) != 0) {
      uVar8 = (((extraout_x8 | extraout_x9) + (ulong)(uVar2 >> 7)) - 1) / (ulong)(uVar2 >> 7);
    }
    if (uVar8 * uVar2 < 0x4001) {
      iVar4 = FUN_0205d648(param_4,param_1,
                           (ulong)*(byte *)(param_3 + 0x4a) << 0x10 |
                           (ulong)*(byte *)(param_3 + 0x4b) << 0x18 |
                           (ulong)*(ushort *)(param_3 + 0x48) |
                           (ulong)((uint)*(byte *)(param_3 + 0x4e) << 0x10 |
                                   (uint)*(byte *)(param_3 + 0x4f) << 0x18 |
                                  (uint)*(ushort *)(param_3 + 0x4c)) << 0x20);
      if (iVar4 != 0) {
        func_0x0205d914(*(undefined1 *)(param_3 + 0x48));
        FUN_0208ada0(0x20bfc9e,0x20ca029,param_1,
                     extraout_x11 | extraout_x10 << 0x18 | extraout_x8_00 |
                     (ulong)(extraout_w14 << 0x10 | extraout_w15 << 0x18 |
                            extraout_w13 | extraout_w12 << 8) << 0x20,iVar4);
        return iVar4;
      }
      uVar5 = func_0x0205cae8(0xffffffff,param_4,uVar5 << 7,*plVar3 + 0x610a0);
      if (((uint)*(byte *)(param_3 + 0x5a) << 0x10 | (uint)*(byte *)(param_3 + 0x5b) << 0x18 |
          (uint)*(ushort *)(param_3 + 0x58)) != ~uVar5) {
        FUN_0208ada0(0x20b290e,0x20ca029);
        return 1;
      }
      lVar6 = 0;
      uVar8 = 0;
      lVar9 = 0x7a2a8;
      while ((func_0x0205d8dc(), uVar8 < (extraout_x8_01 | extraout_x9_00) &&
             (func_0x0205d88c(param_4), extraout_x8_02 != 0))) {
        lVar1 = lRam000000000211bce8 + lVar6;
        *(long *)(lVar1 + 0x20) = *plVar3 + lVar9;
        func_0x0205d6e4(param_1,lVar1,param_4);
        uVar8 = uVar8 + 1;
        lVar6 = lVar6 + 0x28;
        param_4 = param_4 + 0x80;
        lVar9 = lVar9 + 0x50;
      }
      return 0;
    }
    uVar7 = 0x20b47ca;
  }
  else {
    uVar7 = 0x20c70f6;
  }
  FUN_0205d900(uVar7);
LAB_0205d284:
  FUN_0208ada0();
  return 1;
}


