// ffff000090787790  size=196  FUN_ffff000090787790

void FUN_ffff000090787790(void)

{
  long lVar1;
  undefined8 uVar2;
  
  lVar1 = FUN_ffff00009077d514(0x800);
  if (lVar1 == 0) {
    FUN_ffff00009077e11c(s__LK_RECOVERY__allocate_misc_msg_f_ffff0000907e6d27);
    FUN_ffff00009077e11c(s__LK_RECOVERY__reboot_recovery_wi_ffff000090803011);
    return;
  }
  FUN_ffff00009077ee50(lVar1,0,0x800);
  uVar2 = FUN_ffff00009077f250(s_boot_recovery_ffff0000907d8c68);
  FUN_ffff00009077f2ac(lVar1,s_boot_recovery_ffff0000907d8c68,uVar2);
  uVar2 = FUN_ffff00009077f250(s_recovery___wipe_data___reason_Ma_ffff0000907f06c4);
  FUN_ffff00009077f2ac(lVar1 + 0x40,s_recovery___wipe_data___reason_Ma_ffff0000907f06c4,uVar2);
  FUN_ffff000090787854(lVar1,1);
  thunk_FUN_ffff0000907c314c(lVar1);
  FUN_ffff000090745fd8(2);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000907227e0(1,1);
}


