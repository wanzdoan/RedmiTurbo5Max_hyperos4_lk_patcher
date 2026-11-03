// ffff00009073f588  size=524  FUN_ffff00009073f588

/* WARNING: Type propagation algorithm not settling */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009073f588(long param_1)

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
    FUN_ffff00009077de54(s_slbc__failed_to_get_fdt_ffff0000907fb9a0);
    _DAT_ffff00009086f208 = 0xffffffff;
  }
  else {
    iVar2 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_mtk_slbc_ffff0000907fb9b9);
    if (iVar2 < 0) {
      FUN_ffff00009077de54(s_slbc__failed_to_get_slbc_fdt_off_ffff0000907f6494);
      _DAT_ffff00009086f208 = 0xfffffffe;
    }
    else {
      lVar4 = FUN_ffff00009076dcd4(param_1,iVar2,s_trace_dram_base_ffff0000907e4b74,local_18 + 1);
      if (lVar4 == 0) {
        pcVar5 = s_slbc__failed_to_get_slbc_trace_d_ffff0000907f9d2b;
      }
      else {
        lVar4 = FUN_ffff00009076dcd4(param_1,iVar2,s_trace_dram_size_ffff0000907ee2b1,local_18 + 1);
        if (lVar4 == 0) {
          pcVar5 = s_slbc__failed_to_get_slbc_trace_d_ffff00009080271a;
        }
        else {
          lVar4 = FUN_ffff000090719798
                            (0x40000,0x10000,0xffffffff,0,0,
                             s_platform_debug_slbc_shared_ffff0000907e4b3b);
          if (lVar4 == 0) {
            pcVar5 = s_slbc__slbc_shared_mblock_allocat_ffff0000907f49f5;
          }
          else {
            FUN_ffff00009077de54(s_slbc__malloc_start_addr_0x_lx__s_ffff0000907e2ed9,lVar4,0x40000);
            iVar3 = FUN_ffff00009077b79c
                              (DAT_ffff000090839908,s_vm_SLBC_shared_ffff0000907d2e50,0x40000,
                               &local_38,0xc,lVar4,0,0);
            if (iVar3 != 0) {
              FUN_ffff00009077de54(s_slbc__vmm_alloc_physical_failed__ffff0000907d7b16,iVar3);
              return;
            }
            if (local_38 == 0) {
              FUN_ffff00009077de54(s_slbc__slbc_dram_base_va_is_zero__ffff0000907ead8a,0);
              return;
            }
            FUN_ffff00009077eb88(local_38,0,0x40000);
            FUN_ffff00009077bb68(DAT_ffff000090839908,local_38);
            uVar1 = ((uint)lVar4 & 0xff00ff00) >> 8 | ((uint)lVar4 & 0xff00ff) << 8;
            local_18[0] = uVar1 >> 0x10 | uVar1 << 0x10;
            iVar3 = FUN_ffff00009076e40c
                              (param_1,iVar2,s_trace_dram_base_ffff0000907e4b74,local_18,4);
            if (iVar3 < 0) {
              pcVar5 = s_slbc__set_trace_dram_base_fails_ffff0000907dc8dd;
            }
            else {
              local_18[0] = 0x400;
              iVar2 = FUN_ffff00009076e40c
                                (param_1,iVar2,s_trace_dram_size_ffff0000907ee2b1,local_18,4);
              if (-1 < iVar2) {
                return;
              }
              pcVar5 = s_slbc__set_trace_dram_size_fails_ffff0000907e954f;
            }
          }
        }
      }
      FUN_ffff00009077de54(pcVar5);
    }
  }
  return;
}


