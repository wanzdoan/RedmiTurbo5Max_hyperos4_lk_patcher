// ffff00009079cb70  size=1296  FUN_ffff00009079cb70

/* WARNING: Type propagation algorithm not settling */

int FUN_ffff00009079cb70(char *param_1,char *param_2,char *param_3)

{
  undefined8 uVar1;
  undefined8 uVar2;
  long lVar3;
  int iVar4;
  uint uVar5;
  char *pcVar6;
  char *pcVar7;
  ushort *puVar8;
  uint uVar9;
  ushort *puVar10;
  uint uVar11;
  ushort *puVar12;
  ushort *puVar13;
  undefined8 uVar14;
  ulong uVar15;
  ulong uVar16;
  undefined8 local_78;
  undefined8 local_70;
  ushort *local_68;
  
  if (((param_1 == (char *)0x0) || (param_2 == (char *)0x0)) || (param_3 == (char *)0x0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_sboot_c_ffff0000907e05aa,0x389,
               s_a______void__0_____b______void___ffff0000907d6f3c);
  }
  if (*param_1 == *param_2) {
    pcVar6 = param_2;
    pcVar7 = param_1;
    if (((param_3 != param_2) && (pcVar6 = param_3, pcVar7 = param_2, param_3 != param_1)) &&
       (iVar4 = FUN_ffff00009079bf14(param_1,param_3), iVar4 != 0)) {
      return iVar4;
    }
    iVar4 = FUN_ffff00009079d080(pcVar6,pcVar7);
    if (iVar4 != 0) {
      return iVar4;
    }
  }
  else {
    uVar11 = *(uint *)(param_1 + 8);
    uVar5 = *(uint *)(param_2 + 8);
    if (uVar5 < uVar11) {
LAB_ffff00009079cbf0:
      if (param_3 != param_2) {
        if (param_3 != param_1) {
          iVar4 = FUN_ffff00009079bf14(param_1,param_3);
          if (iVar4 != 0) {
            return iVar4;
          }
          uVar5 = *(uint *)(param_2 + 8);
        }
        puVar8 = *(ushort **)(param_3 + 0x10);
        if (uVar5 == 0) {
          uVar9 = 0;
          puVar13 = puVar8;
        }
        else {
          iVar4 = 0;
          puVar10 = *(ushort **)(param_2 + 0x10);
          puVar12 = puVar8;
          uVar11 = uVar5;
          do {
            uVar9 = (iVar4 - (uint)*puVar10) + (uint)*puVar12 + 0x10000;
            iVar4 = -(uint)(uVar9 < 0x10000);
            uVar11 = uVar11 - 1;
            puVar13 = puVar12 + 1;
            *puVar12 = (ushort)uVar9;
            puVar10 = puVar10 + 1;
            puVar12 = puVar13;
          } while (uVar11 != 0);
          uVar9 = (uint)(uVar9 < 0x10000);
        }
        uVar11 = *(uint *)(param_3 + 8);
        uVar15 = (ulong)uVar11;
        if (uVar5 < uVar11) {
          iVar4 = uVar11 - uVar5;
          do {
            uVar5 = (*puVar13 | 0x10000) - uVar9;
            uVar9 = (uint)(uVar5 < 0x10000);
            iVar4 = iVar4 + -1;
            *puVar13 = (ushort)uVar5;
            puVar13 = puVar13 + 1;
          } while (iVar4 != 0);
        }
        if (1 < uVar11) {
          do {
            if (puVar8[uVar15 - 1] != 0) goto LAB_ffff00009079cec0;
            uVar15 = uVar15 - 1;
          } while (1 < (uint)uVar15);
          uVar15 = 1;
        }
LAB_ffff00009079cec0:
        *(int *)(param_3 + 8) = (int)uVar15;
joined_r0xffff00009079cec4:
        if (uVar9 != 0) {
          return -3;
        }
        goto LAB_ffff00009079d024;
      }
      local_70 = 0;
      local_68 = (ushort *)0x0;
      local_78 = 0;
      if ((char *)&local_78 == param_1) {
        uVar15 = 0;
        if (uVar5 == 0) goto LAB_ffff00009079ced8;
LAB_ffff00009079cdb4:
        iVar4 = 0;
        puVar13 = *(ushort **)(param_2 + 0x10);
        puVar10 = local_68;
        uVar11 = uVar5;
        do {
          uVar9 = (iVar4 - (uint)*puVar13) + (uint)*puVar10 + 0x10000;
          iVar4 = -(uint)(uVar9 < 0x10000);
          uVar11 = uVar11 - 1;
          puVar8 = puVar10 + 1;
          *puVar10 = (ushort)uVar9;
          puVar13 = puVar13 + 1;
          puVar10 = puVar8;
        } while (uVar11 != 0);
        uVar11 = (uint)(uVar9 < 0x10000);
        if (uVar5 < (uint)uVar15) {
LAB_ffff00009079cee8:
          iVar4 = (int)uVar15 - uVar5;
          do {
            uVar5 = (*puVar8 | 0x10000) - uVar11;
            uVar11 = (uint)(uVar5 < 0x10000);
            iVar4 = iVar4 + -1;
            *puVar8 = (ushort)uVar5;
            puVar8 = puVar8 + 1;
          } while (iVar4 != 0);
        }
      }
      else {
        local_68 = (ushort *)FUN_ffff00009077d2e0(uVar11,2);
        if (local_68 == (ushort *)0x0) {
          return -2;
        }
        FUN_ffff00009077e858
                  (local_68,*(undefined8 *)(param_1 + 0x10),(ulong)*(uint *)(param_1 + 8) << 1);
        uVar15 = (ulong)*(uint *)(param_1 + 8);
        uVar5 = *(uint *)(param_2 + 8);
        local_78 = CONCAT44(*(uint *)(param_1 + 8),(undefined4)local_78);
        local_78 = CONCAT71(local_78._1_7_,*param_1);
        if (uVar5 != 0) goto LAB_ffff00009079cdb4;
LAB_ffff00009079ced8:
        uVar11 = 0;
        puVar8 = local_68;
        if (uVar5 < (uint)uVar15) goto LAB_ffff00009079cee8;
      }
      uVar16 = uVar15;
      if (1 < (uint)uVar15) {
        do {
          if (local_68[uVar16 - 1] != 0) goto LAB_ffff00009079cf38;
          uVar16 = uVar16 - 1;
        } while (1 < (uint)uVar16);
        uVar16 = 1;
      }
LAB_ffff00009079cf38:
      local_70 = CONCAT44(local_70._4_4_,(int)uVar16);
      if (uVar11 != 0) {
        if ((local_68 != (ushort *)0x0) &&
           (FUN_ffff00009077eb88(local_68,0,uVar15 << 1), local_68 != (ushort *)0x0)) {
          thunk_FUN_ffff0000907c2ad8();
        }
        return -3;
      }
      uVar1 = *(undefined8 *)param_2;
      uVar2 = *(undefined8 *)(param_2 + 8);
      uVar14 = *(undefined8 *)(param_2 + 0x10);
      *(undefined8 *)param_2 = local_78;
      *(undefined8 *)(param_2 + 8) = local_70;
      *(ushort **)(param_2 + 0x10) = local_68;
      local_78 = uVar1;
      local_70 = uVar2;
      local_68 = (ushort *)uVar14;
    }
    else {
      if (uVar5 <= uVar11) {
        if (-1 < (int)(uVar11 - 1)) {
          lVar3 = (ulong)(uVar11 - 1) * 2;
          puVar8 = (ushort *)(*(long *)(param_2 + 0x10) + lVar3);
          puVar13 = (ushort *)(*(long *)(param_1 + 0x10) + lVar3);
          uVar9 = uVar11;
          do {
            if (*puVar8 < *puVar13) goto LAB_ffff00009079cbf0;
            if (*puVar13 < *puVar8) goto LAB_ffff00009079cc68;
            puVar13 = puVar13 + -1;
            puVar8 = puVar8 + -1;
            uVar9 = uVar9 - 1;
          } while (0 < (int)uVar9);
        }
        FUN_ffff00009077eb88(*(undefined8 *)(param_3 + 0x10),0,(ulong)*(uint *)(param_3 + 4) << 1);
        param_3[8] = '\x01';
        param_3[9] = '\0';
        param_3[10] = '\0';
        param_3[0xb] = '\0';
        goto LAB_ffff00009079d048;
      }
LAB_ffff00009079cc68:
      if (param_3 != param_1) {
        if (param_3 != param_2) {
          iVar4 = FUN_ffff00009079bf14(param_2,param_3);
          if (iVar4 != 0) {
            return iVar4;
          }
          uVar11 = *(uint *)(param_1 + 8);
        }
        puVar8 = *(ushort **)(param_3 + 0x10);
        if (uVar11 == 0) {
          uVar9 = 0;
          puVar13 = puVar8;
        }
        else {
          iVar4 = 0;
          puVar10 = *(ushort **)(param_1 + 0x10);
          puVar12 = puVar8;
          uVar5 = uVar11;
          do {
            uVar9 = (iVar4 - (uint)*puVar10) + (uint)*puVar12 + 0x10000;
            iVar4 = -(uint)(uVar9 < 0x10000);
            uVar5 = uVar5 - 1;
            puVar13 = puVar12 + 1;
            *puVar12 = (ushort)uVar9;
            puVar10 = puVar10 + 1;
            puVar12 = puVar13;
          } while (uVar5 != 0);
          uVar9 = (uint)(uVar9 < 0x10000);
        }
        uVar5 = *(uint *)(param_3 + 8);
        uVar15 = (ulong)uVar5;
        if (uVar11 < uVar5) {
          iVar4 = uVar5 - uVar11;
          do {
            uVar11 = (*puVar13 | 0x10000) - uVar9;
            uVar9 = (uint)(uVar11 < 0x10000);
            iVar4 = iVar4 + -1;
            *puVar13 = (ushort)uVar11;
            puVar13 = puVar13 + 1;
          } while (iVar4 != 0);
        }
        if (1 < uVar5) {
          do {
            if (puVar8[uVar15 - 1] != 0) goto LAB_ffff00009079cfec;
            uVar15 = uVar15 - 1;
          } while (1 < (uint)uVar15);
          uVar15 = 1;
        }
LAB_ffff00009079cfec:
        *(int *)(param_3 + 8) = (int)uVar15;
        goto joined_r0xffff00009079cec4;
      }
      local_70 = 0;
      local_68 = (ushort *)0x0;
      local_78 = 0;
      if ((char *)&local_78 != param_2) {
        local_68 = (ushort *)FUN_ffff00009077d2e0(uVar5,2);
        if (local_68 == (ushort *)0x0) {
          return -2;
        }
        FUN_ffff00009077e858
                  (local_68,*(undefined8 *)(param_2 + 0x10),(ulong)*(uint *)(param_2 + 8) << 1);
        local_78 = CONCAT44(*(undefined4 *)(param_2 + 8),(undefined4)local_78);
        local_70 = CONCAT44(local_70._4_4_,*(undefined4 *)(param_2 + 8));
        local_78 = CONCAT71(local_78._1_7_,*param_2);
      }
      iVar4 = FUN_ffff00009079d238(&local_78,param_1);
      if (iVar4 != 0) {
        FUN_ffff00009079bebc(&local_78);
        return iVar4;
      }
      uVar1 = *(undefined8 *)param_1;
      uVar2 = *(undefined8 *)(param_1 + 8);
      uVar14 = *(undefined8 *)(param_1 + 0x10);
      *(undefined8 *)param_1 = local_78;
      *(undefined8 *)(param_1 + 8) = local_70;
      *(ushort **)(param_1 + 0x10) = local_68;
      local_78 = uVar1;
      local_70 = uVar2;
      local_68 = (ushort *)uVar14;
    }
    FUN_ffff00009079bebc(&local_78);
  }
LAB_ffff00009079d024:
  if ((*(int *)(param_3 + 8) != 1) || (**(short **)(param_3 + 0x10) != 0)) {
    return 0;
  }
LAB_ffff00009079d048:
  *param_3 = '\0';
  return 0;
}


