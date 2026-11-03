
undefined4 FUN_ffff000090720014(long param_1)

{
  uint uVar1;
  ulong uVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  int iVar6;
  int iVar7;
  long lVar8;
  uint *puVar9;
  ulong uVar10;
  char *pcVar11;
  ulong uVar12;
  uint uVar13;
  int *piVar14;
  undefined4 uVar15;
  long local_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  int *local_58;
  ulong local_50;
  long lStack_48;
  ulong local_40;
  undefined8 uStack_38;
  
  local_60 = 0;
  local_58 = (int *)0x0;
  local_70 = 0;
  uStack_68 = 0;
  local_78 = 0;
  local_50 = local_50 & 0xffffffff00000000;
  if (param_1 != 0) {
    iVar6 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_ccu_rproc_ffff0000907ec1f8);
    if (iVar6 < 0) {
      FUN_ffff00009077e11c(s__CCU__CCU_DT_node_not_found__dis_ffff0000907e907e);
      DAT_ffff000090860630 = 0;
    }
    lVar8 = FUN_ffff00009076df9c(param_1,iVar6,s_secured_ffff0000907fb4ae,&local_50);
    if (lVar8 == 0) {
      DAT_ffff000090860630 = 0;
    }
    else {
      iVar7 = FUN_ffff00009077f348(lVar8,&DAT_ffff0000907df35d,(long)(int)local_50);
      DAT_ffff000090860630 = (uint)(iVar7 == 0);
    }
    lVar8 = FUN_ffff00009076df9c(param_1,iVar6,s_ccu_emiStatus_ffff0000907d28dc,&local_50);
    if (lVar8 == 0) {
      DAT_ffff000090860634 = 1;
    }
    else {
      iVar7 = FUN_ffff00009077f348(lVar8,&DAT_ffff0000907f0cf7,(long)(int)local_50);
      DAT_ffff000090860634 = (uint)(iVar7 != 0);
    }
    puVar9 = (uint *)FUN_ffff00009076df9c(param_1,iVar6,s_ccu_sramSize_ffff0000907e5e71,&local_50);
    DAT_ffff000090860624 = 0x20000;
    if (puVar9 != (uint *)0x0) {
      uVar3 = *puVar9;
      uVar5 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
      DAT_ffff000090860624 = 0x20000;
      if (uVar3 != 0) {
        DAT_ffff000090860624 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
    }
    puVar9 = (uint *)FUN_ffff00009076df9c(param_1,iVar6,s_ccu_sramOffset_ffff0000907f9712,&local_50)
    ;
    DAT_ffff000090860628 = 0x20000;
    if (puVar9 != (uint *)0x0) {
      uVar3 = *puVar9;
      uVar5 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
      DAT_ffff000090860628 = 0x20000;
      if (uVar3 != 0) {
        DAT_ffff000090860628 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
    }
    if (DAT_ffff000090860628 <= DAT_ffff000090860624) {
      DAT_ffff000090860628 = DAT_ffff000090860624;
    }
    puVar9 = (uint *)FUN_ffff00009076df9c(param_1,iVar6,s_ccu_dramSize_ffff0000907ef5df,&local_50);
    DAT_ffff000090860620 = 0x400000;
    if (puVar9 != (uint *)0x0) {
      uVar3 = *puVar9;
      uVar5 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
      DAT_ffff000090860620 = 0x400000;
      if (uVar3 != 0) {
        DAT_ffff000090860620 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
    }
    puVar9 = (uint *)FUN_ffff00009076df9c(param_1,iVar6,s_ccu_dramAddr_ffff0000907f60c5,&local_50);
    DAT_ffff000090860618 = 0x80000000;
    if (puVar9 != (uint *)0x0) {
      uVar5 = *puVar9;
      uVar13 = (uVar5 & 0xff00ff00) >> 8 | (uVar5 & 0xff00ff) << 8;
      uVar3 = 0x80000000;
      if (uVar5 != 0) {
        uVar3 = uVar13 >> 0x10 | uVar13 << 0x10;
      }
      DAT_ffff000090860618 = (ulong)uVar3;
    }
    puVar9 = (uint *)FUN_ffff00009076df9c(param_1,iVar6,s_ccu_emiRegion_ffff0000908022f1,&local_50);
    DAT_ffff00009086062c = 0x14;
    if (puVar9 != (uint *)0x0) {
      uVar3 = *puVar9;
      uVar5 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
      DAT_ffff00009086062c = 0x14;
      if (uVar3 != 0) {
        DAT_ffff00009086062c = uVar5 >> 0x10 | uVar5 << 0x10;
      }
    }
  }
  uVar5 = DAT_ffff000090860628;
  uVar3 = DAT_ffff000090860624;
  uVar10 = DAT_ffff000090860618;
  if (DAT_ffff000090860630 == 0) {
    FUN_ffff00009077e11c(s___s___get_ccu_status_disabled_ffff0000907ec1d9,&DAT_ffff0000907ea9d8);
    uVar15 = 0;
  }
  else {
    uVar2 = (ulong)DAT_ffff000090860620;
    iVar6 = FUN_ffff00009071c708();
    if (iVar6 == 0) {
      uVar10 = FUN_ffff0000907198dc
                         (uVar2,0x100000,uVar10 + uVar2,0,0,s_mm_ccu_reserved_ffff0000907e5e61);
    }
    else {
      uVar10 = FUN_ffff00009071a09c
                         (uVar2,0x100000,0x240000000,0x340000000,0,0,
                          s_mm_ccu_reserved_ffff0000907e5e61);
    }
    if (uVar10 == 0) {
      FUN_ffff00009077e11c(s___s__mblock_allocation_failed_ffff0000908022d2,&DAT_ffff0000907ea9d8);
      uVar15 = 0xfffffffb;
    }
    else {
      iVar6 = FUN_ffff00009077ba64
                        (PTR_DAT_ffff000090839928,s_mm_ccu_reserved_ffff0000907e5e61,uVar2,&local_58
                         ,0xc,uVar10,0,0);
      if (iVar6 == 0) {
        iVar6 = FUN_ffff000090785d18(&DAT_ffff0000907ef5db,&DAT_ffff0000907ef5db,local_58,uVar2);
        if (iVar6 < 0) {
          FUN_ffff00009077e11c
                    (s___s__load_ccu_fail__ret____d_ffff0000907cf3ce,&DAT_ffff0000907ea9d8,iVar6);
          uVar15 = 0xffffffec;
        }
        else {
          FUN_ffff000090707c50(local_58,uVar2);
          if (*local_58 == 0x30554343) {
            uVar12 = (ulong)(uint)local_58[2];
            if (local_58[2] != 0) {
              uVar13 = 0;
              piVar14 = local_58 + 5;
              do {
                uVar4 = piVar14[-1];
                if (uVar4 >> 0x1e == 0) {
                  uVar1 = uVar4 & 0xff000000;
                  if (uVar13 != 0) {
                    uVar1 = uVar13;
                  }
                  uVar4 = uVar4 - uVar1;
                  uVar13 = uVar1;
                  if (uVar4 < uVar5) {
                    if (uVar3 < *piVar14 + uVar4) {
                      pcVar11 = s___s__CCU_binary_itcm_addr_size_e_ffff0000907fb486;
LAB_ffff00009072049c:
                      FUN_ffff00009077e11c(pcVar11,&DAT_ffff0000907ea9d8);
                      uVar15 = 0xffffff9a;
                      goto LAB_ffff0000907204ac;
                    }
                  }
                  else if (uVar3 < (uVar4 - uVar5) + *piVar14) {
                    pcVar11 = s___s__CCU_binary_dtcm_addr_size_e_ffff0000907f7ece;
                    goto LAB_ffff00009072049c;
                  }
                }
                piVar14 = piVar14 + 3;
                uVar12 = uVar12 - 1;
              } while (uVar12 != 0);
            }
            FUN_ffff00009071ebf4(0xc2000412,1,uVar10,uVar2,0,0,0,0,&local_78);
            if (local_78 == 0) {
              if (DAT_ffff000090860634 != 0) {
                lStack_48 = uVar10 + uVar2;
                uStack_38 = 0;
                local_40 = (ulong)DAT_ffff00009086062c;
                local_50 = uVar10;
                FUN_ffff00009078b0fc(&local_50);
                return 0;
              }
              return 0;
            }
            FUN_ffff00009077e11c
                      (s___s__CCU_REQ_INIT_errors___d___ffff0000907cf3ec,&DAT_ffff0000907ea9d8);
            uVar15 = 0xffffff96;
          }
          else {
            FUN_ffff00009077e11c
                      (s___s__CCU_binary_does_NOT_have_CC_ffff0000907d415c,&DAT_ffff0000907ea9d8);
            uVar15 = 0xffffff9b;
          }
        }
LAB_ffff0000907204ac:
        FUN_ffff00009077be30(PTR_DAT_ffff000090839928,local_58);
      }
      else {
        FUN_ffff00009077e11c
                  (s___s__vmm_alloc_physical_failed_ffff0000907edd92,&DAT_ffff0000907ea9d8);
        uVar15 = 0xfffffffb;
      }
      FUN_ffff00009071a7f0(uVar10);
    }
  }
  return uVar15;
}


