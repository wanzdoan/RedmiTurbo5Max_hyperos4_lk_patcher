
undefined8 FUN_ffff000090730738(uint param_1,int param_2,uint param_3,uint param_4)

{
  int iVar1;
  uint *puVar2;
  long lVar3;
  uint uVar4;
  char *pcVar5;
  undefined8 uVar6;
  ulong uVar7;
  int iVar8;
  
  if ((DAT_ffff00009086da40 & 1) == 0) {
    pcVar5 = s__CCCI___s__d__MD_memory_layout_o_ffff0000907f2b46;
    uVar6 = 0x1ac;
  }
  else if (param_2 == 0) {
    pcVar5 = s__CCCI___s__d__Args__size_is_inva_ffff0000907fb6e2;
    uVar6 = 0x1b1;
  }
  else {
    if ((DAT_ffff00009086da50 & 1) != 0) {
      iVar8 = DAT_ffff00009086da4c;
      uVar4 = DAT_ffff00009086da48;
      if (0 < DAT_ffff00009086da4c) {
        do {
          uVar7 = (ulong)uVar4;
          puVar2 = (uint *)(DAT_ffff00009086da30 + uVar7 * 0x20);
          uVar4 = *puVar2;
          iVar1 = puVar2[1] + uVar4;
          if (uVar4 <= param_1 && param_1 <= iVar1 - 1U) {
            iVar8 = param_2 + param_1;
            if (iVar8 - 1U <= iVar1 - 1U) {
              if (uVar4 == param_1 && iVar8 == iVar1) {
                if (((param_3 & (puVar2[2] ^ 0xffffffff)) == 0) &&
                   ((param_4 & (puVar2[3] ^ 0xffffffff)) == 0)) {
                  FUN_ffff000090730228
                            (s__CCCI___s__d__Insert_dupliacte_m_ffff0000907f1382,
                             s_ccci_insert_mem_node_ffff0000907cf6b5,0x1ec,param_1);
                  FUN_ffff000090730b40();
                  return 0xffffffff;
                }
                FUN_ffff00009077e11c
                          (s__s__d___CCCI_Update_attr___inf_f_ffff0000907fd35e,
                           s_ccci_insert_mem_node_ffff0000907cf6b5,0x1e5);
                lVar3 = DAT_ffff00009086da30 + uVar7 * 0x20;
                *(uint *)(lVar3 + 8) = *(uint *)(lVar3 + 8) | param_3;
                *(uint *)(lVar3 + 0xc) = *(uint *)(lVar3 + 0xc) | param_4;
                return 0;
              }
              if (uVar4 == param_1) {
                uVar6 = FUN_ffff000090730bd0();
              }
              else {
                if (iVar8 != iVar1) {
                  uVar6 = FUN_ffff000090730e64(uVar7,param_1,param_2,param_3,param_4);
                  if (0 < (int)uVar6) {
                    DAT_ffff00009086da54 = DAT_ffff00009086da54 + (int)uVar6;
                    return uVar6;
                  }
                  return uVar6;
                }
                uVar6 = FUN_ffff000090730d1c();
              }
              if (0 < (int)uVar6) {
                DAT_ffff00009086da54 = DAT_ffff00009086da54 + (int)uVar6;
                return 1;
              }
              return uVar6;
            }
            FUN_ffff000090730228
                      (s__CCCI___s__d__Invalid_args__offs_ffff0000907f6376,
                       s_ccci_insert_mem_node_ffff0000907cf6b5,0x1dd,param_1);
            if (DAT_ffff00009086da30 == 0) {
              return 0xffffffff;
            }
            if (DAT_ffff00009086da4c < 1) {
              return 0xffffffff;
            }
            if ((int)DAT_ffff00009086da48 < 0) {
              return 0xffffffff;
            }
            if ((int)DAT_ffff00009086da48 < DAT_ffff00009086da4c) {
              iVar8 = 1;
              uVar4 = DAT_ffff00009086da48;
              while( true ) {
                FUN_ffff00009073138c(DAT_ffff00009086da30 + (ulong)uVar4 * 0x20);
                if (DAT_ffff00009086da4c <= iVar8) {
                  return 0xffffffff;
                }
                uVar4 = *(uint *)(DAT_ffff00009086da30 + (ulong)uVar4 * 0x20 + 0x18);
                if ((int)uVar4 < 0) break;
                iVar8 = iVar8 + 1;
                if (DAT_ffff00009086da4c <= (int)uVar4) {
                  return 0xffffffff;
                }
              }
              return 0xffffffff;
            }
            return 0xffffffff;
          }
          uVar4 = puVar2[6];
          if ((int)uVar4 < 0) {
            FUN_ffff000090730228
                      (s__CCCI___s__d__Enter_md_mem_node_e_ffff0000907ec49d,
                       s_ccci_insert_mem_node_ffff0000907cf6b5,0x1cb);
            if (DAT_ffff00009086da30 == 0) {
              return 0xffffffff;
            }
            if (DAT_ffff00009086da4c < 1) {
              return 0xffffffff;
            }
            if ((int)DAT_ffff00009086da48 < 0) {
              return 0xffffffff;
            }
            if ((int)DAT_ffff00009086da48 < DAT_ffff00009086da4c) {
              iVar8 = 1;
              uVar4 = DAT_ffff00009086da48;
              while( true ) {
                FUN_ffff00009073138c(DAT_ffff00009086da30 + (ulong)uVar4 * 0x20);
                if (DAT_ffff00009086da4c <= iVar8) {
                  return 0xffffffff;
                }
                uVar4 = *(uint *)(DAT_ffff00009086da30 + (ulong)uVar4 * 0x20 + 0x18);
                if ((int)uVar4 < 0) break;
                iVar8 = iVar8 + 1;
                if (DAT_ffff00009086da4c <= (int)uVar4) {
                  return 0xffffffff;
                }
              }
              return 0xffffffff;
            }
            return 0xffffffff;
          }
          if (DAT_ffff00009086da4c < (int)uVar4) {
            FUN_ffff000090730228
                      (s__CCCI___s__d__MD_mem_node_id_inv_ffff00009080263a,
                       s_ccci_insert_mem_node_ffff0000907cf6b5,0x1d0);
            if (DAT_ffff00009086da30 == 0) {
              return 0xffffffff;
            }
            if (DAT_ffff00009086da4c < 1) {
              return 0xffffffff;
            }
            if ((int)DAT_ffff00009086da48 < 0) {
              return 0xffffffff;
            }
            if ((int)DAT_ffff00009086da48 < DAT_ffff00009086da4c) {
              iVar8 = 1;
              uVar4 = DAT_ffff00009086da48;
              while( true ) {
                FUN_ffff00009073138c(DAT_ffff00009086da30 + (ulong)uVar4 * 0x20);
                if (DAT_ffff00009086da4c <= iVar8) {
                  return 0xffffffff;
                }
                uVar4 = *(uint *)(DAT_ffff00009086da30 + (ulong)uVar4 * 0x20 + 0x18);
                if ((int)uVar4 < 0) break;
                iVar8 = iVar8 + 1;
                if (DAT_ffff00009086da4c <= (int)uVar4) {
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
      FUN_ffff000090730228
                (s__CCCI___s__d__Enter_md_mem_node_e_ffff0000907d78cf,
                 s_ccci_insert_mem_node_ffff0000907cf6b5,0x1d6);
      if (DAT_ffff00009086da30 == 0) {
        return 0xffffffff;
      }
      if (DAT_ffff00009086da4c < 1) {
        return 0xffffffff;
      }
      if ((int)DAT_ffff00009086da48 < 0) {
        return 0xffffffff;
      }
      if ((int)DAT_ffff00009086da48 < DAT_ffff00009086da4c) {
        iVar8 = 1;
        uVar4 = DAT_ffff00009086da48;
        while( true ) {
          FUN_ffff00009073138c(DAT_ffff00009086da30 + (ulong)uVar4 * 0x20);
          if (DAT_ffff00009086da4c <= iVar8) {
            return 0xffffffff;
          }
          uVar4 = *(uint *)(DAT_ffff00009086da30 + (ulong)uVar4 * 0x20 + 0x18);
          if ((int)uVar4 < 0) break;
          iVar8 = iVar8 + 1;
          if (DAT_ffff00009086da4c <= (int)uVar4) {
            return 0xffffffff;
          }
        }
        return 0xffffffff;
      }
      return 0xffffffff;
    }
    pcVar5 = s__CCCI___s__d__MD_mem_tbl_1st_res_ffff0000907f9b7b;
    uVar6 = 0x1b6;
  }
  FUN_ffff000090730228(pcVar5,s_ccci_insert_mem_node_ffff0000907cf6b5,uVar6);
  return 0xffffffff;
}


