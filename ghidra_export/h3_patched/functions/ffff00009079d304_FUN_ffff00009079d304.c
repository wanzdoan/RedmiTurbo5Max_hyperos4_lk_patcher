// ffff00009079d304  size=1336  FUN_ffff00009079d304

int FUN_ffff00009079d304(char *param_1,char *param_2,char *param_3)

{
  undefined8 uVar1;
  undefined8 uVar2;
  long lVar3;
  char cVar4;
  int iVar5;
  ushort *puVar6;
  uint uVar7;
  ushort *puVar8;
  uint uVar9;
  ushort *puVar10;
  ushort *puVar11;
  undefined8 uVar12;
  uint uVar13;
  ulong uVar14;
  ulong uVar15;
  undefined8 local_78;
  undefined8 local_70;
  ushort *local_68;
  
  if (((param_1 == (char *)0x0) || (param_2 == (char *)0x0)) || (param_3 == (char *)0x0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_sboot_c_ffff0000907e0593,0x3ef,
               s_a______void__0_____b______void___ffff0000907d6f3c);
  }
  if (*param_1 == *param_2) {
    uVar7 = *(uint *)(param_1 + 8);
    uVar9 = *(uint *)(param_2 + 8);
    if (uVar9 < uVar7) {
LAB_ffff00009079d354:
      if (param_3 != param_2) {
        if (param_3 != param_1) {
          iVar5 = FUN_ffff00009079bf14(param_1,param_3);
          if (iVar5 != 0) {
            return iVar5;
          }
          uVar9 = *(uint *)(param_2 + 8);
        }
        puVar6 = *(ushort **)(param_3 + 0x10);
        if (uVar9 == 0) {
          uVar7 = 0;
          puVar11 = puVar6;
        }
        else {
          iVar5 = 0;
          puVar8 = *(ushort **)(param_2 + 0x10);
          puVar10 = puVar6;
          uVar7 = uVar9;
          do {
            uVar13 = (iVar5 - (uint)*puVar8) + (uint)*puVar10 + 0x10000;
            iVar5 = -(uint)(uVar13 < 0x10000);
            uVar7 = uVar7 - 1;
            puVar11 = puVar10 + 1;
            *puVar10 = (ushort)uVar13;
            puVar8 = puVar8 + 1;
            puVar10 = puVar11;
          } while (uVar7 != 0);
          uVar7 = (uint)(uVar13 < 0x10000);
        }
        uVar13 = *(uint *)(param_3 + 8);
        uVar14 = (ulong)uVar13;
        if (uVar9 < uVar13) {
          iVar5 = uVar13 - uVar9;
          do {
            uVar9 = (*puVar11 | 0x10000) - uVar7;
            uVar7 = (uint)(uVar9 < 0x10000);
            iVar5 = iVar5 + -1;
            *puVar11 = (ushort)uVar9;
            puVar11 = puVar11 + 1;
          } while (iVar5 != 0);
        }
        if (1 < uVar13) {
          do {
            if (puVar6[uVar14 - 1] != 0) goto LAB_ffff00009079d664;
            uVar14 = uVar14 - 1;
          } while (1 < (uint)uVar14);
          uVar14 = 1;
        }
LAB_ffff00009079d664:
        *(int *)(param_3 + 8) = (int)uVar14;
        if (uVar7 != 0) {
          return -3;
        }
        goto LAB_ffff00009079d7e0;
      }
      local_70 = 0;
      local_68 = (ushort *)0x0;
      local_78 = 0;
      if ((char *)&local_78 == param_1) {
        uVar14 = 0;
        if (uVar9 == 0) goto LAB_ffff00009079d67c;
LAB_ffff00009079d558:
        iVar5 = 0;
        puVar11 = *(ushort **)(param_2 + 0x10);
        puVar8 = local_68;
        uVar7 = uVar9;
        do {
          uVar13 = (iVar5 - (uint)*puVar11) + (uint)*puVar8 + 0x10000;
          iVar5 = -(uint)(uVar13 < 0x10000);
          uVar7 = uVar7 - 1;
          puVar6 = puVar8 + 1;
          *puVar8 = (ushort)uVar13;
          puVar11 = puVar11 + 1;
          puVar8 = puVar6;
        } while (uVar7 != 0);
        uVar7 = (uint)(uVar13 < 0x10000);
        if (uVar9 < (uint)uVar14) {
LAB_ffff00009079d68c:
          iVar5 = (int)uVar14 - uVar9;
          do {
            uVar9 = (*puVar6 | 0x10000) - uVar7;
            uVar7 = (uint)(uVar9 < 0x10000);
            iVar5 = iVar5 + -1;
            *puVar6 = (ushort)uVar9;
            puVar6 = puVar6 + 1;
          } while (iVar5 != 0);
        }
      }
      else {
        local_68 = (ushort *)FUN_ffff00009077d2e0(uVar7,2);
        if (local_68 == (ushort *)0x0) {
          return -2;
        }
        FUN_ffff00009077e858
                  (local_68,*(undefined8 *)(param_1 + 0x10),(ulong)*(uint *)(param_1 + 8) << 1);
        uVar14 = (ulong)*(uint *)(param_1 + 8);
        uVar9 = *(uint *)(param_2 + 8);
        local_78 = CONCAT44(*(uint *)(param_1 + 8),(undefined4)local_78);
        local_78 = CONCAT71(local_78._1_7_,*param_1);
        if (uVar9 != 0) goto LAB_ffff00009079d558;
LAB_ffff00009079d67c:
        uVar7 = 0;
        puVar6 = local_68;
        if (uVar9 < (uint)uVar14) goto LAB_ffff00009079d68c;
      }
      uVar15 = uVar14;
      if (1 < (uint)uVar14) {
        do {
          if (local_68[uVar15 - 1] != 0) goto LAB_ffff00009079d6dc;
          uVar15 = uVar15 - 1;
        } while (1 < (uint)uVar15);
        uVar15 = 1;
      }
LAB_ffff00009079d6dc:
      local_70 = CONCAT44(local_70._4_4_,(int)uVar15);
      if (uVar7 != 0) {
        if ((local_68 != (ushort *)0x0) &&
           (FUN_ffff00009077eb88(local_68,0,uVar14 << 1), local_68 != (ushort *)0x0)) {
          thunk_FUN_ffff0000907c2ad8();
        }
        return -3;
      }
      uVar1 = *(undefined8 *)param_2;
      uVar2 = *(undefined8 *)(param_2 + 8);
      uVar12 = *(undefined8 *)(param_2 + 0x10);
      *(undefined8 *)param_2 = local_78;
      *(undefined8 *)(param_2 + 8) = local_70;
      *(ushort **)(param_2 + 0x10) = local_68;
      local_78 = uVar1;
      local_70 = uVar2;
      local_68 = (ushort *)uVar12;
      FUN_ffff00009079bebc(&local_78);
    }
    else {
      if (uVar9 <= uVar7) {
        if (-1 < (int)(uVar7 - 1)) {
          lVar3 = (ulong)(uVar7 - 1) * 2;
          puVar6 = (ushort *)(*(long *)(param_2 + 0x10) + lVar3);
          puVar11 = (ushort *)(*(long *)(param_1 + 0x10) + lVar3);
          uVar13 = uVar7;
          do {
            if (*puVar6 < *puVar11) goto LAB_ffff00009079d354;
            if (*puVar11 < *puVar6) goto LAB_ffff00009079d40c;
            puVar11 = puVar11 + -1;
            puVar6 = puVar6 + -1;
            uVar13 = uVar13 - 1;
          } while (0 < (int)uVar13);
        }
        FUN_ffff00009077eb88(*(undefined8 *)(param_3 + 0x10),0,(ulong)*(uint *)(param_3 + 4) << 1);
        param_3[8] = '\x01';
        param_3[9] = '\0';
        param_3[10] = '\0';
        param_3[0xb] = '\0';
        goto LAB_ffff00009079d804;
      }
LAB_ffff00009079d40c:
      if (param_3 == param_1) {
        local_70 = 0;
        local_68 = (ushort *)0x0;
        local_78 = 0;
        if ((char *)&local_78 != param_2) {
          local_68 = (ushort *)FUN_ffff00009077d2e0(uVar9,2);
          if (local_68 == (ushort *)0x0) {
            return -2;
          }
          FUN_ffff00009077e858
                    (local_68,*(undefined8 *)(param_2 + 0x10),(ulong)*(uint *)(param_2 + 8) << 1);
          local_78 = CONCAT44(*(undefined4 *)(param_2 + 8),(undefined4)local_78);
          local_70 = CONCAT44(local_70._4_4_,*(undefined4 *)(param_2 + 8));
          local_78 = CONCAT71(local_78._1_7_,*param_2);
        }
        iVar5 = FUN_ffff00009079d238(&local_78,param_1);
        if (iVar5 != 0) {
          FUN_ffff00009079bebc(&local_78);
          return iVar5;
        }
        uVar1 = *(undefined8 *)param_1;
        uVar2 = *(undefined8 *)(param_1 + 8);
        uVar12 = *(undefined8 *)(param_1 + 0x10);
        *(undefined8 *)param_1 = local_78;
        *(undefined8 *)(param_1 + 8) = local_70;
        *(ushort **)(param_1 + 0x10) = local_68;
        local_78 = uVar1;
        local_70 = uVar2;
        local_68 = (ushort *)uVar12;
        FUN_ffff00009079bebc(&local_78);
      }
      else {
        if (param_3 != param_2) {
          iVar5 = FUN_ffff00009079bf14(param_2,param_3);
          if (iVar5 != 0) {
            return iVar5;
          }
          uVar7 = *(uint *)(param_1 + 8);
        }
        puVar6 = *(ushort **)(param_3 + 0x10);
        if (uVar7 == 0) {
          uVar9 = 0;
          puVar11 = puVar6;
        }
        else {
          iVar5 = 0;
          puVar8 = *(ushort **)(param_1 + 0x10);
          puVar10 = puVar6;
          uVar9 = uVar7;
          do {
            uVar13 = (iVar5 - (uint)*puVar8) + (uint)*puVar10 + 0x10000;
            iVar5 = -(uint)(uVar13 < 0x10000);
            uVar9 = uVar9 - 1;
            puVar11 = puVar10 + 1;
            *puVar10 = (ushort)uVar13;
            puVar8 = puVar8 + 1;
            puVar10 = puVar11;
          } while (uVar9 != 0);
          uVar9 = (uint)(uVar13 < 0x10000);
        }
        uVar13 = *(uint *)(param_3 + 8);
        uVar14 = (ulong)uVar13;
        if (uVar7 < uVar13) {
          iVar5 = uVar13 - uVar7;
          do {
            uVar7 = (*puVar11 | 0x10000) - uVar9;
            uVar9 = (uint)(uVar7 < 0x10000);
            iVar5 = iVar5 + -1;
            *puVar11 = (ushort)uVar7;
            puVar11 = puVar11 + 1;
          } while (iVar5 != 0);
        }
        if (1 < uVar13) {
          do {
            if (puVar6[uVar14 - 1] != 0) goto LAB_ffff00009079d798;
            uVar14 = uVar14 - 1;
          } while (1 < (uint)uVar14);
          uVar14 = 1;
        }
LAB_ffff00009079d798:
        *(int *)(param_3 + 8) = (int)uVar14;
        if (uVar9 != 0) {
          return -3;
        }
      }
      cVar4 = *param_2 == '\0';
LAB_ffff00009079d7dc:
      *param_3 = cVar4;
    }
  }
  else {
    if (param_3 != param_1) {
      if ((param_3 != param_2) && (iVar5 = FUN_ffff00009079bf14(param_2,param_3), iVar5 != 0)) {
        return iVar5;
      }
      iVar5 = FUN_ffff00009079d080(param_3,param_1);
      if (iVar5 != 0) {
        return iVar5;
      }
      cVar4 = *param_1;
      goto LAB_ffff00009079d7dc;
    }
    iVar5 = FUN_ffff00009079d080(param_1,param_2);
    if (iVar5 != 0) {
      return iVar5;
    }
  }
LAB_ffff00009079d7e0:
  if ((*(int *)(param_3 + 8) != 1) || (**(short **)(param_3 + 0x10) != 0)) {
    return 0;
  }
LAB_ffff00009079d804:
  *param_3 = '\0';
  return 0;
}


