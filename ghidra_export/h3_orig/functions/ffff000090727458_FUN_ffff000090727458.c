// ffff000090727458  size=400  FUN_ffff000090727458

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090727458(code *UNRECOVERED_JUMPTABLE)

{
  ulong uVar1;
  char *pcVar2;
  undefined8 uVar3;
  undefined1 *puVar4;
  
  uVar1 = FUN_ffff000090727a4c();
  if ((uVar1 & 1) == 0) {
    pcVar2 = s__s__GPUEB_MFG0__NOT_power_on____ffff0000907e5ebe;
  }
  else {
    FUN_ffff00009072795c();
    _DAT_ffff00004b140100 = 0;
    if (_DAT_ffff00004b09fd0c != -0x21524111) {
      FUN_ffff00009077de54
                (s__s__gpueb_sram_data_invalid__PLT_ffff0000907f7db2,
                 s__GPUEB__EXPDB__ffff0000907fb40d,0x4b09fd0c,_DAT_ffff00004b09fd0c,0xdeadbeef);
      return;
    }
    if (_DAT_ffff00004b09fd20 != 0xdeadbeef) {
      uVar1 = (ulong)_DAT_ffff00004b09fd1c;
      if ((int)_DAT_ffff00004b09fd1c < 1) {
        FUN_ffff00009077de54
                  (s__s__invalid_last_log_size____d_ffff0000907f96b0,
                   s__GPUEB__EXPDB__ffff0000907fb40d,_DAT_ffff00004b09fd1c);
        return;
      }
      puVar4 = &DAT_ffff00004b000000 + _DAT_ffff00004b09fd20;
      if ((undefined1 *)0xffff00004b09f000 < puVar4 + uVar1) {
        FUN_ffff00009077de54
                  (s__s__invalid_last_log_addr_0x_lx__ffff0000907df6fc,
                   s__GPUEB__EXPDB__ffff0000907fb40d);
        FUN_ffff00009077de54
                  (s__s__end_log_addr_0x_lx__>_sram_r_ffff0000907d5d18,
                   s__GPUEB__EXPDB__ffff0000907fb40d,puVar4 + uVar1,0xffff00004b09f000);
        return;
      }
      _DAT_ffff00004b140100 = 0;
      uVar3 = FUN_ffff00009077b06c(puVar4);
                    /* WARNING: Could not recover jumptable at 0xffff0000907275e4. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
      (*UNRECOVERED_JUMPTABLE)(uVar3,uVar1);
      return;
    }
    pcVar2 = s__s__gpueb_CFG_LOGGER_SUPPORT___n_ffff0000907df6d9;
  }
  FUN_ffff00009077de54(pcVar2,s__GPUEB__EXPDB__ffff0000907fb40d);
  return;
}


