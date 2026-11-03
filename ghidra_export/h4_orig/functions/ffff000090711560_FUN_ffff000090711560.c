// ffff000090711560  size=172  FUN_ffff000090711560

void FUN_ffff000090711560(void)

{
  int iVar1;
  long lVar2;
  
  if (((DAT_ffff00009083f768 & 1) == 0) &&
     (lVar2 = FUN_ffff000090721d30(s_enhance_menu_ffff0000907e8d42), lVar2 != 0)) {
    FUN_ffff00009077e11c
              (s__s__d__BootMenu__Ext_mode___s__ffff0000907f93d8,s_bootmenu_cmdline_ffff0000907f93f8
               ,0x76,lVar2);
    iVar1 = FUN_ffff00009077f0ac(lVar2,s_initcall_ffff0000907fb12d);
    if (iVar1 == 0) {
      FUN_ffff000090719078(s_initcall_debug_1_log_buf_len_4M_p_ffff0000907e5bbd);
      return;
    }
    iVar1 = FUN_ffff00009077f0ac(lVar2,s_bootftrace_ffff0000907f0dac);
    if (iVar1 == 0) {
      FUN_ffff00009071160c();
      return;
    }
  }
  return;
}


