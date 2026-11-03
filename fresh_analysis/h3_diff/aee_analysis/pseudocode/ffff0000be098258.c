
undefined8 FUN_ffff0000be098258(undefined8 param_1,undefined8 param_2)

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
  puVar4 = (undefined8 *)FUN_ffff0000be09695c(param_2,s____fixups___ffff0000be0c39aa);
  if (puVar4 == (undefined8 *)0x0) {
    return 0;
  }
  lVar5 = FUN_ffff0000be09695c(param_1,s____symbols___ffff0000be0c6dfc);
  if (*(int *)*puVar4 == 0x1000000) {
    puVar4 = puVar4 + 2;
    puVar11 = (undefined8 *)*puVar4;
    puVar10 = puVar11;
    if (puVar11 != (undefined8 *)0x0) {
      do {
        if (*(int *)*puVar10 == 0x3000000) {
          if (lVar5 == 0) {
            FUN_ffff0000be09664c
                      (s_ERROR___s____ffff0000be0bcb11,s_ufdt_overlay_do_fixups_ffff0000be0ad15f);
            pcVar9 = s_No_node___symbols___in_main_dtb__ffff0000be0b4d44;
LAB_ffff0000be098408:
            FUN_ffff0000be09664c(pcVar9);
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
            piVar6 = (int *)FUN_ffff0000be0976e4(lVar5,puVar11[2],&local_58);
            if (piVar6 == (int *)0x0) {
              FUN_ffff0000be09664c
                        (s_ERROR___s____ffff0000be0bcb11,s_ufdt_overlay_do_fixups_ffff0000be0ad15f);
              if (*(int *)*puVar11 == 0x1000000) {
                piVar6 = (int *)*puVar11 + 1;
              }
              else {
                piVar6 = (int *)puVar11[2];
              }
              pcVar9 = s_Couldn_t_find___s__symbol_in_mai_ffff0000be0ac20e;
LAB_ffff0000be098490:
              FUN_ffff0000be09664c(pcVar9,piVar6);
              return 0xffffffff;
            }
            lVar7 = FUN_ffff0000be09695c(param_1,piVar6);
            if (lVar7 == 0) {
              FUN_ffff0000be09664c
                        (s_ERROR___s____ffff0000be0bcb11,s_ufdt_overlay_do_fixups_ffff0000be0ad15f);
              pcVar9 = s_Couldn_t_find___s__path_in_main_d_ffff0000be0ac235;
              goto LAB_ffff0000be098490;
            }
            uVar3 = FUN_ffff0000be0977f8();
            lVar7 = FUN_ffff0000be09769c(puVar11,&local_58);
            uVar3 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
            local_54 = uVar3 >> 0x10 | uVar3 << 0x10;
            for (iVar2 = local_58; 0 < iVar2; iVar2 = iVar2 + ~uVar3) {
              lVar8 = FUN_ffff0000be098004(param_2,lVar7);
              if (lVar8 == 0) {
                FUN_ffff0000be09664c
                          (s_ERROR___s____ffff0000be0bcb11,s_ufdt_overlay_do_fixups_ffff0000be0ad15f
                          );
                pcVar9 = s_Failed_one_fixup_in_ufdt_do_one__ffff0000be0a3fb9;
                goto LAB_ffff0000be098408;
              }
              thunk_FUN_ffff0000be0607b0(lVar8,&local_54,4);
              uVar3 = thunk_FUN_ffff0000be060e5c(lVar7);
              lVar8 = thunk_FUN_ffff0000be060e5c(lVar7);
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


