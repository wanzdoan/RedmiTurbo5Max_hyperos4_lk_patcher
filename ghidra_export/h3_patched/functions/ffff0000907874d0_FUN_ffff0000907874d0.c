// ffff0000907874d0  size=196  FUN_ffff0000907874d0

void FUN_ffff0000907874d0(void)

{
  long lVar1;
  undefined8 uVar2;
  
  lVar1 = FUN_ffff00009077d24c(0x800);
  if (lVar1 == 0) {
    FUN_ffff00009077de54(s__LK_RECOVERY__allocate_misc_msg_f_ffff0000907e6bcc);
    FUN_ffff00009077de54(s__LK_RECOVERY__reboot_recovery_wi_ffff000090802e0d);
    return;
  }
  FUN_ffff00009077eb88(lVar1,0,0x800);
  uVar2 = FUN_ffff00009077ef88(s_boot_recovery_ffff0000907d8bd1);
  FUN_ffff00009077efe4(lVar1,s_boot_recovery_ffff0000907d8bd1,uVar2);
  uVar2 = FUN_ffff00009077ef88(s_recovery___wipe_data___reason_Ma_ffff0000907f053c);
  FUN_ffff00009077efe4(lVar1 + 0x40,s_recovery___wipe_data___reason_Ma_ffff0000907f053c,uVar2);
  FUN_ffff000090787594(lVar1,1);
  thunk_FUN_ffff0000907c2ad8(lVar1);
  FUN_ffff000090745d40(2);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009072259c(1,1);
}


