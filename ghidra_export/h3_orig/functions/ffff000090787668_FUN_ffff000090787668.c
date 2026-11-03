// ffff000090787668  size=596  FUN_ffff000090787668

ulong FUN_ffff000090787668(undefined4 *param_1,ulong param_2)

{
  undefined4 *puVar1;
  undefined4 uVar2;
  uint uVar3;
  int iVar4;
  uint uVar5;
  long lVar6;
  ulong uVar7;
  undefined4 *puVar8;
  undefined4 *puVar9;
  ulong uVar10;
  ulong local_60 [4];
  undefined8 local_40;
  undefined8 uStack_38;
  
  local_40 = 0;
  uStack_38 = 0;
  local_60[2] = 0;
  local_60[3] = 0;
  local_60[0] = 0;
  local_60[1] = 0;
  uVar10 = 1;
  if (param_1 == (undefined4 *)0x0) goto LAB_ffff000090787754;
  uVar5 = (uint)param_2;
  if (4 < uVar5) goto LAB_ffff000090787754;
  uVar3 = FUN_ffff00009071c55c();
  if (((4 < uVar3) || ((1 << (ulong)(uVar3 & 0x1f) & 0x13U) == 0)) &&
     (iVar4 = FUN_ffff00009071c55c(), iVar4 != 3)) {
    if (uVar5 == 0) {
      uVar10 = 0;
    }
    else {
      FUN_ffff00009071ea6c(0xc2000110,0,0,0,0,0,0,0,local_60);
      uVar10 = local_60[0];
      if (local_60[0] == 0) {
        *param_1 = (int)local_60[1];
      }
      if (uVar5 != 1) {
        FUN_ffff00009071ea6c(0xc2000110,0,0,0,0,0,0,0,local_60);
        if (local_60[0] == 0) {
          param_1[1] = (int)local_60[1];
        }
        else {
          uVar10 = local_60[0] & 0xffffffff;
        }
        if (uVar5 != 2) {
          FUN_ffff00009071ea6c(0xc2000110,0,0,0,0,0,0,0,local_60);
          if (local_60[0] == 0) {
            param_1[2] = (int)local_60[1];
          }
          else {
            uVar10 = local_60[0] & 0xffffffff;
          }
          if (uVar5 != 3) {
            FUN_ffff00009071ea6c(0xc2000110,0,0,0,0,0,0,0,local_60);
            if (local_60[0] == 0) {
              param_1[3] = (int)local_60[1];
            }
            else {
              uVar10 = local_60[0] & 0xffffffff;
            }
          }
        }
      }
    }
    goto LAB_ffff000090787754;
  }
  iVar4 = thunk_FUN_ffff0000907ab154(&local_40);
  if (uVar5 != 0) {
    if (uVar5 == 1) {
      uVar7 = 0;
    }
    else {
      uVar7 = param_2 & 0xfffffffe;
      puVar8 = (undefined4 *)((ulong)&local_40 | 4);
      puVar9 = param_1 + 1;
      uVar10 = uVar7;
      do {
        puVar1 = puVar8 + -1;
        uVar2 = *puVar8;
        puVar8 = puVar8 + 2;
        uVar10 = uVar10 - 2;
        puVar9[-1] = *puVar1;
        *puVar9 = uVar2;
        puVar9 = puVar9 + 2;
      } while (uVar10 != 0);
      if (uVar7 == (param_2 & 0xffffffff)) goto LAB_ffff00009078773c;
    }
    lVar6 = (param_2 & 0xffffffff) - uVar7;
    puVar9 = (undefined4 *)((long)&local_40 + uVar7 * 4);
    puVar8 = param_1 + uVar7;
    do {
      lVar6 = lVar6 + -1;
      *puVar8 = *puVar9;
      puVar9 = puVar9 + 1;
      puVar8 = puVar8 + 1;
    } while (lVar6 != 0);
  }
LAB_ffff00009078773c:
  uVar5 = 2;
  if (iVar4 != 0x7264) {
    uVar5 = 3;
  }
  uVar3 = 0;
  if (iVar4 != 0) {
    uVar3 = uVar5;
  }
  uVar10 = (ulong)uVar3;
LAB_ffff000090787754:
  return uVar10 & 0xffffffff;
}


