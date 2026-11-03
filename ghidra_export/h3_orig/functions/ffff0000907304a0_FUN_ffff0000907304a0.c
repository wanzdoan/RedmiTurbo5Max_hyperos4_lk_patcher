// ffff0000907304a0  size=1032  FUN_ffff0000907304a0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907304a0(uint param_1,int param_2,uint param_3,uint param_4)

{
  int iVar1;
  uint *puVar2;
  long lVar3;
  uint uVar4;
  char *pcVar5;
  undefined8 uVar6;
  ulong uVar7;
  int iVar8;
  
  if ((DAT_ffff00009086d9a8 & 1) == 0) {
    pcVar5 = s__CCCI___s__d__MD_memory_layout_o_ffff0000907f29ba;
    uVar6 = 0x1ac;
  }
  else if (param_2 == 0) {
    pcVar5 = s__CCCI___s__d__Args__size_is_inva_ffff0000907fb546;
    uVar6 = 0x1b1;
  }
  else {
    if ((DAT_ffff00009086d9b8 & 1) != 0) {
      iVar8 = _DAT_ffff00009086d9b4;
      uVar4 = _DAT_ffff00009086d9b0;
      if (0 < _DAT_ffff00009086d9b4) {
        do {
          uVar7 = (ulong)uVar4;
          puVar2 = (uint *)(_DAT_ffff00009086d998 + uVar7 * 0x20);
          uVar4 = *puVar2;
          iVar1 = puVar2[1] + uVar4;
          if (uVar4 <= param_1 && param_1 <= iVar1 - 1U) {
            iVar8 = param_2 + param_1;
            if (iVar8 - 1U <= iVar1 - 1U) {
              if (uVar4 == param_1 && iVar8 == iVar1) {
                if (((param_3 & (puVar2[2] ^ 0xffffffff)) == 0) &&
                   ((param_4 & (puVar2[3] ^ 0xffffffff)) == 0)) {
                  FUN_ffff00009072ff90
                            (s__CCCI___s__d__Insert_dupliacte_m_ffff0000907f1211,
                             s_ccci_insert_mem_node_ffff0000907cf696,0x1ec,param_1);
                  FUN_ffff0000907308a8();
                  return 0xffffffff;
                }
                FUN_ffff00009077de54
                          (s__s__d___CCCI_Update_attr___inf_f_ffff0000907fd1b6,
                           s_ccci_insert_mem_node_ffff0000907cf696,0x1e5);
                lVar3 = _DAT_ffff00009086d998 + uVar7 * 0x20;
                *(uint *)(lVar3 + 8) = *(uint *)(lVar3 + 8) | param_3;
                *(uint *)(lVar3 + 0xc) = *(uint *)(lVar3 + 0xc) | param_4;
                return 0;
              }
              if (uVar4 == param_1) {
                uVar6 = FUN_ffff000090730938();
              }
              else {
                if (iVar8 != iVar1) {
                  uVar6 = FUN_ffff000090730bcc(uVar7,param_1,param_2,param_3,param_4);
                  if (0 < (int)uVar6) {
                    _DAT_ffff00009086d9bc = _DAT_ffff00009086d9bc + (int)uVar6;
                    return uVar6;
                  }
                  return uVar6;
                }
                uVar6 = FUN_ffff000090730a84();
              }
              if (0 < (int)uVar6) {
                _DAT_ffff00009086d9bc = _DAT_ffff00009086d9bc + (int)uVar6;
                return 1;
              }
              return uVar6;
            }
            FUN_ffff00009072ff90
                      (s__CCCI___s__d__Invalid_args__offs_ffff0000907f61ec,
                       s_ccci_insert_mem_node_ffff0000907cf696,0x1dd,param_1);
            if (_DAT_ffff00009086d998 == 0) {
              return 0xffffffff;
            }
            if (_DAT_ffff00009086d9b4 < 1) {
              return 0xffffffff;
            }
            if ((int)_DAT_ffff00009086d9b0 < 0) {
              return 0xffffffff;
            }
            if ((int)_DAT_ffff00009086d9b0 < _DAT_ffff00009086d9b4) {
              iVar8 = 1;
              uVar4 = _DAT_ffff00009086d9b0;
              while( true ) {
                FUN_ffff0000907310f4(_DAT_ffff00009086d998 + (ulong)uVar4 * 0x20);
                if (_DAT_ffff00009086d9b4 <= iVar8) {
                  return 0xffffffff;
                }
                uVar4 = *(uint *)(_DAT_ffff00009086d998 + (ulong)uVar4 * 0x20 + 0x18);
                if ((int)uVar4 < 0) break;
                iVar8 = iVar8 + 1;
                if (_DAT_ffff00009086d9b4 <= (int)uVar4) {
                  return 0xffffffff;
                }
              }
              return 0xffffffff;
            }
            return 0xffffffff;
          }
          uVar4 = puVar2[6];
          if ((int)uVar4 < 0) {
            FUN_ffff00009072ff90
                      (s__CCCI___s__d__Enter_md_mem_node_e_ffff0000907ec31a,
                       s_ccci_insert_mem_node_ffff0000907cf696,0x1cb);
            if (_DAT_ffff00009086d998 == 0) {
              return 0xffffffff;
            }
            if (_DAT_ffff00009086d9b4 < 1) {
              return 0xffffffff;
            }
            if ((int)_DAT_ffff00009086d9b0 < 0) {
              return 0xffffffff;
            }
            if ((int)_DAT_ffff00009086d9b0 < _DAT_ffff00009086d9b4) {
              iVar8 = 1;
              uVar4 = _DAT_ffff00009086d9b0;
              while( true ) {
                FUN_ffff0000907310f4(_DAT_ffff00009086d998 + (ulong)uVar4 * 0x20);
                if (_DAT_ffff00009086d9b4 <= iVar8) {
                  return 0xffffffff;
                }
                uVar4 = *(uint *)(_DAT_ffff00009086d998 + (ulong)uVar4 * 0x20 + 0x18);
                if ((int)uVar4 < 0) break;
                iVar8 = iVar8 + 1;
                if (_DAT_ffff00009086d9b4 <= (int)uVar4) {
                  return 0xffffffff;
                }
              }
              return 0xffffffff;
            }
            return 0xffffffff;
          }
          if (_DAT_ffff00009086d9b4 < (int)uVar4) {
            FUN_ffff00009072ff90
                      (s__CCCI___s__d__MD_mem_node_id_inv_ffff000090802436,
                       s_ccci_insert_mem_node_ffff0000907cf696,0x1d0);
            if (_DAT_ffff00009086d998 == 0) {
              return 0xffffffff;
            }
            if (_DAT_ffff00009086d9b4 < 1) {
              return 0xffffffff;
            }
            if ((int)_DAT_ffff00009086d9b0 < 0) {
              return 0xffffffff;
            }
            if ((int)_DAT_ffff00009086d9b0 < _DAT_ffff00009086d9b4) {
              iVar8 = 1;
              uVar4 = _DAT_ffff00009086d9b0;
              while( true ) {
                FUN_ffff0000907310f4(_DAT_ffff00009086d998 + (ulong)uVar4 * 0x20);
                if (_DAT_ffff00009086d9b4 <= iVar8) {
                  return 0xffffffff;
                }
                uVar4 = *(uint *)(_DAT_ffff00009086d998 + (ulong)uVar4 * 0x20 + 0x18);
                if ((int)uVar4 < 0) break;
                iVar8 = iVar8 + 1;
                if (_DAT_ffff00009086d9b4 <= (int)uVar4) {
                  return 0xffffffff;
                }
              }
              return 0xffffffff;
            }
            return 0xffffffff;
          }
          iVar8 = iVar8 + -1;
        } while (iVar8 != 0);
      }
      FUN_ffff00009072ff90
                (s__CCCI___s__d__Enter_md_mem_node_e_ffff0000907d7839,
                 s_ccci_insert_mem_node_ffff0000907cf696,0x1d6);
      if (_DAT_ffff00009086d998 == 0) {
        return 0xffffffff;
      }
      if (_DAT_ffff00009086d9b4 < 1) {
        return 0xffffffff;
      }
      if ((int)_DAT_ffff00009086d9b0 < 0) {
        return 0xffffffff;
      }
      if ((int)_DAT_ffff00009086d9b0 < _DAT_ffff00009086d9b4) {
        iVar8 = 1;
        uVar4 = _DAT_ffff00009086d9b0;
        while( true ) {
          FUN_ffff0000907310f4(_DAT_ffff00009086d998 + (ulong)uVar4 * 0x20);
          if (_DAT_ffff00009086d9b4 <= iVar8) {
            return 0xffffffff;
          }
          uVar4 = *(uint *)(_DAT_ffff00009086d998 + (ulong)uVar4 * 0x20 + 0x18);
          if ((int)uVar4 < 0) break;
          iVar8 = iVar8 + 1;
          if (_DAT_ffff00009086d9b4 <= (int)uVar4) {
            return 0xffffffff;
          }
        }
        return 0xffffffff;
      }
      return 0xffffffff;
    }
    pcVar5 = s__CCCI___s__d__MD_mem_tbl_1st_res_ffff0000907f99df;
    uVar6 = 0x1b6;
  }
  FUN_ffff00009072ff90(pcVar5,s_ccci_insert_mem_node_ffff0000907cf696,uVar6);
  return 0xffffffff;
}


