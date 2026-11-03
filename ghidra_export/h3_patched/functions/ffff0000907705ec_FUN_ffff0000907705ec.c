// ffff0000907705ec  size=756  FUN_ffff0000907705ec

int FUN_ffff0000907705ec(long *param_1,long param_2,undefined8 *param_3,undefined8 param_4)

{
  undefined4 *puVar1;
  ulong uVar2;
  ulong uVar3;
  uint uVar4;
  uint uVar5;
  int iVar7;
  long lVar8;
  char *pcVar9;
  uint *puVar10;
  undefined8 *puVar11;
  ulong uVar12;
  ulong uVar13;
  uint uVar14;
  ulong uVar15;
  undefined8 *puVar16;
  uint local_44;
  uint uVar6;
  
  if (param_3 == (undefined8 *)0x0) {
LAB_ffff0000907706c8:
    puVar10 = (uint *)param_3[2];
  }
  else {
    uVar14 = *(uint *)*param_3;
    uVar14 = (uVar14 & 0xff00ff00) >> 8 | (uVar14 & 0xff00ff) << 8;
    uVar14 = uVar14 >> 0x10 | uVar14 << 0x10;
    if (uVar14 != 1) {
      if (uVar14 == 3) {
        uVar15 = param_3[2];
        lVar8 = FUN_ffff0000907725a8(param_4,uVar15);
        if (lVar8 != 0) {
          uVar14 = *(uint *)(lVar8 + 8);
          if (uVar14 == 0) {
            return -1;
          }
          uVar14 = (uVar14 & 0xff00ff00) >> 8 | (uVar14 & 0xff00ff) << 8;
          uVar14 = uVar14 >> 0x10 | uVar14 << 0x10;
LAB_ffff000090770658:
          local_44 = 0;
          lVar8 = FUN_ffff000090770c50(param_3,&local_44);
          if (lVar8 == 0) {
            FUN_ffff00009076fc00
                      (s_ERROR___s____ffff0000907f3517,
                       s__ufdt_output_property_to_fdt_ffff0000907fdfe5);
            pcVar9 = s_Failed_to_get_property_data__ffff0000907e1dee;
          }
          else {
            uVar4 = (*(uint *)(param_2 + 8) & 0xff00ff00) >> 8 |
                    (*(uint *)(param_2 + 8) & 0xff00ff) << 8;
            uVar5 = (*(uint *)(param_2 + 0x24) & 0xff00ff00) >> 8 |
                    (*(uint *)(param_2 + 0x24) & 0xff00ff) << 8;
            uVar6 = uVar5 >> 0x10 | uVar5 << 0x10;
            uVar5 = (local_44 + 3 & 0xfffffffc) + 0xc;
            puVar1 = (undefined4 *)(param_2 + (ulong)(uVar4 >> 0x10 | uVar4 << 0x10) + (ulong)uVar6)
            ;
            uVar4 = (*(uint *)(param_2 + 4) & 0xff00ff00) >> 8 |
                    (*(uint *)(param_2 + 4) & 0xff00ff) << 8;
            if ((long)puVar1 + (ulong)uVar5 <= param_2 + (ulong)(uVar4 >> 0x10 | uVar4 << 0x10)) {
              uVar6 = uVar6 + uVar5;
              uVar5 = (uVar6 & 0xff00ff00) >> 8 | (uVar6 & 0xff00ff) << 8;
              uVar14 = (uVar14 & 0xff00ff00) >> 8 | (uVar14 & 0xff00ff) << 8;
              uVar6 = (local_44 & 0xff00ff00) >> 8 | (local_44 & 0xff00ff) << 8;
              *(uint *)(param_2 + 0x24) = uVar5 >> 0x10 | uVar5 << 0x10;
              puVar1[1] = uVar6 >> 0x10 | uVar6 << 0x10;
              puVar1[2] = uVar14 >> 0x10 | uVar14 << 0x10;
              *puVar1 = 0x3000000;
              thunk_FUN_ffff00009077e858(puVar1 + 3,lVar8);
              FUN_ffff000090772368(param_4,puVar1);
              return 0;
            }
            FUN_ffff00009076fc00
                      (s_ERROR___s____ffff0000907f3517,
                       s__ufdt_output_property_to_fdt_ffff0000907fdfe5);
            pcVar9 = s_Not_enough_space_for_adding_prop_ffff0000907f1c3f;
          }
          FUN_ffff00009076fc00(pcVar9);
          return -1;
        }
        uVar12 = (ulong)*(uint *)((long)param_1 + 0xc);
        if (0 < (int)*(uint *)((long)param_1 + 0xc)) {
          iVar7 = 0;
          param_1 = (long *)*param_1;
          do {
            lVar8 = *param_1;
            uVar14 = (*(uint *)(lVar8 + 0xc) & 0xff00ff00) >> 8 |
                     (*(uint *)(lVar8 + 0xc) & 0xff00ff) << 8;
            uVar2 = lVar8 + (ulong)(uVar14 >> 0x10 | uVar14 << 0x10);
            uVar13 = (long)(int)((uint)*(byte *)(lVar8 + 0x20) << 0x18) |
                     (ulong)*(byte *)(lVar8 + 0x21) << 0x10 | (ulong)*(byte *)(lVar8 + 0x22) << 8 |
                     (ulong)*(byte *)(lVar8 + 0x23);
            uVar3 = uVar2 + uVar13;
            if ((uVar15 >= uVar2 && uVar15 <= uVar3) && (uVar15 < uVar2 || uVar3 != uVar15)) {
              uVar14 = iVar7 + ((int)uVar15 - (int)uVar3);
              if (uVar14 != 0) goto LAB_ffff000090770658;
              break;
            }
            iVar7 = iVar7 - (int)uVar13;
            uVar12 = uVar12 - 1;
            param_1 = param_1 + 1;
          } while (uVar12 != 0);
        }
        FUN_ffff00009076fc00
                  (s_ERROR___s____ffff0000907f3517,s__ufdt_get_property_nameoff_ffff0000907e38bb);
        FUN_ffff00009076fc00(s_Cannot_find_property_name_in_str_ffff0000907ecfe7,uVar15);
        return -1;
      }
      goto LAB_ffff0000907706c8;
    }
    puVar10 = (uint *)*param_3 + 1;
  }
  iVar7 = FUN_ffff00009076efc4(param_2,puVar10);
  if (iVar7 < 0) {
    return -1;
  }
  if (*(int *)*param_3 == 0x1000000) {
    puVar16 = param_3 + 2;
    puVar11 = (undefined8 *)*puVar16;
    if (puVar11 != (undefined8 *)0x0) {
      do {
        if (*(int *)*puVar11 == 0x3000000) {
          iVar7 = FUN_ffff0000907705ec(param_1,param_2,puVar11,param_4);
          if (iVar7 < 0) {
            return -1;
          }
          puVar11 = (undefined8 *)*puVar16;
        }
        puVar16 = puVar11 + 1;
        puVar11 = (undefined8 *)*puVar16;
      } while (puVar11 != (undefined8 *)0x0);
      if (*(int *)*param_3 != 0x1000000) goto LAB_ffff0000907707ac;
    }
    param_3 = param_3 + 2;
    puVar11 = (undefined8 *)*param_3;
    while (puVar11 != (undefined8 *)0x0) {
      if (*(int *)*puVar11 == 0x1000000) {
        iVar7 = FUN_ffff0000907705ec(param_1,param_2,puVar11,param_4);
        if (iVar7 < 0) {
          return -1;
        }
        puVar11 = (undefined8 *)*param_3;
      }
      param_3 = puVar11 + 1;
      puVar11 = (undefined8 *)*param_3;
    }
  }
LAB_ffff0000907707ac:
  iVar7 = FUN_ffff00009076f11c(param_2);
  return iVar7 >> 0x1f;
}


