// ffff00009072769c  size=400  FUN_ffff00009072769c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009072769c(code *UNRECOVERED_JUMPTABLE)

{
  ulong uVar1;
  char *pcVar2;
  undefined8 uVar3;
  undefined1 *puVar4;
  
  uVar1 = FUN_ffff000090727c90();
  if ((uVar1 & 1) == 0) {
    pcVar2 = s__s__GPUEB_MFG0__NOT_power_on____ffff0000907e5fd2;
  }
  else {
    FUN_ffff000090727ba0();
    _DAT_ffff00004b140100 = 0;
    if (_DAT_ffff00004b09fd0c != -0x21524111) {
      FUN_ffff00009077e11c
                (s__s__gpueb_sram_data_invalid__PLT_ffff0000907f7f4e,
                 s__GPUEB__EXPDB__ffff0000907fb5a9,0x4b09fd0c,_DAT_ffff00004b09fd0c,0xdeadbeef);
      return;
    }
    if (_DAT_ffff00004b09fd20 != 0xdeadbeef) {
      uVar1 = (ulong)_DAT_ffff00004b09fd1c;
      if ((int)_DAT_ffff00004b09fd1c < 1) {
        FUN_ffff00009077e11c
                  (s__s__invalid_last_log_size____d_ffff0000907f984c,
                   s__GPUEB__EXPDB__ffff0000907fb5a9,_DAT_ffff00004b09fd1c);
        return;
      }
      puVar4 = &DAT_ffff00004b000000 + _DAT_ffff00004b09fd20;
      if ((undefined1 *)0xffff00004b09f000 < puVar4 + uVar1) {
        FUN_ffff00009077e11c
                  (s__s__invalid_last_log_addr_0x_lx__ffff0000907df79f,
                   s__GPUEB__EXPDB__ffff0000907fb5a9);
        FUN_ffff00009077e11c
                  (s__s__end_log_addr_0x_lx__>_sram_r_ffff0000907d5dae,
                   s__GPUEB__EXPDB__ffff0000907fb5a9,puVar4 + uVar1,0xffff00004b09f000);
        return;
      }
      _DAT_ffff00004b140100 = 0;
      uVar3 = FUN_ffff00009077b334(puVar4);
                    /* WARNING: Could not recover jumptable at 0xffff000090727828. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
      (*UNRECOVERED_JUMPTABLE)(uVar3,uVar1);
      return;
    }
    pcVar2 = s__s__gpueb_CFG_LOGGER_SUPPORT___n_ffff0000907df77c;
  }
  FUN_ffff00009077e11c(pcVar2,s__GPUEB__EXPDB__ffff0000907fb5a9);
  return;
}


