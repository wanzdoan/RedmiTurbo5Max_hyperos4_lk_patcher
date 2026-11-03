// ffff000090710a40  size=172  FUN_ffff000090710a40

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090710a40(undefined8 param_1,long param_2,long param_3)

{
  long lVar1;
  long lVar2;
  
  if ((DAT_ffff00009083f638 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x210,
               s_g_boot_info_hdr_loaded_ffff0000907d25df);
  }
  DAT_ffff00009083f640 = 1;
  _DAT_ffff00009083f648 = param_1;
  lVar1 = _DAT_ffff00009083f650;
  lVar2 = _DAT_ffff00009083f658;
  if (3 < _DAT_ffff00009083e790) {
    if (param_2 == 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x217,s_vendorboot_addr_ffff0000907d3ed4
                );
    }
    lVar1 = param_2;
    lVar2 = param_3;
    if (param_3 == 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x218,s_initboot_addr_ffff0000907d5813);
    }
  }
  _DAT_ffff00009083f658 = lVar2;
  _DAT_ffff00009083f650 = lVar1;
  return;
}


