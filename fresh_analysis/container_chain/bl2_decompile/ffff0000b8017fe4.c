
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b8017fe4(code *UNRECOVERED_JUMPTABLE)

{
  ulong uVar1;
  ulong uVar2;
  char *pcVar3;
  undefined8 uVar4;
  
  uVar2 = FUN_ffff0000b80185a8();
  if ((uVar2 & 1) == 0) {
    pcVar3 = s__s__GPUEB_MFG0__NOT_power_on____ffff0000b8091734;
  }
  else {
    FUN_ffff0000b80184e8();
    _DAT_ffff00004b140100 = 0;
    if (_DAT_ffff00004b09fd0c != -0x21524111) {
      FUN_ffff0000b804c33c
                (s__s__gpueb_sram_data_invalid__PLT_ffff0000b809b6af,
                 s__GPUEB__EXPDB__ffff0000b809d5e3,0x4b09fd0c,_DAT_ffff00004b09fd0c,0xdeadbeef);
      return;
    }
    if (_DAT_ffff00004b09fd20 != 0xdeadbeef) {
      uVar2 = (ulong)_DAT_ffff00004b09fd1c;
      if ((int)_DAT_ffff00004b09fd1c < 1) {
        FUN_ffff0000b804c33c
                  (s__s__invalid_last_log_size____d_ffff0000b809c429,
                   s__GPUEB__EXPDB__ffff0000b809d5e3,_DAT_ffff00004b09fd1c);
        return;
      }
      uVar1 = ((ulong)_DAT_ffff00004b09fd20 - 0xffffb5000000) + uVar2;
      if (0xffff00004b09f000 < uVar1) {
        FUN_ffff0000b804c33c
                  (s__s__invalid_last_log_addr_0x_lx__ffff0000b808dd27,
                   s__GPUEB__EXPDB__ffff0000b809d5e3);
        FUN_ffff0000b804c33c
                  (s__s__end_log_addr_0x_lx__>_sram_r_ffff0000b8088d3f,
                   s__GPUEB__EXPDB__ffff0000b809d5e3,uVar1,0xffff00004b09f000);
        return;
      }
      _DAT_ffff00004b140100 = 0;
      uVar4 = FUN_ffff0000b8049ec8((ulong)_DAT_ffff00004b09fd20 - 0xffffb5000000);
                    /* WARNING: Could not recover jumptable at 0xffff0000b8018170. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
      (*UNRECOVERED_JUMPTABLE)(uVar4,uVar2);
      return;
    }
    pcVar3 = s__s__gpueb_CFG_LOGGER_SUPPORT___n_ffff0000b808dd04;
  }
  FUN_ffff0000b804c33c(pcVar3,s__GPUEB__EXPDB__ffff0000b809d5e3);
  return;
}


