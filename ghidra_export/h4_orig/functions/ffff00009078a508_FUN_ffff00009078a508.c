// ffff00009078a508  size=1024  FUN_ffff00009078a508

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009078a508(undefined8 param_1)

{
  uint *puVar1;
  ulong uVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  int iVar6;
  undefined4 uVar7;
  uint *puVar8;
  undefined8 uVar9;
  uint uVar10;
  uint uVar11;
  ulong uVar12;
  ulong uVar13;
  ulong *puVar14;
  undefined8 local_2a8;
  undefined8 local_2a0;
  uint local_298 [2];
  ulong local_290 [74];
  undefined4 local_28;
  uint local_24;
  
  local_2a8 = 0;
  local_2a0 = 0;
  local_28 = 0;
  FUN_ffff00009078a908();
  if (_DAT_ffff0000908877a4 == 0) {
    FUN_ffff0000907389e4();
    uVar9 = 0xfffffff0;
  }
  else {
    FUN_ffff000090738a4c(&local_2a0,&local_28);
    FUN_ffff00009077ba64
              (DAT_ffff000090839928,s_SCP_reserved_ffff0000907f48b4,local_28,&local_2a8,0xc,
               local_2a0,0,0);
    FUN_ffff0000907379d0(&DAT_ffff0000907eb91e,&local_2a0,&local_2a8);
    if (_DAT_ffff0000908877bc != 0) {
      iVar6 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_scp_ffff0000907e873e);
      if (iVar6 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___SCP__Error__fdt_abnormal_ffff0000907eb922,
                   s_scp_fdt_update_dram_size_ffff0000907f5656,0x67);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_scp_scp_ffff0000907e55c8,0x68,&DAT_ffff0000907d2506);
      }
      uVar3 = (_DAT_ffff0000908877bc & 0xff00ff00) >> 8 | (_DAT_ffff0000908877bc & 0xff00ff) << 8;
      local_298[0] = uVar3 >> 0x10 | uVar3 << 0x10;
      FUN_ffff00009076e6d4(param_1,iVar6,s_scp_dram_size_ffff0000907fc6de,local_298,4);
    }
    _DAT_ffff0000908877b0 = 0;
    if (DAT_ffff0000908877ac != '\0') {
      FUN_ffff000090738e28(1,_DAT_ffff0000908877a0);
      uVar7 = FUN_ffff000090738e0c(1);
      FUN_ffff000090738bf8(1,uVar7);
      FUN_ffff000090738e28(2,_DAT_ffff0000908877c0);
      uVar7 = FUN_ffff000090738e0c(2);
      FUN_ffff000090738bf8(2,uVar7);
      FUN_ffff000090738e28(3,_DAT_ffff0000908877c4);
      uVar7 = FUN_ffff000090738e0c(3);
      FUN_ffff000090738bf8(3,uVar7);
      FUN_ffff000090738e28(4,_DAT_ffff0000908877c8);
      uVar7 = FUN_ffff000090738e0c(4);
      FUN_ffff000090738bf8(4,uVar7);
      FUN_ffff000090738e28(5,_DAT_ffff0000908877b8);
      uVar7 = FUN_ffff000090738e0c(5);
      FUN_ffff000090738bf8(5,uVar7);
      _DAT_ffff0000908877b0 = FUN_ffff000090738e0c(5);
      if ((DAT_ffff0000908877ac != '\0') && (_DAT_ffff0000908877b0 == 0)) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_scp_scp_ffff0000907e55c8,0x87,
                   s___scp_configs_secure_dump_____sc_ffff0000907f1ff6);
      }
    }
    local_24 = 0;
    iVar6 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_scp_ffff0000907e873e);
    if (iVar6 < 0) {
      FUN_ffff00009077e11c
                (s__s__d___SCP__Error__fdt_abnormal_ffff0000907eb922,
                 s_scp_sharedram_layout_init_ffff0000907dd483,0xa3);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_scp_scp_ffff0000907e55c8,0xa4,&DAT_ffff0000907d2506);
    }
    puVar8 = (uint *)FUN_ffff00009076df9c(param_1,iVar6,s_scp_mem_tbl_ffff0000907ded2d,&local_24);
    uVar3 = local_24;
    if ((puVar8 != (uint *)0x0) && (0 < (int)local_24)) {
      FUN_ffff00009077ee50(local_298,0,600);
      if (uVar3 < 0x138) {
        if (0xb < uVar3) {
          uVar11 = 0;
          uVar10 = uVar3 / 0xc;
          if (uVar3 / 0xc < 2) {
            uVar10 = 1;
          }
          do {
            uVar3 = *puVar8;
            uVar4 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
            uVar4 = uVar4 >> 0x10 | uVar4 << 0x10;
            if (0x18 < uVar4) {
              FUN_ffff00009077e11c
                        (s__s__d___SCP__Error__idx__d_entry_ffff0000907db9b3,
                         s_scp_sharedram_layout_init_ffff0000907dd483,0xbf,uVar4,uVar11,0x19);
              goto LAB_ffff00009078a838;
            }
            uVar11 = uVar11 + 1;
            uVar5 = (puVar8[1] & 0xff00ff00) >> 8 | (puVar8[1] & 0xff00ff) << 8;
            uVar5 = uVar5 >> 0x10 | uVar5 << 0x10;
            if (DAT_ffff0000908877ac != '\0' && uVar3 == 0) {
              uVar5 = _DAT_ffff0000908877b0;
            }
            local_290[(ulong)uVar4 * 3] = (ulong)uVar5;
            puVar1 = puVar8 + 2;
            puVar8 = puVar8 + 3;
            uVar3 = (*puVar1 & 0xff00ff00) >> 8 | (*puVar1 & 0xff00ff) << 8;
            local_298[(ulong)uVar4 * 6] = uVar3 >> 0x10 | uVar3 << 0x10;
          } while (uVar10 != uVar11);
        }
        uVar12 = 0;
        uVar13 = 0;
        puVar14 = local_290 + 1;
        do {
          if (puVar14[-1] != 0) {
            iVar6 = (int)puVar14[-2];
            uVar2 = uVar13;
            if (iVar6 != 0) {
              uVar2 = (ulong)(((int)uVar13 + iVar6) - 1U & -iVar6);
            }
            uVar13 = uVar2 + puVar14[-1];
            *puVar14 = uVar2;
            FUN_ffff000090738ba0(uVar12 & 0xffffffff);
          }
          uVar12 = uVar12 + 1;
          puVar14 = puVar14 + 3;
        } while (uVar12 != 0x19);
        _DAT_ffff0000908877b4 = (undefined4)uVar13;
      }
      else {
        FUN_ffff00009077e11c
                  (s__s__d___SCP__Error__number_of_sc_ffff0000907ded39,
                   s_scp_sharedram_layout_init_ffff0000907dd483,0xb4,(ulong)uVar3 / 0xc,0x19);
      }
    }
LAB_ffff00009078a838:
    if (_DAT_ffff0000908877bc == 0) {
      FUN_ffff000090707c70(local_2a8,local_28);
    }
    else {
      FUN_ffff000090738918();
    }
    FUN_ffff00009077be30(DAT_ffff000090839928,local_2a8);
    FUN_ffff000090738968(local_2a0);
    FUN_ffff000090738c50();
    uVar9 = 0;
  }
  return uVar9;
}


