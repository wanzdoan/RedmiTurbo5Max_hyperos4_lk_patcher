// ffff0000907c49cc  size=152  FUN_ffff0000907c49cc

void FUN_ffff0000907c49cc(void)

{
  int iVar1;
  char *pcVar2;
  uint uVar3;
  uint local_4;
  
  local_4 = 0;
  iVar1 = FUN_ffff00009074a4e8(0,&local_4);
  if (iVar1 == 0) {
    uVar3 = local_4 & 0xff;
    if (uVar3 == 0) {
      FUN_ffff00009077de54(s_use_CN_avb_pubkey_ffff0000907e082d);
      pcVar2 = s_If_secureboot_is_not_enabled__mi_ffff0000907e70f3;
    }
    else {
      pcVar2 = s_use_GL_avb_pubkey_ffff0000907d3c5b;
    }
    FUN_ffff00009077de54(pcVar2);
    DAT_ffff00009090d904 = uVar3 != 0;
    return;
  }
  FUN_ffff00009077de54(s__SEC__Read_efuse_fail_in_sboot__0_ffff0000907e595b,iVar1);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_common_avb_hal_ffff0000907da34b,0xd6,&DAT_ffff0000907d24c9);
}


