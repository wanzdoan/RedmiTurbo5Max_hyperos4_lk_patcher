
long FUN_ffff0000be098868(long param_1,ulong param_2,long param_3,ulong param_4)

{
  undefined8 *puVar1;
  int iVar2;
  uint uVar3;
  long lVar4;
  long lVar5;
  long *plVar6;
  undefined8 uVar7;
  undefined8 uVar8;
  undefined8 *puVar9;
  char *pcVar10;
  int *piVar11;
  int *piVar12;
  long lVar13;
  undefined8 *puVar14;
  long *plVar15;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 *local_68;
  
  if (param_1 != 0) {
    if ((param_4 < 8) ||
       (uVar3 = (*(uint *)(param_3 + 4) & 0xff00ff00) >> 8 |
                (*(uint *)(param_3 + 4) & 0xff00ff) << 8, (uVar3 >> 0x10 | uVar3 << 0x10) != param_4
       )) {
      FUN_ffff0000be09664c(s_ERROR___s____ffff0000be0bcb11,s_ufdt_apply_overlay_ffff0000be0a53e8);
      pcVar10 = s_Bad_overlay_size__ffff0000be0ac25a;
    }
    else {
      if ((7 < param_2) &&
         (uVar3 = (*(uint *)(param_1 + 4) & 0xff00ff00) >> 8 |
                  (*(uint *)(param_1 + 4) & 0xff00ff) << 8,
         (uVar3 >> 0x10 | uVar3 << 0x10) == param_2)) {
        lVar4 = thunk_FUN_ffff0000be05f1bc(param_2 + param_4);
        if (lVar4 == 0) {
          FUN_ffff0000be09664c
                    (s_ERROR___s____ffff0000be0bcb11,s_ufdt_apply_overlay_ffff0000be0a53e8);
          FUN_ffff0000be09664c(s_failed_to_allocate_memory_for_DT_ffff0000be0a53fb);
          return 0;
        }
        local_78 = 0;
        uStack_70 = 0;
        FUN_ffff0000be097c70(&local_78);
        lVar5 = FUN_ffff0000be096b80(param_1,param_2,&local_78);
        plVar6 = (long *)FUN_ffff0000be096b80(param_3,param_4,&local_78);
        if (0 < *(int *)((long)plVar6 + 0xc)) {
          lVar13 = 0;
          do {
            iVar2 = FUN_ffff0000be096774(lVar5,*(undefined8 *)(*plVar6 + lVar13 * 8));
            if (iVar2 < 0) {
              FUN_ffff0000be09664c
                        (s_ERROR___s____ffff0000be0bcb11,s_ufdt_overlay_apply_ffff0000be0a3fe0);
              pcVar10 = s_failed_to_add_more_fdt_into_main_ffff0000be0a9d75;
              goto LAB_ffff0000be098c38;
            }
            lVar13 = lVar13 + 1;
          } while (lVar13 < *(int *)((long)plVar6 + 0xc));
        }
        if (param_4 < 0x28) {
          FUN_ffff0000be09664c
                    (s_ERROR___s____ffff0000be0bcb11,s_ufdt_overlay_apply_ffff0000be0a3fe0);
          FUN_ffff0000be09664c(s_Overlay_length__zu_smaller_than_h_ffff0000be0a3ff3,param_4,0x28);
        }
        else {
          if (*(int *)(lVar5 + 0x18) < 1) {
            iVar2 = 0;
          }
          else {
            iVar2 = *(int *)(*(long *)(lVar5 + 0x20) + (ulong)(*(int *)(lVar5 + 0x18) - 1) * 0x10);
            if (iVar2 != 0) {
              FUN_ffff0000be097ec8(plVar6,iVar2);
            }
          }
          uVar7 = FUN_ffff0000be09695c(plVar6,&DAT_ffff0000be0b71b6);
          uVar8 = FUN_ffff0000be09695c(plVar6,s____local_fixups___ffff0000be0b5efb);
          iVar2 = FUN_ffff0000be0985bc(uVar7,uVar8,iVar2);
          if (iVar2 < 0) {
            FUN_ffff0000be09664c
                      (s_ERROR___s____ffff0000be0bcb11,
                       s_ufdt_overlay_local_ref_update_ffff0000be0ac298);
            FUN_ffff0000be09664c(s_failed_to_perform_local_fixups_i_ffff0000be0ac26d);
            FUN_ffff0000be09664c
                      (s_ERROR___s____ffff0000be0bcb11,s_ufdt_overlay_apply_ffff0000be0a3fe0);
            pcVar10 = s_failed_to_perform_local_fixups_i_ffff0000be0ac26d;
          }
          else {
            iVar2 = FUN_ffff0000be098258(lVar5,plVar6);
            if (iVar2 < 0) {
              FUN_ffff0000be09664c
                        (s_ERROR___s____ffff0000be0bcb11,s_ufdt_overlay_apply_ffff0000be0a3fe0);
              pcVar10 = s_failed_to_perform_fixups_in_over_ffff0000be0b71cc;
            }
            else {
              puVar14 = (undefined8 *)plVar6[2];
              if ((puVar14 != (undefined8 *)0x0) && (*(int *)*puVar14 == 0x1000000)) {
                plVar15 = puVar14 + 2;
                puVar14 = (undefined8 *)*plVar15;
                while (puVar14 != (undefined8 *)0x0) {
                  if (*(int *)*puVar14 == 0x1000000) {
                    local_68 = (undefined8 *)0x0;
                    puVar9 = (undefined8 *)
                             FUN_ffff0000be0979b8(puVar14,s___overlay___ffff0000be0c274d);
                    if (puVar9 != (undefined8 *)0x0) {
                      uVar3 = FUN_ffff0000be098498(lVar5,puVar14,&local_68);
                      puVar1 = local_68;
                      if (local_68 == (undefined8 *)0x0) {
                        FUN_ffff0000be09664c
                                  (s_ERROR___s____ffff0000be0bcb11,
                                   s_ufdt_apply_fragment_ffff0000be0c6e09);
                        if (*(int *)*puVar14 == 0x1000000) {
                          piVar11 = (int *)*puVar14 + 1;
                        }
                        else {
                          piVar11 = (int *)puVar14[2];
                        }
                        FUN_ffff0000be09664c
                                  (s_Unable_to_resolve_target_for__s_ffff0000be0b39d2,piVar11);
                        if ((uVar3 & 0xfffffffd) != 0) {
LAB_ffff0000be098cc8:
                          FUN_ffff0000be09664c
                                    (s_ERROR___s____ffff0000be0bcb11,
                                     s_ufdt_overlay_apply_fragments_ffff0000be0a4024);
                          puVar14 = (undefined8 *)*plVar15;
                          if ((puVar14 == (undefined8 *)0x0) || (*(int *)*puVar14 != 0x1000000)) {
                            piVar11 = (int *)puVar14[2];
                          }
                          else {
                            piVar11 = (int *)*puVar14 + 1;
                          }
                          FUN_ffff0000be09664c
                                    (s_failed_to_apply_overlay_fragment_ffff0000be0b95df,piVar11,
                                     uVar3);
                          FUN_ffff0000be09664c
                                    (s_ERROR___s____ffff0000be0bcb11,
                                     s_ufdt_overlay_apply_ffff0000be0a3fe0);
                          pcVar10 = s_failed_to_apply_fragments_ffff0000be0a8ac5;
                          goto LAB_ffff0000be098c38;
                        }
                      }
                      else {
                        iVar2 = FUN_ffff0000be0979f4(local_68,puVar9,&local_78);
                        if (iVar2 < 0) {
                          FUN_ffff0000be09664c
                                    (s_ERROR___s____ffff0000be0bcb11,
                                     s_ufdt_apply_fragment_ffff0000be0c6e09);
                          if (*(int *)*puVar9 == 0x1000000) {
                            piVar11 = (int *)*puVar9 + 1;
                          }
                          else {
                            piVar11 = (int *)puVar9[2];
                          }
                          piVar12 = (int *)*puVar1;
                          if (*piVar12 == 0x1000000) {
                            piVar12 = piVar12 + 1;
                          }
                          else {
                            piVar12 = (int *)puVar1[2];
                          }
                          FUN_ffff0000be09664c
                                    (s_failed_to_overlay_node__s_to_tar_ffff0000be0b073b,piVar11,
                                     piVar12);
                          uVar3 = 5;
                          goto LAB_ffff0000be098cc8;
                        }
                      }
                    }
                  }
                  plVar15 = (long *)(*plVar15 + 8);
                  puVar14 = (undefined8 *)*plVar15;
                }
              }
              iVar2 = FUN_ffff0000be096e40(lVar5,lVar4,param_2 + param_4 & 0xffffffff);
              if (-1 < iVar2) {
                FUN_ffff0000be096728(plVar6,&local_78);
                FUN_ffff0000be096728(lVar5,&local_78);
                FUN_ffff0000be097c80(&local_78);
                return lVar4;
              }
              FUN_ffff0000be09664c
                        (s_ERROR___s____ffff0000be0bcb11,s_ufdt_apply_overlay_ffff0000be0a53e8);
              pcVar10 = s_Failed_to_dump_the_device_tree_t_ffff0000be0b95ad;
            }
          }
LAB_ffff0000be098c38:
          FUN_ffff0000be09664c(pcVar10);
        }
        FUN_ffff0000be096728(plVar6,&local_78);
        FUN_ffff0000be096728(lVar5,&local_78);
        FUN_ffff0000be097c80(&local_78);
        thunk_FUN_ffff0000be09937c(lVar4);
        return 0;
      }
      FUN_ffff0000be09664c(s_ERROR___s____ffff0000be0bcb11,s_ufdt_apply_overlay_ffff0000be0a53e8);
      pcVar10 = s_Bad_fdt_size__ffff0000be0a6713;
    }
    FUN_ffff0000be09664c(pcVar10);
  }
  return 0;
}


