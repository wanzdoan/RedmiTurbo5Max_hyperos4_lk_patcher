// ffff00009078a248  size=1024  FUN_ffff00009078a248

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009078a248(undefined8 param_1)

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
  FUN_ffff00009078a648();
  if (_DAT_ffff00009088770c == 0) {
    FUN_ffff00009073874c();
    uVar9 = 0xfffffff0;
  }
  else {
    FUN_ffff0000907387b4(&local_2a0,&local_28);
    FUN_ffff00009077b79c
              (DAT_ffff000090839908,s_SCP_reserved_ffff0000907f4730,local_28,&local_2a8,0xc,
               local_2a0,0,0);
    FUN_ffff000090737738(&DAT_ffff0000907eb784,&local_2a0,&local_2a8);
    if (_DAT_ffff000090887724 != 0) {
      iVar6 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_scp_ffff0000907e85e3);
      if (iVar6 < 0) {
        FUN_ffff00009077de54
                  (s__s__d___SCP__Error__fdt_abnormal_ffff0000907eb788,
                   s_scp_fdt_update_dram_size_ffff0000907f54d2,0x67);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_scp_scp_ffff0000907e54ad,0x68,&DAT_ffff0000907d24c9);
      }
      uVar3 = (_DAT_ffff000090887724 & 0xff00ff00) >> 8 | (_DAT_ffff000090887724 & 0xff00ff) << 8;
      local_298[0] = uVar3 >> 0x10 | uVar3 << 0x10;
      FUN_ffff00009076e40c(param_1,iVar6,s_scp_dram_size_ffff0000907fc542,local_298,4);
    }
    _DAT_ffff000090887718 = 0;
    if (DAT_ffff000090887714 != '\0') {
      FUN_ffff000090738b90(1,_DAT_ffff000090887708);
      uVar7 = FUN_ffff000090738b74(1);
      FUN_ffff000090738960(1,uVar7);
      FUN_ffff000090738b90(2,_DAT_ffff000090887728);
      uVar7 = FUN_ffff000090738b74(2);
      FUN_ffff000090738960(2,uVar7);
      FUN_ffff000090738b90(3,_DAT_ffff00009088772c);
      uVar7 = FUN_ffff000090738b74(3);
      FUN_ffff000090738960(3,uVar7);
      FUN_ffff000090738b90(4,_DAT_ffff000090887730);
      uVar7 = FUN_ffff000090738b74(4);
      FUN_ffff000090738960(4,uVar7);
      FUN_ffff000090738b90(5,_DAT_ffff000090887720);
      uVar7 = FUN_ffff000090738b74(5);
      FUN_ffff000090738960(5,uVar7);
      _DAT_ffff000090887718 = FUN_ffff000090738b74(5);
      if ((DAT_ffff000090887714 != '\0') && (_DAT_ffff000090887718 == 0)) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_scp_scp_ffff0000907e54ad,0x87,
                   s___scp_configs_secure_dump_____sc_ffff0000907f1e53);
      }
    }
    local_24 = 0;
    iVar6 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_scp_ffff0000907e85e3);
    if (iVar6 < 0) {
      FUN_ffff00009077de54
                (s__s__d___SCP__Error__fdt_abnormal_ffff0000907eb788,
                 s_scp_sharedram_layout_init_ffff0000907dd3c9,0xa3);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_common_scp_scp_ffff0000907e54ad,0xa4,&DAT_ffff0000907d24c9);
    }
    puVar8 = (uint *)FUN_ffff00009076dcd4(param_1,iVar6,s_scp_mem_tbl_ffff0000907dec73,&local_24);
    uVar3 = local_24;
    if ((puVar8 != (uint *)0x0) && (0 < (int)local_24)) {
      FUN_ffff00009077eb88(local_298,0,600);
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
              FUN_ffff00009077de54
                        (s__s__d___SCP__Error__idx__d_entry_ffff0000907db891,
                         s_scp_sharedram_layout_init_ffff0000907dd3c9,0xbf,uVar4,uVar11,0x19);
              goto LAB_ffff00009078a578;
            }
            uVar11 = uVar11 + 1;
            uVar5 = (puVar8[1] & 0xff00ff00) >> 8 | (puVar8[1] & 0xff00ff) << 8;
            uVar5 = uVar5 >> 0x10 | uVar5 << 0x10;
            if (DAT_ffff000090887714 != '\0' && uVar3 == 0) {
              uVar5 = _DAT_ffff000090887718;
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
            FUN_ffff000090738908(uVar12 & 0xffffffff);
          }
          uVar12 = uVar12 + 1;
          puVar14 = puVar14 + 3;
        } while (uVar12 != 0x19);
        _DAT_ffff00009088771c = (undefined4)uVar13;
      }
      else {
        FUN_ffff00009077de54
                  (s__s__d___SCP__Error__number_of_sc_ffff0000907dec7f,
                   s_scp_sharedram_layout_init_ffff0000907dd3c9,0xb4,(ulong)uVar3 / 0xc,0x19);
      }
    }
LAB_ffff00009078a578:
    if (_DAT_ffff000090887724 == 0) {
      FUN_ffff000090707ce4(local_2a8,local_28);
    }
    else {
      FUN_ffff000090738680();
    }
    FUN_ffff00009077bb68(DAT_ffff000090839908,local_2a8);
    FUN_ffff0000907386d0(local_2a0);
    FUN_ffff0000907389b8();
    uVar9 = 0;
  }
  return uVar9;
}


