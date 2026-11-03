// ffff0000907c8430  size=1740  FUN_ffff0000907c8430

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907c8430(short *param_1,short *param_2,short *param_3,short *param_4)

{
  long lVar1;
  uint uVar2;
  uint uVar3;
  bool bVar4;
  int iVar5;
  undefined4 *puVar6;
  undefined4 uVar7;
  ulong uVar8;
  long lVar9;
  long lVar10;
  long lVar11;
  ulong uVar12;
  ulong uVar13;
  ulong local_108;
  ulong local_100;
  long local_f8;
  undefined4 *local_f0;
  ulong local_e8;
  long local_e0;
  undefined4 *local_d8;
  ulong local_d0;
  ulong local_c8;
  undefined4 *local_c0;
  ulong local_b8;
  long local_b0;
  long local_a8;
  ulong local_a0;
  long local_98;
  long local_90;
  undefined4 local_84;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined4 *local_70;
  
  uVar8 = *(ulong *)(param_4 + 4);
  do {
    uVar8 = uVar8 - 1;
    if (uVar8 == 0xffffffffffffffff) {
      return -0xc;
    }
  } while (*(int *)(*(long *)(param_4 + 8) + uVar8 * 4) == 0);
  if (*param_4 == 0) {
    return -0xc;
  }
  local_f8 = 0;
  local_98 = 0;
  local_90 = 0;
  bVar4 = _DAT_ffff00009090e560 != 0;
  local_b0 = 0;
  local_a8 = 0;
  local_c8 = 0;
  local_c0 = (undefined4 *)0x0;
  local_a0 = (ulong)CONCAT12(bVar4,1);
  local_b8 = (ulong)CONCAT12(bVar4,1);
  local_d0 = (ulong)CONCAT12(bVar4,1);
  local_e8 = (ulong)CONCAT12(bVar4,1);
  local_e0 = 0;
  local_d8 = (undefined4 *)0x0;
  local_100 = (ulong)CONCAT12(bVar4,1);
  local_f0 = (undefined4 *)0x0;
  uVar8 = *(ulong *)(param_3 + 4);
  do {
    uVar13 = uVar8;
    if (uVar13 == 0) break;
    uVar8 = uVar13 - 1;
  } while (*(int *)(*(long *)(param_3 + 8) + uVar13 * 4 + -4) == 0);
  lVar10 = *(long *)(param_4 + 8) + -4;
  uVar8 = *(ulong *)(param_4 + 4);
  do {
    uVar12 = uVar8;
    if (uVar12 == 0) goto LAB_ffff0000907c8538;
    uVar8 = uVar12 - 1;
  } while (*(int *)(lVar10 + uVar12 * 4) == 0);
  if (uVar13 <= uVar12) {
    if (uVar12 <= uVar13) {
      do {
        if (uVar13 == 0) goto LAB_ffff0000907c8538;
        uVar3 = *(uint *)(lVar10 + uVar13 * 4);
        uVar2 = *(uint *)(*(long *)(param_3 + 8) + uVar13 * 4 + -4);
        if (uVar3 < uVar2) goto LAB_ffff0000907c8538;
        uVar13 = uVar13 - 1;
      } while (uVar3 <= uVar2);
    }
    if ((param_1 == (short *)0x0) || (iVar5 = FUN_ffff0000907c6eb4(param_1,0), iVar5 == 0)) {
      if (param_2 == (short *)0x0) {
        return 0;
      }
      iVar5 = FUN_ffff0000907c6bec(param_2,param_3);
      if (iVar5 == 0) {
        return 0;
      }
    }
    goto LAB_ffff0000907c8688;
  }
LAB_ffff0000907c8538:
  iVar5 = FUN_ffff0000907c6bec(&local_a0,param_3);
  if ((iVar5 != 0) || (iVar5 = FUN_ffff0000907c6bec(&local_b8,param_4), iVar5 != 0))
  goto LAB_ffff0000907c8688;
  uVar8 = *(long *)(param_3 + 4) + 2;
  local_b8 = CONCAT62(local_b8._2_6_,1);
  local_a0 = CONCAT62(local_a0._2_6_,1);
  if (uVar8 < 0x2711) {
    if (uVar8 == 0) {
      puVar6 = (undefined4 *)FUN_ffff00009077d2e0(1,4);
      if (puVar6 != (undefined4 *)0x0) {
        lVar10 = 4;
        local_c8 = 1;
        goto LAB_ffff0000907c8644;
      }
    }
    else {
      puVar6 = (undefined4 *)FUN_ffff00009077d2e0(uVar8,4);
      if (puVar6 != (undefined4 *)0x0) {
        lVar10 = uVar8 * 4;
        local_c8 = uVar8;
LAB_ffff0000907c8644:
        local_c0 = puVar6;
        FUN_ffff00009077eb88(puVar6,0,lVar10);
        *puVar6 = 0;
        local_d0 = CONCAT62(local_d0._2_6_,1);
        iVar5 = FUN_ffff0000907c6a38(&local_e8,2);
        if ((iVar5 == 0) && (iVar5 = FUN_ffff0000907c6a38(&local_100,3), iVar5 == 0)) {
          uVar8 = FUN_ffff0000907c6fb4(&local_b8);
          if ((uVar8 & 0x1f) == 0x1f) {
            local_108 = 0;
          }
          else {
            local_108 = uVar8 & 0x1f ^ 0x1f;
            iVar5 = FUN_ffff0000907c7208(&local_a0);
            if ((iVar5 != 0) || (iVar5 = FUN_ffff0000907c7208(&local_b8,local_108), iVar5 != 0))
            goto LAB_ffff0000907c8688;
          }
          lVar10 = local_b0;
          uVar8 = local_98 - 1;
          uVar13 = local_b0 - 1;
          lVar11 = uVar8 - uVar13;
          iVar5 = FUN_ffff0000907c7208(&local_b8,lVar11 * 0x20);
          while (iVar5 == 0) {
            iVar5 = FUN_ffff0000907c75d0(&local_a0,&local_b8);
            if (iVar5 < 0) {
              iVar5 = FUN_ffff0000907c7474(&local_b8,lVar11 * 0x20);
              if (iVar5 == 0) {
                if (uVar8 <= uVar13) goto LAB_ffff0000907c8a58;
                goto LAB_ffff0000907c8844;
              }
              break;
            }
            puVar6[lVar11] = puVar6[lVar11] + 1;
            iVar5 = FUN_ffff0000907c7ce4(&local_a0,&local_a0,&local_b8);
          }
        }
        goto LAB_ffff0000907c8688;
      }
    }
  }
  iVar5 = -0x10;
LAB_ffff0000907c8688:
  if (local_90 != 0) {
    FUN_ffff0000907cc428(local_90,local_98 << 2);
    thunk_FUN_ffff0000907c2ad8(local_90);
  }
  local_98 = 0;
  local_90 = 0;
  local_a0 = CONCAT62(local_a0._2_6_,1);
  if (local_a8 != 0) {
    FUN_ffff0000907cc428(local_a8,local_b0 << 2);
    thunk_FUN_ffff0000907c2ad8(local_a8);
  }
  local_b8 = CONCAT62(local_b8._2_6_,1);
  local_b0 = 0;
  local_a8 = 0;
  if (local_c0 != (undefined4 *)0x0) {
    FUN_ffff0000907cc428(local_c0,local_c8 << 2);
    thunk_FUN_ffff0000907c2ad8(local_c0);
  }
  local_c8 = 0;
  local_c0 = (undefined4 *)0x0;
  local_d0 = CONCAT62(local_d0._2_6_,1);
  if (local_d8 != (undefined4 *)0x0) {
    FUN_ffff0000907cc428(local_d8,local_e0 << 2);
    thunk_FUN_ffff0000907c2ad8(local_d8);
  }
  puVar6 = local_f0;
  local_e8 = CONCAT62(local_e8._2_6_,1);
  local_e0 = 0;
  local_d8 = (undefined4 *)0x0;
  if (local_f0 != (undefined4 *)0x0) {
    FUN_ffff0000907cc428(local_f0,local_f8 << 2);
    thunk_FUN_ffff0000907c2ad8(puVar6);
  }
  return iVar5;
LAB_ffff0000907c8844:
  do {
    uVar2 = *(uint *)(local_90 + uVar8 * 4);
    uVar3 = *(uint *)(local_a8 + uVar13 * 4);
    if (uVar2 < uVar3) {
      uVar12 = 0;
      if ((ulong)uVar3 != 0) {
        uVar12 = CONCAT44(uVar2,*(undefined4 *)(local_90 + (uVar8 - 1) * 4)) / (ulong)uVar3;
      }
      lVar11 = uVar8 - lVar10;
      if (0xfffffffe < uVar12) {
        uVar12 = 0xffffffff;
      }
      iVar5 = (int)uVar12;
      puVar6[lVar11] = iVar5;
    }
    else {
      lVar11 = uVar8 - lVar10;
      iVar5 = -1;
      puVar6[lVar11] = 0xffffffff;
    }
    uVar12 = uVar8 - 1;
    puVar6[lVar11] = iVar5 + 1;
    do {
      puVar6[lVar11] = puVar6[lVar11] + -1;
      iVar5 = FUN_ffff0000907c6eb4(&local_e8,0);
      if (iVar5 != 0) goto LAB_ffff0000907c8688;
      if (uVar13 == 0) {
        uVar7 = 0;
      }
      else {
        uVar7 = *(undefined4 *)(local_a8 + (lVar10 + -2) * 4);
      }
      *local_d8 = uVar7;
      local_d8[1] = *(undefined4 *)(local_a8 + uVar13 * 4);
      local_84 = puVar6[lVar11];
      local_80 = 1;
      uStack_78 = 1;
      local_70 = &local_84;
      iVar5 = FUN_ffff0000907c7df8(&local_e8,&local_e8,&local_80);
      if ((iVar5 != 0) || (iVar5 = FUN_ffff0000907c6eb4(&local_100,0), iVar5 != 0))
      goto LAB_ffff0000907c8688;
      if (uVar8 < 2) {
        uVar7 = 0;
      }
      else {
        uVar7 = *(undefined4 *)(local_90 + (uVar8 - 2) * 4);
      }
      *local_f0 = uVar7;
      local_f0[1] = *(undefined4 *)(local_90 + uVar12 * 4);
      local_f0[2] = *(undefined4 *)(local_90 + uVar8 * 4);
      iVar5 = FUN_ffff0000907c75d0(&local_e8,&local_100);
    } while (0 < iVar5);
    local_84 = puVar6[lVar11];
    local_80 = 1;
    uStack_78 = 1;
    local_70 = &local_84;
    iVar5 = FUN_ffff0000907c7df8(&local_e8,&local_b8,&local_80);
    if (iVar5 != 0) goto LAB_ffff0000907c8688;
    iVar5 = FUN_ffff0000907c7208(&local_e8,lVar11 << 5);
    if ((iVar5 != 0) || (iVar5 = FUN_ffff0000907c7ce4(&local_a0,&local_a0,&local_e8), iVar5 != 0))
    goto LAB_ffff0000907c8688;
    lVar9 = local_98;
    do {
      if (lVar9 == 0) goto LAB_ffff0000907c8a50;
      lVar1 = lVar9 * 4;
      lVar9 = lVar9 + -1;
    } while (*(int *)(local_90 + -4 + lVar1) == 0);
    if ((short)local_a0 < 0) {
      iVar5 = FUN_ffff0000907c6bec(&local_e8,&local_b8);
      if (((iVar5 != 0) || (iVar5 = FUN_ffff0000907c7208(&local_e8,lVar11 << 5), iVar5 != 0)) ||
         (iVar5 = FUN_ffff0000907c7bd4(&local_a0,&local_a0,&local_e8), iVar5 != 0))
      goto LAB_ffff0000907c8688;
      puVar6[lVar11] = puVar6[lVar11] + -1;
    }
LAB_ffff0000907c8a50:
    uVar8 = uVar12;
  } while (uVar13 < uVar12);
LAB_ffff0000907c8a58:
  if (param_1 != (short *)0x0) {
    iVar5 = FUN_ffff0000907c6bec(param_1,&local_d0);
    if (iVar5 != 0) goto LAB_ffff0000907c8688;
    *param_1 = *param_4 * *param_3;
  }
  if (param_2 != (short *)0x0) {
    iVar5 = FUN_ffff0000907c7474(&local_a0,local_108);
    if (iVar5 != 0) goto LAB_ffff0000907c8688;
    local_a0 = CONCAT62(local_a0._2_6_,*param_3);
    iVar5 = FUN_ffff0000907c6bec(param_2,&local_a0);
    if (iVar5 != 0) goto LAB_ffff0000907c8688;
    lVar10 = *(long *)(param_2 + 4);
    do {
      lVar10 = lVar10 + -1;
      if (lVar10 == -1) goto LAB_ffff0000907c8aec;
    } while (*(int *)(*(long *)(param_2 + 8) + lVar10 * 4) == 0);
    if (*param_2 == 0) {
LAB_ffff0000907c8aec:
      iVar5 = 0;
      *param_2 = 1;
      goto LAB_ffff0000907c8688;
    }
  }
  iVar5 = 0;
  goto LAB_ffff0000907c8688;
}


