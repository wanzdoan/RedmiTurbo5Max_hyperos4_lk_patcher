
void FUN_ffff000090710588(undefined8 param_1,long param_2,long param_3)

{
  long lVar1;
  long lVar2;

  if ((DAT_ffff000090837600 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077d934
              (s_app_mt_boot_boot_info_c_ffff0000907d90c4,0x210,
               s_g_boot_info_hdr_loaded_ffff0000907ca6d1);
  }
  DAT_ffff000090837608 = 1;
  DAT_ffff000090837610 = param_1;
  lVar1 = DAT_ffff000090837618;
  lVar2 = DAT_ffff000090837620;
  if (3 < DAT_ffff000090836758) {
    if (param_2 == 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077d934
                (s_app_mt_boot_boot_info_c_ffff0000907d90c4,0x217,s_vendorboot_addr_ffff0000907cbfac
                );
    }
    lVar1 = param_2;
    lVar2 = param_3;
    if (param_3 == 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077d934
                (s_app_mt_boot_boot_info_c_ffff0000907d90c4,0x218,s_initboot_addr_ffff0000907cd9ef);
    }
  }
  DAT_ffff000090837620 = lVar2;
  DAT_ffff000090837618 = lVar1;
  return;
}
