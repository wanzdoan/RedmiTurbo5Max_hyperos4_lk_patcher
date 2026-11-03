
int FUN_ffff0000907c8aa4(short *param_1,short *param_2,short *param_3,short *param_4)

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
  bVar4 = DAT_ffff00009090e620 != 0;
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
    if (uVar12 == 0) goto LAB_ffff0000907c8bac;
    uVar8 = uVar12 - 1;
  } while (*(int *)(lVar10 + uVar12 * 4) == 0);
  if (uVar13 <= uVar12) {
    if (uVar12 <= uVar13) {
      do {
        if (uVar13 == 0) goto LAB_ffff0000907c8bac;
        uVar3 = *(uint *)(lVar10 + uVar13 * 4);
        uVar2 = *(uint *)(*(long *)(param_3 + 8) + uVar13 * 4 + -4);
        if (uVar3 < uVar2) goto LAB_ffff0000907c8bac;
        uVar13 = uVar13 - 1;
      } while (uVar3 <= uVar2);
    }
    if ((param_1 == (short *)0x0) || (iVar5 = FUN_ffff0000907c7528(param_1,0), iVar5 == 0)) {
      if (param_2 == (short *)0x0) {
        return 0;
      }
      iVar5 = FUN_ffff0000907c7260(param_2,param_3);
      if (iVar5 == 0) {
        return 0;
      }
    }
    goto LAB_ffff0000907c8cfc;
  }
LAB_ffff0000907c8bac:
  iVar5 = FUN_ffff0000907c7260(&local_a0,param_3);
  if ((iVar5 != 0) || (iVar5 = FUN_ffff0000907c7260(&local_b8,param_4), iVar5 != 0))
  goto LAB_ffff0000907c8cfc;
  uVar8 = *(long *)(param_3 + 4) + 2;
  local_b8 = CONCAT62(local_b8._2_6_,1);
  local_a0 = CONCAT62(local_a0._2_6_,1);
  if (uVar8 < 0x2711) {
    if (uVar8 == 0) {
      puVar6 = (undefined4 *)FUN_ffff00009077d5a8(1,4);
      if (puVar6 != (undefined4 *)0x0) {
        lVar10 = 4;
        local_c8 = 1;
        goto LAB_ffff0000907c8cb8;
      }
    }
    else {
      puVar6 = (undefined4 *)FUN_ffff00009077d5a8(uVar8,4);
      if (puVar6 != (undefined4 *)0x0) {
        lVar10 = uVar8 * 4;
        local_c8 = uVar8;
LAB_ffff0000907c8cb8:
        local_c0 = puVar6;
        FUN_ffff00009077ee50(puVar6,0,lVar10);
        *puVar6 = 0;
        local_d0 = CONCAT62(local_d0._2_6_,1);
        iVar5 = FUN_ffff0000907c70ac(&local_e8,2);
        if ((iVar5 == 0) && (iVar5 = FUN_ffff0000907c70ac(&local_100,3), iVar5 == 0)) {
          uVar8 = FUN_ffff0000907c7628(&local_b8);
          if ((uVar8 & 0x1f) == 0x1f) {
            local_108 = 0;
          }
          else {
            local_108 = uVar8 & 0x1f ^ 0x1f;
            iVar5 = FUN_ffff0000907c787c(&local_a0);
            if ((iVar5 != 0) || (iVar5 = FUN_ffff0000907c787c(&local_b8,local_108), iVar5 != 0))
            goto LAB_ffff0000907c8cfc;
          }
          lVar10 = local_b0;
          uVar8 = local_98 - 1;
          uVar13 = local_b0 - 1;
          lVar11 = uVar8 - uVar13;
          iVar5 = FUN_ffff0000907c787c(&local_b8,lVar11 * 0x20);
          while (iVar5 == 0) {
            iVar5 = FUN_ffff0000907c7c44(&local_a0,&local_b8);
            if (iVar5 < 0) {
              iVar5 = FUN_ffff0000907c7ae8(&local_b8,lVar11 * 0x20);
              if (iVar5 == 0) {
                if (uVar8 <= uVar13) goto LAB_ffff0000907c90cc;
                goto LAB_ffff0000907c8eb8;
              }
              break;
            }
            puVar6[lVar11] = puVar6[lVar11] + 1;
            iVar5 = FUN_ffff0000907c8358(&local_a0,&local_a0,&local_b8);
          }
        }
        goto LAB_ffff0000907c8cfc;
      }
    }
  }
  iVar5 = -0x10;
LAB_ffff0000907c8cfc:
  if (local_90 != 0) {
    FUN_ffff0000907cca9c(local_90,local_98 << 2);
    thunk_FUN_ffff0000907c314c(local_90);
  }
  local_98 = 0;
  local_90 = 0;
  local_a0 = CONCAT62(local_a0._2_6_,1);
  if (local_a8 != 0) {
    FUN_ffff0000907cca9c(local_a8,local_b0 << 2);
    thunk_FUN_ffff0000907c314c(local_a8);
  }
  local_b8 = CONCAT62(local_b8._2_6_,1);
  local_b0 = 0;
  local_a8 = 0;
  if (local_c0 != (undefined4 *)0x0) {
    FUN_ffff0000907cca9c(local_c0,local_c8 << 2);
    thunk_FUN_ffff0000907c314c(local_c0);
  }
  local_c8 = 0;
  local_c0 = (undefined4 *)0x0;
  local_d0 = CONCAT62(local_d0._2_6_,1);
  if (local_d8 != (undefined4 *)0x0) {
    FUN_ffff0000907cca9c(local_d8,local_e0 << 2);
    thunk_FUN_ffff0000907c314c(local_d8);
  }
  puVar6 = local_f0;
  local_e8 = CONCAT62(local_e8._2_6_,1);
  local_e0 = 0;
  local_d8 = (undefined4 *)0x0;
  if (local_f0 != (undefined4 *)0x0) {
    FUN_ffff0000907cca9c(local_f0,local_f8 << 2);
    thunk_FUN_ffff0000907c314c(puVar6);
  }
  return iVar5;
LAB_ffff0000907c8eb8:
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
      iVar5 = FUN_ffff0000907c7528(&local_e8,0);
      if (iVar5 != 0) goto LAB_ffff0000907c8cfc;
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
      iVar5 = FUN_ffff0000907c846c(&local_e8,&local_e8,&local_80);
      if ((iVar5 != 0) || (iVar5 = FUN_ffff0000907c7528(&local_100,0), iVar5 != 0))
      goto LAB_ffff0000907c8cfc;
      if (uVar8 < 2) {
        uVar7 = 0;
      }
      else {
        uVar7 = *(undefined4 *)(local_90 + (uVar8 - 2) * 4);
      }
      *local_f0 = uVar7;
      local_f0[1] = *(undefined4 *)(local_90 + uVar12 * 4);
      local_f0[2] = *(undefined4 *)(local_90 + uVar8 * 4);
      iVar5 = FUN_ffff0000907c7c44(&local_e8,&local_100);
    } while (0 < iVar5);
    local_84 = puVar6[lVar11];
    local_80 = 1;
    uStack_78 = 1;
    local_70 = &local_84;
    iVar5 = FUN_ffff0000907c846c(&local_e8,&local_b8,&local_80);
    if (iVar5 != 0) goto LAB_ffff0000907c8cfc;
    iVar5 = FUN_ffff0000907c787c(&local_e8,lVar11 << 5);
    if ((iVar5 != 0) || (iVar5 = FUN_ffff0000907c8358(&local_a0,&local_a0,&local_e8), iVar5 != 0))
    goto LAB_ffff0000907c8cfc;
    lVar9 = local_98;
    do {
      if (lVar9 == 0) goto LAB_ffff0000907c90c4;
      lVar1 = lVar9 * 4;
      lVar9 = lVar9 + -1;
    } while (*(int *)(local_90 + -4 + lVar1) == 0);
    if ((short)local_a0 < 0) {
      iVar5 = FUN_ffff0000907c7260(&local_e8,&local_b8);
      if (((iVar5 != 0) || (iVar5 = FUN_ffff0000907c787c(&local_e8,lVar11 << 5), iVar5 != 0)) ||
         (iVar5 = FUN_ffff0000907c8248(&local_a0,&local_a0,&local_e8), iVar5 != 0))
      goto LAB_ffff0000907c8cfc;
      puVar6[lVar11] = puVar6[lVar11] + -1;
    }
LAB_ffff0000907c90c4:
    uVar8 = uVar12;
  } while (uVar13 < uVar12);
LAB_ffff0000907c90cc:
  if (param_1 != (short *)0x0) {
    iVar5 = FUN_ffff0000907c7260(param_1,&local_d0);
    if (iVar5 != 0) goto LAB_ffff0000907c8cfc;
    *param_1 = *param_4 * *param_3;
  }
  if (param_2 != (short *)0x0) {
    iVar5 = FUN_ffff0000907c7ae8(&local_a0,local_108);
    if (iVar5 != 0) goto LAB_ffff0000907c8cfc;
    local_a0 = CONCAT62(local_a0._2_6_,*param_3);
    iVar5 = FUN_ffff0000907c7260(param_2,&local_a0);
    if (iVar5 != 0) goto LAB_ffff0000907c8cfc;
    lVar10 = *(long *)(param_2 + 4);
    do {
      lVar10 = lVar10 + -1;
      if (lVar10 == -1) goto LAB_ffff0000907c9160;
    } while (*(int *)(*(long *)(param_2 + 8) + lVar10 * 4) == 0);
    if (*param_2 == 0) {
LAB_ffff0000907c9160:
      iVar5 = 0;
      *param_2 = 1;
      goto LAB_ffff0000907c8cfc;
    }
  }
  iVar5 = 0;
  goto LAB_ffff0000907c8cfc;
}


