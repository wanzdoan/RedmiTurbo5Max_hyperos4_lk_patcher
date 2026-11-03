
int FUN_ffff0000907105e8(undefined8 param_1)

{
  int iVar1;
  undefined1 auStack_18 [4];
  int local_14;
  
  local_14 = 0;
  FUN_ffff000090710448(param_1,auStack_18,&local_14);
  iVar1 = local_14;
  if ((local_14 == 0) && (iVar1 = DAT_ffff00009083ee00, (DAT_ffff00009083f638 & 1) == 0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x1cd,
               s_g_boot_info_hdr_loaded_ffff0000907d25df);
  }
  return iVar1;
}


