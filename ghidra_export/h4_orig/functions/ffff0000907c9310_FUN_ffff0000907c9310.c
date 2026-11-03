// ffff0000907c9310  size=3056  FUN_ffff0000907c9310

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907c9310(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  uint uVar1;
  int iVar2;
  undefined4 *puVar3;
  uint uVar4;
  ulong uVar5;
  ulong uVar6;
  long lVar7;
  ulong uVar8;
  long lVar9;
  uint uVar10;
  uint *puVar11;
  ulong uVar12;
  ulong uVar13;
  ulong uVar14;
  ulong uVar15;
  ulong local_70;
  ulong local_68;
  undefined4 *local_60;
  ulong local_58;
  ulong local_50;
  undefined4 *local_48;
  
  local_50 = 0;
  local_48 = (undefined4 *)0x0;
  local_68 = 0;
  local_60 = (undefined4 *)0x0;
  local_58 = (ulong)CONCAT12(_DAT_ffff00009090e620 != 0,1);
  local_70 = (ulong)CONCAT12(_DAT_ffff00009090e620 != 0,1);
  iVar2 = FUN_ffff0000907c7260(&local_58);
  if ((iVar2 != 0) || (iVar2 = FUN_ffff0000907c7260(&local_70,param_3), iVar2 != 0))
  goto joined_r0xffff0000907c9c8c;
  if (local_50 != 0) {
    uVar5 = 0;
    lVar7 = 0;
    do {
      lVar9 = 0;
      do {
        if (((uint)local_48[uVar5] >> (ulong)((uint)lVar9 & 0x1f) & 1) != 0) {
          uVar5 = lVar7 + lVar9;
          goto joined_r0xffff0000907c93e8;
        }
        lVar9 = lVar9 + 1;
      } while (lVar9 != 0x20);
      uVar5 = uVar5 + 1;
      lVar7 = lVar7 + 0x20;
    } while (uVar5 != local_50);
  }
  uVar5 = 0;
joined_r0xffff0000907c93e8:
  if (local_68 != 0) {
    uVar8 = 0;
    lVar7 = 0;
    do {
      lVar9 = 0;
      do {
        if (((uint)local_60[uVar8] >> (ulong)((uint)lVar9 & 0x1f) & 1) != 0) {
          uVar8 = lVar7 + lVar9;
          goto LAB_ffff0000907c9434;
        }
        lVar9 = lVar9 + 1;
      } while (lVar9 != 0x20);
      uVar8 = uVar8 + 1;
      lVar7 = lVar7 + 0x20;
    } while (uVar8 != local_68);
  }
  uVar8 = 0;
LAB_ffff0000907c9434:
  if (uVar5 <= uVar8) {
    uVar8 = uVar5;
  }
  uVar15 = uVar8 >> 5;
  uVar5 = uVar8 & 0x1f;
  if ((local_50 < uVar15) || (uVar5 != 0 && local_50 == uVar15)) {
    if (local_50 != 0) {
      lVar7 = local_50 << 2;
LAB_ffff0000907c94f8:
      FUN_ffff00009077ee50(local_48,0,lVar7);
      *local_48 = 0;
      local_58 = CONCAT62(local_58._2_6_,1);
      goto LAB_ffff0000907c9518;
    }
    puVar3 = (undefined4 *)FUN_ffff00009077d5a8(1,4);
    if (puVar3 != (undefined4 *)0x0) {
      if (local_48 != (undefined4 *)0x0) {
        FUN_ffff00009077eb20(puVar3,local_48,local_50 << 2);
        FUN_ffff0000907cca9c(local_48,local_50 << 2);
        thunk_FUN_ffff0000907c314c(local_48);
      }
      lVar7 = 4;
      local_50 = 1;
      local_48 = puVar3;
      goto LAB_ffff0000907c94f8;
    }
LAB_ffff0000907c9d30:
    iVar2 = -0x10;
joined_r0xffff0000907c9c8c:
    if (local_48 != (undefined4 *)0x0) {
      FUN_ffff0000907cca9c(local_48,local_50 << 2);
      thunk_FUN_ffff0000907c314c(local_48);
    }
    local_50 = 0;
    local_48 = (undefined4 *)0x0;
    local_58 = CONCAT62(local_58._2_6_,1);
    if (local_60 != (undefined4 *)0x0) {
      FUN_ffff0000907cca9c(local_60,local_68 << 2);
      thunk_FUN_ffff0000907c314c(local_60);
    }
    return iVar2;
  }
  if (0x1f < uVar8) {
    uVar14 = local_50 - uVar15;
    puVar3 = local_48;
    for (uVar6 = uVar14; uVar6 != 0; uVar6 = uVar6 - 1) {
      *puVar3 = puVar3[uVar15];
      puVar3 = puVar3 + 1;
    }
    uVar6 = local_50 - uVar14;
    if (uVar14 <= local_50 && uVar6 != 0) {
      if (1 < uVar6) {
        uVar13 = uVar6 & 0xfffffffffffffffe;
        puVar3 = local_48 + uVar14 + 1;
        uVar12 = uVar13;
        do {
          *(undefined8 *)(puVar3 + -1) = 0;
          uVar12 = uVar12 - 2;
          puVar3 = puVar3 + 2;
        } while (uVar12 != 0);
        uVar14 = uVar14 + uVar13;
        if (uVar6 == uVar13) goto LAB_ffff0000907c9d80;
      }
      lVar7 = local_50 - uVar14;
      puVar3 = local_48 + uVar14;
      do {
        lVar7 = lVar7 + -1;
        *puVar3 = 0;
        puVar3 = puVar3 + 1;
      } while (lVar7 != 0);
    }
  }
LAB_ffff0000907c9d80:
  if ((uVar5 != 0) && (local_50 != 0)) {
    uVar4 = 0x20 - (int)uVar5;
    if (local_50 < 2) {
      uVar10 = 0;
      uVar6 = local_50;
    }
    else {
      uVar10 = 0;
      uVar6 = local_50 & 1;
      puVar11 = local_48 + (local_50 - 1);
      uVar14 = local_50 & 0xfffffffffffffffe;
      do {
        uVar14 = uVar14 - 2;
        uVar1 = *puVar11 >> uVar5 | uVar10;
        uVar10 = puVar11[-1] << (ulong)(uVar4 & 0x1f);
        puVar11[-1] = puVar11[-1] >> uVar5 | *puVar11 << (ulong)(uVar4 & 0x1f);
        *puVar11 = uVar1;
        puVar11 = puVar11 + -2;
      } while (uVar14 != 0);
      if (local_50 == (local_50 & 0xfffffffffffffffe)) goto LAB_ffff0000907c9518;
    }
    do {
      uVar14 = uVar6 - 1;
      uVar1 = local_48[uVar6 - 1];
      local_48[uVar6 - 1] = uVar1 >> uVar5 | uVar10;
      uVar6 = uVar14;
      uVar10 = uVar1 << (ulong)(uVar4 & 0x1f);
    } while (uVar14 != 0);
  }
LAB_ffff0000907c9518:
  if ((local_68 < uVar15) || ((uVar5 != 0 && (local_68 == uVar15)))) {
    if (local_68 == 0) {
      puVar3 = (undefined4 *)FUN_ffff00009077d5a8(1,4);
      if (puVar3 == (undefined4 *)0x0) goto LAB_ffff0000907c9d30;
      if (local_60 != (undefined4 *)0x0) {
        FUN_ffff00009077eb20(puVar3,local_60,local_68 << 2);
        FUN_ffff0000907cca9c(local_60,local_68 << 2);
        thunk_FUN_ffff0000907c314c(local_60);
      }
      lVar7 = 4;
      local_68 = 1;
      local_60 = puVar3;
    }
    else {
      lVar7 = local_68 << 2;
    }
    FUN_ffff00009077ee50(local_60,0,lVar7);
    *local_60 = 0;
    goto LAB_ffff0000907c959c;
  }
  if (0x1f < uVar8) {
    uVar14 = local_68 - uVar15;
    puVar3 = local_60;
    for (uVar6 = uVar14; uVar6 != 0; uVar6 = uVar6 - 1) {
      *puVar3 = puVar3[uVar15];
      puVar3 = puVar3 + 1;
    }
    uVar15 = local_68 - uVar14;
    if (uVar14 <= local_68 && uVar15 != 0) {
      if (1 < uVar15) {
        uVar12 = uVar15 & 0xfffffffffffffffe;
        puVar3 = local_60 + uVar14 + 1;
        uVar6 = uVar12;
        do {
          *(undefined8 *)(puVar3 + -1) = 0;
          uVar6 = uVar6 - 2;
          puVar3 = puVar3 + 2;
        } while (uVar6 != 0);
        uVar14 = uVar14 + uVar12;
        if (uVar15 == uVar12) goto LAB_ffff0000907c9e60;
      }
      lVar7 = local_68 - uVar14;
      puVar3 = local_60 + uVar14;
      do {
        lVar7 = lVar7 + -1;
        *puVar3 = 0;
        puVar3 = puVar3 + 1;
      } while (lVar7 != 0);
    }
  }
LAB_ffff0000907c9e60:
  if ((uVar5 != 0) && (local_68 != 0)) {
    uVar4 = 0x20 - (int)uVar5;
    if (local_68 < 2) {
      uVar10 = 0;
      uVar15 = local_68;
    }
    else {
      uVar10 = 0;
      uVar15 = local_68 & 1;
      puVar11 = local_60 + (local_68 - 1);
      uVar6 = local_68 & 0xfffffffffffffffe;
      do {
        uVar6 = uVar6 - 2;
        uVar1 = *puVar11 >> uVar5 | uVar10;
        uVar10 = puVar11[-1] << (ulong)(uVar4 & 0x1f);
        puVar11[-1] = puVar11[-1] >> uVar5 | *puVar11 << (ulong)(uVar4 & 0x1f);
        *puVar11 = uVar1;
        puVar11 = puVar11 + -2;
      } while (uVar6 != 0);
      if (local_68 == (local_68 & 0xfffffffffffffffe)) goto LAB_ffff0000907c959c;
    }
    do {
      uVar6 = uVar15 - 1;
      uVar1 = local_60[uVar15 - 1];
      local_60[uVar15 - 1] = uVar1 >> uVar5 | uVar10;
      uVar15 = uVar6;
      uVar10 = uVar1 << (ulong)(uVar4 & 0x1f);
    } while (uVar6 != 0);
  }
LAB_ffff0000907c959c:
  local_70 = CONCAT62(local_70._2_6_,1);
LAB_ffff0000907c95a8:
  local_58 = CONCAT62(local_58._2_6_,1);
  uVar5 = local_50;
LAB_ffff0000907c95b4:
  if (uVar5 != 0) goto code_r0xffff0000907c95b8;
  goto LAB_ffff0000907c9c64;
code_r0xffff0000907c95b8:
  lVar7 = uVar5 - 1;
  uVar5 = uVar5 - 1;
  if (local_48[lVar7] == 0) goto LAB_ffff0000907c95b4;
  if ((short)local_58 == 0) {
LAB_ffff0000907c9c64:
    iVar2 = FUN_ffff0000907c787c(&local_70,uVar8);
    if (iVar2 == 0) {
      iVar2 = FUN_ffff0000907c7260(param_1,&local_70);
    }
    goto joined_r0xffff0000907c9c8c;
  }
  if (local_50 != 0) {
    uVar5 = 0;
    lVar7 = 0;
    do {
      lVar9 = 0;
      do {
        if (((uint)local_48[uVar5] >> (ulong)((uint)lVar9 & 0x1f) & 1) != 0) {
          uVar5 = lVar7 + lVar9;
          goto LAB_ffff0000907c9618;
        }
        lVar9 = lVar9 + 1;
      } while (lVar9 != 0x20);
      uVar5 = uVar5 + 1;
      lVar7 = lVar7 + 0x20;
    } while (uVar5 != local_50);
  }
  uVar5 = 0;
LAB_ffff0000907c9618:
  uVar15 = uVar5 >> 5;
  if ((local_50 < uVar15) || ((uVar6 = uVar5 & 0x1f, uVar6 != 0 && (local_50 == uVar15)))) {
    if (local_50 == 0) {
      puVar3 = (undefined4 *)FUN_ffff00009077d5a8(1,4);
      if (puVar3 == (undefined4 *)0x0) goto LAB_ffff0000907c9d30;
      if (local_48 != (undefined4 *)0x0) {
        FUN_ffff00009077eb20(puVar3,local_48,local_50 << 2);
        FUN_ffff0000907cca9c(local_48,local_50 << 2);
        thunk_FUN_ffff0000907c314c(local_48);
      }
      lVar7 = 4;
      local_50 = 1;
      local_48 = puVar3;
    }
    else {
      lVar7 = local_50 << 2;
    }
    FUN_ffff00009077ee50(local_48,0,lVar7);
    *local_48 = 0;
    local_58 = CONCAT62(local_58._2_6_,1);
  }
  else {
    if (0x1f < uVar5) {
      uVar14 = local_50 - uVar15;
      puVar3 = local_48;
      for (uVar5 = uVar14; uVar5 != 0; uVar5 = uVar5 - 1) {
        *puVar3 = puVar3[uVar15];
        puVar3 = puVar3 + 1;
      }
      uVar5 = local_50 - uVar14;
      if (uVar14 <= local_50 && uVar5 != 0) {
        if (1 < uVar5) {
          uVar12 = uVar5 & 0xfffffffffffffffe;
          puVar3 = local_48 + uVar14 + 1;
          uVar15 = uVar12;
          do {
            *(undefined8 *)(puVar3 + -1) = 0;
            uVar15 = uVar15 - 2;
            puVar3 = puVar3 + 2;
          } while (uVar15 != 0);
          uVar14 = uVar14 + uVar12;
          if (uVar5 == uVar12) goto LAB_ffff0000907c9a8c;
        }
        lVar7 = local_50 - uVar14;
        puVar3 = local_48 + uVar14;
        do {
          lVar7 = lVar7 + -1;
          *puVar3 = 0;
          puVar3 = puVar3 + 1;
        } while (lVar7 != 0);
      }
    }
LAB_ffff0000907c9a8c:
    if ((uVar6 != 0) && (local_50 != 0)) {
      uVar4 = 0x20 - (int)uVar6;
      if (local_50 < 2) {
        uVar10 = 0;
        uVar5 = local_50;
      }
      else {
        uVar10 = 0;
        uVar5 = local_50 & 1;
        puVar11 = local_48 + (local_50 - 1);
        uVar15 = local_50 & 0xfffffffffffffffe;
        do {
          uVar15 = uVar15 - 2;
          uVar1 = *puVar11 >> uVar6 | uVar10;
          uVar10 = puVar11[-1] << (ulong)(uVar4 & 0x1f);
          puVar11[-1] = puVar11[-1] >> uVar6 | *puVar11 << (ulong)(uVar4 & 0x1f);
          *puVar11 = uVar1;
          puVar11 = puVar11 + -2;
        } while (uVar15 != 0);
        if (local_50 == (local_50 & 0xfffffffffffffffe)) goto LAB_ffff0000907c96a0;
      }
      do {
        uVar15 = uVar5 - 1;
        uVar1 = local_48[uVar5 - 1];
        local_48[uVar5 - 1] = uVar1 >> uVar6 | uVar10;
        uVar5 = uVar15;
        uVar10 = uVar1 << (ulong)(uVar4 & 0x1f);
      } while (uVar15 != 0);
    }
  }
LAB_ffff0000907c96a0:
  if (local_68 != 0) {
    uVar5 = 0;
    lVar7 = 0;
    do {
      lVar9 = 0;
      do {
        if (((uint)local_60[uVar5] >> (ulong)((uint)lVar9 & 0x1f) & 1) != 0) {
          uVar5 = lVar7 + lVar9;
          goto LAB_ffff0000907c96f0;
        }
        lVar9 = lVar9 + 1;
      } while (lVar9 != 0x20);
      uVar5 = uVar5 + 1;
      lVar7 = lVar7 + 0x20;
    } while (uVar5 != local_68);
  }
  uVar5 = 0;
LAB_ffff0000907c96f0:
  uVar15 = uVar5 >> 5;
  if ((local_68 < uVar15) || ((uVar6 = uVar5 & 0x1f, uVar6 != 0 && (local_68 == uVar15)))) {
    if (local_68 == 0) {
      puVar3 = (undefined4 *)FUN_ffff00009077d5a8(1,4);
      if (puVar3 == (undefined4 *)0x0) goto LAB_ffff0000907c9d30;
      if (local_60 != (undefined4 *)0x0) {
        FUN_ffff00009077eb20(puVar3,local_60,local_68 << 2);
        FUN_ffff0000907cca9c(local_60,local_68 << 2);
        thunk_FUN_ffff0000907c314c(local_60);
      }
      lVar7 = 4;
      local_68 = 1;
      local_60 = puVar3;
    }
    else {
      lVar7 = local_68 << 2;
    }
    FUN_ffff00009077ee50(local_60,0,lVar7);
    *local_60 = 0;
    local_70 = CONCAT62(local_70._2_6_,1);
    uVar5 = local_50;
  }
  else {
    if (0x1f < uVar5) {
      uVar14 = local_68 - uVar15;
      puVar3 = local_60;
      for (uVar5 = uVar14; uVar5 != 0; uVar5 = uVar5 - 1) {
        *puVar3 = puVar3[uVar15];
        puVar3 = puVar3 + 1;
      }
      uVar5 = local_68 - uVar14;
      if (uVar14 <= local_68 && uVar5 != 0) {
        if (1 < uVar5) {
          uVar12 = uVar5 & 0xfffffffffffffffe;
          puVar3 = local_60 + uVar14 + 1;
          uVar15 = uVar12;
          do {
            *(undefined8 *)(puVar3 + -1) = 0;
            uVar15 = uVar15 - 2;
            puVar3 = puVar3 + 2;
          } while (uVar15 != 0);
          uVar14 = uVar14 + uVar12;
          if (uVar5 == uVar12) goto LAB_ffff0000907c9b68;
        }
        lVar7 = local_68 - uVar14;
        puVar3 = local_60 + uVar14;
        do {
          lVar7 = lVar7 + -1;
          *puVar3 = 0;
          puVar3 = puVar3 + 1;
        } while (lVar7 != 0);
      }
    }
LAB_ffff0000907c9b68:
    uVar5 = local_50;
    if ((uVar6 != 0) && (local_68 != 0)) {
      uVar4 = 0x20 - (int)uVar6;
      if (local_68 < 2) {
        uVar10 = 0;
        uVar15 = local_68;
      }
      else {
        uVar10 = 0;
        uVar15 = local_68 & 1;
        puVar11 = local_60 + (local_68 - 1);
        uVar14 = local_68 & 0xfffffffffffffffe;
        do {
          uVar14 = uVar14 - 2;
          uVar1 = *puVar11 >> uVar6 | uVar10;
          uVar10 = puVar11[-1] << (ulong)(uVar4 & 0x1f);
          puVar11[-1] = puVar11[-1] >> uVar6 | *puVar11 << (ulong)(uVar4 & 0x1f);
          *puVar11 = uVar1;
          puVar11 = puVar11 + -2;
        } while (uVar14 != 0);
        if (local_68 == (local_68 & 0xfffffffffffffffe)) goto LAB_ffff0000907c9784;
      }
      do {
        uVar14 = uVar15 - 1;
        uVar1 = local_60[uVar15 - 1];
        local_60[uVar15 - 1] = uVar1 >> uVar6 | uVar10;
        uVar15 = uVar14;
        uVar10 = uVar1 << (ulong)(uVar4 & 0x1f);
      } while (uVar14 != 0);
    }
  }
LAB_ffff0000907c9784:
  do {
    uVar6 = uVar5;
    uVar15 = local_68;
    if (uVar6 == 0) break;
    uVar5 = uVar6 - 1;
  } while (local_48[uVar6 - 1] == 0);
  do {
    uVar5 = uVar15;
    if (uVar5 == 0) {
      if (uVar6 == 0) goto LAB_ffff0000907c97e0;
      break;
    }
    uVar15 = uVar5 - 1;
  } while (local_60[uVar5 - 1] == 0);
  if (uVar5 < uVar6) {
    iVar2 = (int)(short)local_58;
  }
  else if (uVar6 < uVar5) {
    iVar2 = -(int)(short)local_70;
  }
  else {
    iVar2 = (int)(short)local_58;
    if (iVar2 < 1) {
      if ((0 < (short)local_70) && (iVar2 != 0)) goto LAB_ffff0000907c994c;
    }
    else if ((short)local_70 < 0) goto LAB_ffff0000907c97e0;
    do {
      if (uVar6 == 0) goto LAB_ffff0000907c97e0;
      lVar7 = uVar6 - 1;
      lVar9 = uVar6 - 1;
      if ((uint)local_60[lVar9] < (uint)local_48[lVar7]) goto LAB_ffff0000907c97dc;
      uVar6 = uVar6 - 1;
    } while ((uint)local_60[lVar9] <= (uint)local_48[lVar7]);
    iVar2 = -iVar2;
  }
LAB_ffff0000907c97dc:
  if (iVar2 < 0) {
LAB_ffff0000907c994c:
    iVar2 = FUN_ffff0000907c8058(&local_70,&local_70,&local_58);
    if (iVar2 != 0) goto joined_r0xffff0000907c9c8c;
    if (local_68 != 0) {
      uVar5 = local_50;
      if (local_68 == 1) {
        uVar4 = 0;
        uVar15 = 1;
      }
      else {
        uVar4 = 0;
        uVar15 = local_68 & 1;
        puVar11 = local_60 + (local_68 - 1);
        uVar6 = local_68 & 0xfffffffffffffffe;
        do {
          uVar6 = uVar6 - 2;
          uVar10 = uVar4 | *puVar11 >> 1;
          uVar4 = puVar11[-1] << 0x1f;
          puVar11[-1] = (uint)(CONCAT44(*puVar11,puVar11[-1]) >> 1);
          *puVar11 = uVar10;
          puVar11 = puVar11 + -2;
        } while (uVar6 != 0);
        if (local_68 == (local_68 & 0xfffffffffffffffe)) goto LAB_ffff0000907c95b4;
      }
      do {
        uVar6 = uVar15 - 1;
        uVar10 = local_60[uVar15 - 1];
        local_60[uVar15 - 1] = uVar4 | uVar10 >> 1;
        uVar15 = uVar6;
        uVar4 = uVar10 << 0x1f;
      } while (uVar6 != 0);
      goto LAB_ffff0000907c95b4;
    }
    puVar3 = (undefined4 *)FUN_ffff00009077d5a8(1,4);
    if (puVar3 == (undefined4 *)0x0) goto LAB_ffff0000907c9d30;
    if (local_60 != (undefined4 *)0x0) {
      FUN_ffff00009077eb20(puVar3,local_60,local_68 << 2);
      FUN_ffff0000907cca9c(local_60,local_68 << 2);
      thunk_FUN_ffff0000907c314c(local_60);
    }
    local_68 = 1;
    local_60 = puVar3;
    FUN_ffff00009077ee50(puVar3,0,4);
    *local_60 = 0;
    local_70 = CONCAT62(local_70._2_6_,1);
    uVar5 = local_50;
    goto LAB_ffff0000907c95b4;
  }
LAB_ffff0000907c97e0:
  iVar2 = FUN_ffff0000907c8058(&local_58,&local_58,&local_70);
  if (iVar2 != 0) goto joined_r0xffff0000907c9c8c;
  if (local_50 != 0) {
    uVar5 = local_50;
    if (local_50 == 1) {
      uVar4 = 0;
      uVar15 = 1;
    }
    else {
      uVar4 = 0;
      uVar15 = local_50 & 1;
      puVar11 = local_48 + (local_50 - 1);
      uVar6 = local_50 & 0xfffffffffffffffe;
      do {
        uVar6 = uVar6 - 2;
        uVar10 = uVar4 | *puVar11 >> 1;
        uVar4 = puVar11[-1] << 0x1f;
        puVar11[-1] = (uint)(CONCAT44(*puVar11,puVar11[-1]) >> 1);
        *puVar11 = uVar10;
        puVar11 = puVar11 + -2;
      } while (uVar6 != 0);
      if (local_50 == (local_50 & 0xfffffffffffffffe)) goto LAB_ffff0000907c95b4;
    }
    do {
      uVar6 = uVar15 - 1;
      uVar10 = local_48[uVar15 - 1];
      local_48[uVar15 - 1] = uVar4 | uVar10 >> 1;
      uVar15 = uVar6;
      uVar4 = uVar10 << 0x1f;
    } while (uVar6 != 0);
    goto LAB_ffff0000907c95b4;
  }
  puVar3 = (undefined4 *)FUN_ffff00009077d5a8(1,4);
  if (puVar3 == (undefined4 *)0x0) goto LAB_ffff0000907c9d30;
  if (local_48 != (undefined4 *)0x0) {
    FUN_ffff00009077eb20(puVar3,local_48,local_50 << 2);
    FUN_ffff0000907cca9c(local_48,local_50 << 2);
    thunk_FUN_ffff0000907c314c(local_48);
  }
  local_50 = 1;
  local_48 = puVar3;
  FUN_ffff00009077ee50(puVar3,0,4);
  *local_48 = 0;
  goto LAB_ffff0000907c95a8;
}


