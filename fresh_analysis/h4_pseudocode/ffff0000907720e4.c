
long FUN_ffff0000907720e4(long param_1,ulong param_2,long param_3,ulong param_4)

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
      FUN_ffff00009076fec8(s_ERROR___s____ffff0000907f36ba,s_ufdt_apply_overlay_ffff0000907d1d79);
      pcVar10 = s_Bad_overlay_size__ffff0000907db7a0;
    }
    else {
      if ((7 < param_2) &&
         (uVar3 = (*(uint *)(param_1 + 4) & 0xff00ff00) >> 8 |
                  (*(uint *)(param_1 + 4) & 0xff00ff) << 8,
         (uVar3 >> 0x10 | uVar3 << 0x10) == param_2)) {
        lVar4 = thunk_FUN_ffff00009077d514(param_2 + param_4);
        if (lVar4 == 0) {
          FUN_ffff00009076fec8
                    (s_ERROR___s____ffff0000907f36ba,s_ufdt_apply_overlay_ffff0000907d1d79);
          FUN_ffff00009076fec8(s_failed_to_allocate_memory_for_DT_ffff0000907d1d8c);
          return 0;
        }
        local_78 = 0;
        uStack_70 = 0;
        FUN_ffff0000907714ec(&local_78);
        lVar5 = FUN_ffff0000907703fc(param_1,param_2,&local_78);
        plVar6 = (long *)FUN_ffff0000907703fc(param_3,param_4,&local_78);
        if (0 < *(int *)((long)plVar6 + 0xc)) {
          lVar13 = 0;
          do {
            iVar2 = FUN_ffff00009076fff0(lVar5,*(undefined8 *)(*plVar6 + lVar13 * 8));
            if (iVar2 < 0) {
              FUN_ffff00009076fec8
                        (s_ERROR___s____ffff0000907f36ba,s_ufdt_overlay_apply_ffff0000907d0126);
              pcVar10 = s_failed_to_add_more_fdt_into_main_ffff0000907d84fc;
              goto LAB_ffff0000907724b4;
            }
            lVar13 = lVar13 + 1;
          } while (lVar13 < *(int *)((long)plVar6 + 0xc));
        }
        if (param_4 < 0x28) {
          FUN_ffff00009076fec8
                    (s_ERROR___s____ffff0000907f36ba,s_ufdt_overlay_apply_ffff0000907d0126);
          FUN_ffff00009076fec8(s_Overlay_length__zu_smaller_than_h_ffff0000907d0139,param_4,0x28);
        }
        else {
          if (*(int *)(lVar5 + 0x18) < 1) {
            iVar2 = 0;
          }
          else {
            iVar2 = *(int *)(*(long *)(lVar5 + 0x20) + (ulong)(*(int *)(lVar5 + 0x18) - 1) * 0x10);
            if (iVar2 != 0) {
              FUN_ffff000090771744(plVar6,iVar2);
            }
          }
          uVar7 = FUN_ffff0000907701d8(plVar6,&DAT_ffff0000907ea7ef);
          uVar8 = FUN_ffff0000907701d8(plVar6,s____local_fixups___ffff0000907e9f70);
          iVar2 = FUN_ffff000090771e38(uVar7,uVar8,iVar2);
          if (iVar2 < 0) {
            FUN_ffff00009076fec8
                      (s_ERROR___s____ffff0000907f36ba,
                       s_ufdt_overlay_local_ref_update_ffff0000907db7de);
            FUN_ffff00009076fec8(s_failed_to_perform_local_fixups_i_ffff0000907db7b3);
            FUN_ffff00009076fec8
                      (s_ERROR___s____ffff0000907f36ba,s_ufdt_overlay_apply_ffff0000907d0126);
            pcVar10 = s_failed_to_perform_local_fixups_i_ffff0000907db7b3;
          }
          else {
            iVar2 = FUN_ffff000090771ad4(lVar5,plVar6);
            if (iVar2 < 0) {
              FUN_ffff00009076fec8
                        (s_ERROR___s____ffff0000907f36ba,s_ufdt_overlay_apply_ffff0000907d0126);
              pcVar10 = s_failed_to_perform_fixups_in_over_ffff0000907eb7e4;
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
                             FUN_ffff000090771234(puVar14,s___overlay___ffff0000907fc475);
                    if (puVar9 != (undefined8 *)0x0) {
                      uVar3 = FUN_ffff000090771d14(lVar5,puVar14,&local_68);
                      puVar1 = local_68;
                      if (local_68 == (undefined8 *)0x0) {
                        FUN_ffff00009076fec8
                                  (s_ERROR___s____ffff0000907f36ba,
                                   s_ufdt_apply_fragment_ffff000090802ebb);
                        if (*(int *)*puVar14 == 0x1000000) {
                          piVar11 = (int *)*puVar14 + 1;
                        }
                        else {
                          piVar11 = (int *)puVar14[2];
                        }
                        FUN_ffff00009076fec8
                                  (s_Unable_to_resolve_target_for__s_ffff0000907e6bcc,piVar11);
                        if ((uVar3 & 0xfffffffd) != 0) {
LAB_ffff000090772544:
                          FUN_ffff00009076fec8
                                    (s_ERROR___s____ffff0000907f36ba,
                                     s_ufdt_overlay_apply_fragments_ffff0000907d016a);
                          puVar14 = (undefined8 *)*plVar15;
                          if ((puVar14 == (undefined8 *)0x0) || (*(int *)*puVar14 != 0x1000000)) {
                            piVar11 = (int *)puVar14[2];
                          }
                          else {
                            piVar11 = (int *)*puVar14 + 1;
                          }
                          FUN_ffff00009076fec8
                                    (s_failed_to_apply_overlay_fragment_ffff0000907eecb8,piVar11,
                                     uVar3);
                          FUN_ffff00009076fec8
                                    (s_ERROR___s____ffff0000907f36ba,
                                     s_ufdt_overlay_apply_ffff0000907d0126);
                          pcVar10 = s_failed_to_apply_fragments_ffff0000907d6bfc;
                          goto LAB_ffff0000907724b4;
                        }
                      }
                      else {
                        iVar2 = FUN_ffff000090771270(local_68,puVar9,&local_78);
                        if (iVar2 < 0) {
                          FUN_ffff00009076fec8
                                    (s_ERROR___s____ffff0000907f36ba,
                                     s_ufdt_apply_fragment_ffff000090802ebb);
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
                          FUN_ffff00009076fec8
                                    (s_failed_to_overlay_node__s_to_tar_ffff0000907e1ef4,piVar11,
                                     piVar12);
                          uVar3 = 5;
                          goto LAB_ffff000090772544;
                        }
                      }
                    }
                  }
                  plVar15 = (long *)(*plVar15 + 8);
                  puVar14 = (undefined8 *)*plVar15;
                }
              }
              iVar2 = FUN_ffff0000907706bc(lVar5,lVar4,param_2 + param_4 & 0xffffffff);
              if (-1 < iVar2) {
                FUN_ffff00009076ffa4(plVar6,&local_78);
                FUN_ffff00009076ffa4(lVar5,&local_78);
                FUN_ffff0000907714fc(&local_78);
                return lVar4;
              }
              FUN_ffff00009076fec8
                        (s_ERROR___s____ffff0000907f36ba,s_ufdt_apply_overlay_ffff0000907d1d79);
              pcVar10 = s_Failed_to_dump_the_device_tree_t_ffff0000907eec86;
            }
          }
LAB_ffff0000907724b4:
          FUN_ffff00009076fec8(pcVar10);
        }
        FUN_ffff00009076ffa4(plVar6,&local_78);
        FUN_ffff00009076ffa4(lVar5,&local_78);
        FUN_ffff0000907714fc(&local_78);
        thunk_FUN_ffff0000907c314c(lVar4);
        return 0;
      }
      FUN_ffff00009076fec8(s_ERROR___s____ffff0000907f36ba,s_ufdt_apply_overlay_ffff0000907d1d79);
      pcVar10 = s_Bad_fdt_size__ffff0000907d3783;
    }
    FUN_ffff00009076fec8(pcVar10);
  }
  return 0;
}


