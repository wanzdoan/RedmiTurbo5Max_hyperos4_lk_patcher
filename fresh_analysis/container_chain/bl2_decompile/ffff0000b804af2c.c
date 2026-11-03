
ulong FUN_ffff0000b804af2c(long param_1,ulong param_2,ulong param_3,ulong param_4)

{
  uint uVar1;
  long lVar2;
  ulong uVar3;
  ulong uVar4;
  long lVar5;
  ulong uVar6;
  uint uVar7;
  uint uVar8;
  ulong uVar9;
  int iVar10;
  uint uVar11;
  
  lVar2 = FUN_ffff0000b804bd14(0x40,*(undefined8 *)(param_1 + 0x28));
  if (lVar2 == 0) {
    return 0xfffffffffffffffb;
  }
  uVar4 = *(ulong *)(param_1 + 0x28);
  uVar9 = 0;
  if (uVar4 != 0) {
    uVar9 = param_3 / uVar4;
  }
  if (param_3 == uVar9 * uVar4) {
    uVar6 = 0;
LAB_ffff0000b804b058:
    uVar8 = (uint)uVar9;
    if ((((uint)((param_2 & 0x3f) != 0) & *(uint *)(param_1 + 0x6c) >> 1) == 0) &&
       (((uint)((param_2 & 3) != 0) & *(uint *)(param_1 + 0x6c) >> 2) == 0)) {
      uVar3 = 0;
      uVar7 = *(uint *)(param_1 + 0x38);
      uVar1 = (uint)param_4 >> (ulong)(*(uint *)(param_1 + 0x30) & 0x1f);
      uVar11 = uVar7 - uVar8;
      if (uVar1 + uVar8 <= uVar7) {
        uVar11 = uVar1;
      }
      if (((uVar8 <= uVar7) && (uVar1 != 0)) && (uVar11 != 0)) {
        uVar3 = (**(code **)(param_1 + 0x88))(param_1,param_2,uVar9 & 0xffffffff);
        if ((long)uVar3 < 0) goto LAB_ffff0000b804b22c;
        uVar4 = *(ulong *)(param_1 + 0x28);
      }
      if (uVar3 != uVar4 * uVar1) goto LAB_ffff0000b804b21c;
      param_2 = param_2 + uVar3;
      param_4 = param_4 - uVar3;
      uVar6 = uVar3 + uVar6;
      uVar8 = uVar1 + uVar8;
    }
    else if (uVar4 <= param_4) {
      iVar10 = -uVar8;
      uVar11 = uVar8;
      do {
        uVar11 = uVar11 + 1;
        FUN_ffff0000b804cd40(lVar2,param_2);
        uVar1 = *(uint *)(param_1 + 0x38);
        uVar4 = 0;
        uVar7 = uVar1;
        if (uVar11 <= uVar1) {
          uVar7 = uVar11;
        }
        uVar8 = (uint)uVar9;
        if (((uVar8 <= uVar1) && (uVar7 != uVar8)) &&
           (uVar4 = (**(code **)(param_1 + 0x88))(param_1,lVar2,uVar9 & 0xffffffff,uVar7 + iVar10),
           uVar3 = uVar4, (long)uVar4 < 0)) goto LAB_ffff0000b804b22c;
        if (uVar4 != *(ulong *)(param_1 + 0x28)) goto LAB_ffff0000b804b21c;
        param_2 = param_2 + uVar4;
        param_4 = param_4 - uVar4;
        uVar6 = uVar4 + uVar6;
        iVar10 = iVar10 + -1;
        uVar8 = uVar8 + 1;
        uVar9 = (ulong)uVar8;
      } while (uVar4 <= param_4);
    }
    uVar3 = uVar6;
    if (param_4 == 0) goto LAB_ffff0000b804b22c;
    uVar11 = *(uint *)(param_1 + 0x38);
    uVar3 = 0;
    iVar10 = uVar11 - uVar8;
    if (uVar8 + 1 <= uVar11) {
      iVar10 = 1;
    }
    if ((uVar8 <= uVar11) && (iVar10 != 0)) {
      uVar3 = (**(code **)(param_1 + 0x78))(param_1,lVar2,uVar8);
      if ((long)uVar3 < 0) goto LAB_ffff0000b804b22c;
      uVar4 = *(ulong *)(param_1 + 0x28);
    }
    if (uVar3 == uVar4) {
      FUN_ffff0000b804cd40(lVar2,param_2,param_4);
      uVar11 = *(uint *)(param_1 + 0x38);
      uVar3 = 0;
      iVar10 = uVar11 - uVar8;
      if (uVar8 + 1 <= uVar11) {
        iVar10 = 1;
      }
      if (((uVar8 <= uVar11) && (iVar10 != 0)) &&
         (uVar3 = (**(code **)(param_1 + 0x88))(param_1,lVar2,uVar8), (long)uVar3 < 0))
      goto LAB_ffff0000b804b22c;
      if (uVar3 == *(ulong *)(param_1 + 0x28)) {
        uVar3 = uVar6 + param_4;
        goto LAB_ffff0000b804b22c;
      }
    }
  }
  else {
    uVar11 = *(uint *)(param_1 + 0x38);
    uVar7 = (uint)uVar9;
    uVar8 = uVar7 + 1;
    iVar10 = uVar11 - uVar7;
    if (uVar8 <= uVar11) {
      iVar10 = 1;
    }
    if (uVar11 < uVar7 || iVar10 == 0) {
      uVar6 = 0;
      if (uVar4 == 0) {
LAB_ffff0000b804afe0:
        uVar4 = 0;
        if (uVar6 != 0) {
          uVar4 = param_3 / uVar6;
        }
        lVar5 = param_3 - uVar4 * uVar6;
        uVar6 = uVar6 - lVar5;
        if (param_4 <= uVar6) {
          uVar6 = param_4;
        }
        FUN_ffff0000b804cd40(lVar2 + lVar5,param_2,uVar6);
        uVar11 = *(uint *)(param_1 + 0x38);
        uVar4 = 0;
        iVar10 = uVar11 - uVar7;
        if (uVar8 <= uVar11) {
          iVar10 = 1;
        }
        if (((uVar7 <= uVar11) && (iVar10 != 0)) &&
           (uVar4 = (**(code **)(param_1 + 0x88))(param_1,lVar2,uVar9 & 0xffffffff), uVar3 = uVar4,
           (long)uVar4 < 0)) goto LAB_ffff0000b804b22c;
        if (uVar4 == *(ulong *)(param_1 + 0x28)) {
          param_2 = param_2 + uVar6;
          param_4 = param_4 - uVar6;
          uVar9 = (ulong)uVar8;
          goto LAB_ffff0000b804b058;
        }
      }
    }
    else {
      uVar3 = (**(code **)(param_1 + 0x78))(param_1,lVar2,uVar9 & 0xffffffff);
      if ((long)uVar3 < 0) goto LAB_ffff0000b804b22c;
      uVar6 = *(ulong *)(param_1 + 0x28);
      if (uVar3 == uVar6) goto LAB_ffff0000b804afe0;
    }
  }
LAB_ffff0000b804b21c:
  uVar3 = 0xffffffffffffffec;
LAB_ffff0000b804b22c:
  thunk_FUN_ffff0000b8082558(lVar2);
  return uVar3;
}


