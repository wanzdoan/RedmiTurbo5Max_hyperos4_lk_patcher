// ffff00009071147c  size=172  FUN_ffff00009071147c

void FUN_ffff00009071147c(void)

{
  int iVar1;
  long lVar2;
  
  if (((DAT_ffff00009083f730 & 1) == 0) &&
     (lVar2 = FUN_ffff000090721af0(s_enhance_menu_ffff0000907e8be7), lVar2 != 0)) {
    FUN_ffff00009077de54
              (s__s__d__BootMenu__Ext_mode___s__ffff0000907f923c,s_bootmenu_cmdline_ffff0000907f925c
               ,0x76,lVar2);
    iVar1 = FUN_ffff00009077ede4(lVar2,s_initcall_ffff0000907faf91);
    if (iVar1 == 0) {
      FUN_ffff000090718f34(s_initcall_debug_1_log_buf_len_4M_p_ffff0000907e5a92);
      return;
    }
    iVar1 = FUN_ffff00009077ede4(lVar2,s_bootftrace_ffff0000907f0c24);
    if (iVar1 == 0) {
      FUN_ffff000090711528();
      return;
    }
  }
  return;
}


