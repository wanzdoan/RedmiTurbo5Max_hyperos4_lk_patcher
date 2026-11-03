// ffff0000907122d4  size=176  FUN_ffff0000907122d4

void FUN_ffff0000907122d4(undefined8 param_1)

{
  int iVar1;
  undefined8 local_30;
  uint local_28;
  undefined4 uStack_24;
  
  local_30 = 0x4100080200000010;
  local_28 = FUN_ffff000090710b48();
  local_28 = local_28 & 0xff;
  uStack_24 = 2;
  iVar1 = FUN_ffff00009076d75c(param_1,s__chosen_ffff0000907ea59a);
  if (iVar1 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_fdt_para_c_ffff0000907e5ac4,0x34,s_offset_>__0_ffff0000907f3f73);
  }
  iVar1 = FUN_ffff00009076e40c(param_1,iVar1,s_atag_boot_ffff0000907f0c47,&local_30,0x10);
  if (iVar1 == 0) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04(s_app_mt_boot_fdt_para_c_ffff0000907e5ac4,0x37,&DAT_ffff0000907d24c9);
}


