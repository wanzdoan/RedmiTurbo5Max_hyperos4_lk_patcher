// ffff000090712eb8  size=316  FUN_ffff000090712eb8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090712eb8(undefined8 param_1)

{
  char cVar1;
  int iVar2;
  undefined8 local_44;
  undefined4 local_3c;
  undefined4 uStack_38;
  undefined4 local_34;
  
  cVar1 = FUN_ffff000090710b48();
  if ((cVar1 == '\x01') || (cVar1 = FUN_ffff000090710b48(), cVar1 == '\x04')) {
    local_44 = 0x4100080300000020;
    local_3c = _DAT_ffff00009083f800;
    uStack_38 = _DAT_ffff00009083f804;
    local_34 = FUN_ffff00009074293c(_DAT_ffff00009083f808);
    iVar2 = FUN_ffff00009076d75c(param_1,s__chosen_ffff0000907ea5b1);
    if (iVar2 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_app_mt_boot_meta_c_ffff0000907f5bae,0x75,s_offset_>__0_ffff0000907f3f8a);
    }
    iVar2 = FUN_ffff00009076e40c(param_1,iVar2,s_atag_meta_ffff0000907f4011,&local_44,0x14);
    if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04(s_app_mt_boot_meta_c_ffff0000907f5bae,0x78,&DAT_ffff0000907d24c9);
    }
    FUN_ffff00009077de54
              (s__s__d__meta_com_type____d_ffff0000907ebe25,s_set_fdt_atag_meta_ffff0000907e2539,
               0x7a,_DAT_ffff00009083f800);
    FUN_ffff00009077de54
              (s__s__d__meta_com_id____d_ffff0000907d8c85,s_set_fdt_atag_meta_ffff0000907e2539,0x7b,
               _DAT_ffff00009083f804);
    FUN_ffff00009077de54
              (s__s__d__meta_uart_port____d_ffff0000907d582f,s_set_fdt_atag_meta_ffff0000907e2539,
               0x7c,_DAT_ffff00009083f808);
  }
  return;
}


