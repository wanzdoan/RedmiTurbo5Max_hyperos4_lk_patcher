// ffff00009077180c  size=576  FUN_ffff00009077180c

undefined8 FUN_ffff00009077180c(undefined8 param_1,undefined8 param_2)

{
  undefined8 *puVar1;
  int iVar2;
  uint uVar3;
  undefined8 *puVar4;
  long lVar5;
  int *piVar6;
  long lVar7;
  long lVar8;
  char *pcVar9;
  undefined8 *puVar10;
  undefined8 *puVar11;
  int local_58;
  uint local_54;
  
  local_58 = 0;
  puVar4 = (undefined8 *)FUN_ffff00009076ff10(param_2,s____fixups___ffff0000907fe002);
  if (puVar4 == (undefined8 *)0x0) {
    return 0;
  }
  lVar5 = FUN_ffff00009076ff10(param_1,s____symbols___ffff000090802caa);
  if (*(int *)*puVar4 == 0x1000000) {
    puVar4 = puVar4 + 2;
    puVar11 = (undefined8 *)*puVar4;
    puVar10 = puVar11;
    if (puVar11 != (undefined8 *)0x0) {
      do {
        if (*(int *)*puVar10 == 0x3000000) {
          if (lVar5 == 0) {
            FUN_ffff00009076fc00
                      (s_ERROR___s____ffff0000907f3517,s_ufdt_overlay_do_fixups_ffff0000907dd1f0);
            pcVar9 = s_No_node___symbols___in_main_dtb__ffff0000907e82ef;
LAB_ffff0000907719bc:
            FUN_ffff00009076fc00(pcVar9);
            return 0xffffffff;
          }
          break;
        }
        puVar1 = puVar10 + 1;
        puVar10 = (undefined8 *)*puVar1;
      } while ((undefined8 *)*puVar1 != (undefined8 *)0x0);
      if (puVar11 != (undefined8 *)0x0) {
        do {
          if (*(int *)*puVar11 == 0x3000000) {
            piVar6 = (int *)FUN_ffff000090770c98(lVar5,puVar11[2],&local_58);
            if (piVar6 == (int *)0x0) {
              FUN_ffff00009076fc00
                        (s_ERROR___s____ffff0000907f3517,s_ufdt_overlay_do_fixups_ffff0000907dd1f0);
              if (*(int *)*puVar11 == 0x1000000) {
                piVar6 = (int *)*puVar11 + 1;
              }
              else {
                piVar6 = (int *)puVar11[2];
              }
              pcVar9 = s_Couldn_t_find___s__symbol_in_mai_ffff0000907db632;
LAB_ffff000090771a44:
              FUN_ffff00009076fc00(pcVar9,piVar6);
              return 0xffffffff;
            }
            lVar7 = FUN_ffff00009076ff10(param_1,piVar6);
            if (lVar7 == 0) {
              FUN_ffff00009076fc00
                        (s_ERROR___s____ffff0000907f3517,s_ufdt_overlay_do_fixups_ffff0000907dd1f0);
              pcVar9 = s_Couldn_t_find___s__path_in_main_d_ffff0000907db659;
              goto LAB_ffff000090771a44;
            }
            uVar3 = FUN_ffff000090770dac();
            lVar7 = FUN_ffff000090770c50(puVar11,&local_58);
            uVar3 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
            local_54 = uVar3 >> 0x10 | uVar3 << 0x10;
            for (iVar2 = local_58; 0 < iVar2; iVar2 = iVar2 + ~uVar3) {
              lVar8 = FUN_ffff0000907715b8(param_2,lVar7);
              if (lVar8 == 0) {
                FUN_ffff00009076fc00
                          (s_ERROR___s____ffff0000907f3517,s_ufdt_overlay_do_fixups_ffff0000907dd1f0
                          );
                pcVar9 = s_Failed_one_fixup_in_ufdt_do_one__ffff0000907d00e0;
                goto LAB_ffff0000907719bc;
              }
              thunk_FUN_ffff00009077e858(lVar8,&local_54,4);
              uVar3 = thunk_FUN_ffff00009077ef88(lVar7);
              lVar8 = thunk_FUN_ffff00009077ef88(lVar7);
              lVar7 = lVar8 + lVar7 + 1;
            }
            puVar11 = (undefined8 *)*puVar4;
          }
          puVar4 = puVar11 + 1;
          puVar11 = (undefined8 *)*puVar4;
          if (puVar11 == (undefined8 *)0x0) {
            return 0;
          }
        } while( true );
      }
    }
  }
  return 0;
}


