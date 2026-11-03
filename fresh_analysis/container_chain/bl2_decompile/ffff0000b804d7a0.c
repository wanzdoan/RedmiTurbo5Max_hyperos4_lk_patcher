
void FUN_ffff0000b804d7a0(long param_1,ulong param_2,char *param_3,undefined8 *param_4)

{
  char *pcVar1;
  long lVar2;
  char cVar3;
  bool bVar4;
  int iVar5;
  ulong uVar6;
  undefined1 *puVar7;
  ulong uVar8;
  uint uVar9;
  ulong *puVar10;
  ulong uVar11;
  long *plVar12;
  undefined1 uVar13;
  ulong *puVar14;
  uint uVar15;
  uint uVar16;
  uint uVar17;
  byte bVar18;
  long lVar19;
  ulong uVar20;
  uint local_94;
  ulong *local_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  char local_64 [4];
  
  local_90 = (ulong *)*param_4;
  lVar2 = param_4[1];
  uVar20 = 0;
  lVar19 = 0;
  local_94 = *(uint *)(param_4 + 3);
  local_78 = 0;
  uStack_70 = 0;
  local_88 = 0;
  uStack_80 = 0;
LAB_ffff0000b804d7fc:
  do {
    uVar6 = 0;
    pcVar1 = param_3 + 2;
    local_64[0] = '\0';
    cVar3 = *param_3;
    while ((cVar3 != '\0' && (cVar3 != '%'))) {
      uVar6 = uVar6 + 1;
      pcVar1 = pcVar1 + 1;
      cVar3 = param_3[uVar6];
    }
    if (uVar6 != 0) {
      uVar11 = 0;
      uVar8 = uVar20;
      do {
        uVar20 = uVar8;
        if (uVar8 < param_2) {
          uVar20 = uVar8 + 1;
          *(char *)(param_1 + uVar8) = param_3[uVar11];
        }
        uVar11 = uVar11 + 1;
        uVar8 = uVar20;
      } while (uVar6 != uVar11);
      if ((int)uVar6 < 0) {
LAB_ffff0000b804e060:
        iVar5 = (int)uVar6;
        if (-1 < iVar5) {
          iVar5 = (int)lVar19;
        }
        lVar19 = (long)iVar5;
        if (param_2 <= uVar20) {
          lVar19 = param_2 - 1;
        }
        *(undefined1 *)(param_1 + lVar19) = 0;
        return;
      }
    }
    lVar19 = lVar19 + (uVar6 & 0xffffffff);
    if (cVar3 == '\0') goto LAB_ffff0000b804e060;
    uVar15 = 0;
    uVar17 = 0;
code_r0xffff0000b804d880:
    uVar16 = uVar17;
    param_3 = pcVar1;
    bVar18 = param_3[-1];
    if (0x7a < bVar18) goto code_r0xffff0000b804d974;
    pcVar1 = param_3 + 1;
    uVar17 = uVar16;
    uVar9 = local_94;
    switch(bVar18) {
    case 0:
      goto LAB_ffff0000b804e060;
    default:
      goto code_r0xffff0000b804d974;
    case 0x20:
      uVar17 = uVar16 | 0x2000;
      goto code_r0xffff0000b804d880;
    case 0x23:
      uVar17 = uVar16 | 0x80;
      goto code_r0xffff0000b804d880;
    case 0x25:
      if (uVar20 < param_2) {
        *(undefined1 *)(param_1 + uVar20) = 0x25;
        uVar20 = uVar20 + 1;
      }
      lVar19 = lVar19 + 1;
      goto LAB_ffff0000b804d7fc;
    case 0x2b:
      uVar17 = uVar16 | 0x200;
      goto code_r0xffff0000b804d880;
    case 0x2d:
      uVar17 = uVar16 | 0x800;
    case 0x2e:
      goto code_r0xffff0000b804d880;
    case 0x30:
    case 0x31:
    case 0x32:
    case 0x33:
    case 0x34:
    case 0x35:
    case 0x36:
    case 0x37:
    case 0x38:
    case 0x39:
      bVar4 = uVar15 != 0;
      uVar15 = ((uint)bVar18 + uVar15 * 10) - 0x30;
      uVar17 = uVar16 | 0x1000;
      if (bVar4 || bVar18 != 0x30) {
        uVar17 = uVar16;
      }
      goto code_r0xffff0000b804d880;
    case 0x58:
      uVar16 = uVar16 | 0x100;
    case 0x78:
      uVar17 = uVar16;
      if ((uVar16 >> 1 & 1) == 0) {
LAB_ffff0000b804da34:
        if ((uVar17 & 1) != 0) goto LAB_ffff0000b804da58;
        if ((uVar17 >> 3 & 1) != 0) {
          if (((int)local_94 < 0) && (uVar9 = local_94 + 8, local_94 < 0xfffffff9)) {
            uVar6 = (ulong)*(byte *)(lVar2 + (int)local_94);
            goto LAB_ffff0000b804dacc;
          }
          uVar6 = (ulong)(byte)*local_90;
          goto LAB_ffff0000b804dac8;
        }
        if ((uVar17 >> 2 & 1) == 0) {
          if ((((uVar17 >> 4 & 1) != 0) || ((uVar17 >> 5 & 1) != 0)) || ((uVar17 >> 6 & 1) != 0))
          goto LAB_ffff0000b804da58;
          if (((int)local_94 < 0) && (uVar9 = local_94 + 8, local_94 < 0xfffffff9)) {
            puVar14 = (ulong *)(lVar2 + (int)local_94);
            puVar10 = local_90;
          }
          else {
            puVar10 = local_90 + 1;
            puVar14 = local_90;
          }
          uVar6 = (ulong)(uint)*puVar14;
          local_90 = puVar10;
        }
        else {
          if ((-1 < (int)local_94) || (uVar9 = local_94 + 8, 0xfffffff8 < local_94)) {
            uVar6 = (ulong)(ushort)*local_90;
            goto LAB_ffff0000b804dac8;
          }
          uVar6 = (ulong)*(ushort *)(lVar2 + (int)local_94);
        }
      }
      else {
LAB_ffff0000b804da58:
        if (((int)local_94 < 0) && (uVar9 = local_94 + 8, local_94 < 0xfffffff9)) {
          uVar6 = *(ulong *)(lVar2 + (int)local_94);
        }
        else {
          uVar6 = *local_90;
LAB_ffff0000b804dac8:
          local_90 = local_90 + 1;
        }
      }
LAB_ffff0000b804dacc:
      puVar7 = (undefined1 *)FUN_ffff0000b804e9d0(&local_88,uVar6,uVar17);
      uVar16 = uVar17 & 0xffffff7f;
      if (uVar6 != 0) {
        uVar16 = uVar17;
      }
      bVar18 = 0x78;
      local_94 = uVar9;
      break;
    case 99:
      uVar15 = local_94;
      if (((int)local_94 < 0) && (uVar15 = local_94 + 8, local_94 < 0xfffffff9)) {
        puVar14 = (ulong *)(lVar2 + (int)local_94);
        puVar10 = local_90;
        local_94 = uVar15;
      }
      else {
        puVar10 = local_90 + 1;
        puVar14 = local_90;
        local_94 = uVar15;
      }
      local_90 = puVar10;
      if (uVar20 < param_2) {
        *(char *)(param_1 + uVar20) = (char)(uint)*puVar14;
        uVar20 = uVar20 + 1;
      }
      lVar19 = lVar19 + 1;
      goto LAB_ffff0000b804d7fc;
    case 100:
    case 0x69:
      uVar17 = local_94;
      if (((uVar16 >> 1 & 1) == 0) && ((uVar16 & 1) == 0)) {
        if ((uVar16 >> 3 & 1) == 0) {
          if ((uVar16 >> 2 & 1) == 0) {
            if ((((uVar16 >> 4 & 1) != 0) || ((uVar16 >> 5 & 1) != 0)) || ((uVar16 >> 6 & 1) != 0))
            goto LAB_ffff0000b804d964;
            if (((int)local_94 < 0) && (uVar17 = local_94 + 8, local_94 < 0xfffffff9)) {
              puVar14 = (ulong *)(lVar2 + (int)local_94);
              puVar10 = local_90;
            }
            else {
              puVar10 = local_90 + 1;
              puVar14 = local_90;
            }
            uVar6 = (ulong)(int)(uint)*puVar14;
            local_90 = puVar10;
          }
          else {
            if ((-1 < (int)local_94) || (uVar17 = local_94 + 8, 0xfffffff8 < local_94)) {
              uVar6 = (ulong)(short)*local_90;
              goto LAB_ffff0000b804db30;
            }
            uVar6 = (ulong)*(short *)(lVar2 + (int)local_94);
          }
        }
        else {
          if ((-1 < (int)local_94) || (uVar17 = local_94 + 8, 0xfffffff8 < local_94)) {
            uVar6 = (ulong)(char)*local_90;
            goto LAB_ffff0000b804db30;
          }
          uVar6 = (ulong)*(char *)(lVar2 + (int)local_94);
        }
      }
      else {
LAB_ffff0000b804d964:
        if (((int)local_94 < 0) && (uVar17 = local_94 + 8, local_94 < 0xfffffff9)) {
          uVar6 = *(ulong *)(lVar2 + (int)local_94);
        }
        else {
          uVar6 = *local_90;
LAB_ffff0000b804db30:
          local_90 = local_90 + 1;
        }
      }
      uVar16 = uVar16 | 0x400;
      puVar7 = (undefined1 *)FUN_ffff0000b804e93c(&local_88,uVar6,uVar16,local_64);
      local_94 = uVar17;
      break;
    case 0x68:
      uVar17 = uVar16 | (uVar16 & 4) << 1 | 4;
      goto code_r0xffff0000b804d880;
    case 0x6a:
      uVar17 = uVar16 | 0x20;
      goto code_r0xffff0000b804d880;
    case 0x6c:
      uVar17 = uVar16 | (uVar16 & 1) << 1 | 1;
      goto code_r0xffff0000b804d880;
    case 0x6e:
      uVar15 = local_94;
      if (((int)local_94 < 0) && (uVar15 = local_94 + 8, local_94 < 0xfffffff9)) {
        plVar12 = *(long **)(lVar2 + (int)local_94);
        local_94 = local_94 + 8;
      }
      else {
        plVar12 = (long *)*local_90;
        local_94 = uVar15;
        local_90 = local_90 + 1;
      }
      if (((uVar16 >> 1 & 1) == 0) && ((uVar16 & 1) == 0)) {
        if ((uVar16 >> 3 & 1) != 0) {
          *(char *)plVar12 = (char)lVar19;
          goto LAB_ffff0000b804d7fc;
        }
        if ((uVar16 >> 2 & 1) != 0) {
          *(short *)plVar12 = (short)lVar19;
          goto LAB_ffff0000b804d7fc;
        }
        if ((uVar16 >> 4 & 1) == 0) {
          *(int *)plVar12 = (int)lVar19;
          goto LAB_ffff0000b804d7fc;
        }
      }
      *plVar12 = lVar19;
      goto LAB_ffff0000b804d7fc;
    case 0x70:
      uVar17 = uVar16 | 0x90;
      if ((uVar16 & 2) == 0) goto LAB_ffff0000b804da34;
      goto LAB_ffff0000b804da58;
    case 0x73:
      uVar17 = local_94;
      if (((int)local_94 < 0) && (uVar17 = local_94 + 8, local_94 < 0xfffffff9)) {
        puVar14 = (ulong *)(lVar2 + (int)local_94);
        puVar10 = local_90;
      }
      else {
        puVar10 = local_90 + 1;
        puVar14 = local_90;
      }
      uVar16 = uVar16 & 0xffffefff;
      bVar18 = 0x73;
      puVar7 = (undefined1 *)0xffff0000b808bda8;
      local_94 = uVar17;
      local_90 = puVar10;
      if ((undefined1 *)*puVar14 != (undefined1 *)0x0) {
        puVar7 = (undefined1 *)*puVar14;
      }
      break;
    case 0x74:
      uVar17 = uVar16 | 0x40;
      goto code_r0xffff0000b804d880;
    case 0x75:
      uVar17 = local_94;
      if (((uVar16 >> 1 & 1) == 0) && ((uVar16 & 1) == 0)) {
        if ((uVar16 >> 3 & 1) == 0) {
          if ((uVar16 >> 2 & 1) == 0) {
            if ((((uVar16 >> 4 & 1) != 0) || ((uVar16 >> 5 & 1) != 0)) || ((uVar16 >> 6 & 1) != 0))
            goto LAB_ffff0000b804dafc;
            if (((int)local_94 < 0) && (uVar17 = local_94 + 8, local_94 < 0xfffffff9)) {
              puVar14 = (ulong *)(lVar2 + (int)local_94);
              puVar10 = local_90;
            }
            else {
              puVar10 = local_90 + 1;
              puVar14 = local_90;
            }
            uVar6 = (ulong)(uint)*puVar14;
            local_90 = puVar10;
          }
          else {
            if ((-1 < (int)local_94) || (uVar17 = local_94 + 8, 0xfffffff8 < local_94)) {
              uVar6 = (ulong)(ushort)*local_90;
              goto LAB_ffff0000b804dc3c;
            }
            uVar6 = (ulong)*(ushort *)(lVar2 + (int)local_94);
          }
        }
        else {
          if ((-1 < (int)local_94) || (uVar17 = local_94 + 8, 0xfffffff8 < local_94)) {
            uVar6 = (ulong)(byte)*local_90;
            goto LAB_ffff0000b804dc3c;
          }
          uVar6 = (ulong)*(byte *)(lVar2 + (int)local_94);
        }
      }
      else {
LAB_ffff0000b804dafc:
        if (((int)local_94 < 0) && (uVar17 = local_94 + 8, local_94 < 0xfffffff9)) {
          uVar6 = *(ulong *)(lVar2 + (int)local_94);
        }
        else {
          uVar6 = *local_90;
LAB_ffff0000b804dc3c:
          local_90 = local_90 + 1;
        }
      }
      puVar7 = (undefined1 *)FUN_ffff0000b804e93c(&local_88,uVar6,uVar16,local_64);
      bVar18 = 0x75;
      local_94 = uVar17;
      break;
    case 0x7a:
      uVar17 = uVar16 | 0x10;
      goto code_r0xffff0000b804d880;
    }
    uVar6 = FUN_ffff0000b804d3cc(puVar7);
    uVar17 = (uint)uVar6;
    if ((uVar16 >> 0xb & 1) != 0) {
      uVar11 = uVar6;
      uVar8 = uVar20;
      if (uVar6 != 0) break;
      goto LAB_ffff0000b804de04;
    }
    uVar15 = uVar15 - (local_64[0] != '\0' && uVar15 != 0);
    if (((uVar16 >> 0xc & 1) != 0) && (local_64[0] != '\0')) {
      uVar11 = uVar20;
      if (uVar20 < param_2) {
        uVar11 = uVar20 + 1;
        *(char *)(param_1 + uVar20) = local_64[0];
      }
      lVar19 = lVar19 + 1;
      uVar20 = uVar11;
    }
    uVar9 = uVar15;
    if ((bVar18 == 0x78) && ((uVar16 >> 7 & 1) != 0)) {
      uVar11 = uVar20;
      if (uVar20 < param_2) {
        uVar11 = uVar20 + 1;
        *(undefined1 *)(param_1 + uVar20) = 0x30;
      }
      uVar20 = uVar11;
      if (uVar11 < param_2) {
        uVar20 = uVar11 + 1;
        uVar13 = 0x78;
        if ((uVar16 & 0x100) != 0) {
          uVar13 = 0x58;
        }
        *(undefined1 *)(param_1 + uVar11) = uVar13;
      }
      lVar19 = lVar19 + 2;
      uVar9 = uVar15 - 2;
      if (uVar15 < 2) {
        uVar9 = uVar15;
      }
    }
    if (uVar6 < uVar9) {
      uVar13 = 0x30;
      uVar11 = uVar20;
      if ((uVar16 & 0x1000) == 0) {
        uVar13 = 0x20;
      }
      do {
        uVar20 = uVar11;
        if (uVar11 < param_2) {
          uVar20 = uVar11 + 1;
          *(undefined1 *)(param_1 + uVar11) = uVar13;
        }
        lVar19 = lVar19 + 1;
        uVar9 = uVar9 - 1;
        uVar11 = uVar20;
      } while (uVar17 < uVar9);
    }
    uVar11 = uVar20;
    if (((uVar16 >> 0xc & 1) == 0) && (local_64[0] != '\0')) {
      if (uVar20 < param_2) {
        uVar11 = uVar20 + 1;
        *(char *)(param_1 + uVar20) = local_64[0];
      }
      lVar19 = lVar19 + 1;
    }
    uVar8 = uVar6;
    uVar20 = uVar11;
    if (uVar6 != 0) {
      do {
        uVar20 = uVar11;
        if (uVar11 < param_2) {
          uVar20 = uVar11 + 1;
          *(undefined1 *)(param_1 + uVar11) = *puVar7;
        }
        puVar7 = puVar7 + 1;
        uVar8 = uVar8 - 1;
        uVar11 = uVar20;
      } while (uVar8 != 0);
      if ((int)uVar17 < 0) goto LAB_ffff0000b804e060;
    }
    lVar19 = lVar19 + (uVar6 & 0xffffffff);
  } while( true );
  do {
    uVar20 = uVar8;
    if (uVar8 < param_2) {
      uVar20 = uVar8 + 1;
      *(undefined1 *)(param_1 + uVar8) = *puVar7;
    }
    puVar7 = puVar7 + 1;
    uVar11 = uVar11 - 1;
    uVar8 = uVar20;
  } while (uVar11 != 0);
  if ((int)uVar17 < 0) goto LAB_ffff0000b804e060;
LAB_ffff0000b804de04:
  lVar19 = lVar19 + (uVar6 & 0xffffffff);
  for (; uVar17 < uVar15; uVar15 = uVar15 - 1) {
    uVar6 = uVar20;
    if (uVar20 < param_2) {
      uVar6 = uVar20 + 1;
      *(undefined1 *)(param_1 + uVar20) = 0x20;
    }
    lVar19 = lVar19 + 1;
    uVar20 = uVar6;
  }
  goto LAB_ffff0000b804d7fc;
code_r0xffff0000b804d974:
  uVar6 = uVar20;
  if (uVar20 < param_2) {
    uVar6 = uVar20 + 1;
    *(undefined1 *)(param_1 + uVar20) = 0x25;
  }
  uVar20 = uVar6;
  if (uVar6 < param_2) {
    uVar20 = uVar6 + 1;
    *(byte *)(param_1 + uVar6) = bVar18;
  }
  lVar19 = lVar19 + 2;
  goto LAB_ffff0000b804d7fc;
}


