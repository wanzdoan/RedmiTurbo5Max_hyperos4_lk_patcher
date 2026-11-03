
void FUN_ffff0000907c5040(void)

{
  int iVar1;
  char *pcVar2;
  uint uVar3;
  uint local_4;
  
  local_4 = 0;
  iVar1 = FUN_ffff00009074a780(0,&local_4);
  if (iVar1 == 0) {
    uVar3 = local_4 & 0xff;
    if (uVar3 == 0) {
      FUN_ffff00009077e11c(s_use_CN_avb_pubkey_ffff0000907e08e7);
      pcVar2 = s_If_secureboot_is_not_enabled__mi_ffff0000907e724e;
    }
    else {
      pcVar2 = s_use_GL_avb_pubkey_ffff0000907d3c98;
    }
    FUN_ffff00009077e11c(pcVar2);
    DAT_ffff00009090d9c4 = uVar3 != 0;
    return;
  }
  FUN_ffff00009077e11c(s__SEC__Read_efuse_fail_in_sboot__0_ffff0000907e5a76,iVar1);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_common_avb_hal_ffff0000907da3f9,0xd6,&DAT_ffff0000907d2506);
}


