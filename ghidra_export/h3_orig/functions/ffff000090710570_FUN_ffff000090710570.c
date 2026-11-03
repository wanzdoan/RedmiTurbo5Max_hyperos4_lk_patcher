// ffff000090710570  size=104  FUN_ffff000090710570

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090710570(undefined8 param_1)

{
  int iVar1;
  undefined1 auStack_18 [4];
  int local_14;
  
  local_14 = 0;
  FUN_ffff0000907103d0(param_1,auStack_18,&local_14);
  iVar1 = local_14;
  if ((local_14 == 0) && (iVar1 = _DAT_ffff00009083edc8, (DAT_ffff00009083f600 & 1) == 0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_boot_info_c_ffff0000907e0ac8,0x1cd,
               s_g_boot_info_hdr_loaded_ffff0000907d25a2);
  }
  return iVar1;
}


