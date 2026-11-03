// ffff000090712f9c  size=316  FUN_ffff000090712f9c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090712f9c(undefined8 param_1)

{
  char cVar1;
  int iVar2;
  undefined8 local_44;
  undefined4 local_3c;
  undefined4 uStack_38;
  undefined4 local_34;
  
  cVar1 = FUN_ffff000090710bc0();
  if ((cVar1 == '\x01') || (cVar1 = FUN_ffff000090710bc0(), cVar1 == '\x04')) {
    local_44 = 0x4100080300000020;
    local_3c = _DAT_ffff00009083f838;
    uStack_38 = _DAT_ffff00009083f83c;
    local_34 = FUN_ffff000090742bd4(_DAT_ffff00009083f840);
    iVar2 = FUN_ffff00009076da24(param_1,s__chosen_ffff0000907ea70b);
    if (iVar2 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_app_mt_boot_meta_c_ffff0000907f5d25,0x75,s_offset_>__0_ffff0000907f4116);
    }
    iVar2 = FUN_ffff00009076e6d4(param_1,iVar2,s_atag_meta_ffff0000907f419d,&local_44,0x14);
    if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc(s_app_mt_boot_meta_c_ffff0000907f5d25,0x78,&DAT_ffff0000907d2506);
    }
    FUN_ffff00009077e11c
              (s__s__d__meta_com_type____d_ffff0000907ebfa8,s_set_fdt_atag_meta_ffff0000907e260a,
               0x7a,_DAT_ffff00009083f838);
    FUN_ffff00009077e11c
              (s__s__d__meta_com_id____d_ffff0000907d8d1c,s_set_fdt_atag_meta_ffff0000907e260a,0x7b,
               _DAT_ffff00009083f83c);
    FUN_ffff00009077e11c
              (s__s__d__meta_uart_port____d_ffff0000907d587f,s_set_fdt_atag_meta_ffff0000907e260a,
               0x7c,_DAT_ffff00009083f840);
  }
  return;
}


