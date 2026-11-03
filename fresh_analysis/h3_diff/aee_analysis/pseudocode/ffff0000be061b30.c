
uint FUN_ffff0000be061b30(code *param_1,undefined8 param_2,char *param_3,long *param_4)

{
  char cVar1;
  byte bVar2;
  bool bVar3;
  uint uVar4;
  uint uVar5;
  long lVar6;
  byte bVar7;
  undefined4 *puVar8;
  undefined8 *puVar9;
  long *plVar10;
  byte *pbVar11;
  char *pcVar12;
  ushort *puVar13;
  short *psVar14;
  ulong *puVar15;
  uint *puVar16;
  int *piVar17;
  long lVar18;
  uint uVar19;
  uint uVar20;
  ulong uVar21;
  byte local_8c [4];
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  byte local_64 [4];
  
  local_78 = 0;
  uStack_70 = 0;
  local_88 = 0;
  uStack_80 = 0;
  lVar6 = 0;
LAB_ffff0000be061b7c:
  lVar18 = lVar6;
  lVar6 = 0;
  pcVar12 = param_3 + 2;
  local_64[0] = 0;
  cVar1 = *param_3;
  while ((cVar1 != '\0' && (cVar1 != '%'))) {
    lVar6 = lVar6 + 1;
    pcVar12 = pcVar12 + 1;
    cVar1 = param_3[lVar6];
  }
  uVar4 = (*param_1)(param_3,lVar6,param_2);
  if ((-1 < (int)uVar4) && (lVar18 = lVar18 + (ulong)uVar4, cVar1 != '\0')) {
    uVar19 = 0;
    uVar5 = 0;
code_r0xffff0000be061be4:
    uVar20 = uVar5;
    param_3 = pcVar12;
    bVar7 = param_3[-1];
    if (0x7a < bVar7) goto code_r0xffff0000be061d08;
    pcVar12 = param_3 + 1;
    uVar5 = uVar20;
    switch(bVar7) {
    case 0:
      goto code_r0xffff0000be062398;
    default:
      goto code_r0xffff0000be061d08;
    case 0x20:
      uVar5 = uVar20 | 0x2000;
      goto code_r0xffff0000be061be4;
    case 0x23:
      uVar5 = uVar20 | 0x80;
      goto code_r0xffff0000be061be4;
    case 0x25:
      local_8c[0] = 0x25;
      goto LAB_ffff0000be061ed0;
    case 0x2b:
      uVar5 = uVar20 | 0x200;
      goto code_r0xffff0000be061be4;
    case 0x2d:
      uVar5 = uVar20 | 0x800;
    case 0x2e:
      goto code_r0xffff0000be061be4;
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
      bVar3 = uVar19 != 0;
      uVar19 = ((uint)bVar7 + uVar19 * 10) - 0x30;
      uVar5 = uVar20 | 0x1000;
      if (bVar3 || bVar7 != 0x30) {
        uVar5 = uVar20;
      }
      goto code_r0xffff0000be061be4;
    case 0x58:
      uVar20 = uVar20 | 0x100;
    case 0x78:
      uVar4 = uVar20;
      if ((uVar20 >> 1 & 1) == 0) {
LAB_ffff0000be061de4:
        if ((uVar4 & 1) != 0) goto LAB_ffff0000be061e20;
        if ((uVar4 >> 3 & 1) == 0) {
          if ((uVar4 >> 2 & 1) != 0) {
            uVar5 = *(uint *)(param_4 + 3);
            if (((int)uVar5 < 0) && (*(uint *)(param_4 + 3) = uVar5 + 8, uVar5 < 0xfffffff9)) {
              uVar21 = (ulong)*(ushort *)(param_4[1] + (long)(int)uVar5);
            }
            else {
              puVar13 = (ushort *)*param_4;
              *param_4 = (long)(puVar13 + 4);
              uVar21 = (ulong)*puVar13;
            }
            goto LAB_ffff0000be062108;
          }
          if (((uVar4 >> 4 & 1) != 0) || ((uVar4 >> 5 & 1) != 0)) goto LAB_ffff0000be061e20;
          uVar5 = *(uint *)(param_4 + 3);
          lVar6 = (long)(int)uVar5;
          if ((uVar4 >> 6 & 1) == 0) {
            if (((int)uVar5 < 0) && (*(uint *)(param_4 + 3) = uVar5 + 8, uVar5 < 0xfffffff9)) {
              puVar16 = (uint *)(param_4[1] + lVar6);
            }
            else {
              puVar16 = (uint *)*param_4;
              *param_4 = (long)(puVar16 + 2);
            }
            uVar21 = (ulong)*puVar16;
            goto LAB_ffff0000be062108;
          }
          if ((int)uVar5 < 0) goto LAB_ffff0000be061e28;
          goto LAB_ffff0000be0620f8;
        }
        uVar5 = *(uint *)(param_4 + 3);
        if (((int)uVar5 < 0) && (*(uint *)(param_4 + 3) = uVar5 + 8, uVar5 < 0xfffffff9)) {
          uVar21 = (ulong)*(byte *)(param_4[1] + (long)(int)uVar5);
        }
        else {
          pbVar11 = (byte *)*param_4;
          *param_4 = (long)(pbVar11 + 8);
          uVar21 = (ulong)*pbVar11;
        }
      }
      else {
LAB_ffff0000be061e20:
        lVar6 = (long)(int)param_4[3];
        if ((int)param_4[3] < 0) {
LAB_ffff0000be061e28:
          *(uint *)(param_4 + 3) = (uint)lVar6 + 8;
          if (0xfffffff8 < (uint)lVar6) goto LAB_ffff0000be0620f8;
          puVar15 = (ulong *)(param_4[1] + lVar6);
        }
        else {
LAB_ffff0000be0620f8:
          puVar15 = (ulong *)*param_4;
          *param_4 = (long)(puVar15 + 1);
        }
        uVar21 = *puVar15;
      }
LAB_ffff0000be062108:
      lVar6 = FUN_ffff0000be062460(&local_88,uVar21,uVar4);
      uVar20 = uVar4 & 0xffffff7f;
      if (uVar21 != 0) {
        uVar20 = uVar4;
      }
      bVar7 = 0x78;
      break;
    case 99:
      uVar4 = *(uint *)(param_4 + 3);
      if (((int)uVar4 < 0) && (*(uint *)(param_4 + 3) = uVar4 + 8, uVar4 < 0xfffffff9)) {
        puVar8 = (undefined4 *)(param_4[1] + (long)(int)uVar4);
      }
      else {
        puVar8 = (undefined4 *)*param_4;
        *param_4 = (long)(puVar8 + 2);
      }
      local_8c[0] = (byte)*puVar8;
LAB_ffff0000be061ed0:
      uVar4 = (*param_1)(local_8c,1,param_2);
      lVar6 = lVar18 + (ulong)uVar4;
      goto joined_r0xffff0000be061edc;
    case 100:
    case 0x69:
      if (((uVar20 >> 1 & 1) == 0) && ((uVar20 & 1) == 0)) {
        if ((uVar20 >> 3 & 1) == 0) {
          if ((uVar20 >> 2 & 1) == 0) {
            if (((uVar20 >> 4 & 1) != 0) || ((uVar20 >> 5 & 1) != 0)) goto LAB_ffff0000be061ce0;
            uVar4 = *(uint *)(param_4 + 3);
            lVar6 = (long)(int)uVar4;
            if ((uVar20 >> 6 & 1) != 0) {
              if ((int)uVar4 < 0) goto LAB_ffff0000be061ce8;
              goto LAB_ffff0000be062144;
            }
            if (((int)uVar4 < 0) && (*(uint *)(param_4 + 3) = uVar4 + 8, uVar4 < 0xfffffff9)) {
              piVar17 = (int *)(param_4[1] + lVar6);
            }
            else {
              piVar17 = (int *)*param_4;
              *param_4 = (long)(piVar17 + 2);
            }
            lVar6 = (long)*piVar17;
          }
          else {
            uVar4 = *(uint *)(param_4 + 3);
            if (((int)uVar4 < 0) && (*(uint *)(param_4 + 3) = uVar4 + 8, uVar4 < 0xfffffff9)) {
              lVar6 = (long)*(short *)(param_4[1] + (long)(int)uVar4);
            }
            else {
              psVar14 = (short *)*param_4;
              *param_4 = (long)(psVar14 + 4);
              lVar6 = (long)*psVar14;
            }
          }
        }
        else {
          uVar4 = *(uint *)(param_4 + 3);
          if (((int)uVar4 < 0) && (*(uint *)(param_4 + 3) = uVar4 + 8, uVar4 < 0xfffffff9)) {
            lVar6 = (long)*(char *)(param_4[1] + (long)(int)uVar4);
          }
          else {
            pcVar12 = (char *)*param_4;
            *param_4 = (long)(pcVar12 + 8);
            lVar6 = (long)*pcVar12;
          }
        }
      }
      else {
LAB_ffff0000be061ce0:
        lVar6 = (long)(int)param_4[3];
        if ((int)param_4[3] < 0) {
LAB_ffff0000be061ce8:
          *(uint *)(param_4 + 3) = (uint)lVar6 + 8;
          if (0xfffffff8 < (uint)lVar6) goto LAB_ffff0000be062144;
          plVar10 = (long *)(param_4[1] + lVar6);
        }
        else {
LAB_ffff0000be062144:
          plVar10 = (long *)*param_4;
          *param_4 = (long)(plVar10 + 1);
        }
        lVar6 = *plVar10;
      }
      uVar20 = uVar20 | 0x400;
      lVar6 = FUN_ffff0000be0623cc(&local_88,lVar6,uVar20,local_64);
      break;
    case 0x68:
      uVar5 = uVar20 | (uVar20 & 4) << 1 | 4;
      goto code_r0xffff0000be061be4;
    case 0x6a:
      uVar5 = uVar20 | 0x20;
      goto code_r0xffff0000be061be4;
    case 0x6c:
      uVar5 = uVar20 | (uVar20 & 1) << 1 | 1;
      goto code_r0xffff0000be061be4;
    case 0x6e:
      uVar4 = *(uint *)(param_4 + 3);
      if (((int)uVar4 < 0) && (*(uint *)(param_4 + 3) = uVar4 + 8, uVar4 < 0xfffffff9)) {
        puVar9 = (undefined8 *)(param_4[1] + (long)(int)uVar4);
      }
      else {
        puVar9 = (undefined8 *)*param_4;
        *param_4 = (long)(puVar9 + 1);
      }
      plVar10 = (long *)*puVar9;
      lVar6 = lVar18;
      if (((uVar20 >> 1 & 1) == 0) && ((uVar20 & 1) == 0)) {
        if ((uVar20 >> 3 & 1) != 0) {
          *(char *)plVar10 = (char)lVar18;
          goto LAB_ffff0000be061b7c;
        }
        if ((uVar20 >> 2 & 1) != 0) {
          *(short *)plVar10 = (short)lVar18;
          goto LAB_ffff0000be061b7c;
        }
        if ((uVar20 >> 4 & 1) == 0) {
          *(int *)plVar10 = (int)lVar18;
          goto LAB_ffff0000be061b7c;
        }
      }
      *plVar10 = lVar18;
      goto LAB_ffff0000be061b7c;
    case 0x70:
      uVar4 = uVar20 | 0x90;
      if ((uVar20 & 2) == 0) goto LAB_ffff0000be061de4;
      goto LAB_ffff0000be061e20;
    case 0x73:
      uVar4 = *(uint *)(param_4 + 3);
      if (((int)uVar4 < 0) && (*(uint *)(param_4 + 3) = uVar4 + 8, uVar4 < 0xfffffff9)) {
        plVar10 = (long *)(param_4[1] + (long)(int)uVar4);
      }
      else {
        plVar10 = (long *)*param_4;
        *param_4 = (long)(plVar10 + 1);
      }
      uVar20 = uVar20 & 0xffffefff;
      bVar7 = 0x73;
      lVar6 = -0xffff41f5429a;
      if (*plVar10 != 0) {
        lVar6 = *plVar10;
      }
      break;
    case 0x74:
      uVar5 = uVar20 | 0x40;
      goto code_r0xffff0000be061be4;
    case 0x75:
      if (((uVar20 >> 1 & 1) == 0) && ((uVar20 & 1) == 0)) {
        if ((uVar20 >> 3 & 1) == 0) {
          if ((uVar20 >> 2 & 1) == 0) {
            if (((uVar20 >> 4 & 1) != 0) || ((uVar20 >> 5 & 1) != 0)) goto LAB_ffff0000be061f44;
            uVar4 = *(uint *)(param_4 + 3);
            lVar6 = (long)(int)uVar4;
            if ((uVar20 >> 6 & 1) != 0) {
              if ((int)uVar4 < 0) goto LAB_ffff0000be061f4c;
              goto LAB_ffff0000be062184;
            }
            if (((int)uVar4 < 0) && (*(uint *)(param_4 + 3) = uVar4 + 8, uVar4 < 0xfffffff9)) {
              puVar16 = (uint *)(param_4[1] + lVar6);
            }
            else {
              puVar16 = (uint *)*param_4;
              *param_4 = (long)(puVar16 + 2);
            }
            uVar21 = (ulong)*puVar16;
          }
          else {
            uVar4 = *(uint *)(param_4 + 3);
            if (((int)uVar4 < 0) && (*(uint *)(param_4 + 3) = uVar4 + 8, uVar4 < 0xfffffff9)) {
              uVar21 = (ulong)*(ushort *)(param_4[1] + (long)(int)uVar4);
            }
            else {
              puVar13 = (ushort *)*param_4;
              *param_4 = (long)(puVar13 + 4);
              uVar21 = (ulong)*puVar13;
            }
          }
        }
        else {
          uVar4 = *(uint *)(param_4 + 3);
          if (((int)uVar4 < 0) && (*(uint *)(param_4 + 3) = uVar4 + 8, uVar4 < 0xfffffff9)) {
            uVar21 = (ulong)*(byte *)(param_4[1] + (long)(int)uVar4);
          }
          else {
            pbVar11 = (byte *)*param_4;
            *param_4 = (long)(pbVar11 + 8);
            uVar21 = (ulong)*pbVar11;
          }
        }
      }
      else {
LAB_ffff0000be061f44:
        lVar6 = (long)(int)param_4[3];
        if ((int)param_4[3] < 0) {
LAB_ffff0000be061f4c:
          *(uint *)(param_4 + 3) = (uint)lVar6 + 8;
          if (0xfffffff8 < (uint)lVar6) goto LAB_ffff0000be062184;
          puVar15 = (ulong *)(param_4[1] + lVar6);
        }
        else {
LAB_ffff0000be062184:
          puVar15 = (ulong *)*param_4;
          *param_4 = (long)(puVar15 + 1);
        }
        uVar21 = *puVar15;
      }
      lVar6 = FUN_ffff0000be0623cc(&local_88,uVar21,uVar20,local_64);
      bVar7 = 0x75;
      break;
    case 0x7a:
      uVar5 = uVar20 | 0x10;
      goto code_r0xffff0000be061be4;
    }
    uVar21 = FUN_ffff0000be060e5c();
    bVar2 = local_64[0];
    if ((uVar20 >> 0xb & 1) != 0) {
      uVar5 = (*param_1)(lVar6,uVar21,param_2);
      uVar4 = uVar5;
      if ((int)uVar5 < 0) goto code_r0xffff0000be062398;
      lVar18 = lVar18 + (ulong)uVar5;
      goto joined_r0xffff0000be0622a4;
    }
    bVar3 = local_64[0] != 0;
    if (((uVar20 >> 0xc & 1) != 0) && (local_64[0] != 0)) {
      local_8c[0] = local_64[0];
      uVar4 = (*param_1)(local_8c,1,param_2);
      if ((int)uVar4 < 0) goto code_r0xffff0000be062398;
      lVar18 = lVar18 + (ulong)uVar4;
    }
    uVar19 = uVar19 - (bVar3 && uVar19 != 0);
    uVar5 = uVar19;
    if ((bVar7 == 0x78) && ((uVar20 >> 7 & 1) != 0)) {
      local_8c[0] = 0x30;
      uVar4 = (*param_1)(local_8c,1,param_2);
      if ((int)uVar4 < 0) goto code_r0xffff0000be062398;
      local_8c[0] = 0x78;
      if ((uVar20 & 0x100) != 0) {
        local_8c[0] = 0x58;
      }
      lVar18 = lVar18 + (ulong)uVar4;
      uVar4 = (*param_1)(local_8c,1,param_2);
      if ((int)uVar4 < 0) goto code_r0xffff0000be062398;
      lVar18 = lVar18 + (ulong)uVar4;
      uVar5 = uVar19 - 2;
      if (uVar19 < 2) {
        uVar5 = uVar19;
      }
    }
    if (uVar5 <= uVar21) goto LAB_ffff0000be062320;
    bVar7 = 0x30;
    if ((uVar20 & 0x1000) == 0) {
      bVar7 = 0x20;
    }
    goto LAB_ffff0000be0622f0;
  }
  goto code_r0xffff0000be062398;
joined_r0xffff0000be0622a4:
  lVar6 = lVar18;
  if (uVar19 <= uVar5) goto LAB_ffff0000be061b7c;
  local_8c[0] = 0x20;
  uVar4 = (*param_1)(local_8c,1,param_2);
  if ((int)uVar4 < 0) goto code_r0xffff0000be062398;
  lVar18 = lVar18 + (ulong)uVar4;
  uVar19 = uVar19 - 1;
  goto joined_r0xffff0000be0622a4;
  while( true ) {
    lVar18 = lVar18 + (ulong)uVar4;
    uVar5 = uVar5 - 1;
    if (uVar5 <= (uint)uVar21) break;
LAB_ffff0000be0622f0:
    local_8c[0] = bVar7;
    uVar4 = (*param_1)(local_8c,1,param_2);
    if ((int)uVar4 < 0) goto code_r0xffff0000be062398;
  }
LAB_ffff0000be062320:
  if (((uVar20 >> 0xc & 1) == 0) && (bVar2 != 0)) {
    local_8c[0] = bVar2;
    uVar4 = (*param_1)(local_8c,1,param_2);
    if ((int)uVar4 < 0) goto code_r0xffff0000be062398;
    lVar18 = lVar18 + (ulong)uVar4;
  }
  uVar4 = (*param_1)(lVar6,uVar21,param_2);
  if ((int)uVar4 < 0) goto code_r0xffff0000be062398;
  lVar6 = lVar18 + (ulong)uVar4;
  goto LAB_ffff0000be061b7c;
code_r0xffff0000be061d08:
  local_8c[0] = 0x25;
  uVar4 = (*param_1)(local_8c,1,param_2);
  if ((int)uVar4 < 0) goto code_r0xffff0000be062398;
  lVar18 = lVar18 + (ulong)uVar4;
  local_8c[0] = bVar7;
  uVar4 = (*param_1)(local_8c,1,param_2);
  lVar6 = lVar18 + (ulong)uVar4;
joined_r0xffff0000be061edc:
  if ((int)uVar4 < 0) {
code_r0xffff0000be062398:
    if (-1 < (int)uVar4) {
      uVar4 = (uint)lVar18;
    }
    return uVar4;
  }
  goto LAB_ffff0000be061b7c;
}


