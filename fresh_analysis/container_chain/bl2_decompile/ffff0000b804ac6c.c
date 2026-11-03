
ulong FUN_ffff0000b804ac6c(long param_1,ulong param_2,ulong param_3,ulong param_4)

{
  uint uVar1;
  uint uVar2;
  long lVar3;
  ulong uVar4;
  ulong uVar5;
  long lVar6;
  uint uVar7;
  ulong uVar8;
  ulong uVar9;
  int iVar10;
  uint uVar11;
  
  lVar3 = FUN_ffff0000b804bd14(0x40,*(undefined8 *)(param_1 + 0x28));
  if (lVar3 == 0) {
    return 0xfffffffffffffffb;
  }
  uVar5 = *(ulong *)(param_1 + 0x28);
  uVar8 = 0;
  if (uVar5 != 0) {
    uVar8 = param_3 / uVar5;
  }
  if (param_3 == uVar8 * uVar5) {
    uVar5 = 0;
LAB_ffff0000b804ad58:
    uVar7 = (uint)uVar8;
    if (((*(uint *)(param_1 + 0x6c) & (uint)((param_2 & 0x3f) != 0)) == 0) &&
       (((uint)((param_2 & 3) != 0) & *(uint *)(param_1 + 0x6c) >> 2) == 0)) {
      uVar9 = 0;
      uVar1 = *(uint *)(param_1 + 0x38);
      uVar2 = (uint)param_4 >> (ulong)(*(uint *)(param_1 + 0x30) & 0x1f);
      uVar11 = uVar1 - uVar7;
      if (uVar2 + uVar7 <= uVar1) {
        uVar11 = uVar2;
      }
      if ((((uVar7 <= uVar1) && (uVar2 != 0)) && (uVar11 != 0)) &&
         (uVar9 = (**(code **)(param_1 + 0x78))(param_1,param_2,uVar8 & 0xffffffff), (long)uVar9 < 0
         )) goto LAB_ffff0000b804aee8;
      uVar4 = *(ulong *)(param_1 + 0x28);
      if (uVar9 != uVar4 * uVar2) goto LAB_ffff0000b804aee4;
      param_2 = param_2 + uVar9;
      param_4 = param_4 - uVar9;
      uVar5 = uVar9 + uVar5;
      uVar7 = uVar2 + uVar7;
    }
    else {
      uVar4 = *(ulong *)(param_1 + 0x28);
      if (uVar4 <= param_4) {
        iVar10 = -uVar7;
        uVar11 = uVar7;
        do {
          uVar11 = uVar11 + 1;
          uVar2 = *(uint *)(param_1 + 0x38);
          uVar9 = 0;
          uVar1 = uVar2;
          if (uVar11 <= uVar2) {
            uVar1 = uVar11;
          }
          uVar7 = (uint)uVar8;
          if ((uVar7 <= uVar2) && (uVar1 != uVar7)) {
            uVar9 = (**(code **)(param_1 + 0x78))(param_1,lVar3,uVar8 & 0xffffffff,uVar1 + iVar10);
            if ((long)uVar9 < 0) goto LAB_ffff0000b804aee8;
            uVar4 = *(ulong *)(param_1 + 0x28);
          }
          if (uVar9 != uVar4) goto LAB_ffff0000b804aee4;
          FUN_ffff0000b804cd40(param_2,lVar3);
          uVar4 = *(ulong *)(param_1 + 0x28);
          iVar10 = iVar10 + -1;
          uVar7 = uVar7 + 1;
          uVar8 = (ulong)uVar7;
          param_2 = param_2 + uVar4;
          param_4 = param_4 - uVar4;
          uVar5 = uVar4 + uVar5;
        } while (uVar4 <= param_4);
      }
    }
    uVar9 = uVar5;
    if (param_4 == 0) goto LAB_ffff0000b804aee8;
    uVar11 = *(uint *)(param_1 + 0x38);
    uVar9 = 0;
    iVar10 = uVar11 - uVar7;
    if (uVar7 + 1 <= uVar11) {
      iVar10 = 1;
    }
    if ((uVar7 <= uVar11) && (iVar10 != 0)) {
      uVar9 = (**(code **)(param_1 + 0x78))(param_1,lVar3,uVar7);
      if ((long)uVar9 < 0) goto LAB_ffff0000b804aee8;
      uVar4 = *(ulong *)(param_1 + 0x28);
    }
    if (uVar9 == uVar4) {
      FUN_ffff0000b804cd40(param_2,lVar3,param_4);
      uVar9 = uVar5 + param_4;
      goto LAB_ffff0000b804aee8;
    }
  }
  else {
    uVar7 = *(uint *)(param_1 + 0x38);
    uVar11 = (uint)uVar8;
    iVar10 = uVar7 - uVar11;
    if (uVar11 + 1 <= uVar7) {
      iVar10 = 1;
    }
    if (uVar7 < uVar11 || iVar10 == 0) {
      if (uVar5 == 0) {
        uVar5 = 0;
LAB_ffff0000b804ad28:
        uVar8 = 0;
        if (uVar5 != 0) {
          uVar8 = param_3 / uVar5;
        }
        lVar6 = param_3 - uVar8 * uVar5;
        uVar5 = uVar5 - lVar6;
        if (param_4 <= uVar5) {
          uVar5 = param_4;
        }
        FUN_ffff0000b804cd40(param_2,lVar3 + lVar6,uVar5);
        param_2 = param_2 + uVar5;
        param_4 = param_4 - uVar5;
        uVar8 = (ulong)(uVar11 + 1);
        goto LAB_ffff0000b804ad58;
      }
    }
    else {
      uVar9 = (**(code **)(param_1 + 0x78))(param_1,lVar3,uVar8 & 0xffffffff);
      if ((long)uVar9 < 0) goto LAB_ffff0000b804aee8;
      uVar5 = *(ulong *)(param_1 + 0x28);
      if (uVar9 == uVar5) goto LAB_ffff0000b804ad28;
    }
  }
LAB_ffff0000b804aee4:
  uVar9 = 0xffffffffffffffec;
LAB_ffff0000b804aee8:
  thunk_FUN_ffff0000b8082558(lVar3);
  return uVar9;
}


