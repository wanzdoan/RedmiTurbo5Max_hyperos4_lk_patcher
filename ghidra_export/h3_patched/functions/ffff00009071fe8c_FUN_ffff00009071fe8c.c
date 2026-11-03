// ffff00009071fe8c  size=1236  FUN_ffff00009071fe8c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff00009071fe8c(long param_1)

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
    iVar6 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_ccu_rproc_ffff0000907ec05e);
    if (iVar6 < 0) {
      FUN_ffff00009077de54(s__CCU__CCU_DT_node_not_found__dis_ffff0000907e8f23);
      _DAT_ffff000090860598 = 0;
    }
    lVar8 = FUN_ffff00009076dcd4(param_1,iVar6,s_secured_ffff0000907fb312,&local_50);
    if (lVar8 == 0) {
      _DAT_ffff000090860598 = 0;
    }
    else {
      iVar7 = FUN_ffff00009077f080(lVar8,&DAT_ffff0000907df2a3,(long)(int)local_50);
      _DAT_ffff000090860598 = (uint)(iVar7 == 0);
    }
    lVar8 = FUN_ffff00009076dcd4(param_1,iVar6,s_ccu_emiStatus_ffff0000907d289f,&local_50);
    if (lVar8 == 0) {
      _DAT_ffff00009086059c = 1;
    }
    else {
      iVar7 = FUN_ffff00009077f080(lVar8,&DAT_ffff0000907f0b6f,(long)(int)local_50);
      _DAT_ffff00009086059c = (uint)(iVar7 != 0);
    }
    puVar9 = (uint *)FUN_ffff00009076dcd4(param_1,iVar6,s_ccu_sramSize_ffff0000907e5d46,&local_50);
    _DAT_ffff00009086058c = 0x20000;
    if (puVar9 != (uint *)0x0) {
      uVar3 = *puVar9;
      uVar5 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
      _DAT_ffff00009086058c = 0x20000;
      if (uVar3 != 0) {
        _DAT_ffff00009086058c = uVar5 >> 0x10 | uVar5 << 0x10;
      }
    }
    puVar9 = (uint *)FUN_ffff00009076dcd4(param_1,iVar6,s_ccu_sramOffset_ffff0000907f9576,&local_50)
    ;
    _DAT_ffff000090860590 = 0x20000;
    if (puVar9 != (uint *)0x0) {
      uVar3 = *puVar9;
      uVar5 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
      _DAT_ffff000090860590 = 0x20000;
      if (uVar3 != 0) {
        _DAT_ffff000090860590 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
    }
    if (_DAT_ffff000090860590 <= _DAT_ffff00009086058c) {
      _DAT_ffff000090860590 = _DAT_ffff00009086058c;
    }
    puVar9 = (uint *)FUN_ffff00009076dcd4(param_1,iVar6,s_ccu_dramSize_ffff0000907ef457,&local_50);
    _DAT_ffff000090860588 = 0x400000;
    if (puVar9 != (uint *)0x0) {
      uVar3 = *puVar9;
      uVar5 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
      _DAT_ffff000090860588 = 0x400000;
      if (uVar3 != 0) {
        _DAT_ffff000090860588 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
    }
    puVar9 = (uint *)FUN_ffff00009076dcd4(param_1,iVar6,s_ccu_dramAddr_ffff0000907f5f24,&local_50);
    _DAT_ffff000090860580 = 0x80000000;
    if (puVar9 != (uint *)0x0) {
      uVar5 = *puVar9;
      uVar13 = (uVar5 & 0xff00ff00) >> 8 | (uVar5 & 0xff00ff) << 8;
      uVar3 = 0x80000000;
      if (uVar5 != 0) {
        uVar3 = uVar13 >> 0x10 | uVar13 << 0x10;
      }
      _DAT_ffff000090860580 = (ulong)uVar3;
    }
    puVar9 = (uint *)FUN_ffff00009076dcd4(param_1,iVar6,s_ccu_emiRegion_ffff00009080210b,&local_50);
    _DAT_ffff000090860594 = 0x14;
    if (puVar9 != (uint *)0x0) {
      uVar3 = *puVar9;
      uVar5 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
      _DAT_ffff000090860594 = 0x14;
      if (uVar3 != 0) {
        _DAT_ffff000090860594 = uVar5 >> 0x10 | uVar5 << 0x10;
      }
    }
  }
  uVar5 = _DAT_ffff000090860590;
  uVar3 = _DAT_ffff00009086058c;
  uVar10 = _DAT_ffff000090860580;
  if (_DAT_ffff000090860598 == 0) {
    FUN_ffff00009077de54(s___s___get_ccu_status_disabled_ffff0000907ec03f,&DAT_ffff0000907ea83e);
    uVar15 = 0;
  }
  else {
    uVar2 = (ulong)_DAT_ffff000090860588;
    iVar6 = FUN_ffff00009071c57c();
    if (iVar6 == 0) {
      uVar10 = FUN_ffff000090719798
                         (uVar2,0x100000,uVar10 + uVar2,0,0,s_mm_ccu_reserved_ffff0000907e5d36);
    }
    else {
      uVar10 = FUN_ffff000090719f58
                         (uVar2,0x100000,0x240000000,0x340000000,0,0,
                          s_mm_ccu_reserved_ffff0000907e5d36);
    }
    if (uVar10 == 0) {
      FUN_ffff00009077de54(s___s__mblock_allocation_failed_ffff0000908020ec,&DAT_ffff0000907ea83e);
      uVar15 = 0xfffffffb;
    }
    else {
      iVar6 = FUN_ffff00009077b79c
                        (DAT_ffff000090839908,s_mm_ccu_reserved_ffff0000907e5d36,uVar2,&local_58,0xc
                         ,uVar10,0,0);
      if (iVar6 == 0) {
        iVar6 = FUN_ffff000090785a50(&DAT_ffff0000907ef453,&DAT_ffff0000907ef453,local_58,uVar2);
        if (iVar6 < 0) {
          FUN_ffff00009077de54
                    (s___s__load_ccu_fail__ret____d_ffff0000907cf3af,&DAT_ffff0000907ea83e,iVar6);
          uVar15 = 0xffffffec;
        }
        else {
          FUN_ffff000090707cc4(local_58,uVar2);
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
                      pcVar11 = s___s__CCU_binary_itcm_addr_size_e_ffff0000907fb2ea;
LAB_ffff000090720314:
                      FUN_ffff00009077de54(pcVar11,&DAT_ffff0000907ea83e);
                      uVar15 = 0xffffff9a;
                      goto LAB_ffff000090720324;
                    }
                  }
                  else if (uVar3 < (uVar4 - uVar5) + *piVar14) {
                    pcVar11 = s___s__CCU_binary_dtcm_addr_size_e_ffff0000907f7d32;
                    goto LAB_ffff000090720314;
                  }
                }
                piVar14 = piVar14 + 3;
                uVar12 = uVar12 - 1;
              } while (uVar12 != 0);
            }
            FUN_ffff00009071ea6c(0xc2000412,1,uVar10,uVar2,0,0,0,0,&local_78);
            if (local_78 == 0) {
              if (_DAT_ffff00009086059c != 0) {
                lStack_48 = uVar10 + uVar2;
                uStack_38 = 0;
                local_40 = (ulong)_DAT_ffff000090860594;
                local_50 = uVar10;
                FUN_ffff00009078ae3c(&local_50);
                return 0;
              }
              return 0;
            }
            FUN_ffff00009077de54
                      (s___s__CCU_REQ_INIT_errors___d___ffff0000907cf3cd,&DAT_ffff0000907ea83e);
            uVar15 = 0xffffff96;
          }
          else {
            FUN_ffff00009077de54
                      (s___s__CCU_binary_does_NOT_have_CC_ffff0000907d411f,&DAT_ffff0000907ea83e);
            uVar15 = 0xffffff9b;
          }
        }
LAB_ffff000090720324:
        FUN_ffff00009077bb68(DAT_ffff000090839908,local_58);
      }
      else {
        FUN_ffff00009077de54
                  (s___s__vmm_alloc_physical_failed_ffff0000907edc0a,&DAT_ffff0000907ea83e);
        uVar15 = 0xfffffffb;
      }
      FUN_ffff00009071a6ac(uVar10);
    }
  }
  return uVar15;
}


