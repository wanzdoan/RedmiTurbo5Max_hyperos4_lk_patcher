
void FUN_ffff00009074a688(void)

{
  int iVar1;
  char local_14 [4];
  
  local_14[0] = '\0';
  DAT_ffff000090877708 = FUN_ffff000090740610(1,5);
  iVar1 = FUN_ffff0000907404fc(DAT_ffff000090877708,0x1ca3,local_14,1,0);
  if (iVar1 < 0) {
    FUN_ffff00009077e11c
              (s_SPMI_read_fail_at___s___addr___d_ffff0000907fbd17,
               s_efuse_fsource_is_enabled_ffff0000907ee5a6,0x1ca3,iVar1);
  }
  if (local_14[0] != '\x01') {
    return;
  }
  FUN_ffff00009077e11c(s_vefuse_is_on__shouldn_t_happen_ffff0000907dcbc0);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_mt6991_devinfo_ffff0000907f8702,0xf1,&DAT_ffff0000907d2506);
}


