
void FUN_ffff0000907873d8(void)

{
  long lVar1;
  undefined8 uVar2;

  lVar1 = FUN_ffff00009077de7c(0x800);
  if (lVar1 == 0) {
    FUN_ffff00009077ea84(s__LK_RECOVERY__allocate_misc_msg_f_ffff0000907df4eb);
    FUN_ffff00009077ea84(s__LK_RECOVERY__reboot_recovery_wi_ffff0000907fc016);
    return;
  }
  FUN_ffff00009077f7b8(lVar1,0,0x800);
  uVar2 = FUN_ffff00009077fb34(s_boot_recovery_ffff0000907d102a);
  FUN_ffff00009077fb90(lVar1,s_boot_recovery_ffff0000907d102a,uVar2);
  uVar2 = FUN_ffff00009077fb34(s_recovery___wipe_data___reason_Ma_ffff0000907e91c9);
  FUN_ffff00009077fb90(lVar1 + 0x40,s_recovery___wipe_data___reason_Ma_ffff0000907e91c9,uVar2);
  FUN_ffff00009078749c(lVar1,1);
  thunk_FUN_ffff0000907c310c(lVar1);
  FUN_ffff000090746d68(2);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009072272c(1,1);
}
