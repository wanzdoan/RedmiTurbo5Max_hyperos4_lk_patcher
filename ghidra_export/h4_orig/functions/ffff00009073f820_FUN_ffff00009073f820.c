// ffff00009073f820  size=524  FUN_ffff00009073f820

/* WARNING: Type propagation algorithm not settling */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009073f820(long param_1)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  long lVar4;
  char *pcVar5;
  long local_38;
  uint local_18 [2];
  
  local_18[1] = 0;
  local_38 = 0;
  if (param_1 == 0) {
    FUN_ffff00009077e11c(s_slbc__failed_to_get_fdt_ffff0000907fbb3c);
    _DAT_ffff00009086f2a0 = 0xffffffff;
  }
  else {
    iVar2 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_mtk_slbc_ffff0000907fbb55);
    if (iVar2 < 0) {
      FUN_ffff00009077e11c(s_slbc__failed_to_get_slbc_fdt_off_ffff0000907f6635);
      _DAT_ffff00009086f2a0 = 0xfffffffe;
    }
    else {
      lVar4 = FUN_ffff00009076df9c(param_1,iVar2,s_trace_dram_base_ffff0000907e4c8f,local_18 + 1);
      if (lVar4 == 0) {
        pcVar5 = s_slbc__failed_to_get_slbc_trace_d_ffff0000907f9ec7;
      }
      else {
        lVar4 = FUN_ffff00009076df9c(param_1,iVar2,s_trace_dram_size_ffff0000907ee439,local_18 + 1);
        if (lVar4 == 0) {
          pcVar5 = s_slbc__failed_to_get_slbc_trace_d_ffff00009080291e;
        }
        else {
          lVar4 = FUN_ffff0000907198dc
                            (0x40000,0x10000,0xffffffff,0,0,
                             s_platform_debug_slbc_shared_ffff0000907e4c56);
          if (lVar4 == 0) {
            pcVar5 = s_slbc__slbc_shared_mblock_allocat_ffff0000907f4b79;
          }
          else {
            FUN_ffff00009077e11c(s_slbc__malloc_start_addr_0x_lx__s_ffff0000907e2fc1,lVar4,0x40000);
            iVar3 = FUN_ffff00009077ba64
                              (DAT_ffff000090839928,s_vm_SLBC_shared_ffff0000907d2e8d,0x40000,
                               &local_38,0xc,lVar4,0,0);
            if (iVar3 != 0) {
              FUN_ffff00009077e11c(s_slbc__vmm_alloc_physical_failed__ffff0000907d7bac,iVar3);
              return;
            }
            if (local_38 == 0) {
              FUN_ffff00009077e11c(s_slbc__slbc_dram_base_va_is_zero__ffff0000907eaf24,0);
              return;
            }
            FUN_ffff00009077ee50(local_38,0,0x40000);
            FUN_ffff00009077be30(DAT_ffff000090839928,local_38);
            uVar1 = ((uint)lVar4 & 0xff00ff00) >> 8 | ((uint)lVar4 & 0xff00ff) << 8;
            local_18[0] = uVar1 >> 0x10 | uVar1 << 0x10;
            iVar3 = FUN_ffff00009076e6d4
                              (param_1,iVar2,s_trace_dram_base_ffff0000907e4c8f,local_18,4);
            if (iVar3 < 0) {
              pcVar5 = s_slbc__set_trace_dram_base_fails_ffff0000907dc9ca;
            }
            else {
              local_18[0] = 0x400;
              iVar2 = FUN_ffff00009076e6d4
                                (param_1,iVar2,s_trace_dram_size_ffff0000907ee439,local_18,4);
              if (-1 < iVar2) {
                return;
              }
              pcVar5 = s_slbc__set_trace_dram_size_fails_ffff0000907e96aa;
            }
          }
        }
      }
      FUN_ffff00009077e11c(pcVar5);
    }
  }
  return;
}


