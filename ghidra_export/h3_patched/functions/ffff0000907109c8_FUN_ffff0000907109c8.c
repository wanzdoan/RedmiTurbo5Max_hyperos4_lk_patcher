// ffff0000907109c8  size=172  FUN_ffff0000907109c8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907109c8(undefined8 param_1,long param_2,long param_3)

{
  long lVar1;
  long lVar2;
  
  if ((DAT_ffff00009083f600 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_boot_info_c_ffff0000907e0ab1,0x210,
               s_g_boot_info_hdr_loaded_ffff0000907d25a2);
  }
  DAT_ffff00009083f608 = 1;
  _DAT_ffff00009083f610 = param_1;
  lVar1 = _DAT_ffff00009083f618;
  lVar2 = _DAT_ffff00009083f620;
  if (3 < _DAT_ffff00009083e758) {
    if (param_2 == 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_app_mt_boot_boot_info_c_ffff0000907e0ab1,0x217,s_vendorboot_addr_ffff0000907d3e97
                );
    }
    lVar1 = param_2;
    lVar2 = param_3;
    if (param_3 == 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_app_mt_boot_boot_info_c_ffff0000907e0ab1,0x218,s_initboot_addr_ffff0000907d57c3);
    }
  }
  _DAT_ffff00009083f620 = lVar2;
  _DAT_ffff00009083f618 = lVar1;
  return;
}


